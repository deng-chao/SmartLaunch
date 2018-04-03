package name.dengchao.test.fx.hotkey.handler;

import java.io.InputStream;
import java.util.Arrays;

import javafx.event.Event;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import name.dengchao.test.fx.PublicComponent;
import name.dengchao.test.fx.hotkey.handler.display.DisplayJson;
import name.dengchao.test.fx.hotkey.handler.display.DisplayResult;
import name.dengchao.test.fx.hotkey.handler.display.DisplayTooltip;
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
    private ListView<Plugin> listView;

    public void execute(InputEvent event) {
        Plugin activePlugin = null;
        if (event instanceof KeyEvent) {

            // read command from input box
            KeyEvent evt = (KeyEvent) event;
            String input = textField.getText() + evt.getText();
            String[] inputs = input.trim().split(" ");
            activePlugin = PluginManager.pluginMap.get(inputs[0]);

            // set parameter if match command
            if (activePlugin != null) {
                boolean overwriteParams = inputs.length > 1;
                String[] params = new String[inputs.length - 1];
                System.out.println(Arrays.asList(inputs));
                if (overwriteParams) {
                    System.arraycopy(inputs, 1, params, 0, params.length);
                    activePlugin.setParameters(params);
                }
            } else {
                // if not command match, read selected suggestion.
                Plugin potentialCandidate = listView.getSelectionModel().getSelectedItem();
                textField.setText(potentialCandidate.getName());
                activePlugin = PluginManager.pluginMap.get(potentialCandidate.getName());
            }
        } else if (event instanceof MouseEvent) {
            activePlugin = listView.getSelectionModel().getSelectedItem();
        }

        execute(event, activePlugin);
    }

    public void execute(Event event, Plugin activePlugin) {
        InputStream inputStream = null;
        if (activePlugin != null) {
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
        } else if (type == DisplayType.JSON) {
            new DisplayJson().display(inputStream);
        }
    }
}
