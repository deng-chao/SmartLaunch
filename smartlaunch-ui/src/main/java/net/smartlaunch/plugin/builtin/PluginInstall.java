package net.smartlaunch.plugin.builtin;

import net.smartlaunch.base.plugin.DisplayType;
import net.smartlaunch.plugin.BuiltinPlugin;

import java.io.InputStream;

public class PluginInstall extends BuiltinPlugin {

    private String pluginName;

    @Override
    public String getName() {
        return "plugin-install";
    }

    @Override
    public String getDescription() {
        return "install plugin";
    }

    @Override
    public DisplayType getDisplayType() {
        return null;
    }

    @Override
    public void setParameters(String... parameters) {
        if (parameters != null && parameters.length > 0) {
            this.pluginName = parameters[0];
        }
        this.pluginName = null;
    }

    @Override
    public InputStream execute() {
        return null;
    }
}
