package name.dengchao.fx;

import com.alibaba.fastjson.JSON;
import name.dengchao.fx.plugin.DisplayType;
import name.dengchao.fx.plugin.windows.ShortcutPlugin;
import name.dengchao.fx.utils.Utils;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JacksonTest {

    @Test
    public void testReadPlugin() throws IOException {

        ShortcutPlugin plugin = new ShortcutPlugin();
        plugin.setName("test-plugin");
        plugin.setDisplayType(DisplayType.NONE);
        System.out.println(JSON.toJSONString(plugin));

        File configDir = new File(Utils.getPluginConfigPath());
        File[] configFiles = configDir.listFiles();
        for (File configFile : configFiles) {
            String configStr = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
            System.out.println(JSON.parseObject(configStr));
        }
    }
}
