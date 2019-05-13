package net.smartlaunch.plugin.builtin;

import net.smartlaunch.base.plugin.DisplayType;
import net.smartlaunch.plugin.BuiltinPlugin;

import java.io.InputStream;

public class PluginCreate extends BuiltinPlugin {

    @Override
    public String getName() {
        return "spm-c";
    }

    @Override
    public String getSummary() {
        return "SmartLaunch Plugin Manager - create a plugin archetype";
    }

    @Override
    public DisplayType getDisplayType() {
        return DisplayType.NONE;
    }

    @Override
    public void setParameters(String... parameters) {

    }

    @Override
    public InputStream execute() {
//        MavenCli cli = new MavenCli();
//        cli.doMain(new String[]{"clean", "install"}, "F:\\dengchao\\git\\SmartLaunch", System.out, System.out);
        return null;
    }

    public static void main(String[] args) {
        PluginCreate pc = new PluginCreate();
        pc.execute();
    }
}
