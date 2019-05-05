package name.dengchao.test.fx.plugin.windows;

import java.io.InputStream;
import java.util.Arrays;

import lombok.Data;
import name.dengchao.test.fx.plugin.DisplayType;
import name.dengchao.test.fx.plugin.Plugin;

@Data
public class StartMenu implements Plugin {

    private String name;
    private DisplayType displayType;
    private String[] parameters = new String[0];
    private String path;
    private String description;

    @Override
    public InputStream execute() {
        try {
            System.out.println(path + ", " + Arrays.asList(parameters));
            Runtime.getRuntime().exec("cmd /c  \"" + path + "\"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
