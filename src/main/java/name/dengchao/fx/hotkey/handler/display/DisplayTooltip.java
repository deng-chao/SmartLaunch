package name.dengchao.fx.hotkey.handler.display;

import org.springframework.util.StreamUtils;

import javafx.geometry.Point2D;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import name.dengchao.fx.PublicComponent;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DisplayTooltip implements DisplayResult {

    private Tooltip tooltip = new Tooltip();

    @Override
    public void display(InputStream inputStream) {
        try {
            String numStr = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            NumberFormat format = new DecimalFormat("###,###,###,###,###.###");
            double value = Double.valueOf(numStr);
            tooltip.setText(format.format(value));
            Point2D point2D = PublicComponent.getTextField().localToScreen(0, 0);
            if (!tooltip.isShowing()) {
                tooltip.setY(point2D.getY() + 3);
                tooltip.setFont(Font.font(16));
                tooltip.show(PublicComponent.getPrimaryStage());
                tooltip.setX(point2D.getX() + PublicComponent.getPrimaryStage().getWidth() - tooltip.getWidth());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
