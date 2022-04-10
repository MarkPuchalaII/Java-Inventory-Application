package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;

/** The Add Part view allows a user to add a new part, with corresponding values */
public class AddPartView {
    public Button savePartBtn;
    public Button cancel;
    public RadioButton inHouse;
    public RadioButton outsourced;
    public ToggleGroup partType;
    public Text identifier;

    public TextField id;
    public TextField name;
    public TextField stock;
    public TextField price;
    public TextField max;
    public TextField radioed;
    public TextField min;

    public Text nameError;
    public Text stockError;
    public Text priceError;
    public Text maxError;
    public Text minError;
    public Text radioError;

    private String radio = "inHouse";

    /** Generate a new ID based on the current inventory */
    private int generateID(ObservableList<Part> idList) {
        if (idList.size() == 0) {return 1;}
        int search = 1;
        for (Part part : idList) {
            if (search < part.getId()) {
              return search;
            }
            search++;
        }
        Part lastItem = idList.get(idList.size()-1);
        return lastItem.getId() + 1;
    }

    /** Save our new part info
     * RUNTIME_ERROR
     * sName can return empty without errors.
     * This would allow parts to be added without names.
     * FIX: Added lines 86-88, refusing to save a part with an empty name field.
     */
    public void save(ActionEvent actionEvent) throws IOException {
        if (validate()) {
            int sID        = generateID(Inventory.getAllParts());
            String sName   =                     name.getText() ;
            double sPrice  = Double.parseDouble(price.getText());
            int sStock     = Integer.parseInt(  stock.getText());
            int sMin       = Integer.parseInt(    min.getText());
            int sMax       = Integer.parseInt(    max.getText());

            if (validateStock(sMin, sStock, sMax)) {
                switch(radio) {
                    case "inHouse":
                        int sMachineID = Integer.parseInt(radioed.getText());
                        Inventory.addPart(new InHouse(sID, sName, sPrice, sStock, sMin, sMax, sMachineID));
                        break;
                    case "outsourced":
                        String companyName = radioed.getText();
                        Inventory.addPart(new Outsourced(sID, sName, sPrice, sStock, sMin, sMax, companyName));
                        break;
                }
                toMainView(actionEvent);
            }
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

        if (inHouse.isSelected()) {
            try {
                Integer.parseInt(radioed.getText());
                radioError.setText("");
            } catch (NumberFormatException e) {
                radioError.setText("Machine ID is not an integer");
                allGood = false;
            }
        } else if (outsourced.isSelected()){
            if (radioed.getText().equals("")) {
                radioError .setText("No data in Company name field");
                allGood = false;
            }
        } else {
            radioError .setText("");
        }

        return allGood;
    }

    /** Validate whether min, inventory, and max values are all logically sound */
    public boolean validateStock(int min, int stock, int max) {
        if (min > max) {
            minError.setText("Min must be less than Max");
        }
        else if (stock < min || stock > max) {
            stockError.setText("Inv must be between Min and Max");
        } else {
            minError.setText("");
            stockError.setText("");
        }
        return min <= stock && stock <= max;
    }

    /** Head back to the main view! */
    public void toMainView(ActionEvent actionEvent) throws IOException {
        FXMLLoader mainView = new FXMLLoader(getClass().getResource("/view/mainView.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(mainView.load(), 942, 395);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /** Set the unique input field based on which radio button is selected, and reset any error fields */
    public void setLabel() {
        if (inHouse.isSelected()) {
            radio = "inHouse";
            identifier.setText("Machine ID");
        }
        if (outsourced.isSelected()) {
            radio = "outsourced";
            identifier.setText("Company Name");
        }
        nameError .setText("");
        stockError.setText("");
        priceError.setText("");
        maxError.setText("");
        minError.setText("");
        radioError.setText("");
    }
}
