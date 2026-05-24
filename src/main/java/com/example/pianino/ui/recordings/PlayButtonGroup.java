package com.example.pianino.ui.recordings;

import com.example.pianino.ui.icons.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Objects;

public class PlayButtonGroup extends ButtonGroup {
    private final Pane container;
    private Timeline timer;

    public PlayButtonGroup(RecordingsManager manager) {
        super(manager);
        String filename = manager.getPlayedFilename();

        container = new Pane();
        Label label = new Label(Objects.equals(filename, "") ? "Nagranie" : filename);
        label.getStyleClass().add("recording-source-label");
        label.setMouseTransparent(true);
        group.setLayoutX(0);
        group.setLayoutY(0);
        label.setLayoutX(6);
        label.setLayoutY(50);
        container.getChildren().addAll(group, label);
        container.setPrefSize(240, 48);
        container.setMinSize(240, 48);
        container.setMaxSize(240, 48);

        startTimer();
        updateDuration();
    }

    @Override
    public Pane getRoot() {
        return container;
    }

    @Override
    protected Button createLeftButton() {
        Button btn = super.createLeftButton();

        Icon playIcon = new PlayIcon();
        btn.setGraphic(playIcon.getGraphic());

        btn.setOnAction(e -> {
            if (recordingsManager.getPlayedDuration() == recordingsManager.getPlayerTotalDuration()) {
                recordingsManager.restartPlayer();
            } else if (recordingsManager.isPlaying()) {
                recordingsManager.stopPlayer();
            } else {
                recordingsManager.startPlayer();
            }

            updatePlayIcon();
            updateDuration();
        });

        return btn;
    }

    @Override
    protected Button createCenterButton() {
        Button btn = super.createCenterButton();
        btn.setText("00:00 / 00:00");

        return btn;
    }

    private void startTimer() {
        timer = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> {
                    updateDuration();
                    updatePlayIcon();
                }),
                new KeyFrame(Duration.millis(800))
        );

        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void updateDuration() {
        centerBtn.setText(
                formatTime(recordingsManager.getPlayedDuration())
                        + " / "
                        + formatTime(recordingsManager.getPlayerTotalDuration())
        );
    }

    private String formatTime(double seconds) {
        int total = (int) Math.ceil(seconds);
        return String.format("%02d:%02d", total / 60, total % 60);
    }

    private void updatePlayIcon() {
        if (recordingsManager.isPlaying()) {
            leftBtn.setGraphic(new PauseIcon().getGraphic());
        } else {
            leftBtn.setGraphic(new PlayIcon().getGraphic());
        }
    }

    @Override
    public void cleanup() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }

        recordingsManager.stopPlayer();
    }
}
