package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainController {

    @FXML
    Group mainGroup;

    @FXML
    public void initialize(){
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/Fxml/Menu.fxml"));
        String css = this.getClass().getResource("/Css/MenuDisplay.css").toExternalForm();

        AnchorPane anchorPane = null;
        try {
            anchorPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MenuController menuController = loader.getController();
        menuController.setMainController(this);
        setMainGroup(anchorPane);
        mainGroup.getStylesheets().add(css);
    }

    public void setMainGroup(AnchorPane anchorPane){
        mainGroup.getChildren().clear();
        mainGroup.getStylesheets().clear();
        mainGroup.getChildren().add(anchorPane);
    }
}
