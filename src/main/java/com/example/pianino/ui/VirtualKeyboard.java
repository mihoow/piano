package com.example.pianino.ui;

import com.example.pianino.core.Instrument;
import com.example.pianino.core.PianoKey;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class VirtualKeyboard {

    private Instrument instrument;

    private final Set<PianoKey> pressedKeys = EnumSet.noneOf(PianoKey.class);
    private final List<VirtualKeyboardListener> listeners = new ArrayList<>();

    public VirtualKeyboard(Instrument instrument) {
        this.instrument = instrument;
    }

    public void setInstrument(Instrument instrument) {
        this.instrument = instrument;
    }

    public boolean isPressed(PianoKey key) {
        return pressedKeys.contains(key);
    }

    public void press(PianoKey key) {
        if (isPressed(key)) return;

        pressedKeys.add(key);
        instrument.noteOn(key);

        for (VirtualKeyboardListener listener : listeners) {
            listener.onPressed(key);
        }
    }

    public void release(PianoKey key) {
        if (!isPressed(key)) return;

        pressedKeys.remove(key);
        instrument.noteOff(key);

        for (VirtualKeyboardListener listener : listeners) {
            listener.onReleased(key);
        }

    }

    public void addListener(VirtualKeyboardListener listener) {
        listeners.add(listener);
    }

    public void removeListener(VirtualKeyboardListener listener) {
        listeners.remove(listener);
    }
}