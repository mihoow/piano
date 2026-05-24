package com.example.pianino.ui;

import com.example.pianino.core.PianoKey;

public interface VirtualKeyboardListener {

    void onPressed(PianoKey key);

    void onReleased(PianoKey key);
}