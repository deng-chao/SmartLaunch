package net.smartlaunch.plugin.builtin;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;
import net.smartlaunch.base.plugin.Configurable;
import net.smartlaunch.base.plugin.DisplayType;
import net.smartlaunch.base.plugin.Plugin;
import net.smartlaunch.base.utils.Utils;
import net.smartlaunch.plugin.BuiltinPlugin;
import net.smartlaunch.plugin.PluginManager;
import net.smartlaunch.plugin.config.ConfigWindow;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class Config extends BuiltinPlugin implements Configurable {

    private ImageView iconView;
    private String pluginName;

    public Config() {
        try (InputStream fis = Config.class.getClassLoader().getResourceAsStream("config.png")) {
            Image defaultIcon = new Image(fis);
            iconView = new ImageView(defaultIcon);
            iconView.setFitHeight(30);
            iconView.setFitWidth(30);
        } catch (IOException e) {
            log.error("failed to read icon file for config.", e);
        }
    }

    @Override
    public String getName() {
        return "config";
    }

    @Override
    public String getDescription() {
        return "Config given plugin";
    }

    @Override
    public DisplayType getDisplayType() {
        return DisplayType.JSON;
    }

    @Override
    public void setParameters(String... parameters) {
        if (parameters != null && parameters.length > 0) {
            pluginName = parameters[0];
        } else {
            pluginName = null;
        }
    }

    @Override
    public ImageView getIcon() {
        return iconView;
    }

    @Override
    public InputStream execute() {
        log.info(Utils.getPluginConfigPath());
        if (pluginName == null) {
            Utils.openDir(Utils.getPluginConfigPath());
            return null;
        }
        Plugin plugin = PluginManager.getPlugin(pluginName);
        if (plugin == null) {
            log.info("no such plugin: " + pluginName);
            return null;
        }
        if (!(plugin instanceof Configurable)) {
            log.info("plugin [" + pluginName + "] is not configurable");
            return null;
        }
        Configurable configurable = (Configurable) plugin;
        ConfigWindow.show(pluginName);
        return new ByteArrayInputStream(configurable.defaultConfig().toJSONString().getBytes(StandardCharsets.UTF_8));
    }
}
