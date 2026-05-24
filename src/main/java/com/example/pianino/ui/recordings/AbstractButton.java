package com.example.pianino.ui.recordings;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public abstract class AbstractButton {
    protected final VBox root;
    protected Button mainButton;
    protected final RecordingsManager recordingsManager;
    protected final String text;

    protected ButtonGroup activeGroup;

    public AbstractButton(VBox root, RecordingsManager recordingsManager, String text) {
        this.root = root;
        this.recordingsManager = recordingsManager;
        this.text = text;
    }

    public void render() {
        mainButton = createMainButton();
        root.getChildren().add(mainButton);
    }

    protected Button createMainButton() {
        Button button = new Button(text);
        button.getStyleClass().add("file-main-button");
        button.setOnAction(event -> {
            boolean ok = handleMainAction(button);
            if (ok) replaceWithButtonGroup(button);
        });

        return button;
    }

    abstract protected boolean handleMainAction(Button btn);

    protected void replaceWithButtonGroup(Node elementToReplace) {
        int index = root.getChildren().indexOf(elementToReplace);

        if (index < 0) {
            return;
        }

        if (activeGroup != null) {
            activeGroup.cleanup();
        }

        mainButton = null;
        activeGroup = createButtonGroup();
        activeGroup.setOnClose(this::replaceWithMainButton);

        root.getChildren().set(index, activeGroup.getRoot());
    }

    abstract protected ButtonGroup createButtonGroup();

    protected void replaceWithMainButton(Node elementToReplace) {
        int index = root.getChildren().indexOf(elementToReplace);

        if (index < 0) {
            return;
        }

        if (activeGroup != null) {
            activeGroup.cleanup();
            activeGroup = null;
        }

        mainButton = createMainButton();
        root.getChildren().set(index, mainButton);
    }
}
