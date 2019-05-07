package net.smartlaunch.base.utils;

public abstract class Constants {

    public static final int PREF_WIDTH = 794;
    public static final int INTERACT_WINDOW_X = 3;
    public static final int INTERACT_WINDOW_Y = 57;
    public static final int INTERACT_WINDOW_HEIGHT = 400;

    public static final String HOME_WINDOWS = "\\AppData\\Local\\SmartLaunch";

    public static final String HOME_MAC = "/Library/Application Support/SmartLaunch";

    // TODO Correct it
    public static final String HOME_LINUX = "/AppData/Local/SmartLaunch";

    public static final String USER_HOME = System.getProperty("user.home");
}
