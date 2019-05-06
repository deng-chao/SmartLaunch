package net.smartlaunch.ui;

import lombok.extern.slf4j.Slf4j;
import net.smartlaunch.base.utils.Utils;
import net.smartlaunch.plugin.os.GlobalKeyListener;
import net.smartlaunch.plugin.os.VoidDispatchService;
import net.smartlaunch.plugin.tray.Tray;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
// TODO should be a hook, allow developer register operation here.
public class PreStart {

    public static void check() {
        registerKey();
        Tray.createTray();
        checkDir(Utils.getPluginConfigPath(), "Plugin config");
        checkDir(Utils.getLogHome(), "Log");
        String logConfig = Utils.getAppHome() + "/log4j2.xml";
        File logConfigFile = new File(logConfig);
        if (!logConfigFile.exists()) {
            try (InputStream fis = PreStart.class.getClassLoader().getResourceAsStream("log4j2-template.xml")) {
                String cfg = Utils.streamToStr(fis);
                cfg = cfg.replaceAll("\\$\\{logHome\\}", Utils.getLogHome().replaceAll("\\\\", "/"));
                System.out.println("config: " + cfg);
                FileUtils.write(logConfigFile, cfg, StandardCharsets.UTF_8);
            } catch (IOException e) {
                log.error("failed to create log4j2.xml on path: " + logConfig);
            }
        }
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        context.setConfigLocation(logConfigFile.toURI());
    }

    private static void checkDir(String dirPath, String name) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            log.info(name + " dir not exists, create it.");
            dir.mkdirs();
        }
    }

    public static void registerKey() {
        // shutdown the logger in GlobalScreen
        java.util.logging.LogManager.getLogManager().reset();
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            log.error("There was a problem registering the native hook.", ex);
            System.exit(1);
        }
        GlobalScreen.setEventDispatcher(new VoidDispatchService());
        GlobalScreen.addNativeKeyListener(new GlobalKeyListener());
    }
}
