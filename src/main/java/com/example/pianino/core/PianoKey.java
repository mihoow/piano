package com.example.pianino.core;

public enum PianoKey {
    C3(48, "C3"),
    C_SHARP3(49, "C#3"),
    D3(50, "D3"),
    D_SHARP3(51, "D#3"),
    E3(52, "E3"),
    F3(53, "F3"),
    F_SHARP3(54, "F#3"),
    G3(55, "G3"),
    G_SHARP3(56, "G#3"),
    A3(57, "A3"),
    A_SHARP3(58, "A#3"),
    B3(59, "B3"),

    C4(60, "C4"),
    C_SHARP4(61, "C#4"),
    D4(62, "D4"),
    D_SHARP4(63, "D#4"),
    E4(64, "E4"),
    F4(65, "F4"),
    F_SHARP4(66, "F#4"),
    G4(67, "G4"),
    G_SHARP4(68, "G#4"),
    A4(69, "A4"),
    A_SHARP4(70, "A#4"),
    B4(71, "B4"),

    C5(72, "C5"),
    C_SHARP5(73, "C#5"),
    D5(74, "D5"),
    D_SHARP5(75, "D#5"),
    E5(76, "E5"),
    F5(77, "F5"),
    F_SHARP5(78, "F#5"),
    G5(79, "G5"),
    G_SHARP5(80, "G#5"),
    A5(81, "A5"),
    A_SHARP5(82, "A#5"),
    B5(83, "B5");

    private final int midi;
    private final String label;

    PianoKey(int midi, String label) {
        this.midi = midi;
        this.label = label;
    }

    public static PianoKey fromMidi(int midi) {
        for (PianoKey key : values()) {
            if (key.getMidi() == midi) {
                return key;
            }
        }

        return null;
    }

    public int getMidi() {
        return midi;
    }

    public String getLabel() {
        return label;
    }

    public boolean isSharp() {
        return label.contains("#");
    }

    public String getToneLabel() {
        return label.replaceAll("\\d", "");
    }

    @Override
    public String toString() {
        return label;
    }
}
