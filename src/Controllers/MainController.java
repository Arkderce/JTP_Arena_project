package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainController {

    @FXML
    AnchorPane mainAnchorPane;

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
        setMainAnchorPane(anchorPane);
        mainAnchorPane.getStylesheets().add(css);
    }

    public void setMainAnchorPane(AnchorPane anchorPane){
        mainAnchorPane.getChildren().clear();
        mainAnchorPane.getStylesheets().clear();
        mainAnchorPane.getChildren().add(anchorPane);
    }
}
