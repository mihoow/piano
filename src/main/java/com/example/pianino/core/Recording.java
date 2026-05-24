package com.example.pianino.core;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

public class Recording implements Instrument, AutoCloseable {

    private static final int PPQ = 480;
    private static final int BPM = 120;

    private static final double MILLISECONDS_PER_MINUTE = 60_000.0;
    private static final int MICROSECONDS_PER_MINUTE = 60_000_000;

    private final Piano piano;
    private final Sequence sequence;
    private final Track track;

    private final long startedAt;
    private Long stoppedAt = null;

    public Recording(Piano piano) throws InvalidMidiDataException {
        this.piano = piano;
        this.sequence = new Sequence(Sequence.PPQ, PPQ);
        this.track = sequence.createTrack();
        this.startedAt = System.currentTimeMillis();

        addTempoEvent();
    }

    @Override
    public int noteOn(PianoKey key) {
        if (isStopped()) {
            return -1;
        }

        int midi = piano.noteOn(key);
        addMidiEvent(ShortMessage.NOTE_ON, midi, piano.getVelocity());

        return midi;
    }

    @Override
    public int noteOff(PianoKey key) {
        if (isStopped()) {
            return -1;
        }

        int midi = piano.noteOff(key);
        addMidiEvent(ShortMessage.NOTE_OFF, midi, 0);

        return midi;
    }

    private void addMidiEvent(int command, int midi, int velocity) {
        try {
            ShortMessage message = new ShortMessage();
            message.setMessage(command, 0, midi, velocity);

            track.add(new MidiEvent(message, currentTick()));
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    private long currentTick() {
        long elapsedMs = System.currentTimeMillis() - startedAt;

        double msPerQuarterNote = MILLISECONDS_PER_MINUTE / BPM;
        double ticksPerMs = PPQ / msPerQuarterNote;

        return Math.round(elapsedMs * ticksPerMs);
    }

    private void addTempoEvent() throws InvalidMidiDataException {
        int microsecondsPerQuarterNote = MICROSECONDS_PER_MINUTE / BPM;

        byte[] data = {
                (byte) ((microsecondsPerQuarterNote >> 16) & 0xFF),
                (byte) ((microsecondsPerQuarterNote >> 8) & 0xFF),
                (byte) (microsecondsPerQuarterNote & 0xFF)
        };

        MetaMessage tempoMessage = new MetaMessage();
        tempoMessage.setMessage(0x51, data, 3);

        track.add(new MidiEvent(tempoMessage, 0));
    }

    public Sequence stop() {
        if (stoppedAt == null) {
            stoppedAt = System.currentTimeMillis();
        }

        return sequence;
    }

    public boolean isStopped() {
        return stoppedAt != null;
    }

    /**
     * Returns recording duration in seconds.
     */
    public double getDuration() {
        long end = stoppedAt != null ? stoppedAt : System.currentTimeMillis();
        return (end - startedAt) / 1000.0;
    }


    public void writeToFile(File file) throws IOException {
        stop();
        MidiSystem.write(sequence, 1, file);
    }

    @Override
    public void close() {
        stop();
    }
}