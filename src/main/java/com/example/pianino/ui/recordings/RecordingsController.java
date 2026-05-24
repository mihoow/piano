package com.example.pianino.ui.recordings;

import com.example.pianino.core.Piano;
import com.example.pianino.ui.VirtualKeyboard;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

public class RecordingsController {

    private final VBox root;
    private final Piano piano;
    private final VirtualKeyboard virtualKeyboard;

    public RecordingsController(VBox root, Piano piano, VirtualKeyboard virtualKeyboard) {
        this.root = root;
        this.piano = piano;
        this.virtualKeyboard = virtualKeyboard;
    }

    public void initialize(Window window) {
        root.getChildren().clear();

        root.setAlignment(Pos.CENTER);
        root.setSpacing(16);

        RecordingsManager recordingsManager = new RecordingsManager(piano, virtualKeyboard, window);
        AbstractButton recordButton = new RecordButton(root, recordingsManager);
        AbstractButton playButton = new PlayButton(root, recordingsManager);

        recordButton.render();
        playButton.render();
    }
}