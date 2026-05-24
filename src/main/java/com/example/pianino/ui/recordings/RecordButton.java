package com.example.pianino.ui.recordings;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;


public class RecordButton extends AbstractButton {
    public RecordButton(VBox root, RecordingsManager manager) {
        super(root, manager, "Rozpocznij nagrywanie");
    }

    @Override
    protected boolean handleMainAction(Button btn) {
        return recordingsManager.startRecording();
    }

    @Override
    protected ButtonGroup createButtonGroup() {
        return new RecordButtonGroup(recordingsManager);
    }
}
