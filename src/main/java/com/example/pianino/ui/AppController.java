package com.example.pianino.ui;

import com.example.pianino.core.Piano;
import com.example.pianino.ui.recordings.RecordingsController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javax.sound.midi.MidiUnavailableException;

public class AppController {
    private Piano piano;
    private VirtualKeyboard virtualKeyboard;
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

    public AppController() {
        try {
            piano = new Piano();
            virtualKeyboard = new VirtualKeyboard(piano);
        } catch (MidiUnavailableException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd MIDI");
            alert.setHeaderText("Nie udało się uruchomić systemu MIDI");
            alert.setContentText("""
                Wystąpił problem podczas inicjalizacji MIDI.
                
                Aplikacja zostanie zamknięta.
                Spróbuj uruchomić ją ponownie.
            """);

            alert.showAndWait();
            Platform.exit();
        }
    }

    @FXML
    public void initialize() {
        if (piano == null) return;

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