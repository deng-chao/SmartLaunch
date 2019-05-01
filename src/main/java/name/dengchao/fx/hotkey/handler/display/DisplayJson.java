package name.dengchao.fx.hotkey.handler.display;

import com.alibaba.fastjson.JSON;

public class DisplayJson extends DisplayText {

    @Override
    protected String format(String in) {
        return JSON.toJSONString(JSON.parseObject(in), true);
    }
}
