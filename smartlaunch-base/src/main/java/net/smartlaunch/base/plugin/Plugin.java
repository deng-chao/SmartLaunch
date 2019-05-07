package net.smartlaunch.base.plugin;

import javafx.scene.image.ImageView;
import net.smartlaunch.base.utils.Utils;

import java.io.InputStream;

public interface Plugin {

    String getName();

    default ImageView getIcon() {
        return null;
    }

    String getDescription();

    default String getPath() {
        return Utils.getPluginPath();
    }

    DisplayType getDisplayType();

    void setParameters(String... parameters);

    default String[] getParameterNames() {
        return null;
    }

    InputStream execute();
}
