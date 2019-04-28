package name.dengchao.fx.plugin.windows;

import lombok.Data;

import org.springframework.util.ObjectUtils;

import javafx.scene.image.ImageView;
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

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof StartMenu)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        return ObjectUtils.nullSafeEquals(path, ((StartMenu) object).getPath());
    }

    @Override
    public int hashCode() {
        return path == null ? super.hashCode() : path.hashCode();
    }
}
