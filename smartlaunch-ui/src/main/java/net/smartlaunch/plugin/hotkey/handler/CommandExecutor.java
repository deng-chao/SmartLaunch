package net.smartlaunch.plugin.hotkey.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import net.smartlaunch.base.plugin.DisplayType;
import net.smartlaunch.base.plugin.Plugin;
import net.smartlaunch.base.utils.StreamUtils;
import net.smartlaunch.base.utils.Utils;
import net.smartlaunch.base.plugin.PluginManager;
import net.smartlaunch.plugin.RemotePlugin;
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
        Plugin selectedPlugin = PublicComponent.getListView().getSelectionModel().getSelectedItem();
        if (selectedPlugin != null && selectedPlugin instanceof RemotePlugin) {
            try {
                selectedPlugin.execute();
            } catch (Exception e) {
                log.error("failed to install plugin: " + selectedPlugin.getName(), e);
                Tray.notify("SmartLaunch", e.getMessage(), TrayIcon.MessageType.ERROR);
            }
            event.consume();
            return;
        }
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
                case LIST_VIEW:
                    JSONObject json = JSON.parseObject(StreamUtils.copyToString(inputStream));
                    JSONArray pluginJsonArr = json.getJSONArray("data");
                    List<Plugin> candidates = Lists.newArrayList();
                    for (int i = 0; i < pluginJsonArr.size(); i++) {
                        RemotePlugin plugin = pluginJsonArr.getObject(i, RemotePlugin.class);
                        plugin.setDisplayType(DisplayType.NONE);
                        candidates.add(plugin);
                    }
                    PublicComponent.getListView().setItems(FXCollections.observableArrayList(candidates));
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
