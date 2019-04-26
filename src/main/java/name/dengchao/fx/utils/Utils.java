package name.dengchao.fx.utils;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.apache.commons.lang.SystemUtils;

import java.io.File;
import java.io.IOException;
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

    public static String getPluginPath() {
        return getHomePath().concat("/plugins");
    }

    public static String getPluginConfigPath() {
        return getPluginPath().concat("/config");
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
}
