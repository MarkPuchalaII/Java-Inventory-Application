package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.util.Optional;

/** The Modify Part view allows a user to modify a part's values */
public class ModifyPartView {

    public static Part modifyPart;
    public int updateID = Inventory.getAllParts().indexOf(modifyPart);

    public Button saveProductBtn;
    public Button cancel;
    public ToggleGroup partType;
    public RadioButton inHouse;
    public RadioButton outsourced;
    public Text identifier;

    public TextField id;
    public TextField name;
    public TextField stock;
    public TextField price;
    public TextField max;
    public TextField min;
    public TextField radioed;

    public Text nameError;
    public Text stockError;
    public Text priceError;
    public Text maxError;
    public Text minError;
    public Text radioError;

    private String radio = "inHouse";

    /** Initialize the Modify Part View, with relevent text fields filled in  */
    public void initialize() {
        id.setText(String.join("", String.valueOf(modifyPart.getId()), " - Disabled"));
        name.setText(modifyPart.getName());
        stock.setText(String.valueOf(modifyPart.getStock()));
        price.setText(String.valueOf(modifyPart.getPrice()));
        max.setText(String.valueOf(modifyPart.getMax()));
        min.setText(String.valueOf(modifyPart.getMin()));
        setRadio();
    }

    /** Save our new part info */
    public void save(ActionEvent actionEvent) throws IOException {
        if (validate()) {
            int    sID    =               modifyPart.getId()   ;
            String sName  =                     name.getText() ;
            int    sStock = Integer.parseInt(  stock.getText());
            double sPrice = Double.parseDouble(price.getText());
            int    sMax   = Integer.parseInt(    max.getText());
            int    sMin   = Integer.parseInt(    min.getText());


            if (validateStock(sMin, sStock, sMax)) {
                modifyPart.setId(sID);
                modifyPart.setName(sName);
                modifyPart.setPrice(sPrice);
                modifyPart.setStock(sStock);
                modifyPart.setMin(sMin);
                modifyPart.setMax(sMax);

                switch (radio) {
                    case "inHouse":
                        int sMachineID = Integer.parseInt(radioed.getText());
                        if (modifyPart instanceof Outsourced) {
                            modifyPart = new InHouse(sID, sName, sPrice, sStock, sMin, sMax, sMachineID);
                        } else {
                            ((InHouse) modifyPart).setMachineId(sMachineID);
                        }
                        break;
                    case "outsourced":
                        String companyName = radioed.getText();
                        if (modifyPart instanceof InHouse) {
                            modifyPart = new Outsourced(sID, sName, sPrice, sStock, sMin, sMax, companyName);
                        } else {
                            ((Outsourced) modifyPart).setCompanyName(companyName);
                        }
                        break;
                }
                Inventory.updatePart(updateID, modifyPart);
                toMainView(actionEvent);
            }
        }
    }

    /** Let the main view set what part is being modified */
    public static void setPart(Part part) {
        modifyPart = part;
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

    /** Close out of the Modify Part view without saving anything */
    public void cancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to leave?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
        toMainView(actionEvent);
        }
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

    /** Set the initial radio button during initialization */
    public void setRadio() {
        if (modifyPart instanceof InHouse) {
            inHouse.setSelected(true);
        }
        if (modifyPart instanceof Outsourced) {
            outsourced.setSelected(true);
        }
        setLabel();
    }

    /** Set the unique input field based on which radio button is selected, and reset any error fields */
    public void setLabel() {

        if (inHouse.isSelected()) {
            radio = "inHouse";
            identifier.setText("Machine ID");
            if (modifyPart instanceof Outsourced) {
                radioed.setText("");
            } else {
                radioed.setText(String.valueOf(((InHouse) modifyPart).getMachineId()));
            }
        }
        if (outsourced.isSelected()) {
            identifier.setText("Company Name");
            radio = "outsourced";
            if (modifyPart instanceof InHouse) {
                radioed.setText("");
            } else {
                radioed.setText(((Outsourced) modifyPart).getCompanyName());
            }
        }

        nameError .setText("");
        stockError.setText("");
        priceError.setText("");
        maxError.setText("");
        minError.setText("");
        radioError.setText("");
    }
}
