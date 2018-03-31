package name.dengchao.test.fx;

import com.alibaba.fastjson.JSON;
import name.dengchao.test.fx.plugin.Plugin;
import name.dengchao.test.fx.plugin.builtin.BuiltinPlugin;
import name.dengchao.test.fx.plugin.windows.WindowsPlugin;
import org.apache.commons.io.FileUtils;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PluginManager {

    public static Map<String, Plugin> pluginMap = new ConcurrentHashMap<>();

    public static void load() {
        loadBuiltinPlugin();
        loadWindowsPlugins();
    }

    private static void loadBuiltinPlugin() {
        Reflections reflections = new Reflections("name.dengchao.test.fx.plugin.builtin");
        Set<Class<? extends BuiltinPlugin>> builtinClasses = reflections.getSubTypesOf(BuiltinPlugin.class);
        for (Class<? extends BuiltinPlugin> builtinClass : builtinClasses) {
            try {
                BuiltinPlugin builtin = builtinClass.newInstance();
                Plugin plugin = pluginMap.get(builtin.getName());
                if (plugin == null) {
                    pluginMap.put(builtin.getName(), builtin);
                } else {
                    throw new RuntimeException("Duplicated plugin name");
                }
            } catch (InstantiationException e) {
                // TODO throw exception
            } catch (IllegalAccessException e) {
                // TODO throw exception
            }
        }
    }

    public static void loadWindowsPlugins() {
        String home = System.getProperties().getProperty("user.home");
        String appHome = home + "/AppData/Local/QuickLaunch";
        String pluginConfigPath = appHome + "/plugins/config";
        File file = new File(pluginConfigPath);
        if (!file.isDirectory()) {
            System.out.println("plugin should be a directory");
            return;
        }
        File[] files = file.listFiles();
        for (File configFile : files) {
            try {
                String content = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
                WindowsPlugin plugin = JSON.parseObject(content, WindowsPlugin.class);
                pluginMap.put(plugin.getName(), plugin);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        loadWindowsPlugins();
    }
}
