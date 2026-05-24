package com.example.pianino.core;

public class SilentInstrument implements Instrument {
    @Override
    public int noteOn(PianoKey key) {
        return key.getMidi();
    }

    @Override
    public int noteOff(PianoKey key) {
        return key.getMidi();
    }
}
