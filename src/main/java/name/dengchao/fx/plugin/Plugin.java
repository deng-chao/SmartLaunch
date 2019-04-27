package name.dengchao.fx.plugin;

import javafx.scene.image.ImageView;

import java.io.InputStream;

public interface Plugin {

    String getName();

    default ImageView getIcon() {
        return null;
    }

    String getDescription();

    DisplayType getDisplayType();

    void setParameters(String... parameters);

    default String[] getParameterNames() {
        return null;
    }

    InputStream execute();
}
