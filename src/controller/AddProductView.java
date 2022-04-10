package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.control.Alert.AlertType.CONFIRMATION;
import static javafx.scene.control.Alert.AlertType.ERROR;

/** The Add Product View allows a user to add a new product, complete with values & associated parts */
public class AddProductView implements Initializable {
    public static Product newProduct;

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
    public TextField searchPart;
    public Text inventoryText;
    public Text associatedText;

    @SuppressWarnings("FieldMayBeFinal")
    public TableView<Part> associatedTable;
    public TableColumn<Part, Integer> assID;
    public TableColumn<Part, String> assName;
    public TableColumn<Part, Integer> assStock;
    public TableColumn<Part, Double> assPrice;
    public Button removePart;
    public Text minError;
    public Text maxError;
    public Text priceError;
    public Text stockError;
    public Text nameError;

    /** Initialize the window with relevant product properties & tables */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newProduct = new Product();
        
        inventoryTable.setItems(Inventory.getAllParts());
        invID.setCellValueFactory(new PropertyValueFactory<>("id"));
        invName.setCellValueFactory(new PropertyValueFactory<>("name"));
        invStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        invPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedTable.setItems(newProduct.getAllAssociatedParts());
        assID.setCellValueFactory(new PropertyValueFactory<>("id"));
        assName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /** Generate a new ID based on the current inventory */
    private int generateID(ObservableList<Product> idList) {
        int search = 1000;
        if (idList.size() == 0) {return 1000;}
        for (Product product : idList) {
            if (search < product.getId()) {
                return search;
            }
            search++;
        }
        Product lastItem = idList.get(idList.size()-1);
        return lastItem.getId() + 1;
    }

    /** Add to the product's list of associated parts */
    public void addPart() {
        Part part = inventoryTable.getSelectionModel().getSelectedItem();
        if (part == null) {
            return;
        }
        newProduct.addAssociatedPart(part);
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
        return newProduct.deleteAssociatedPart(part);
    }

    /** A quick, easy to use function for setting alerts */
    Alert alertBox(Alert.AlertType type, String message, String title, String header) {
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setHeaderText(header);
        return alert;
    }

    /**  Update the inventory with our product's new information */
    public void saveProduct(Product product) {
        Inventory.addProduct(product);
    }

    /**  Save our new product info, calling the newProduct function to do so. */
    public void save(ActionEvent actionEvent) throws IOException {
        if(validate()) {
            int sID        = generateID(Inventory.getAllProducts());
            String sName   =                     name.getText() ;
            double sPrice  = Double.parseDouble(price.getText());
            int sStock     = Integer.parseInt(  stock.getText());
            int sMin       = Integer.parseInt(    min.getText());
            int sMax       = Integer.parseInt(    max.getText());
    
            newProduct.setId(sID);
            newProduct.setName(sName);
            newProduct.setPrice(sPrice);
            newProduct.setStock(sStock);
            newProduct.setMin(sMin);
            newProduct.setMax(sMax);
    
            saveProduct(newProduct);
            toMainView(actionEvent);
        }
    }

    /**  Validate whether all input fields are properly filled in */
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

    /** Head back to the main view */
    public void toMainView(ActionEvent actionEvent) throws IOException {
        FXMLLoader mainView = new FXMLLoader(getClass().getResource("/view/mainView.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(mainView.load(), 942, 395);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /** Search for a part based on the search field
     * RUNTIME ERROR
     * Part search doesn't display an error message on "part not found"
     * FIX: Added line 149, wrapped in an if/else to report "part not found"
     * */
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
