package net.smartlaunch.base.utils;


import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SystemUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Arrays;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    public static String getAppHome() {
        String userHome = SystemUtils.getUserHome().getAbsolutePath();
        if (SystemUtils.IS_OS_WINDOWS) {
            return userHome.concat(Constants.HOME_WINDOWS);
        } else if (SystemUtils.IS_OS_MAC) {
            return userHome.concat(Constants.HOME_MAC);
        } else if (SystemUtils.IS_OS_LINUX) {
            return userHome.concat(Constants.HOME_LINUX);
        } else {
            throw new RuntimeException("os not support");
        }
    }

    public static String streamToStr(InputStream inputStream) {
        try {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("failed to convert InputStream to String.", e);
        }
        return null;
    }

    public static String getPluginPath() {
        return getAppHome().concat(File.separator).concat("plugins");
    }

    public static String getPluginConfigPath() {
        return getPluginPath().concat(File.separator).concat("config");
    }

    public static String getShortcutPluginPath() {
        return getPluginPath().concat(File.separator).concat("shortcut");
    }

    public static String getLogHome() {
        return getAppHome().concat(File.separator).concat("log");
    }

    public static String getUserStartMenuPath() {
        return SystemUtils.getUserHome() + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs";
    }

    public static String getSystemStartMenuPath() {
        return "C:\\ProgramData\\Microsoft\\Windows\\Start Menu\\Programs";
    }

    public static boolean isLink(String fileName) {
        return fileName.endsWith(".lnk");
    }

    public static boolean isAppFile(File file) {
        try {
            WindowsShortcut shortcut = new WindowsShortcut(file);
            return shortcut.getRealFilename().endsWith(".exe");
        } catch (IOException e) {
            log.warn("failed to read shortcut file: " + file.getAbsolutePath());
        } catch (ParseException e) {
            log.warn("failed to parse shortcut file: " + file.getAbsolutePath());
            return true;
        }
        return false;
    }

    public static String removeSurplusSpace(String input) {
        while (input.contains("  ")) {
            input = input.replaceAll("  ", " ");
        }
        return input;
    }

    public static boolean isChinese(String string) {
        for (int i = 0; i < string.length(); i++) {
            int n = (int) string.charAt(i);
            if (!(19968 <= n && n < 40869)) {
                return false;
            }
        }
        return true;
    }

    public static Icon getSmallIcon(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        FileSystemView fsv = FileSystemView.getFileSystemView();
        return fsv.getSystemIcon(file);
    }

    public static Image toFxImage(Icon icon) {
        BufferedImage bImg;
        if (icon instanceof BufferedImage) {
            bImg = (BufferedImage) icon;
        } else {
            ImageIcon swingImageIcon = (ImageIcon) icon;
            java.awt.Image awtImage = swingImageIcon.getImage();
            bImg = new BufferedImage(awtImage.getWidth(null), awtImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = bImg.createGraphics();
            graphics.drawImage(awtImage, 0, 0, null);
            graphics.dispose();
        }
        return SwingFXUtils.toFXImage(bImg, null);
    }

    public static Icon getBigIcon(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        try {
            FileSystemView fs = FileSystemView.getFileSystemView();
            return fs.getSystemIcon(file);
        } catch (Exception e) {
            log.error("failed to read big icon, file not exits: " + file.getAbsolutePath(), e);
            return null;
        }
    }

    public static void openDir(String path) {
        try {
            String[] cmd = new String[5];
            cmd[0] = "cmd";
            cmd[1] = "/c";
            cmd[2] = "start";
            cmd[3] = " ";
            cmd[4] = path;
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            log.error("failed to open dir: " + path, e);
        }
    }

    public static String arrayToString(Object[] objects) {
        if (objects == null) {
            return "null";
        }
        return Arrays.toString(objects);
    }
}
