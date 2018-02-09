package Controller;


import View.Main;
import javafx.stage.Stage;

public class MainMenuController {

    private Stage stage;

    public MainMenuController(Stage stage) {
        this.stage = stage;
    }

    public void openMain() {

        Main.openMainScene(stage);

    }
}