package name.dengchao.fx.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import name.dengchao.fx.plugin.Plugin;
import name.dengchao.fx.plugin.PluginManager;
import name.dengchao.fx.plugin.builtin.Configurable;
import name.dengchao.fx.utils.Utils;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ConfigManager {

    public static JSONObject getConfig(String pluginName) {
        Plugin plugin = PluginManager.getPlugin(pluginName);
        if (plugin == null || !(plugin instanceof Configurable)) {
            System.out.println("plugin is not found or not configurable");
            return null;
        }
        String configPath = configFilePath(pluginName);
        File f = new File(configPath);
        if (f.exists()) {
            try {
                return JSON.parseObject(FileUtils.readFileToString(f, StandardCharsets.UTF_8));
            } catch (IOException e) {
                return null;
            }
        }
        return ((Configurable) plugin).defaultConfig();
    }

    public static void saveConfig(String pluginName, String configJson) {

        File f = new File(configFilePath(pluginName));
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.out.println("failed to create config file: " + f.getAbsolutePath());
                return;
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            bw.write(JSON.toJSONString(JSON.parseObject(configJson), true));
        } catch (IOException e) {
            return;
        }
    }

    private static String configFilePath(String pluginName) {
        return Utils.getPluginConfigPath() + "/" + pluginName + ".json";
    }
}
