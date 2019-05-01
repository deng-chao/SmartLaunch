package name.dengchao.fx.plugin.builtin;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;
import name.dengchao.fx.config.ConfigWindow;
import name.dengchao.fx.plugin.DisplayType;
import name.dengchao.fx.plugin.Plugin;
import name.dengchao.fx.plugin.PluginManager;
import name.dengchao.fx.utils.Utils;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class Config extends BuiltinPlugin {

    private ImageView iconView;
    private String pluginName;

    public Config() {
        try (InputStream fis = new ClassPathResource("config.png").getInputStream()) {
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
        return "config given plugin";
    }

    @Override
    public DisplayType getDisplayType() {
        return DisplayType.JSON;
    }

    @Override
    public void setParameters(String... parameters) {
        if (parameters != null && parameters.length > 0) {
            pluginName = parameters[0];
        }
    }

    @Override
    public ImageView getIcon() {
        return iconView;
    }

    @Override
    public InputStream execute() {
        log.info(pluginName);
        log.info(Utils.getPluginConfigPath());
        if (pluginName == null) {
            Utils.openDir(Utils.getPluginConfigPath());
            return null;
        }
        Plugin plugin = PluginManager.getPlugin(pluginName);
        if (plugin == null) {
            log.info("no plugin named " + pluginName);
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
