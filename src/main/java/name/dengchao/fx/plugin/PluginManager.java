package name.dengchao.fx.plugin;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.google.common.collect.Lists;

import org.apache.commons.lang.SystemUtils;
import org.reflections.Reflections;

import javafx.scene.image.ImageView;
import name.dengchao.fx.plugin.builtin.BuiltinPlugin;
import name.dengchao.fx.plugin.rest.RestPlugin;
import name.dengchao.fx.plugin.windows.StartMenu;
import name.dengchao.fx.plugin.windows.WindowsPlugin;
import name.dengchao.fx.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PluginManager {

    public static Map<String, Plugin> pluginMap = new ConcurrentHashMap<>();

    public static void load() {
        loadBuiltinPlugin();
        if (SystemUtils.IS_OS_MAC) {

        } else if (SystemUtils.IS_OS_WINDOWS) {
            loadWindowsPlugins();
            loadRestPlugin();
            Thread t1 = new Thread(() -> loadStartMenu(Utils.getUserStartMenuPath()));
            Thread t2 = new Thread(() -> loadStartMenu(Utils.getSystemStartMenuPath()));
            t1.start();
            t2.start();
        }
    }

    private static void loadBuiltinPlugin() {
        Reflections reflections = new Reflections("name.dengchao.fx.plugin.builtin");
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

    private static ObjectMapper mapper = new ObjectMapper();

    public static void loadWindowsPlugins() {
        File file = new File(Utils.getPluginConfigPath());
        if (!file.isDirectory()) {
            System.out.println("plugin should be a directory");
            return;
        }
        File[] files = file.listFiles();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        for (File configFile : files) {
            try {
                WindowsPlugin plugin = mapper.readValue(configFile, WindowsPlugin.class);
                pluginMap.put(plugin.getName(), plugin);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadStartMenu(String startMenuPath) {
        File dir = new File(startMenuPath);
        System.out.println(dir.getAbsolutePath());
        if (!dir.exists()) {
            System.out.println("start menu folder incorrect");
            return;
        }
        loadStartMenuItem(dir);
    }

    private static void loadRestPlugin() {
        Reflections reflections = new Reflections("name.dengchao.fx.plugin.rest");
        Set<Class<? extends RestPlugin>> restClasses = reflections.getSubTypesOf(RestPlugin.class);
        for (Class<? extends RestPlugin> restClass : restClasses) {
            try {
                RestPlugin builtin = restClass.newInstance();
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

    private static void loadStartMenuItem(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file1 : files) {
                loadStartMenuItem(file1);
            }
        } else {
            if (!Utils.isLink(file.getName()) || !Utils.isAppFile(file)) {
                return;
            }
            if (pluginMap.get(file.getName()) != null) {
                return;
            }
            System.out.println(file.getAbsolutePath());
            String displayName = file.getName().replace(".lnk", "");
            String pluginName = displayName.toLowerCase().replace(" ", "-");

            String[] chars = pluginName.split("|");
            StringBuilder firstLetter = new StringBuilder();
            StringBuilder fullLetter = new StringBuilder();
            for (String aChar : chars) {
                if (Utils.isChinese(aChar)) {
                    try {
                        String pinyin = PinyinHelper.convertToPinyinString(aChar, "", PinyinFormat.WITHOUT_TONE);
                        firstLetter.append(pinyin.substring(0, 1));
                        fullLetter.append(pinyin);
                    } catch (PinyinException e) {
                        e.printStackTrace();
                    }
                } else {
                    firstLetter.append(aChar);
                    fullLetter.append(aChar);
                }
            }

            List<String> pluginNames = Lists.newArrayList(pluginName);
            if (fullLetter.length() != pluginName.length()) {
                pluginNames.add(firstLetter.toString());
                pluginNames.add(fullLetter.toString());
            }
            for (String name : pluginNames) {
                StartMenu startMenu = new StartMenu();
                startMenu.setDisplayType(DisplayType.NONE);
                startMenu.setName(name);
                startMenu.setParameters(new String[0]);
                startMenu.setPath(file.getAbsolutePath());
                startMenu.setDescription(displayName);
                startMenu.setIcon(new ImageView(Utils.toFxImage(Utils.getSmallIcon(file))));
                pluginMap.put(name, startMenu);
            }
        }
    }
}
