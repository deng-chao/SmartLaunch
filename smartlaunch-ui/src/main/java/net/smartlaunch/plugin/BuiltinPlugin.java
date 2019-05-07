package net.smartlaunch.plugin;

import net.smartlaunch.base.plugin.Plugin;

public abstract class BuiltinPlugin implements Plugin {
    @Override
    public String getPath() {
        return "builtin";
    }
}
