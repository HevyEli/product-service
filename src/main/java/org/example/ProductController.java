package org.example;

import org.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        if (productRepository.getAllProducts().size() < 1) {
            logger.info("getAllProducts response: There are no products available.");
        }
        if (productRepository.getAllProducts().size() > 0) {
            logger.info("getAllProducts response: Returned list {} of products.", productRepository.getAllProducts().size());
            logger.info("Products: {}", productRepository.getAllProducts());
        }
        return productRepository.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable long id) {
        logger.info("getProductById received request for product: {}", id);
        Product product = productRepository.getProductById(id);
        if (product == null) {
            logger.info("There is no product with id {}.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        logger.info("Product {} found.", id);
        logger.info(String.valueOf(product));
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable long id) {
        logger.info("deleteProductById received request to delete product: {}.", id);
        Product product = productRepository.getProductById(id);
        if (product == null) {
            logger.info("No product with id {} found.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product " + id + " not found.");
        }
        logger.info("Product id {} has been deleted.", id);
        logger.info(String.valueOf(product) + "has been deleted.");
        return ResponseEntity.status(HttpStatus.OK).body("Product " + id + " has been deleted.");
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
        logger.info("createProduct received request to create product: " + product);
        productRepository.createProduct(product);
        logger.info("Product " + product + " has been created.");
        return ResponseEntity.status(HttpStatus.CREATED).body("Product " + product + " has been created.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editProductById(@PathVariable long id, @RequestBody Product product) {
        logger.info("editProductById received request to edit product {}.", id);
        product = productRepository.getProductById(id);
        if (product == null) {
            logger.info("Product {} has not been found.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product " + id + "has not been found!");
        }
        productRepository.editProductById(id, product.getName(), product.getPrice(), product.getDescription());
        logger.info("Product " + id + " has been updated. Old values Id: " + product.getId() + "name: " + product.getName()
                    + "Price: " + product.getPrice() + "Description: " + product.getDescription() + "new values id:.", id );

        return ResponseEntity.status(HttpStatus.OK).body("Product " + id + " has been updated.");
    }

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
}
