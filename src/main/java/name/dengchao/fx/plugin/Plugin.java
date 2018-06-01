package name.dengchao.fx.plugin;

import java.io.InputStream;

public interface Plugin {

    String getName();

    String getDescription();

    DisplayType getDisplayType();

    void setParameters(String... parameters);

    default String[] getParameterNames() {
        return null;
    }

    InputStream execute();
}
