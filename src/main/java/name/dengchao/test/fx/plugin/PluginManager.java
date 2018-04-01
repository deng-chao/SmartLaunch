package name.dengchao.test.fx.plugin;

import com.alibaba.fastjson.JSON;

import mslinks.ShellLink;
import mslinks.ShellLinkException;
import name.dengchao.test.fx.plugin.windows.StartMenu;
import org.apache.commons.io.FileUtils;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import name.dengchao.test.fx.plugin.Plugin;
import name.dengchao.test.fx.plugin.builtin.BuiltinPlugin;
import name.dengchao.test.fx.plugin.windows.WindowsPlugin;
import name.dengchao.test.fx.utils.Utils;

public class PluginManager {

    public static Map<String, Plugin> pluginMap = new ConcurrentHashMap<>();

    public static void load() {
        loadBuiltinPlugin();
        loadWindowsPlugins();
        loadStartMenu();
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
        File file = new File(Utils.getPluginConfigPath());
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

    public static void loadStartMenu() {
        String startMenuPath = Utils.getStartMenuPath();
        File dir = new File(startMenuPath);
        System.out.println(dir.getAbsolutePath());
        if (!dir.exists()) {
            System.out.println("start menu folder incorrect");
            return;
        }
        loadStartMenuItem(dir);
    }

    private static void loadStartMenuItem(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file1 : files) {
                loadStartMenuItem(file1);
            }
        } else {
            try {
                StartMenu startMenu = new StartMenu();
                startMenu.setDisplayType(DisplayType.NONE);
                startMenu.setName(file.getName());
                startMenu.setParameters(new String[0]);
                // TODO distinguish the dir shortcut, open the dir in file explorer directly.
                File target = new File(new ShellLink(file).resolveTarget());
                if (target.exists() && !target.isDirectory()) {
                    startMenu.setPath(target.getAbsolutePath());
                    String name = startMenu.getName().toLowerCase().
                            replace(".lnk", "").
                            replace(" ", "_");
                    pluginMap.put(name, startMenu);
                }
            } catch (IOException e) {
                System.out.println("failed to read shortcut: " + file.getAbsolutePath());
            } catch (ShellLinkException e) {
                System.out.println("failed to read shortcut: " + file.getAbsolutePath());
            }
        }
    }
}
