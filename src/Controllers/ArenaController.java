package Controllers;

import Arena.Arena;
import javafx.application.Platform;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ArenaController {

    @FXML
    public Canvas canvas;

    @FXML
    TextArea textArea;

    @FXML
    AnchorPane anchorPane;

    @FXML
    ProgressBar progressBarHealth;

    @FXML
    ProgressBar progressBarArmor;

    private Arena arena = new Arena();

    public void initialize() {
        arena.setController(this);
        arena.start(canvas, anchorPane);
        canvas.setFocusTraversable(true);
        progressBarHealth.setProgress(arena.getHealth()*0.01);
    }


    public void writeOut(String msg) {
        Platform.runLater(() -> {
            textArea.setText(textArea.getText() + "[" + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + "] " + msg + "\n");
            textArea.setScrollTop(Double.MAX_VALUE);
        });
    }

    public void changeHealth(double value) {
        Platform.runLater(() -> {
        if (value >= 0 && value <= 100) {
            progressBarHealth.setProgress(value*0.01);

        }
        });
    }

    public void changeArmor(int value) {
        Platform.runLater(() -> {
        if (value >= 0 && value <= 100) {
            progressBarArmor.setProgress(value*0.01);
        }
        });
    }
}
