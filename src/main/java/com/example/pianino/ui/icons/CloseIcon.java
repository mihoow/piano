package com.example.pianino.ui.icons;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class CloseIcon implements Icon {

    private final StackPane root = new StackPane();

    public CloseIcon() {
        Color textColor = Color.web("#333333");

        Line line1 = new Line(0, 0, 14, 14);
        line1.setStroke(textColor);
        line1.setStrokeWidth(2.5);

        Line line2 = new Line(14, 0, 0, 14);
        line2.setStroke(textColor);
        line2.setStrokeWidth(2.5);

        root.getChildren().addAll(line1, line2);
    }

    @Override
    public Node getGraphic() {
        return root;
    }
}