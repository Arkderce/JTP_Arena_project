package Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    double x = 0;
    double y = 0;

    private MainController mainController;
    private ArenaController arenaController;

    @FXML
    Button button1;

    @FXML
    Button button2;

    /**
     * Quits game.
     */
    @FXML
    void exit(ActionEvent event) {
        Platform.exit();
    }
    /**
     * Loads main game screen.
     */
    @FXML
    void play(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/Fxml/Arena.fxml"));
        AnchorPane anchorPane = null;
        try {
            anchorPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainController.setMainGroup(anchorPane);
    }

    /**
     *Gets directions where the mouse was pressed.
     */
    @FXML
    void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    /**
     *Gets directions where the mouse was moved.
     */
    @FXML
    void dragged(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    /**
     * Sets controller for GUI manipulation.
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
