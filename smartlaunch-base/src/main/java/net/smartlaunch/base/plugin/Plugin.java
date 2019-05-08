package net.smartlaunch.base.plugin;

import javafx.scene.image.ImageView;

import java.io.InputStream;

public interface Plugin {

    String getName();

    default ImageView getIcon() {
        return null;
    }

    String getDescription();

    default String getPath() {
        return this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
    }

    DisplayType getDisplayType();

    void setParameters(String... parameters);

    default String[] getParameterNames() {
        return null;
    }

    InputStream execute();
}
