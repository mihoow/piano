package com.example.pianino.core;

public interface Instrument {

    int noteOn(PianoKey key);

    int noteOff(PianoKey key);
}