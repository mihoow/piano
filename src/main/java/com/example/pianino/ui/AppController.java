package com.example.pianino.ui;

import com.example.pianino.core.Piano;
import com.example.pianino.ui.recordings.RecordingsController;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class AppController {
    private final Piano piano;
    private final VirtualKeyboard virtualKeyboard;
    private PianoKeyboardController pianoKeyboardController;
    private ComputerKeyboardHandler computerKeyboardHandler;
    private SettingsController settingsController;
    private RecordingsController recordingsController;

    @FXML
    private VBox settingsPane;

    @FXML
    private VBox rightPane;

    @FXML
    private Pane pianoPane;

    public AppController() throws Exception {
        piano = new Piano();
        virtualKeyboard = new VirtualKeyboard(piano);
    }

    @FXML
    public void initialize() {
        computerKeyboardHandler = new ComputerKeyboardHandler(virtualKeyboard);
        pianoKeyboardController = new PianoKeyboardController(pianoPane, virtualKeyboard, computerKeyboardHandler);
        settingsController = new SettingsController(settingsPane, piano);
        recordingsController = new RecordingsController(rightPane, piano, virtualKeyboard);

        settingsController.initialize();

        pianoPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene == null) return;

            computerKeyboardHandler.initialize(newScene);
            pianoKeyboardController.initialize(newScene.getWidth());
            recordingsController.initialize(newScene.getWindow());

            newScene.widthProperty().addListener((o, oldW, newW) -> {
                pianoKeyboardController.initialize(newW.doubleValue());
            });
        });
    }
}