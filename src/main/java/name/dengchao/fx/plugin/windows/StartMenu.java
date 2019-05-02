package name.dengchao.fx.plugin.windows;

import javafx.scene.image.ImageView;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import name.dengchao.fx.plugin.DisplayType;
import name.dengchao.fx.plugin.Plugin;
import org.apache.commons.lang.ObjectUtils;

import java.io.InputStream;
import java.util.Arrays;

@Data
@Slf4j
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
            log.info(path + ", " + Arrays.asList(parameters));
            Runtime.getRuntime().exec("cmd /c  \'" + path + "\'");
        } catch (Exception e) {
            log.error("failed to execute start menu plugin: " + path, e);
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
        return ObjectUtils.equals(path, ((StartMenu) object).getPath());
    }

    @Override
    public int hashCode() {
        return path == null ? super.hashCode() : path.hashCode();
    }
}
