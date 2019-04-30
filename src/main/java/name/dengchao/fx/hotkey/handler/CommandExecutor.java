package name.dengchao.fx.hotkey.handler;

import com.google.common.collect.Lists;

import javafx.event.Event;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;
import name.dengchao.fx.PublicComponent;
import name.dengchao.fx.hotkey.handler.display.DisplayJson;
import name.dengchao.fx.hotkey.handler.display.DisplayText;
import name.dengchao.fx.hotkey.handler.display.DisplayTooltip;
import name.dengchao.fx.plugin.DisplayType;
import name.dengchao.fx.plugin.Plugin;
import name.dengchao.fx.plugin.PluginManager;
import name.dengchao.fx.utils.Utils;

import java.io.InputStream;
import java.util.Arrays;
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
            String[] commandParts = command.trim().split(" ");
            Plugin plugin = PluginManager.pluginMap.get(commandParts[0]);
            // no plugin match
            if (plugin == null) {
                // TODO give some tips
                System.out.println("no plugin match for command: " + commandParts[0]);
                event.consume();
                return;
            }
            boolean overwriteParams = commandParts.length > 1;
            System.out.println("commandParts: "  + Arrays.toString(commandParts));
            String[] params = new String[commandParts.length - 1];
            if (overwriteParams) {
                System.arraycopy(commandParts, 1, params, 0, params.length);
                System.out.println("params: " + Arrays.toString(params));
                plugin.setParameters(params);
            }
            activePlugins.add(plugin);
        }
        execute(event, activePlugins);
    }

    public void execute(Event event, List<Plugin> activePlugins) {
        InputStream inputStream = null;
        String[] parameters = null;
        DisplayType displayType = null;
        for (Plugin activePlugin : activePlugins) {
            if (inputStream != null) {
                parameters = new String[]{Utils.streamToStr(inputStream)};
            }
            if (displayType != null && displayType != DisplayType.NONE && parameters != null) {
                activePlugin.setParameters(parameters);
            }
            inputStream = activePlugin.execute();
            displayType = activePlugin.getDisplayType();
        }
        display(activePlugins.get(activePlugins.size() - 1).getDisplayType(), inputStream);
        event.consume();
    }

    private void display(DisplayType type, InputStream inputStream) {
        switch (type) {
            case NONE:
                primaryStage.hide();
                break;
            case JSON:
                new DisplayJson().display(inputStream);
                break;
            case NUM:
                new DisplayTooltip().display(inputStream);
                break;
            case TEXT:
                new DisplayText().display(inputStream);
                break;
            default:
        }
    }
}
