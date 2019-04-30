package name.dengchao.fx.utils;


import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.SystemUtils;
import org.springframework.util.StreamUtils;
import sun.awt.shell.ShellFolder;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    public static String getHomePath() {
        String userHome = SystemUtils.getUserHome().getAbsolutePath();
        if (SystemUtils.IS_OS_WINDOWS) {
            return userHome.concat(Constants.HOME_WINDOWS);
        } else if (SystemUtils.IS_OS_MAC) {
            return userHome.concat(Constants.HOME_MAC);
        } else if (SystemUtils.IS_OS_LINUX) {
            return userHome.concat(Constants.HOME_LINUX);
        } else {
            throw new RuntimeException("WTH OS are you using, do your mind support?");
        }
    }

    public static String streamToStr(InputStream inputStream) {
        try {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPluginPath() {
        return getHomePath().concat("/plugins");
    }

    public static String getPluginConfigPath() {
        return getPluginPath().concat("/config");
    }

    public static String getShortcutPluginPath() {
        return getPluginPath().concat("/shortcut");
    }

    public static String getLogPath() {
        return getHomePath().concat("/log");
    }

    public static String getUserStartMenuPath() {
        return SystemUtils.getUserHome() + "/AppData/Roaming/Microsoft/Windows/Start Menu/Programs";
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
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
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
        if (file == null && !file.exists()) {
            return null;
        }
        try {
            ShellFolder sf = ShellFolder.getShellFolder(file);
            return new ImageIcon(sf.getIcon(true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }
}
