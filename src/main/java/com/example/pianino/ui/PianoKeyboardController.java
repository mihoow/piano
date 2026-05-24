package com.example.pianino.ui;

import com.example.pianino.core.PianoKey;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.css.PseudoClass;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;

public class PianoKeyboardController {
    private static final PseudoClass ACTIVE = PseudoClass.getPseudoClass("active");

    private final Pane pianoPane;
    private final VirtualKeyboard virtualKeyboard;
    private final ComputerKeyboardHandler computerKeyboardHandler;

    public PianoKeyboardController(
            Pane pianoPane,
            VirtualKeyboard virtualKeyboard,
            ComputerKeyboardHandler computerKeyboardHandler
    ) {
        this.pianoPane = pianoPane;
        this.virtualKeyboard = virtualKeyboard;
        this.computerKeyboardHandler = computerKeyboardHandler;
    }

    public void initialize(double sceneWidth) {
        pianoPane.getChildren().clear();

        List<PianoKey> keys = Arrays.asList(PianoKey.values());

        long whiteCount = keys.stream().filter(k -> !k.isSharp()).count();
        double whiteWidth = sceneWidth / whiteCount;
        double blackWidth = whiteWidth * 0.7;
        double whiteHeight = pianoPane.getPrefHeight();
        double blackHeight = whiteHeight * 0.60;


        renderWhiteKeys(keys, whiteWidth, whiteHeight);
        renderBlackKeys(keys, whiteWidth, blackWidth, blackHeight);
    }

    private void renderWhiteKeys(List<PianoKey> keys, double width, double height) {
        int whiteIndex = 0;

        for (PianoKey pianoKey : keys) {
            if (pianoKey.isSharp()) {
                continue;
            }

            StackPane keyNode = createKeyNode(pianoKey, false);

            keyNode.setLayoutX(whiteIndex * width);
            keyNode.setLayoutY(0);
            keyNode.setPrefSize(width, height);

            pianoPane.getChildren().add(keyNode);
            whiteIndex++;
        }

        pianoPane.setPrefSize(whiteIndex * width, height);
    }

    private void renderBlackKeys(List<PianoKey> keys, double whiteWidth, double blackWidth, double blackHeight) {
        int whiteIndex = 0;

        for (PianoKey pianoKey : keys) {
            if (!pianoKey.isSharp()) {
                whiteIndex++;
                continue;
            }

            StackPane keyNode = createKeyNode(pianoKey, true);

            keyNode.setLayoutX((whiteIndex * whiteWidth) - (blackWidth / 2.0));
            keyNode.setLayoutY(0);
            keyNode.setPrefSize(blackWidth, blackHeight);

            pianoPane.getChildren().add(keyNode);
        }
    }

    private StackPane createKeyNode(PianoKey pianoKey, boolean black) {
        StackPane wrapper = new StackPane();
        wrapper.getStyleClass().add("piano-key-wrapper");
        wrapper.setPickOnBounds(false);

        Region shadowNode = new Region();
        shadowNode.getStyleClass().add("piano-key-shadow");
        shadowNode.setMouseTransparent(true);

        StackPane keyNode = new StackPane();
        keyNode.setId(pianoKey.getLabel());
        keyNode.getStyleClass().add("piano-key");
        keyNode.getStyleClass().add(black ? "black-key" : "white-key");

        Label toneLabel = new Label(pianoKey.getToneLabel());
        toneLabel.getStyleClass().add("tone-label");

        Label shortcutLabel = new Label(
                computerKeyboardHandler.getMappedKey(pianoKey)
        );
        shortcutLabel.getStyleClass().add("shortcut-label");

        VBox content = new VBox();

        if (black) {
            content.getChildren().addAll(toneLabel, shortcutLabel);
            content.getStyleClass().add("black-key-content");
        } else {
            content.getChildren().addAll(shortcutLabel, toneLabel);
            content.getStyleClass().add("white-key-content");
        }

        keyNode.getChildren().add(content);

        keyNode.setOnMousePressed(event -> virtualKeyboard.press(pianoKey));
        keyNode.setOnMouseReleased(event -> virtualKeyboard.release(pianoKey));
        keyNode.setOnMouseExited(event -> virtualKeyboard.release(pianoKey));

        TranslateTransition pressAnimation = new TranslateTransition(Duration.millis(45), keyNode);
        pressAnimation.setToY(6);

        TranslateTransition releaseAnimation = new TranslateTransition(Duration.millis(70), keyNode);
        releaseAnimation.setToY(0);

        virtualKeyboard.addListener(new VirtualKeyboardListener() {
            @Override
            public void onPressed(PianoKey key) {
                if (key == pianoKey) {
                    keyNode.pseudoClassStateChanged(ACTIVE, true);
                    releaseAnimation.stop();
                    pressAnimation.playFromStart();
                }
            }

            @Override
            public void onReleased(PianoKey key) {
                if (key == pianoKey) {
                    keyNode.pseudoClassStateChanged(ACTIVE, false);
                    pressAnimation.stop();
                    releaseAnimation.playFromStart();
                }
            }
        });

        wrapper.getChildren().addAll(shadowNode, keyNode);

        return wrapper;
    }
}