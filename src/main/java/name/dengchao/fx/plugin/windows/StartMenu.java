package name.dengchao.fx.plugin.windows;

import javafx.scene.image.ImageView;
import lombok.Data;

import name.dengchao.fx.plugin.DisplayType;
import name.dengchao.fx.plugin.Plugin;

import java.io.InputStream;
import java.util.Arrays;

@Data
public class StartMenu implements Plugin {

    private String name;
    private DisplayType displayType;
    private String[] parameters = new String[0];
    private String path;
    private String description;
    private ImageView icon;

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
