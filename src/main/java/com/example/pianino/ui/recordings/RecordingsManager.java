package com.example.pianino.ui.recordings;

import com.example.pianino.core.Piano;
import com.example.pianino.core.Player;
import com.example.pianino.core.Recording;
import com.example.pianino.core.SilentInstrument;
import com.example.pianino.ui.PlayerNoteTracker;
import com.example.pianino.ui.VirtualKeyboard;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.sound.midi.Sequence;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecordingsManager {

    private final Piano piano;
    private final VirtualKeyboard virtualKeyboard;
    private final Window ownerWindow;

    private final List<PlayerChangeListener> playerChangeListeners = new ArrayList<>();

    private Recording recording;
    private Player player;

    public RecordingsManager(Piano piano, VirtualKeyboard virtualKeyboard, Window ownerWindow) {
        this.piano = piano;
        this.virtualKeyboard = virtualKeyboard;
        this.ownerWindow = ownerWindow;
    }

    public boolean startRecording() {
        try {
            stopPlayer();

            recording = new Recording(piano);
            virtualKeyboard.setInstrument(recording);

            return true;
        } catch (Exception e) {
            throw new RuntimeException("Could not start recording", e);
        }
    }

    public double getRecordingDuration() {
        if (recording == null) {
            return 0;
        }

        return recording.getDuration();
    }

    public boolean stopRecording() {
        if (recording == null) {
            return false;
        }

        Sequence seq = recording.stop();
        long duration = seq.getMicrosecondLength();

        if (duration == 0) {
            return false;
        }

        virtualKeyboard.setInstrument(piano);

        try {
            setPlayer(Player.fromSequence(seq));
            for (PlayerChangeListener listener : playerChangeListeners) {
                listener.onPlayerChanged();
            }

            return true;
        } catch (Exception e) {
            throw new RuntimeException("Could not create player from recording", e);
        }
    }

    public boolean writeRecordingToFile() {
        if (recording == null) {
            return false;
        }

        recording.stop();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Zapisz nagranie");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("MIDI files", "*.mid", "*.midi")
        );
        fileChooser.setInitialFileName("nagranie.mid");

        File file = fileChooser.showSaveDialog(ownerWindow);

        if (file == null) {
            stopRecording();
            return false;
        }

        try {
            recording.writeToFile(file);
            virtualKeyboard.setInstrument(piano);

            setPlayer(Player.fromFile(file));
            for (PlayerChangeListener listener : playerChangeListeners) {
                listener.onPlayerChanged();
            }

            return true;
        } catch (Exception e) {
            throw new RuntimeException("Could not write recording to file", e);
        }
    }

    public boolean playFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik MIDI");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("MIDI files", "*.mid", "*.midi")
        );

        File file = fileChooser.showOpenDialog(ownerWindow);

        if (file == null) {
            return false;
        }

        try {
            setPlayer(Player.fromFile(file));

            return true;
        } catch (Exception e) {
            throw new RuntimeException("Could not play file", e);
        }
    }

    public void startPlayer() {
        if (player != null) {
            player.start();
        }
    }

    public void stopPlayer() {
        if (player != null) {
            player.stop();
        }
    }

    public void restartPlayer() {
        if (player != null) {
            player.restart();
        }
    }

    public boolean isPlaying() {
        return player != null && player.isPlaying();
    }

    public double getPlayedDuration() {
        if (player == null) {
            return 0;
        }

        return player.getPlayedDuration();
    }

    public double getPlayerTotalDuration() {
        if (player == null) {
            return 0;
        }

        return player.getDuration();
    }

    public String getPlayedFilename() {
        if (player == null) return "";

        return player.getFilename();
    }

    public void addPlayerChangeListener(PlayerChangeListener listener) {
        playerChangeListeners.add(listener);
    }

    public void removePlayerChangeListener(PlayerChangeListener listener) {
        playerChangeListeners.remove(listener);
    }

    private void setPlayer(Player newPlayer) {
        if (player != null) {
            player.close();
        }

        player = newPlayer;
        player.setOnPlay(() -> virtualKeyboard.setInstrument(new SilentInstrument()));
        player.setOnStop(() -> virtualKeyboard.setInstrument(piano));

        PlayerNoteTracker noteTracker = new PlayerNoteTracker(newPlayer);
        noteTracker.setOnNote((pianoKey, isNoteOn) -> {
            if (isNoteOn) {
                virtualKeyboard.press(pianoKey);
            } else {
                virtualKeyboard.release(pianoKey);
            }
        });
    }
}