package name.dengchao.test.fx.plugin.windows;

import lombok.Data;
import name.dengchao.test.fx.plugin.DisplayType;
import name.dengchao.test.fx.plugin.Plugin;

import java.io.IOException;
import java.io.InputStream;

@Data
public class WindowsPlugin implements Plugin {

    private String name;
    private DisplayType displayType;
    private String[] parameters = new String[0];
    private String path;
    private String description;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DisplayType getDisplayType() {
        return displayType;
    }

    @Override
    public InputStream execute() {
        try {
            Runtime.getRuntime().exec(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
