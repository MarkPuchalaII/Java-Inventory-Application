package controller;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;

import static javafx.scene.control.Alert.AlertType.CONFIRMATION;
import static javafx.scene.control.Alert.AlertType.ERROR;

/** The Modify Product View allows a user to edit any product's values & associated parts */
public class ModifyProductView {
    public static Product modifyProduct;
    public int updateID = Inventory.getAllProducts().indexOf(modifyProduct);
    public static Product originalProduct;

    public Button saveProductBtn;
    public Button cancel;

    public TextField id;
    public TextField name;
    public TextField stock;
    public TextField price;
    public TextField max;
    public TextField min;

    public TableView<Part> inventoryTable;
    public TableColumn<Part, Integer> invID;
    public TableColumn<Part, String> invName;
    public TableColumn<Part, Integer> invStock;
    public TableColumn<Part, Double> invPrice;
    public Button addPart;

    @SuppressWarnings("FieldMayBeFinal")
    public TableView<Part> associatedTable;
    public TableColumn<Part, Integer> assID;
    public TableColumn<Part, String> assName;
    public TableColumn<Part, Integer> assStock;
    public TableColumn<Part, Double> assPrice;
    public Button removePart;
    public TextField searchPart;
    public Text inventoryText;
    public Text associatedText;
    public Text nameError;
    public Text stockError;
    public Text priceError;
    public Text maxError;
    public Text minError;

    /** Initialize the window with relevant product properties & tables */
    public void initialize() {
        id   .setText(String.join("", String.valueOf(modifyProduct.getId   ()), " - Disabled"));
        name .setText(                                                 modifyProduct.getName ())                 ;
        stock.setText(                                  String.valueOf(modifyProduct.getStock())                );
        price.setText(                                  String.valueOf(modifyProduct.getPrice())                );
        max  .setText(                                  String.valueOf(modifyProduct.getMax  ())                );
        min  .setText(                                  String.valueOf(modifyProduct.getMin  ())                );

        inventoryTable.setItems(Inventory.getAllParts());
        invID.setCellValueFactory(new PropertyValueFactory<>("id"));
        invName.setCellValueFactory(new PropertyValueFactory<>("name"));
        invStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        invPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedTable.setItems(modifyProduct.getAllAssociatedParts());
        assID.setCellValueFactory(new PropertyValueFactory<>("id"));
        assName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /** Let the main view set what product is being modified, as well as a copy of the product in its original state (in case we cancel) */
    public static void setProduct(Product product) {
        modifyProduct = product;
        originalProduct = new Product(modifyProduct.getId(),    modifyProduct.getName(), modifyProduct.getPrice(),
                                      modifyProduct.getStock(), modifyProduct.getMax(),  modifyProduct.getMin());
        for (Part part : modifyProduct.getAllAssociatedParts()) {
            originalProduct.addAssociatedPart(part);
        }
    }

    /** Add to the product's list of associated parts */
    public void addPart() {
        Part part = inventoryTable.getSelectionModel().getSelectedItem();
        if (part == null) {
            return;
        }
        modifyProduct.addAssociatedPart(part);
    }

    /** Remove from the product's list of associated parts */
    public boolean removePart() {
        Part part = associatedTable.getSelectionModel().getSelectedItem();
        if (part == null) {
            Alert alert = alertBox(ERROR, "Please select a part to delete.", "Parts", "Delete");
            alert.showAndWait();
            return true;
        }
        Alert alert = alertBox(CONFIRMATION,"Do you want to remove this part?", "Associated Parts", "Remove");
        alert.showAndWait();
        return modifyProduct.deleteAssociatedPart(part);
    }

    /** A quick, easy to use function for setting alerts */
    Alert alertBox(Alert.AlertType type, String message, String title, String header) {
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setHeaderText(header);
        return alert;
    }

    /** Save our new product info, calling the newProduct function to do so. */
    public void save(ActionEvent actionEvent) throws IOException {
        if (validate()) {
            int    sID    =            modifyProduct.getId()   ;
            String sName  =                     name.getText() ;
            int    sStock = Integer.parseInt(  stock.getText());
            double sPrice = Double.parseDouble(price.getText());
            int    sMax   = Integer.parseInt(    max.getText());
            int    sMin   = Integer.parseInt(    min.getText());

            modifyProduct.setId(sID);
            modifyProduct.setName(sName);
            modifyProduct.setPrice(sPrice);
            modifyProduct.setStock(sStock);
            modifyProduct.setMin(sMin);
            modifyProduct.setMax(sMax);

            Inventory.updateProduct(updateID, modifyProduct);
            toMainView(actionEvent);
        }
    }

    /** Validate whether all input fields are properly filled in */
    public boolean validate() {
        boolean allGood = true;
        if (name.getText().equals("")) {
            nameError .setText("No data in name field");
            allGood = false;
        } else {
            nameError .setText("");
        }

        try {
            Integer.parseInt(stock.getText());
            stockError.setText("");
        } catch (NumberFormatException e) {
            stockError.setText("Inventory is not an integer");
            allGood = false;
        }

        try {
            Double.parseDouble(price.getText());
            priceError.setText("");
        } catch (NumberFormatException e) {
            priceError.setText("Price is not a double");
            allGood = false;
        }

        try {
            Integer.parseInt(max.getText());
            maxError.setText("");
        } catch (NumberFormatException e) {
            maxError.setText("Max is not an integer");
            allGood = false;
        }

        try {
            Integer.parseInt(min.getText());
            minError.setText("");
        } catch (NumberFormatException e) {
            minError.setText("Min is not an integer");
            allGood = false;
        }
        return allGood;
    }

    /** Revert our product to its original form before exiting back to the main view */
    public void cancel(ActionEvent actionEvent) throws IOException {
        Inventory.updateProduct(updateID, originalProduct);
        toMainView(actionEvent);
    }

    /** Head back to the main view */
    public void toMainView(ActionEvent actionEvent) throws IOException {
        FXMLLoader mainView = new FXMLLoader(getClass().getResource("/view/mainView.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(mainView.load(), 942, 395);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /** Search for parts based on the search field */
    public void searchParts() {
        String query = searchPart.getText();
        ObservableList<Part> lookupList = Inventory.lookupPart(query);

        if (lookupList.size() == 0) {
            try {
                int queryID = Integer.parseInt(query);
                Part lookupID = Inventory.lookupPart(queryID);
                if (lookupID != null) {
                    lookupList.add(lookupID);
                }
            } catch (NumberFormatException e) {
                //ignore
            }
        }

        if (lookupList.size() !=0) {
            inventoryTable.setItems(lookupList);
            searchPart.setText("");
            inventoryText.setText("");
        } else {
            inventoryText.setText("No results found");
        }
    }

}
