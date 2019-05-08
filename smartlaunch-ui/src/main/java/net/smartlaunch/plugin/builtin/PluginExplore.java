package net.smartlaunch.plugin.builtin;

import net.smartlaunch.base.plugin.DisplayType;
import net.smartlaunch.plugin.BuiltinPlugin;

import java.io.InputStream;

public class PluginExplore extends BuiltinPlugin {

    @Override
    public String getName() {
        return "plugin-explore";
    }

    @Override
    public String getDescription() {
        return "explore plugin in repository";
    }

    @Override
    public DisplayType getDisplayType() {
        return DisplayType.JSON;
    }

    @Override
    public void setParameters(String... parameters) {

    }

    @Override
    public InputStream execute() {
        return null;
    }
}
