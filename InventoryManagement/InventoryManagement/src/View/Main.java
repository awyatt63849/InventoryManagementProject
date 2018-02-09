package View;

import Controller.MainController;
import Model.Colour;
import Model.Product;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main  {

    private static MainController controller;

    private static ListView<Product> productList = new ListView<>();
    private static ListView<Colour> productColourList = new ListView<>();
    private static ListView<Colour> colourList = new ListView<>();

    /**
     * Here is the start point of the program.
     * A controller object is instantiated and the JavaFX process is launched.
     */


    /**
     * This is the method automatically called by the JavaFX process to create
     * the stage and the scene and populate them. The events (e.g. button clicks)
     * are all wired up to call the appropriate controller methods.
     */

    public static void openMainScene(Stage stage) {

        controller = new MainController(productList, productColourList, colourList);

        /* First, create the root BorderPane for the scene and set up the stage. */

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add("Resources/stylesheet.css");
        stage.setTitle("Inventory Management Project");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setOnCloseRequest((WindowEvent we) -> controller.exitPrompt(we));
        stage.show();

        /* Top section of root BorderPane, containing the title. */

        HBox topPane = new HBox(70);
        topPane.setPadding(new Insets(20));
        Label titleLabel = new Label("Inventory");
        titleLabel.getStyleClass().add("title");
        topPane.getChildren().add(titleLabel);
        root.setTop(topPane);
        topPane.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(topPane, Pos.TOP_CENTER);


        /* Left section of root BorderPane, containing the list of products. */

        VBox leftPane = new VBox(20);
        leftPane.setPadding(new Insets(30));
        Label productHeading = new Label("Products:");
        productHeading.getStyleClass().add("heading");
        leftPane.getChildren().add(productHeading);
        productList.setPrefWidth(280);
        productList.setPrefHeight(500);
        productList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> controller.productSelected(newValue)
        );
        leftPane.getChildren().add(productList);
        root.setLeft(leftPane);
        leftPane.setAlignment(Pos.TOP_CENTER);
        BorderPane.setAlignment(leftPane, Pos.CENTER_LEFT);

        /* Centre section of root BorderPane, containing the list of Colours on the current product. */

        VBox centerPane = new VBox(20);
        centerPane.setPadding(new Insets(30));
        Label productColourHeading = new Label("Colours In Stock:");
        productColourHeading.getStyleClass().add("heading");
        centerPane.getChildren().add(productColourHeading);
        productColourList.setMaxWidth(280);
        productColourList.setPrefHeight(400);
        centerPane.getChildren().add(productColourList);
        Button applyColourButton = new Button("Add Colour");
        applyColourButton.getStyleClass().add("add_button");
        applyColourButton.setOnAction((event) -> controller.applyColour());
        centerPane.getChildren().add(applyColourButton);
        Button removeColourButton = new Button("Remove Colour");
        removeColourButton.getStyleClass().add("delete_button");
        removeColourButton.setOnAction((event) -> controller.removeColour());
        centerPane.getChildren().add(removeColourButton);
        root.setCenter(centerPane);
        centerPane.setAlignment(Pos.TOP_CENTER);
        BorderPane.setAlignment(centerPane, Pos.CENTER);

        /* Right section of root BorderPane, containing the list of Colours available. */

        VBox rightPane = new VBox(20);
        rightPane.setPadding(new Insets(30));
        Label colourHeading = new Label("Available Colours:");
        colourHeading.getStyleClass().add("heading");
        rightPane.getChildren().add(colourHeading);
        colourList.setPrefWidth(280);
        colourList.setPrefHeight(500);
        rightPane.getChildren().add(colourList);
        root.setRight(rightPane);
        rightPane.setAlignment(Pos.TOP_CENTER);
        BorderPane.setAlignment(rightPane, Pos.CENTER_RIGHT);

        /* Bottom section of root BorderPane, containing the buttons to create and delete products and Colours. */

        HBox bottomPane = new HBox(20);
        bottomPane.setAlignment(Pos.CENTER_LEFT);
        bottomPane.setPadding(new Insets(30));
        HBox bottomPaneRight = new HBox(20);
        HBox.setHgrow(bottomPaneRight, Priority.ALWAYS);
        bottomPaneRight.setAlignment(Pos.CENTER_RIGHT);
        Button addProductButton = new Button("Add New Product");
        addProductButton.getStyleClass().add("add_button");
        addProductButton.setOnAction((event) -> controller.createNewProduct());
        bottomPane.getChildren().add(addProductButton);
        Button deleteProductButton = new Button("Delete Product");
        deleteProductButton.getStyleClass().add("delete_button");
        deleteProductButton.setOnAction((event) -> controller.deleteProduct());
        bottomPane.getChildren().add(deleteProductButton);
        Button addColourButton = new Button("Add New Colour");
        addColourButton.getStyleClass().add("add_button");
        addColourButton.setOnAction((event) -> controller.createNewColour());
        bottomPaneRight.getChildren().add(addColourButton);
        Button deleteColourButton = new Button("Remove Colour");
        deleteColourButton.getStyleClass().add("delete_button");
        deleteColourButton.setOnAction((event) -> controller.deleteColour());
        bottomPaneRight.getChildren().add(deleteColourButton);
        bottomPane.getChildren().add(bottomPaneRight);
        root.setBottom(bottomPane);
        BorderPane.setAlignment(bottomPane, Pos.BOTTOM_CENTER);

    }

}