package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Product;

import java.io.IOException;



/** This class creates an interactive Inventory Management System.
 * Javadocs can be found in inventoryApplication/src/javadoc/index.html
 */
public class Main extends Application {

    /** Create the main window & set its title, scene, etc. */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/mainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 942, 395);

        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /** Populate the initial tables with test data, initializing parts & products along the way. */
    private static void addTestData() {
        InHouse brakes = new InHouse(1, "Brakes", 15.00, 10, 1, 20, 1);
        InHouse wheel  = new InHouse(2, "Wheel" , 11.00, 16, 1, 20, 2);
        InHouse seat   = new InHouse(3, "Seat"  , 15.00, 10, 1, 20, 3);

        Product giantBike = new Product(1000, "Giant Bike", 299.99, 5, 1, 20);
        Product tricycle  = new Product(1001, "Tricycle"  , 99.99, 3, 1, 20);

        Inventory.addPart(brakes);
        Inventory.addPart(wheel);
        Inventory.addPart(seat);

        Inventory.addProduct(giantBike);
        Inventory.addProduct(tricycle);

    }

    /** The main program adds test data, then launches the main view
     * FUTURE_ENHANCEMENT
     *  The ability to edit cells from the Main View would be valuable.
     *  This would require extensive backend work to ensure stability,
     *  but would allow more intuitive use of the program.
     *  ALSO: A dark mode should be implemented.
     *  Virtually nothing in the tech industry should exist without a dark mode, these days.
     */
    public static void main(String[] args) {
        addTestData();
        launch();
    }
}