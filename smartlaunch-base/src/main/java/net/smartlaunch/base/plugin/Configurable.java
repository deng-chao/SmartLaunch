package net.smartlaunch.base.plugin;

import com.alibaba.fastjson.JSONObject;

public interface Configurable {

    default JSONObject defaultConfig() {
        return new JSONObject();
    }
}
