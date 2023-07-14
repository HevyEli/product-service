package org.example.repository;

import org.example.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {
    private List<Product> products = new ArrayList<Product>();

    public Product getProductById(long id) {
        for (Product p : products) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public boolean deleteProductById(long id) {
        Product product = getProductById(id);
        if (product != null) {
            products.remove(product);
            return true;
        } else {
            return false;
        }
    }

    public void createProduct(Product item) {
        products.add(item);
    }

    public Product editProductById(long id, String name, double price, String description, int quantity) {
        Product product = getProductById(id);
        if (product != null) {
            product.setName(name);
            product.setPrice(price);
            product.setDescription(description);
            product.setQuantity(quantity);
            return product;
        }
        return null;
    }
    public Product editProductQtyById(long id, int quantity) {
        Product product = getProductById(id);
        if (product != null) {
            product.setQuantity(quantity);
            return product;
        }
        return null;
    }

}
