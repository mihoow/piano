package com.example.pianino.core;

import javax.sound.midi.*;

public class Piano implements Instrument {
    private PianoInstrument instrument;
    private byte velocity;
    private short octaveOffset;

    private final MidiChannel synthChannel;

    public Piano() throws MidiUnavailableException {
        instrument = PianoInstrument.ACOUSTIC_GRAND;
        velocity = 90;
        octaveOffset = 0;

        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        MidiChannel[] channels = synth.getChannels();

        synthChannel = channels[channels.length - 1];
        synthChannel.programChange(instrument.getProgram());
    }

    public PianoInstrument getInstrument() {
        return instrument;
    }

    public void setInstrument(PianoInstrument instrument) {
        this.instrument = instrument;
        synthChannel.programChange(instrument.getProgram());
    }

    public int getVelocity() {
        return (int)velocity;
    }

    public void setVelocity(int velocity) {
        if (velocity < 0 || velocity > 127) {
            throw new IllegalArgumentException("Siła nacisku musi być między 0 a 127");
        }
        this.velocity = (byte)velocity;
    }

    public short getOctaveOffset() {
        return octaveOffset;
    }

    public void setOctaveOffset(short octaveOffset) {
        if (octaveOffset < -2 || octaveOffset > 2) {
            throw new IllegalArgumentException("Przesunięcie oktawy musi być między -2 a 2");
        }
        this.octaveOffset = octaveOffset;
    }

    public int noteOn(PianoKey key) {
        int midi = key.getMidi() + octaveOffset * 12;
        synthChannel.noteOn(midi, velocity);

        return midi;
    }

    public int noteOff(PianoKey key) {
        int midi = key.getMidi() + octaveOffset * 12;
        synthChannel.noteOff(midi);

        return midi;
    }
}
