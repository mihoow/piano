package com.example.pianino.ui.icons;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class RecordIcon implements Icon {

    private final StackPane root = new StackPane();

    private final Circle dot = new Circle(4);
    private final Transition pulseAnimation;

    public RecordIcon(boolean animated) {
        Circle outline = new Circle(9);

        outline.setFill(Color.TRANSPARENT);
        outline.setStroke(Color.RED);
        outline.setStrokeWidth(2);

        dot.setFill(Color.RED);

        root.getChildren().addAll(outline, dot);

        FadeTransition fade = new FadeTransition(Duration.millis(700), dot);
        fade.setFromValue(1.0);
        fade.setToValue(0.35);
        fade.setAutoReverse(true);
        fade.setCycleCount(Animation.INDEFINITE);

        pulseAnimation = fade;

        if (animated) {
            fade.play();
        }
    }

    @Override
    public Node getGraphic() {
        return root;
    }

    public void playAnimation() {
        pulseAnimation.play();
    }

    public void stopAnimation() {
        pulseAnimation.stop();

        dot.setOpacity(1.0);
    }
}