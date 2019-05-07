package net.smartlaunch.plugin.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.smartlaunch.base.plugin.Configurable;
import net.smartlaunch.base.plugin.Plugin;
import net.smartlaunch.base.utils.Utils;
import net.smartlaunch.plugin.PluginManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ConfigManager {

    public static JSONObject getConfig(String pluginName) {
        Plugin plugin = PluginManager.getPlugin(pluginName);
        if (plugin == null || !(plugin instanceof Configurable)) {
            log.info("plugin is not found or not configurable: " + pluginName);
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
                log.info("failed to create config file: " + f.getAbsolutePath());
                return;
            }
        }

        try (FileOutputStream fos = new FileOutputStream(f)) {
            fos.write(JSON.toJSONString(JSON.parseObject(configJson), true).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            return;
        }
    }

    private static String configFilePath(String pluginName) {
        return Utils.getPluginConfigPath() + "/" + pluginName + ".json";
    }
}
