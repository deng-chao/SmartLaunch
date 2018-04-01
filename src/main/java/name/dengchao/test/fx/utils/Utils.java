package name.dengchao.test.fx.utils;


import org.apache.commons.lang.SystemUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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

    public static String getStartMenuPath() {
        return SystemUtils.getUserHome() + "/AppData/Roaming/Microsoft/Windows/Start Menu/Programs";
    }
}
