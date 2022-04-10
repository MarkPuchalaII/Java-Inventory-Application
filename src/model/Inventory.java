package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** The inventory contains inHouse parts & Outsourced parts, as well as products potentially associated with parts */
public class Inventory {
    @SuppressWarnings("FieldMayBeFinal")
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    @SuppressWarnings("FieldMayBeFinal")
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /** Add a part to the inventory's list of parts */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /** Add a product to the inventory's list of products */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /** Lookup a part based on ID */
    public static Part lookupPart(int partId) {
        ObservableList<Part> allParts = Inventory.getAllParts();

        for (Part part : allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }

    /** Lookup a product based on ID */
    public static Product lookupProduct(int productId) {
        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for (Product product : allProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    /** Lookup all parts containing the given text input */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> lookup = FXCollections.observableArrayList();

        for (Part pn : allParts) {
            if (pn.getName().toLowerCase().contains(partName.toLowerCase())) {
                lookup.add(pn);
            }
        }
        return lookup;
    }

    /** Lookup all products containing the given text input */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        ObservableList<Product> lookup = FXCollections.observableArrayList();

        for (Product pn : allProducts) {
            if (pn.getName().toLowerCase().contains(productName.toLowerCase())) {
                lookup.add(pn);
            }
        }
        return lookup;
    }

    /** Update the inventory's values for the given part at the given ID */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /** Update the inventory's values for the given product at the given ID */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /** Delete the selected part from the inventory */
    public static boolean deletePart(Part selectedPart) {
        if (allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return false;
        } else {
            return true;
        }
    }

    /** Delete the selected product from the inventory */
    public static boolean deleteProduct(Product selectedProduct) {
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return false;
        }
        return true;
    }

    /** get the inventory's list of all current parts */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /** get the inventory's list of all current products */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
