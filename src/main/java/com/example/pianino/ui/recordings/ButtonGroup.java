package com.example.pianino.ui.recordings;

import com.example.pianino.ui.icons.CloseIcon;
import com.example.pianino.ui.icons.Icon;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.function.Consumer;

public abstract class ButtonGroup {
    protected final RecordingsManager recordingsManager;

    protected final Pane group;
    protected final Button leftBtn;
    protected final Button centerBtn;
    protected final Button rightBtn;

    protected Consumer<Pane> onCloseCallback;

    public ButtonGroup(RecordingsManager recordingsManager) {
        this.recordingsManager = recordingsManager;

        HBox group = new HBox(0);
        group.getStyleClass().add("file-button-group");
        group.setAlignment(Pos.CENTER);

        leftBtn = createLeftButton();
        centerBtn = createCenterButton();
        rightBtn = createRightButton();

        group.getChildren().addAll(leftBtn, centerBtn, rightBtn);
        this.group = group;
    }

    public Pane getRoot() {
        return group;
    }

    public void setOnClose(Consumer<Pane> callback) {
        onCloseCallback = callback;
    }

    public void cleanup() {}

    protected Button createLeftButton() {
        Button left = new Button("");
        left.getStyleClass().addAll("file-group-button", "file-group-left");
        left.setPrefWidth(48);

        return left;
    }

    protected Button createCenterButton() {
        Button center = new Button("");
        center.getStyleClass().addAll("file-group-button", "file-group-center");
        center.setPrefWidth(144);

        return center;
    }

    protected Button createRightButton() {
        Button right = new Button("");
        right.getStyleClass().addAll("file-group-button", "file-group-right");
        right.setPrefWidth(48);

        Icon closeIcon = new CloseIcon();
        right.setGraphic(closeIcon.getGraphic());

        right.setOnAction(e -> handleClose());

        return right;
    }

    protected void handleClose() {
        cleanup();

        if (onCloseCallback != null) {
            onCloseCallback.accept(getRoot());
        }
    }
}
