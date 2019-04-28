package name.dengchao.fx.hotkey.handler;

import com.google.common.collect.Lists;
import javafx.event.Event;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;
import name.dengchao.fx.PublicComponent;
import name.dengchao.fx.hotkey.handler.display.DisplayJson;
import name.dengchao.fx.hotkey.handler.display.DisplayTooltip;
import name.dengchao.fx.plugin.DisplayType;
import name.dengchao.fx.plugin.Plugin;
import name.dengchao.fx.plugin.PluginManager;

import java.io.InputStream;
import java.util.List;

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
        AutoComplete.complete();
        List<Plugin> activePlugins = Lists.newArrayList();

        String input = textField.getText();
        String[] commands = input.split("\\|");
        for (String command : commands) {
            String[] commandParts = command.split(" ");
            Plugin plugin = PluginManager.pluginMap.get(commandParts[0]);
            // no plugin match
            if (plugin == null) {
                // TODO give some tips
                event.consume();
                return;
            }
            boolean overwriteParams = commandParts.length > 1;
            String[] params = new String[commandParts.length - 1];
            if (overwriteParams) {
                System.arraycopy(commandParts, 1, params, 0, params.length);
                plugin.setParameters(params);
            }
            activePlugins.add(plugin);
        }
        execute(event, activePlugins);
    }

    public void execute(Event event, List<Plugin> activePlugins) {
        InputStream inputStream = null;
        for (Plugin activePlugin : activePlugins) {
            if (activePlugin != null) {
                inputStream = activePlugin.execute();
            }
            display(activePlugin.getDisplayType(), inputStream);
        }
        event.consume();
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
