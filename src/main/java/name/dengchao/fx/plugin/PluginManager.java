package name.dengchao.fx.plugin;

import com.alibaba.fastjson.JSON;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.google.common.collect.Lists;
import javafx.scene.image.ImageView;
import name.dengchao.fx.plugin.builtin.BuiltinPlugin;
import name.dengchao.fx.plugin.windows.ShortcutPlugin;
import name.dengchao.fx.plugin.windows.StartMenu;
import name.dengchao.fx.utils.Utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

public class PluginManager {

    public static Map<String, Plugin> pluginMap = new ConcurrentHashMap<>();

    public static Plugin getPlugin(String pluginName) {
        return pluginMap.get(pluginName);
    }


    public static void load() {
        loadBuiltinPlugin();
        if (SystemUtils.IS_OS_MAC) {

        } else if (SystemUtils.IS_OS_WINDOWS) {
            loadShortcutPlugins();
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

    public static void loadShortcutPlugins() {
        File file = new File(Utils.getShortcutPluginPath());
        if (!file.isDirectory()) {
            System.out.println("plugin should be a directory");
            return;
        }
        File[] files = file.listFiles();
        for (File configFile : files) {
            try {
                String configStr = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
                ShortcutPlugin plugin = JSON.parseObject(configStr, ShortcutPlugin.class);
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
        Set<Class<? extends Plugin>> restClasses = reflections.getSubTypesOf(Plugin.class);
        for (Class<? extends Plugin> restClass : restClasses) {
            try {
                Plugin builtin = restClass.newInstance();
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
                queue.offer(startMenu);
                pluginMap.put(name, startMenu);
            }
        }
    }

    private static BlockingQueue<StartMenu> queue = new LinkedBlockingDeque<>();

    private static void readIconBackGround() {
        while (true) {
            try {
                System.out.println("queue: " + queue.size());
                StartMenu menu = queue.take();
                System.out.println("reading icon for:" + menu);
                menu.setIcon(new ImageView(Utils.toFxImage(Utils.getBigIcon(new File(menu.getPath())))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static {
        (new Thread(() -> readIconBackGround())).start();
    }
}
