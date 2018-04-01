package name.dengchao.test.fx.hotkey.handler;

import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javafx.geometry.Point2D;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import name.dengchao.test.fx.plugin.DisplayType;
import name.dengchao.test.fx.plugin.Plugin;
import name.dengchao.test.fx.plugin.PluginManager;

public class CommandExecutor {

    public CommandExecutor(Stage stage, TextField textField) {
        this.primaryStage = stage;
        this.textField = textField;
    }

    private Stage primaryStage;
    private TextField textField;

    private Tooltip tooltip = new Tooltip();

    public void execute(KeyEvent event) {
        String input = textField.getText() + event.getText();
        String[] inputs = input.trim().split(" ");
        Plugin activePlugin = PluginManager.pluginMap.get(inputs[0]);
        InputStream inputStream = null;
        if (activePlugin != null) {
            String[] params = new String[inputs.length - 1];
            System.arraycopy(inputs, 1, params, 0, params.length);
            activePlugin.setParameters(params);
            inputStream = activePlugin.execute();
        }
        event.consume();

        if (activePlugin.getDisplayType() == DisplayType.NONE) {
            primaryStage.hide();
        } else if (activePlugin.getDisplayType() == DisplayType.NUM) {
            try {
                String numStr = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
                NumberFormat format = new DecimalFormat("###,###,###,###,###.###");
                double value = Double.valueOf(numStr);
                tooltip.setText(format.format(value));
                Point2D point2D = textField.localToScreen(0, 0);
                if (!tooltip.isShowing()) {
                    tooltip.setY(point2D.getY() + 3);
                    tooltip.setFont(Font.font(16));
                }
                tooltip.hide();
                tooltip.show(primaryStage);
                tooltip.setX(point2D.getX() + primaryStage.getWidth() - tooltip.getWidth());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
