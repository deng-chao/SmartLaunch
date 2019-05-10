package net.smartlaunch.base.plugin;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Data
@Slf4j
class ShortcutPlugin implements Plugin {

    private String name;
    private DisplayType displayType;
    private String[] parameters = new String[0];
    private String path;
    private String summary;

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
            throw new PluginExecutionException("failed to execute shortcut plugin: " + name, e);
        }
        return null;
    }
}
