package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Products can be associated with lists of parts */
public class Product {
    @SuppressWarnings("FieldMayBeFinal")
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /** Initialize an empty product instance, ready for properties down the line. */
    public Product(){
    }

    /** Initialize a product with all of its properties preset */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;

    }

    /** Set the product's ID value */
    public void setId(int id) {
        this.id = id;
    }

    /** Set the product's name value */
    public void setName(String name) {
        this.name = name;
    }

    /** Set the product's price value */
    public void setPrice(double price) {
        this.price = price;
    }

    /** Set the product's inventory value */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /** Set the product's min value */
    public void setMin(int min) {
        this.min = min;
    }

    /** Set the product's max value */
    public void setMax(int max) {
        this.max = max;
    }

    /** Get the product's ID value */
    public int getId() {
        return id;
    }

    /** Get the product's name value */
    public String getName() {
        return name;
    }

    /** Get the product's price value */
    public double getPrice() {
        return price;
    }

    /** Get the product's inventory value */
    public int getStock() {
        return stock;
    }

    /** Get the product's min value */
    public int getMin() {
        return min;
    }

    /** Get the product's max value */
    public int getMax() {
        return max;
    }

    /** Add the selected part from the inventory list to the associated parts list */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /** Delete the selected part from the associated parts list */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        if (associatedParts.contains(selectedAssociatedPart)) {
            associatedParts.remove(selectedAssociatedPart);
            return false;
        } else {
            return true;
        }
    }

    /** Return the current list of associated parts */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
