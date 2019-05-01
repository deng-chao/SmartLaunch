package name.dengchao.fx.plugin.builtin;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
            e.printStackTrace();
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
        System.out.println(pluginName);
        System.out.println(Utils.getPluginConfigPath());
        if (pluginName == null) {
            Utils.openDir(Utils.getPluginConfigPath());
            return null;
        }
        Plugin plugin = PluginManager.getPlugin(pluginName);
        if (plugin == null) {
            System.out.println("no plugin named " + pluginName);
            return null;
        }
        if (!(plugin instanceof Configurable)) {
            System.out.println("plugin [" + pluginName + "] is not configurable");
            return null;
        }
        Configurable configurable = (Configurable) plugin;
        ConfigWindow.show(pluginName);
        return new ByteArrayInputStream(configurable.defaultConfig().toJSONString().getBytes(StandardCharsets.UTF_8));
    }
}
