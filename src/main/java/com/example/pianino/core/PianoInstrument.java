package com.example.pianino.core;

public enum PianoInstrument {
    ACOUSTIC_GRAND(0, "Fortepian koncertowy"),
    BRIGHT_ACOUSTIC(1, "Fortepian 2"),
    ELECTRIC_GRAND(2, "Fortepian elektryczny"),
    HONKY_TONK(3, "Pianino honky-tonk"),
    ELECTRIC_PIANO_1(4, "Pianino elektryczne 1"),
    ELECTRIC_PIANO_2(5, "Pianino elektryczne 2"),
    HARPSICHORD(6, "Klawesyn"),
    CLAV(7, "Klawinet");

    private final int program;
    private final String displayName;

    PianoInstrument(int program, String displayName) {
        this.program = program;
        this.displayName = displayName;
    }

    public int getProgram() {
        return program;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
