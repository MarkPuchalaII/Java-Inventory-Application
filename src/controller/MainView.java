package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Part;
import model.Inventory;
import model.Product;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.control.Alert.AlertType.*;

/** The Main view display's the inventory's current parts & products
 * FUTURE ENHANCEMENT
 * Apply context to the descriptiveText message, line 82.
 */

public class MainView implements Initializable {


    public TextField searchPart;
    public TableView<Part> partTable;
    public TableColumn<Part, Integer> partID;
    public TableColumn<Part, String> partName;
    public TableColumn<Part, Integer> partStock;
    public TableColumn<Part, Double> partPrice;
    public Button addPartBtn;
    public Button modPartBtn;
    public Button delPartBtn;

    public TextField searchProduct;
    public TableView<Product> productTable;
    public TableColumn<Product, Integer> productID;
    public TableColumn<Product, String> productName;
    public TableColumn<Product, Integer> productStock;
    public TableColumn<Product, Double> productPrice;
    public Button addProductBtn;
    public Button modProductBtn;
    public Button delProductBtn;

    public Button exitMainBtn;
    public Text partText;
    public Text productText;

    /** Initialize the main view, complete with tables appropriately bound to current parts & products */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partTable.setItems(Inventory.getAllParts());
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTable.setItems(Inventory.getAllProducts());
        productID.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**  A quick, easy to use function for setting alerts */
    Alert alertBox(Alert.AlertType type, String message, String title, String header) {
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setHeaderText(header);
        return alert;
    }

    /**  Delete the currently selected part
     * RUNTIME ERROR
     * Selecting a row in one table does not un-select any rows in the other table.
     * This can allow deletePart to run regardless of whether a user's aware of what's selected.
     * STILL NOT FIXED
     */
    public boolean deletePart() {
        Part selected = partTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = alertBox(ERROR, "Please select a part to delete.", "Parts", "Delete");
            alert.showAndWait();
            return true;
        }
        Alert alert = alertBox(CONFIRMATION, "Do you want to delete this part?", "Parts", "Delete");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            partText.setText(String.join("","Deleted #", ""+selected.getId(), ": ", selected.getName()));
            return Inventory.deletePart(selected);
        } else {
            partText.setText("Didn't delete");
            return true;
        }
    }

    /**  Delete the currently selected product
     * RUNTIME ERROR
     * Selecting a row in one table does not un-select any rows in the other table.
     * This can allow deleteProduct to run regardless of whether a user's aware of what's selected.
     * STILL NOT FIXED
     */
    public boolean deleteProduct() {

        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = alertBox(ERROR, "Please select a product to delete.", "Products", "Delete");
            alert.showAndWait();
            return true;
        }
        if (selected.getAllAssociatedParts().size() > 0) {
            productText.setText("This product has parts");
            return true;
        }
        Alert alert = alertBox(CONFIRMATION, "Do you want to delete this product?", "Products", "Delete");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            productText.setText(String.join("","Deleted #", ""+selected.getId(), ": ", selected.getName()));
            return Inventory.deleteProduct(selected);
        } else {
            productText.setText("Didn't delete");
            return true;
        }
    }

    /** Search for parts based on the search field
     * RUNTIME ERROR
     * Part search doesn't display an error message on "part not found"
     * FIX: Added line 149, wrapped in an if/else to report "part not found"
     */
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
            partTable.setItems(lookupList);
            searchPart.setText("");
            partText.setText("");
        } else {
            partText.setText("No results found");
        }
    }

    /** Search for products based on the search field
     * RUNTIME ERROR
     * Product search doesn't display an error message on "product not found"
     * FIX: Added lines 184-186, setting an error text, "no products found"
    */
    public void searchProducts() {
        String query = searchProduct.getText();
        ObservableList<Product> lookupList = Inventory.lookupProduct(query);

        if (lookupList.size() == 0) {
            try {
                int queryID = Integer.parseInt(query);
                Product lookupID = Inventory.lookupProduct(queryID);
                if (lookupID != null) {
                    lookupList.add(lookupID);
                }
            } catch (NumberFormatException e) {
                //ignore
            }
        }

        if (lookupList.size() !=0) {
            productTable.setItems(lookupList);
            searchPart.setText("");
            productText.setText("");
        } else {
            productText.setText("No results found");
        }
    }

    /**  A quick, easy to use function for picking screens to go to */
    public void goToScreen(String resource, ActionEvent actionEvent, int width, int height) throws IOException {
        FXMLLoader screenView = new FXMLLoader(getClass().getResource(resource));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(screenView.load(), width, height);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /** Go to the Add Part screen */
    public void toAddPart(ActionEvent actionEvent) throws IOException {
        goToScreen("/view/addPartView.fxml", actionEvent, 476, 402);
    }

    /** Go to the Modify Part screen */
    public void toModifyPart(ActionEvent actionEvent) throws IOException {
        Part selected = partTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = alertBox(ERROR, "Please select a part to modify.", "Parts", "Modify");
            alert.showAndWait();
            return;
        }
        ModifyPartView.setPart(selected);
        goToScreen("/view/modifyPartView.fxml", actionEvent, 476, 402);
    }

    /** Go to the Modify Product screen */
    public void toModifyProduct(ActionEvent actionEvent)  throws IOException {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = alertBox(ERROR, "Please select a product to modify.", "Products", "Modify");
            alert.showAndWait();
            return;
        }
        ModifyProductView.setProduct(selected);
        goToScreen("/view/modifyProductView.fxml", actionEvent, 910, 628);
    }

    /** Go to the Add Product screen */
    public void toAddProduct(ActionEvent actionEvent)  throws IOException {
        goToScreen("/view/addProductView.fxml", actionEvent, 910, 628);
    }

    /** Exit the program */
    public void exit() {
        Alert alert = alertBox(CONFIRMATION, "Are you sure?", "Inventory Management System", "Exit");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
}