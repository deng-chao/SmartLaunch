package name.dengchao.fx.plugin.builtin;

import com.alibaba.fastjson.JSONObject;

public interface Configurable {

    default JSONObject defaultConfig() {
        return new JSONObject();
    }
}
