package net.smartlaunch.plugin;

import lombok.Data;

import net.smartlaunch.base.plugin.DisplayType;
import net.smartlaunch.base.plugin.Plugin;

import java.io.InputStream;

@Data
public class RemotePlugin implements Plugin {

    private String name;
    private String description;
    private DisplayType displayType;
    private String[] parameters;

    @Override
    public String getPath() {
        return "plugin repository";
    }

    @Override
    public InputStream execute() {
        return null;
    }
}
