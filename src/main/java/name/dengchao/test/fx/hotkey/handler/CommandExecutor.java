package name.dengchao.test.fx.hotkey.handler;

import javafx.event.Event;
import javafx.scene.control.ListView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import name.dengchao.test.fx.PublicComponent;
import name.dengchao.test.fx.hotkey.handler.display.DisplayTooltip;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

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

    public CommandExecutor() {
        this.primaryStage = PublicComponent.getPrimaryStage();
        this.textField = PublicComponent.getTextField();
        this.listView = PublicComponent.getListView();
    }

    private Stage primaryStage;
    private TextField textField;
    private ListView<String> listView;

    public void execute(InputEvent event) {
        Plugin activePlugin = null;
        String[] inputs = null;
        if (event instanceof KeyEvent) {
            KeyEvent evt = (KeyEvent) event;
            String input = textField.getText() + evt.getText();
            inputs = input.trim().split(" ");
            activePlugin = PluginManager.pluginMap.get(inputs[0]);

            if (activePlugin == null) {
                String potentialCandidate = listView.getSelectionModel().getSelectedItem();
                textField.setText(potentialCandidate);
                activePlugin = PluginManager.pluginMap.get(potentialCandidate);
            }
        } else if (event instanceof MouseEvent) {
            String cmd = listView.getSelectionModel().getSelectedItem();
            activePlugin = PluginManager.pluginMap.get(cmd);
            inputs = new String[]{cmd};
        }

        execute(event, inputs, activePlugin);
    }

    public void execute(Event event, String[] inputs, Plugin activePlugin) {
        InputStream inputStream = null;
        if (activePlugin != null) {
            String[] params = new String[inputs.length - 1];
            System.arraycopy(inputs, 1, params, 0, params.length);
            activePlugin.setParameters(params);
            inputStream = activePlugin.execute();
        }
        event.consume();

        display(activePlugin.getDisplayType(), inputStream);
    }

    private void display(DisplayType type, InputStream inputStream) {
        if (type == DisplayType.NONE) {
            primaryStage.hide();
        } else if (type == DisplayType.NUM) {
            new DisplayTooltip().display(inputStream);
        }
    }
}
