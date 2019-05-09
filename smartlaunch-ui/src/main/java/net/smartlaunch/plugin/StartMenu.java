package net.smartlaunch.plugin;

import javafx.scene.image.ImageView;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.smartlaunch.base.plugin.DisplayType;
import net.smartlaunch.base.plugin.Plugin;
import net.smartlaunch.base.utils.Utils;
import net.smartlaunch.plugin.exception.PluginExecutionException;
import org.apache.commons.lang.ObjectUtils;

import java.io.InputStream;

@Data
@Slf4j
public class StartMenu implements Plugin {

    private String name;
    private DisplayType displayType;
    private String[] parameters = new String[0];
    private String path;
    private String summary;
    private ImageView icon;

    @Override
    public InputStream execute() {
        try {
            log.info(path + ", " + Utils.arrayToString(parameters));
            Runtime.getRuntime().exec("cmd /c  \"" + path + "\"");
        } catch (Exception e) {
            throw new PluginExecutionException("Failed to open file: " + path, e);
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
