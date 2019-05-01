package name.dengchao.fx.plugin.windows;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import name.dengchao.fx.plugin.DisplayType;
import name.dengchao.fx.plugin.Plugin;

import java.io.IOException;
import java.io.InputStream;

@Data
@Slf4j
public class ShortcutPlugin implements Plugin {

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
            log.error("failed to execute shortcut plugin: " + name, e);
        }
        return null;
    }
}
