package net.smartlaunch.plugin.dep;

import com.alibaba.fastjson.JSONObject;

import net.smartlaunch.base.plugin.ConfigManager;
import net.smartlaunch.base.plugin.Configurable;

public interface Dependency extends Configurable {

    String install();

    default JSONObject getConfig() {
        return ConfigManager.getConfig(this);
    }
}
