package net.smartlaunch.ui;

import com.tulskiy.keymaster.common.Provider;
import lombok.extern.slf4j.Slf4j;
import net.smartlaunch.base.utils.Utils;
import net.smartlaunch.plugin.os.BringToFont;
import net.smartlaunch.plugin.tray.Tray;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.pivot.wtk.Keyboard;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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
        final Provider provider = Provider.getCurrentProvider(false);
        Runtime.getRuntime().addShutdownHook(new Thread("shutdown-hook") {
            @Override
            public void run() {
                provider.reset();
                provider.stop();
            }
        });
        provider.reset();
        provider.register(
                KeyStroke.getKeyStroke(Keyboard.KeyCode.SPACE, InputEvent.ALT_MASK),
                hotKey -> bringToFont.act()
        );
    }

    private static BringToFont bringToFont = new BringToFont();
}
