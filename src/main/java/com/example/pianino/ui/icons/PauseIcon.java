package com.example.pianino.ui.icons;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PauseIcon implements Icon {

    private final StackPane root = new StackPane();

    public PauseIcon() {
        Color textColor = Color.web("#333333");

        Rectangle left = new Rectangle(4, 18);
        left.setArcWidth(2);
        left.setArcHeight(2);
        left.setFill(textColor);
        left.setTranslateX(-4);

        Rectangle right = new Rectangle(4, 18);
        right.setArcWidth(2);
        right.setArcHeight(2);
        right.setFill(textColor);
        right.setTranslateX(4);

        root.getChildren().addAll(left, right);
    }

    @Override
    public Node getGraphic() {
        return root;
    }
}