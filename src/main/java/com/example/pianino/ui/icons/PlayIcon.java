package com.example.pianino.ui.icons;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class PlayIcon implements Icon {

    private final StackPane root = new StackPane();

    public PlayIcon() {
        SVGPath play = new SVGPath();

        play.setContent("""
                M 0 0
                L 0 18
                L 16 9
                Z
                """);

        play.setFill(Color.web("#333333"));

        root.getChildren().add(play);
    }

    @Override
    public Node getGraphic() {
        return root;
    }
}