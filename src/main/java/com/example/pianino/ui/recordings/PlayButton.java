package com.example.pianino.ui.recordings;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class PlayButton extends AbstractButton {
    public PlayButton(VBox root, RecordingsManager manager) {
        super(root, manager, "Odtwórz z pliku");

        manager.addPlayerChangeListener(() -> {
            Node elementToReplace = null;
            if (activeGroup != null) {
                elementToReplace = activeGroup.getRoot();
            } else if (mainButton != null) {
                elementToReplace = mainButton;
            }

            if (elementToReplace != null) {
                replaceWithButtonGroup(elementToReplace);
            }
        });
    }

    @Override
    protected boolean handleMainAction(Button btn) {
        return recordingsManager.playFromFile();
    }

    @Override
    protected ButtonGroup createButtonGroup() {
        return new PlayButtonGroup(recordingsManager);
    }
}
