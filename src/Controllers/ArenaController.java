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
        progressBarHealth.setProgress(arena.getHealth());
    }


    public void writeOut(String msg) {
        Platform.runLater(() -> {
            textArea.setText(textArea.getText() + "[" + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()) + "] " + msg + "\n");
        });
    }

    public void changeHealth(int value) {
        if (value >= -100 && value <= 100) {
            progressBarHealth.setProgress(progressBarHealth.getProgress() + value); //KAMIL DAWAJ KOT TO LADNIEJ BD Z LISTENEREM I WOGULE
        }
    }

    public void changeArmour(int value){

    }

    public void changeArmor(int value) {
        if (value >= -100 && value <= 100) {
            progressBarArmor.setProgress(progressBarArmor.getProgress() + value); //KAMIL DAWAJ KOT TO LADNIEJ BD Z LISTENEREM I WOGULE
        }
    }
}
