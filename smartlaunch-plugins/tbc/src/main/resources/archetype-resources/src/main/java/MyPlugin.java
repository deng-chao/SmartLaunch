package ${package};

import net.smartlaunch.base.plugin.DisplayType;
import net.smartlaunch.base.plugin.Plugin;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MyPlugin implements Plugin {

    @Override
    public String getName() {
        return "hello";
    }

    @Override
    public String getSummary() {
        return "SmartLaunch plugin example";
    }

    @Override
    public DisplayType getDisplayType() {
        return DisplayType.TIP;
    }

    @Override
    public void setParameters(String... strings) {

    }

    @Override
    public InputStream execute() {
        return new ByteArrayInputStream("World!".getBytes(StandardCharsets.UTF_8));
    }
}
