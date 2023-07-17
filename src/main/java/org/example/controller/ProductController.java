package org.example.controller;

import org.example.dto.ResponseMessage;
import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;


@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/getAllProducts")
    public List<Product> getAllProducts() {
        logger.info("getAllProducts received request");
        List<Product> products = productRepository.getAllProducts();
        if (productRepository.getAllProducts().size() < 1) {
            logger.info("getAllProducts response: There are no products available.");
        }
        if (productRepository.getAllProducts().size() > 0) {
            logger.info("getAllProducts response: Returned list {} of products.", productRepository.getAllProducts().size());
            logger.info("Products: {}", productRepository.getAllProducts());
        }
        products.sort(Comparator.comparing(Product::getId));
        return productRepository.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> getProductById(@PathVariable long id) {
        logger.info("getProductById received request for product: {}", id);
        Product product = productRepository.getProductById(id);
        if (product == null) {
            logger.info("There is no product with id {}.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage( HttpStatus.NOT_FOUND, "Product not found", "Product not found" ));
        }
        logger.info("Product {} found.", id);
        logger.info(String.valueOf(product));
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(HttpStatus.OK, "Product " + product, "Product " + product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable long id) {
        logger.info("deleteProductById received request to delete product: {}.", id);
        boolean isDeleted = productRepository.deleteProductById(id);
        if (isDeleted == false) {
            logger.info("No product with id {} found.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product " + id + " not found.");
        }
        logger.info("Product id {} has been deleted.", id);
        return ResponseEntity.status(HttpStatus.OK).body("Product " + id + " has been deleted.");
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> createProduct(@RequestBody Product product) {
        logger.info("createProduct received request to create product: " + product);
        boolean checkIfExists = productRepository.checkIfProductExists(product.getId());
        if (checkIfExists == true) {
            logger.info("Product {} already exists.", product.getId());
            ResponseMessage responseMessage = new ResponseMessage(HttpStatus.CONFLICT, "Product " + product.getId() + " already exists", "ProductResponse Details...");
            return new ResponseEntity<>(responseMessage, HttpStatus.CONFLICT);
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessage("Product " + product.getId() + " already exists"));
        }
        productRepository.createProduct(product);
        logger.info("Product " + product + " has been created.");
        ResponseMessage responseMessage = new ResponseMessage(HttpStatus.OK, "product: " + product.getId() + " has been created.", "{ details: " + product + " }");
        return new ResponseEntity(responseMessage, HttpStatus.CREATED);
//        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("Product " + product + " has been created."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editProductById(@PathVariable long id, @RequestBody Product newProduct) {
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

        return ResponseEntity.status(HttpStatus.OK).body("Product " + id + " has been updated with these attributes, "
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
