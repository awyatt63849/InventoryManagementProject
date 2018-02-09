package Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.Optional;

public class MainController {

    /* Here are references to the three lists in the main scene: */

    private ListView<Product> productList;
    private ListView<Colour> productColourList;
    private ListView<Colour> colourList;

    /* Here are the vital constructs required for accessing/organising all the data: */

    private DatabaseConnection database;
    private ArrayList<Colour> allColours = new ArrayList<>();
    private ArrayList<ProductColour> currentColours = new ArrayList<>();

    /**
     * Here is the constructor for the MainController class which receives references
     * to the three lists in the main scene, sets up the database connection and
     * finally populates the lists which the initial data.
     */

    public MainController(ListView<Product> productList, ListView<Colour> productColourList, ListView<Colour> colourList) {

        System.out.println("Initialising main controller...");

        this.productList = productList;
        this.productColourList = productColourList;
        this.colourList = colourList;

        database = new DatabaseConnection("Database.db");
        updateLists(0, 0);

    }

    /**
     * This method needs to be called anytime the lists are changed so that the
     * view is always up to date.
     */

    @SuppressWarnings("Duplicates")
    public void updateLists(int selectedProductId, int selectedColourId) {

        allColours.clear();
        ColoursService.selectAll(allColours, database);
        colourList.setItems(FXCollections.observableArrayList(allColours));

        productList.getItems().clear();
        ProductService.selectAll(productList.getItems(), database);

        if (selectedProductId != 0) {
            for (int n = 0; n < productList.getItems().size(); n++) {
                if (productList.getItems().get(n).getId() == selectedProductId) {
                    productList.getSelectionModel().select(n);
                    productList.getFocusModel().focus(n);
                    productList.scrollTo(n);
                    break;
                }
            }
        }

        if (selectedColourId != 0) {
            for (int n = 0; n < colourList.getItems().size(); n++) {
                if (colourList.getItems().get(n).getId() == selectedColourId) {
                    colourList.getSelectionModel().select(n);
                    colourList.getFocusModel().focus(n);
                    colourList.scrollTo(n);
                    break;
                }
            }
        }
    }

    /**
     * This method is called every time the selected colour is changed. It loads the
     * colour's Colours and makes sure the applied and available Colours lists are
     * correctly populated.
     */

    public void productSelected(Product selectedProduct)
    {
        currentColours.clear();

        ArrayList<Integer> currentToppingIds = new ArrayList<>();

        if (selectedProduct != null) {
            ProductService.selectProductColours(selectedProduct, currentColours, database);
            for (ProductColour pt : currentColours) {
                currentToppingIds.add(pt.getColourId());
            }
        }

        productColourList.getItems().clear();
        colourList.getItems().clear();

        for (Colour t: allColours) {
            if (currentToppingIds.contains(t.getId())) {
                productColourList.getItems().add(t);
            }
            else {
                colourList.getItems().add(t);
            }
        }

    }

    /**
     * This displays a dialog box asking for the name of a new colour and then creates it.
     */

    public void createNewProduct() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Product");
        dialog.setHeaderText(null);
        dialog.setContentText("Product name:");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().equals("")){
            Product newProduct = new Product(0, result.get());
            ProductService.save(newProduct, database);

            Colour selectedColour = colourList.getSelectionModel().getSelectedItem();
            updateLists(database.lastNewId(), selectedColour != null ? selectedColour.getId() : 0);
        }
        else {
            displayError("No name provided.");
        }

    }

    /**
     * This displays a confirmation box before subsequently deleting the selected colour.
     */

    public void deleteProduct() {

        Product selectedProduct = productList.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            displayError("No Product selected.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Product");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete " + selectedProduct + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            ProductService.deleteById(selectedProduct.getId(), database);
            ProductService.deleteProductColoursByProductId(selectedProduct.getId(), database);

            Colour selectedColour = colourList.getSelectionModel().getSelectedItem();
            updateLists(0, selectedColour != null ? selectedColour.getId() : 0);
        }

    }

    /**
     * This displays a dialog box asking for the name of a new Colour and then creates it.
     */

    public void createNewColour() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add new Colour");
        dialog.setHeaderText(null);
        dialog.setContentText("Colour:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().equals("")){
            Colour newColour = new Colour(0, result.get());
            ColoursService.save(newColour, database);

            Product selectedProduct = productList.getSelectionModel().getSelectedItem();
            updateLists(selectedProduct != null ? selectedProduct.getId() : 0, database.lastNewId());
        }
        else {
            displayError("No name provided.");
        }

    }

    /**
     * This displays a confirmation box before subsequently deleting the selected Colour.
     */

    public void deleteColour() {

        Colour selectedColour = colourList.getSelectionModel().getSelectedItem();
        if (selectedColour == null) {
            displayError("No colour selected.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Colour");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete " + selectedColour + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            ColoursService.deleteById(selectedColour.getId(), database);
            ColoursService.deleteProductColoursByProductId(selectedColour.getId(), database);

            Product selectedProduct = productList.getSelectionModel().getSelectedItem();
            updateLists(selectedProduct != null ? selectedProduct.getId() : 0, 0);
        }

    }

    /**
     * This creates a new entry in the many-to-many ProductColour table.
     */

    public void applyColour() {

        Product selectedProduct = productList.getSelectionModel().getSelectedItem();
        Colour selectedColour = colourList.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            displayError("No Product has been selected.");
            return;
        }
        if (selectedColour == null) {
            displayError("No Product has been selected.");
            return;
        }

        ProductColour newColour = new ProductColour(selectedProduct.getId(), selectedColour.getId());
        ProductService.saveProductColour(newColour, database);
        updateLists(selectedProduct != null ? selectedProduct.getId() : 0, 0);

    }

    /**
     * This deletes a new entry from the many-to-many ProductColour table.
     */

    @SuppressWarnings("Duplicates")
    public void removeColour() {

        Product selectedProduct = productList.getSelectionModel().getSelectedItem();
        Colour selectedProductColour = productColourList.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            displayError("No Product has been selected.");
            return;
        }
        if (selectedProductColour == null) {
            displayError("No Colour has been selected.");
            return;
        }

        ProductService.deleteProductColour(selectedProduct.getId(), selectedProductColour.getId(), database);
        updateLists(selectedProduct != null ? selectedProduct.getId() : 0, selectedProductColour.getId());

    }

    /**
     * This displays a confirmation dialog box and if appropriate closes the database connection and exits.
     */

    public void exitPrompt(WindowEvent we) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Inventory Management Project");
        alert.setHeaderText("Are you sure you want to exit?");

        Optional result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            database.disconnect();
            System.exit(0);
        } else {
            we.consume();
        }

    }

    /**
     * A simple method to display an error message.
     */

    private void displayError(String errorMessage) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();

    }

}
