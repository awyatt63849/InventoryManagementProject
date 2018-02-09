package View;

import Controller.MainMenuController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainMenu extends Application {

    private static MainMenuController mmc;

    @Override
    public void start(Stage stage) throws Exception {

        mmc = new MainMenuController(stage);

        stage.setScene(createScene());
        stage.setTitle("Inventory Management Log In");

        stage.show();

    }

    public static Scene createScene (){

        //Pane root = new Pane();
        //Scene MainScene = new Scene(root, 1280, 900);

        //Line line1 = new Line(0, 50, 1290, 50);
        //root.getChildren().add(line1);

        //Line line2 = new Line(0, 850, 1290, 850);
        //root.getChildren().add(line2);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 300, 275);

        Text scenetitle = new Text("Inventory Management");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 22));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button btn = new Button("Log in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        btn.setOnAction((ActionEvent ae) -> mmc.openMain());

        return scene;

    }

    public static void main(String[] args) {



        launch(args);
    }

}
