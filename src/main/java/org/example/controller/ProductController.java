package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.configuration.ProductControllerProperties;
import org.example.dto.ResponseMessage;
import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;


@RestController
@Configuration
@PropertySource("classpath:application.yaml")
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<ResponseMessage>  getAllProducts() throws JsonProcessingException {
        logger.info("getAllProducts received request");
        List<Product> products = productRepository.getAllProducts();
        ObjectMapper objectMapper = new ObjectMapper();
        String productsJson = objectMapper.writeValueAsString(products);

        if (products.isEmpty()) {
            logger.info("getAllProducts response: There are no products available.");
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseMessage(
                            HttpStatus.NOT_FOUND, "There are no products", "[empty list!]"));
        }
        else {
            logger.info("getAllProducts response: Returned list {} of products.", products.size());
        }
        products.sort(Comparator.comparing(Product::getId));
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(HttpStatus.OK, products.size()  +
                " product(s) returned", "{ \"product\": [ " + productsJson + " ] }"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> getProductById(@PathVariable long id) throws JsonProcessingException {
        logger.info("getProductById received request for product: {}", id);
        Product product = productRepository.getProductById(id);
        ObjectMapper objectMapper = new ObjectMapper();
        String productJson = objectMapper.writeValueAsString(product);
        if (product == null) {
            logger.info("There is no product with id {}.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage( HttpStatus.NOT_FOUND, "Product id "+ id + " not found", "Product not found" ));
        }
        logger.info("Product {} found.", id);
        logger.info(String.valueOf(product));
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(HttpStatus.OK, "Product id" + product, "{ \"product\": [ " + productJson + " ] }"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteProductById(@PathVariable long id) {
        logger.info("deleteProductById received request to delete product: {}.", id);
        boolean isDeleted = productRepository.deleteProductById(id);
        if (isDeleted == false) {
            logger.info("No product with id {} found.", id);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product " + id + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(HttpStatus.NOT_FOUND, "Product " + id + " not found", "[]"));
        }
        logger.info("Product id {} has been deleted.", id);
//        return ResponseEntity.status(HttpStatus.OK).body("Product " + id + " has been deleted.");
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(HttpStatus.OK, "Product " + id + " has been deleted", "[DLTD]"));

    }

    @Autowired
    private ProductControllerProperties productControllerProperties;
    @PostMapping
    public ResponseEntity<ResponseMessage> createProduct(@RequestBody Product product) throws JsonProcessingException {
        logger.info("createProduct received request to create product: " + product);
        ObjectMapper objectMapper = new ObjectMapper();
        String productJson = objectMapper.writeValueAsString(product);
        if ("enabled".equals(productControllerProperties.getProductIdValidation())) {
            logger.info("productIdValidation is enabled");
            boolean checkIfExists = productRepository.checkIfProductExists(product.getId());
            if (checkIfExists == true) {
                logger.info("Product {} already exists.", product.getId());
                ResponseMessage responseMessage = new ResponseMessage(HttpStatus.CONFLICT, "Product " + product.getId() + " already exists", "[ERROR_EXIST]");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(responseMessage);
            } if  ("disabled".equals(productControllerProperties.getProductIdValidation())) {
                logger.info("productIdValidation is disabled");
            }
        }
        productRepository.createProduct(product);
        logger.info("Product " + product + " has been created.");
        ResponseMessage responseMessage = new ResponseMessage(HttpStatus.OK, "product: " + product.getId() + " has been created.", "{ \"product\": [ " + productJson + " ] }");
        return  ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editProductById(@PathVariable long id, @RequestBody Product newProduct)  {
        logger.info("editProductById received request to edit product {}.", id);

        Product oldProduct = productRepository.getProductById(id);
        if (oldProduct == null) {
            logger.info("Product {} has not been found.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product " + id + "has not been found!");
        }
        Product updatedProduct = productRepository.editProductById(id, newProduct.getName(), newProduct.getPrice(), newProduct.getDescription(), newProduct.getQuantity());
        logger.info("Product " + id + " has been updated. " +
                "Old values Id: " + oldProduct.getId() + " name: " + oldProduct.getName() + "Price: " + oldProduct.getPrice() + " Description: " + oldProduct.getDescription() +
                "new values id:{}." + " name: " + updatedProduct.getName() + " price: " + updatedProduct.getPrice() + " description: " + updatedProduct.getDescription() + "qty: " + updatedProduct.getQuantity(), id);

        return ResponseEntity.status(HttpStatus.OK).body("Product: "+"id: " + id + " has been updated with these attributes, "
                + "name: " + updatedProduct.getName() + " price: " + updatedProduct.getPrice() + " quantity: "
                + updatedProduct.getQuantity() + " descripriton: " + updatedProduct.getDescription() + ".");
    }

    @PutMapping("/editQty/{id}")
    public ResponseEntity<String> editProductQtyById(@PathVariable long id, @RequestBody Product newQty) {
        logger.info("editProductQtyById received request update Qty for product {}.", id);

//        Product existingProduct = productRepository.getProductById(id);
        Product existingProduct = productRepository.getProductById(id);
        if (existingProduct == null) {
            logger.info("Product {} has not been found.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product " + id + " has not been found!");
        }
        Product newQuantity = productRepository.editProductQtyById(id, newQty.getQuantity());
        logger.info("Product {} quantity has been updated from: {} to {}.", id, existingProduct.getQuantity(), newQuantity.getQuantity());
        return ResponseEntity.status(HttpStatus.OK).body("Product " + id + " quantity has been update from "
                + existingProduct.getQuantity()
                + " to " + newQuantity.getQuantity() + ".");
    }

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
}
