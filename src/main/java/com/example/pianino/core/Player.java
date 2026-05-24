package com.example.pianino.core;

import javax.sound.midi.*;
import java.io.File;

public class Player implements AutoCloseable {
    private final Sequence sequence;
    private final Sequencer sequencer;
    private final String filename;

    private Runnable onPlay;
    private Runnable onStop;

    private Player(Sequence sequence, String filename) throws MidiUnavailableException, InvalidMidiDataException {
        this.sequence = sequence;
        this.sequencer = MidiSystem.getSequencer();
        this.filename = filename;

        sequencer.open();
        sequencer.setSequence(sequence);

        sequencer.addMetaEventListener(meta -> {
            if (meta.getType() == 47 && onStop != null) {
                onStop.run();
            }
        });
    }

    public static Player fromFile(File file) throws Exception {
        Sequence sequence = MidiSystem.getSequence(file);
        return new Player(sequence, file.getName());
    }

    public static Player fromSequence(Sequence sequence) throws Exception {
        return new Player(sequence, "");
    }

    public String getFilename() {
        return filename;
    }

    public void start() {
        if (isAtEnd()) return;

        sequencer.start();

        if (onPlay != null) {
            onPlay.run();
        }
    }

    public void stop() {
        sequencer.stop();

        if (onStop != null) {
            onStop.run();
        }
    }

    public void restart() {
        stop();
        sequencer.setTickPosition(0);
        start();
    }

    public boolean isPlaying() {
        return sequencer.isRunning();
    }

    public boolean isAtEnd() {
        return sequencer.getTickPosition() >= sequence.getTickLength();
    }

    /**
     * Returns total song duration in seconds.
     */
    public double getDuration() {
        return sequence.getMicrosecondLength() / 1_000_000.0;
    }

    /**
     * Returns already played time in seconds.
     */
    public double getPlayedDuration() {
        return sequencer.getMicrosecondPosition() / 1_000_000.0;
    }

    public void setOnPlay(Runnable onPlay) {
        this.onPlay = onPlay;
    }

    public void setOnStop(Runnable onStop) {
        this.onStop = onStop;
    }

    public Sequence getSequence() {
        return sequence;
    }

    public long getTickPosition() {
        return sequencer.getTickPosition();
    }

    @Override
    public void close() {
        sequencer.stop();
        sequencer.close();

        if (onStop != null) {
            onStop.run();
        }
    }
}