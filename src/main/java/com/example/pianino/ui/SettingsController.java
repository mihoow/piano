package com.example.pianino.ui;

import com.example.pianino.core.Piano;
import com.example.pianino.core.PianoInstrument;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class SettingsController {

    private final VBox root;
    private final Piano piano;

    public SettingsController(VBox root, Piano piano) {
        this.root = root;
        this.piano = piano;
    }

    public void initialize() {
        root.getChildren().clear();

        Label title = new Label("Ustawienia");
        title.getStyleClass().add("settings-title");

        ComboBox<PianoInstrument> instrumentSelect = new ComboBox<>();
        instrumentSelect.getItems().addAll(PianoInstrument.values());
        instrumentSelect.setValue(PianoInstrument.ACOUSTIC_GRAND);
        instrumentSelect.setMaxWidth(Double.MAX_VALUE);

        instrumentSelect.setOnAction(event -> {
            PianoInstrument instrument = instrumentSelect.getValue();
            piano.setInstrument(instrument);
        });

        ComboBox<String> octaveRangeSelect = new ComboBox<>();
        octaveRangeSelect.getItems().addAll(
                "C1 – B3",
                "C2 – B4",
                "C3 – B5",
                "C4 – B6",
                "C5 – B7"
        );
        octaveRangeSelect.setValue("C3 – B5");
        octaveRangeSelect.setMaxWidth(Double.MAX_VALUE);

        octaveRangeSelect.setOnAction(event -> {
            String value = octaveRangeSelect.getValue();

            switch (value) {
                case "C1 – B3" -> piano.setOctaveOffset((short)-2);
                case "C2 – B4" -> piano.setOctaveOffset((short)-1);
                case "C3 – B5" -> piano.setOctaveOffset((short)0);
                case "C4 – B6" -> piano.setOctaveOffset((short)1);
                case "C5 – B7" -> piano.setOctaveOffset((short)2);
            }
        });

        Slider velocitySlider = new Slider(1, 127, 90);
        velocitySlider.setShowTickLabels(true);
        velocitySlider.setShowTickMarks(true);
        velocitySlider.setMajorTickUnit(32);
        velocitySlider.setBlockIncrement(1);

        velocitySlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            piano.setVelocity(newValue.byteValue());
        });

        root.getChildren().addAll(
                title,

                field("Instrument", instrumentSelect),
                field("Zakres klawiatury", octaveRangeSelect),
                field("Siła nacisku", velocitySlider)
        );
    }

    private VBox field(String labelText, javafx.scene.Node input) {
        Label label = new Label(labelText);
        label.getStyleClass().add("settings-label");

        VBox box = new VBox(6);
        box.getChildren().addAll(label, input);

        return box;
    }
}