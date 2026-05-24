package com.example.pianino.ui;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.Map;

import com.example.pianino.core.Piano;
import com.example.pianino.core.PianoKey;

public class ComputerKeyboardHandler {
    public record KeyMapping(PianoKey pianoKey, String label) {}

    private final VirtualKeyboard virtualKeyboard;

    private final Map<KeyCode, KeyMapping> keyMap = Map.ofEntries(
            Map.entry(KeyCode.Z, new KeyMapping(PianoKey.C3, "Z")),
            Map.entry(KeyCode.S, new KeyMapping(PianoKey.C_SHARP3, "S")),
            Map.entry(KeyCode.X, new KeyMapping(PianoKey.D3, "X")),
            Map.entry(KeyCode.D, new KeyMapping(PianoKey.D_SHARP3, "D")),
            Map.entry(KeyCode.C, new KeyMapping(PianoKey.E3, "C")),

            Map.entry(KeyCode.Q, new KeyMapping(PianoKey.F3, "Q")),
            Map.entry(KeyCode.DIGIT2, new KeyMapping(PianoKey.F_SHARP3, "2")),
            Map.entry(KeyCode.W, new KeyMapping(PianoKey.G3, "W")),
            Map.entry(KeyCode.DIGIT3, new KeyMapping(PianoKey.G_SHARP3, "3")),
            Map.entry(KeyCode.E, new KeyMapping(PianoKey.A3, "E")),
            Map.entry(KeyCode.DIGIT4, new KeyMapping(PianoKey.A_SHARP3, "4")),
            Map.entry(KeyCode.R, new KeyMapping(PianoKey.B3, "R")),

            Map.entry(KeyCode.T, new KeyMapping(PianoKey.C4, "T")),
            Map.entry(KeyCode.DIGIT6, new KeyMapping(PianoKey.C_SHARP4, "6")),
            Map.entry(KeyCode.Y, new KeyMapping(PianoKey.D4, "Y")),
            Map.entry(KeyCode.DIGIT7, new KeyMapping(PianoKey.D_SHARP4, "7")),
            Map.entry(KeyCode.U, new KeyMapping(PianoKey.E4, "U")),

            Map.entry(KeyCode.I, new KeyMapping(PianoKey.F4, "I")),
            Map.entry(KeyCode.DIGIT9, new KeyMapping(PianoKey.F_SHARP4, "9")),
            Map.entry(KeyCode.O, new KeyMapping(PianoKey.G4, "O")),
            Map.entry(KeyCode.DIGIT0, new KeyMapping(PianoKey.G_SHARP4, "0")),
            Map.entry(KeyCode.P, new KeyMapping(PianoKey.A4, "P")),
            Map.entry(KeyCode.MINUS, new KeyMapping(PianoKey.A_SHARP4, "-")),
            Map.entry(KeyCode.OPEN_BRACKET, new KeyMapping(PianoKey.B4, "[")),

            Map.entry(KeyCode.CLOSE_BRACKET, new KeyMapping(PianoKey.C5, "]")),
            Map.entry(KeyCode.F, new KeyMapping(PianoKey.C_SHARP5, "F")),
            Map.entry(KeyCode.V, new KeyMapping(PianoKey.D5, "V")),
            Map.entry(KeyCode.G, new KeyMapping(PianoKey.D_SHARP5, "G")),
            Map.entry(KeyCode.B, new KeyMapping(PianoKey.E5, "B")),

            Map.entry(KeyCode.N, new KeyMapping(PianoKey.F5, "N")),
            Map.entry(KeyCode.J, new KeyMapping(PianoKey.F_SHARP5, "J")),
            Map.entry(KeyCode.M, new KeyMapping(PianoKey.G5, "M")),
            Map.entry(KeyCode.K, new KeyMapping(PianoKey.G_SHARP5, "K")),
            Map.entry(KeyCode.COMMA, new KeyMapping(PianoKey.A5, ",")),
            Map.entry(KeyCode.L, new KeyMapping(PianoKey.A_SHARP5, "L")),
            Map.entry(KeyCode.PERIOD, new KeyMapping(PianoKey.B5, "."))
    );

    public ComputerKeyboardHandler(VirtualKeyboard virtualKeyboard) {
        this.virtualKeyboard = virtualKeyboard;
    }

    public String getMappedKey(PianoKey pianoKey) {
        return keyMap.values()
                .stream()
                .filter(keyMapping -> keyMapping.pianoKey() == pianoKey)
                .map(KeyMapping::label)
                .findFirst()
                .orElse("");
    }

    public void initialize(Scene scene) {
        scene.setOnKeyPressed(event -> {
            KeyMapping mapping = keyMap.get(event.getCode());

            if (mapping != null) {
                virtualKeyboard.press(mapping.pianoKey());
            }
        });

        scene.setOnKeyReleased(event -> {
            KeyMapping mapping = keyMap.get(event.getCode());

            if (mapping != null) {
                virtualKeyboard.release(mapping.pianoKey());
            }
        });
    }
}