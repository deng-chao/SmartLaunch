package net.smartlaunch.plugin.hotkey.handler;

import com.google.common.collect.Lists;
import javafx.event.Event;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import net.smartlaunch.base.plugin.DisplayType;
import net.smartlaunch.base.plugin.Plugin;
import net.smartlaunch.base.utils.Utils;
import net.smartlaunch.plugin.PluginManager;
import net.smartlaunch.plugin.tray.Tray;
import net.smartlaunch.ui.PublicComponent;
import net.smartlaunch.ui.display.DisplayJson;
import net.smartlaunch.ui.display.DisplayText;
import net.smartlaunch.ui.display.DisplayTooltip;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CommandExecutor {

    public CommandExecutor() {
        this.primaryStage = PublicComponent.getPrimaryStage();
        this.textField = PublicComponent.getTextField();
    }

    private Stage primaryStage;
    private TextField textField;

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
                log.info("no plugin match for command: " + commandParts[0]);
                event.consume();
                return;
            }
            boolean overwriteParams = commandParts.length > 1;
            log.info("commandParts: " + Arrays.toString(commandParts));
            String[] params = new String[commandParts.length - 1];
            if (overwriteParams) {
                System.arraycopy(commandParts, 1, params, 0, params.length);
                log.info("params: " + Arrays.toString(params));
                plugin.setParameters(params);
            } else {
                plugin.setParameters((String[]) null);
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
            try {
                inputStream = activePlugin.execute();
                displayType = activePlugin.getDisplayType();
            } catch (Exception e) {
                log.error("failed to execute plugin: " + activePlugin.getName(), e);
                Tray.notify("SmartLaunch", e.getMessage(), TrayIcon.MessageType.WARNING);
            }
        }
        display(activePlugins.get(activePlugins.size() - 1).getDisplayType(), inputStream);
        event.consume();
    }

    private void display(DisplayType type, InputStream inputStream) {
        if (inputStream == null) {
            primaryStage.hide();
            return;
        }
        try {
            switch (type) {
                case NONE:
                    primaryStage.hide();
                    break;
                case JSON:
                    new DisplayJson().display(inputStream);
                    break;
                case TIP:
                    new DisplayTooltip().display(inputStream);
                    break;
                case TEXT:
                    new DisplayText().display(inputStream);
                    break;
                default:
            }
        } catch (Exception e) {
            log.error("error occur when display results.", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
