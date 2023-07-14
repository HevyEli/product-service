package org.example.model;

public class Product {
    public Product(long id, String name, double price, String description, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
    }

    public Product() {}

    private long id;
    private String name;
    private double price;
    private String description;
    private int quantity;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                '}';
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * get field
     *
     * @return id
     */
    public long getId() {
        return this.id;
    }

    /**
     * set field
     *
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }


    /**
     * get field
     *
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * set field
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get field
     *
     * @return price
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * set field
     *
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * get field
     *
     * @return description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * set field
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}

