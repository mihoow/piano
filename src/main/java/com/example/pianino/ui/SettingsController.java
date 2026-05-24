package com.example.pianino.ui;

import com.example.pianino.core.Piano;
import com.example.pianino.core.PianoInstrument;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.Map;

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
        instrumentSelect.setValue(piano.getInstrument());
        instrumentSelect.setMaxWidth(Double.MAX_VALUE);

        instrumentSelect.setOnAction(event -> {
            PianoInstrument instrument = instrumentSelect.getValue();
            piano.setInstrument(instrument);
        });

        Map<Short, String> octaveRanges = new LinkedHashMap<>();
        octaveRanges.put((short) -2, "C1 – B3");
        octaveRanges.put((short) -1, "C2 – B4");
        octaveRanges.put((short) 0,  "C3 – B5");
        octaveRanges.put((short) 1,  "C4 – B6");
        octaveRanges.put((short) 2,  "C5 – B7");

        ComboBox<String> octaveRangeSelect = new ComboBox<>();
        octaveRangeSelect.getItems().addAll(octaveRanges.values());
        octaveRangeSelect.setValue(octaveRanges.get(piano.getOctaveOffset()));
        octaveRangeSelect.setMaxWidth(Double.MAX_VALUE);

        octaveRangeSelect.setOnAction(event -> {
            String curr = octaveRangeSelect.getValue();
            octaveRanges.forEach((offset, label) -> {
                if (label.equals(curr)) {
                    piano.setOctaveOffset(offset);
                }
            });
        });

        Slider velocitySlider = new Slider(1, 127, piano.getVelocity());
        velocitySlider.setShowTickLabels(true);
        velocitySlider.setShowTickMarks(true);
        velocitySlider.setMajorTickUnit(32);
        velocitySlider.setBlockIncrement(1);

        velocitySlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            piano.setVelocity(newValue.intValue());
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