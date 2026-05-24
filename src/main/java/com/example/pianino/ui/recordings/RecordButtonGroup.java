package com.example.pianino.ui.recordings;

import com.example.pianino.ui.icons.RecordIcon;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class RecordButtonGroup extends ButtonGroup {
    private Timeline timer;
    private RecordIcon recordIcon;

    RecordButtonGroup(RecordingsManager manager) {
        super(manager);
        startTimer();
    }

    @Override
    protected Button createLeftButton() {
        Button btn = super.createLeftButton();

        recordIcon = new RecordIcon(true);
        btn.setGraphic(recordIcon.getGraphic());

        btn.setOnAction(e -> {
            recordIcon.stopAnimation();
            boolean ok = recordingsManager.stopRecording();

            if (!ok) {
                handleClose();
                return;
            }

            cleanup();

            centerBtn.setText("Zapisz do pliku");
        });

        return btn;
    }

    @Override
    protected Button createCenterButton() {
        Button btn = super.createCenterButton();
        btn.setText("00:00");

        btn.setOnAction(e -> {
            recordIcon.stopAnimation();
            boolean ok = recordingsManager.writeRecordingToFile();

            if (!ok) return;

            handleClose();
        });

        return btn;
    }

    private void startTimer() {
        timer = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> updateDuration()),
                new KeyFrame(Duration.millis(800))
        );

        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void updateDuration() {
        centerBtn.setText(formatTime(recordingsManager.getRecordingDuration()));
    }

    private String formatTime(double seconds) {
        int total = (int) Math.ceil(seconds);
        return String.format("%02d:%02d", total / 60, total % 60);
    }

    @Override
    public void cleanup() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }

        if (recordIcon != null) {
            recordIcon.stopAnimation();
        }
    }
}
