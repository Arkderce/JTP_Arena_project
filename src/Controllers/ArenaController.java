package Controllers;

import Arena.Arena;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ArenaController {

    @FXML
    public Canvas canvas;

    @FXML
    public Text level;

    @FXML
    public Text points;

    @FXML
    TextArea textArea;

    @FXML
    AnchorPane anchorPane;

    @FXML
    ProgressBar progressBarHealth;

    @FXML
    ProgressBar progressBarArmor;

    @FXML
    public Button button;

    /**
     *Quits game.
     */
    @FXML
    void pressButton(ActionEvent event) {
        Platform.exit();
    }

    MainController mainController;

    private Arena arena = new Arena();
    /**
     * Initializes controller and binds it to the arena.
     */
    public void initialize() {
        arena.setController(this);
        arena.start(canvas, anchorPane);
        canvas.setFocusTraversable(true);
        progressBarHealth.setProgress(arena.getHealth()*0.01);
        textArea.setEditable(false);
    }

    /**
     * Writes out message with current timestamp on game log.
     *
     * @param msg message for display
     */
    public void writeOut(String msg) {
        Platform.runLater(() -> {
            textArea.setText(textArea.getText() + "[" + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + "] " + msg + "\n");
            textArea.setScrollTop(Double.MAX_VALUE);
        });
    }

    /**
     * Updates GUI healthbar.
     *
     * @param value current players health
     */
    public void changeHealth(double value) {
        Platform.runLater(() -> {
        if (value >= 0 && value <= 100) {
            progressBarHealth.setProgress(value*0.01);

        } else {
            progressBarHealth.setProgress(0);
        }
        });
    }

    /**
     * Updates GUI armorbar.
     *
     * @param value current players armor
     */
    public void changeArmor(int value) {
        Platform.runLater(() -> {
        if (value >= 0 && value <= 100) {
            progressBarArmor.setProgress(value*0.01);
        }
        });
    }

    /**
     * Updates GUI level.
     *
     * @param value current level
     */
    public void setLevel(int value){
        level.setText(String.valueOf(value));
    }

    /**
     * Updates GUI points.
     *
     * @param value current points
     */
    public void setPoints(int value){
        points.setText(String.valueOf(value));
    }

}
