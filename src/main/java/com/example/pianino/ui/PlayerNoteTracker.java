package com.example.pianino.ui;

import com.example.pianino.core.PianoKey;
import com.example.pianino.core.Player;
import javafx.animation.AnimationTimer;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;

public class PlayerNoteTracker {

    private final Player player;
    private final List<PlayerNoteEvent> noteEvents;

    private BiConsumer<PianoKey, Boolean> onNote;

    private int nextNoteEventIndex = 0;
    private AnimationTimer timer;

    public PlayerNoteTracker(Player player) {
        this.player = player;
        this.noteEvents = extractNoteEvents(player.getSequence());

        player.setOnPlay(this::start);
        player.setOnStop(this::stop);
    }

    /**
     * Sets a listener called during playback when a MIDI note event is reached.
     * First argument is PianoKey within the supported UI range, for example C3-B5.
     * Notes outside that range are ignored.
     * Second argument is true for NOTE_ON and false for NOTE_OFF.
     */
    public void setOnNote(BiConsumer<PianoKey, Boolean> onNote) {
        this.onNote = onNote;
    }

    private void start() {
        stop();

        if (player.getTickPosition() == 0) {
            nextNoteEventIndex = 0;
        }

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };

        timer.start();
    }

    private void stop() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    private void update() {
        long currentTick = player.getTickPosition();

        while (nextNoteEventIndex < noteEvents.size()
                && noteEvents.get(nextNoteEventIndex).tick() <= currentTick) {

            PlayerNoteEvent event = noteEvents.get(nextNoteEventIndex);

            if (onNote != null) {
                onNote.accept(event.pianoKey(), event.noteOn());
            }

            nextNoteEventIndex++;
        }
    }

    private List<PlayerNoteEvent> extractNoteEvents(Sequence sequence) {
        List<PlayerNoteEvent> events = new ArrayList<>();

        for (Track track : sequence.getTracks()) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();

                if (!(message instanceof ShortMessage sm)) {
                    continue;
                }

                int command = sm.getCommand();
                int midi = sm.getData1();
                int velocity = sm.getData2();

                PianoKey pianoKey = PianoKey.fromMidi(midi);

                if (pianoKey == null) {
                    continue;
                }

                if (command == ShortMessage.NOTE_ON && velocity > 0) {
                    events.add(new PlayerNoteEvent(event.getTick(), pianoKey, true));
                }

                if (command == ShortMessage.NOTE_OFF
                        || (command == ShortMessage.NOTE_ON && velocity == 0)) {
                    events.add(new PlayerNoteEvent(event.getTick(), pianoKey, false));
                }
            }
        }

        events.sort(Comparator.comparingLong(PlayerNoteEvent::tick));
        return events;
    }

    private record PlayerNoteEvent(
            long tick,
            PianoKey pianoKey,
            boolean noteOn
    ) {}
}