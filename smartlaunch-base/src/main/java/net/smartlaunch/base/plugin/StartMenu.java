package net.smartlaunch.base.plugin;

import javafx.scene.image.ImageView;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import net.smartlaunch.base.utils.Utils;

import org.apache.commons.lang.ObjectUtils;

import java.io.InputStream;

@Data
@Slf4j
class StartMenu implements Plugin {

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
        if (!(object instanceof StartMenu)) {
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
