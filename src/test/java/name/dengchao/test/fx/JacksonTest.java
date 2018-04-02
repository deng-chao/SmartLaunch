package name.dengchao.test.fx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import name.dengchao.test.fx.plugin.DisplayType;
import name.dengchao.test.fx.plugin.windows.WindowsPlugin;
import name.dengchao.test.fx.utils.Utils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class JacksonTest {

    @Test
    public void testReadPlugin() throws IOException {

        WindowsPlugin plugin = new WindowsPlugin();
        plugin.setName("test-plugin");
        plugin.setDisplayType(DisplayType.NONE);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(plugin);
        System.out.println(json);

        File configDir = new File(Utils.getPluginConfigPath());
        File[] configFiles = configDir.listFiles();
        for (File configFile : configFiles) {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            WindowsPlugin value = objectMapper.readValue(configFile, WindowsPlugin.class);
            System.out.println(value);
        }
    }
}
