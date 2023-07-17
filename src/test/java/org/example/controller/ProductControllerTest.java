package org.example.controller;

import org.example.dto.ResponseMessage;
import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.*;

class ProductControllerTest {
    @Mock
    ProductRepository productRepository;
    @Mock
    Logger logger;
    @InjectMocks
    ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.getAllProducts()).thenReturn(List.of(new Product(0L, null, 0d, null, 0)));

        List<Product> result = productController.getAllProducts();
        Assertions.assertEquals(List.of(new Product(0L, null, 0d, null, 0)), result);
    }

    @Test
    void testGetProductById() {
        when(productRepository.getProductById(anyLong())).thenReturn(new Product(0L, "name", 0d, "description", 0));

        ResponseEntity<ResponseMessage> result = productController.getProductById(0L);
        Assertions.assertEquals(null, result);
    }

    @Test
    void testDeleteProductById() {
        when(productRepository.deleteProductById(anyLong())).thenReturn(true);

        ResponseEntity<String> result = productController.deleteProductById(0L);
        Assertions.assertEquals(null, result);
    }

    @Test
    void testCreateProduct() {
        when(productRepository.checkIfProductExists(anyLong())).thenReturn(true);

        ResponseEntity<ResponseMessage> result = productController.createProduct(new Product(1, "Test Mobil 1", 13900, "Testovaci produkt 1", 99));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testEditProductById() {
        when(productRepository.getProductById(anyLong())).thenReturn(new Product(0L, "name", 0d, "description", 0));
        when(productRepository.editProductById(anyLong(), anyString(), anyDouble(), anyString(), anyInt())).thenReturn(new Product(1, "Test Mobil 1", 13900, "Testovaci produkt 1", 99));

        ResponseEntity<String> result = productController.editProductById(0L, new Product(0L, "name", 0d, "description", 0));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testEditProductQtyById() {
        when(productRepository.getProductById(anyLong())).thenReturn(new Product(0L, null, 0d, null, 0));
        when(productRepository.editProductQtyById(anyLong(), anyInt())).thenReturn(new Product(0L, null, 0d, null, 0));

        ResponseEntity<String> result = productController.editProductQtyById(0L, new Product(0L, null, 0d, null, 0));
        Assertions.assertEquals(null, result);
    }
}

