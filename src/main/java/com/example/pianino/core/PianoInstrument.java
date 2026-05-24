package com.example.pianino.core;

public enum PianoInstrument {
    ACOUSTIC_GRAND(0, "Acoustic Grand"),
    BRIGHT_ACOUSTIC(1, "Bright Acoustic"),
    ELECTRIC_GRAND(2, "Electric Grand"),
    HONKY_TONK(3, "Honky-Tonk"),
    ELECTRIC_PIANO_1(4, "Electric Piano 1"),
    ELECTRIC_PIANO_2(5, "Electric Piano 2"),
    HARPSICHORD(6, "Harpsichord"),
    CLAV(7, "Clav");

    private int program;
    private String displayName;

    PianoInstrument(int program, String displayName) {
        this.program = program;
        this.displayName = displayName;
    }

    public int getProgram() {
        return program;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
