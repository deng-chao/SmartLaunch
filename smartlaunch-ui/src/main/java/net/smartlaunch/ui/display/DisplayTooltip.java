package net.smartlaunch.ui.display;

import javafx.geometry.Point2D;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import lombok.extern.slf4j.Slf4j;
import net.smartlaunch.base.utils.Utils;
import net.smartlaunch.ui.PublicComponent;

import java.io.InputStream;

@Slf4j
public class DisplayTooltip implements DisplayResult {

    private Tooltip tooltip = new Tooltip();

    @Override
    public void display(InputStream inputStream) {

        String tip = Utils.streamToStr(inputStream);
        tooltip.setText(tip);
        Point2D point2D = PublicComponent.getTextField().localToScreen(0, 0);
        if (!tooltip.isShowing()) {
            tooltip.setY(point2D.getY());
            tooltip.setFont(Font.font(16));
            tooltip.setHeight(50);
            tooltip.setPrefHeight(50);
            tooltip.show(PublicComponent.getPrimaryStage());
            tooltip.setX(point2D.getX() + PublicComponent.getPrimaryStage().getWidth() - tooltip.getWidth()- 3 );
        }
    }
}
