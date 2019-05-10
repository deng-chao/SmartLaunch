package net.smartlaunch.plugin;

import lombok.Data;
import net.smartlaunch.base.plugin.DisplayType;
import net.smartlaunch.base.plugin.Plugin;
import net.smartlaunch.base.plugin.PluginManager;
import net.smartlaunch.base.utils.Utils;
import net.smartlaunch.base.plugin.PluginExecutionException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@Data
public class RemotePlugin implements Plugin {

    private Integer id;
    private String name;
    private String author;
    private String summary;
    private String readmeName;
    private DisplayType displayType;
    private String[] parameters;
    private String jarName;
    private String iconName;
    private Integer version;

    @Override
    public String getPath() {
        return "plugin repository";
    }

    @Override
    public InputStream execute() {
        File file = new File(Utils.getPluginPath() + File.separator + jarName);
        try {
            if (file.exists()) {
                // clear file to update or prevent unclean install.
                boolean deleted = file.delete();
                if (!deleted)
                    throw new PluginExecutionException("failed to install plugin, unable to delete locate file");
                boolean created = file.createNewFile();
                if (!created)
                    throw new PluginExecutionException("failed to install plugin, unable to create plugin file");
            }
        } catch (IOException e) {
            throw new PluginExecutionException("failed to install plugin, unable to create plugin file");
        }
        String jarUrl = "http://plugin.smart-launch.net/" + jarName;
        try {
            URL url = new URL(jarUrl);
            URLConnection conn = url.openConnection();
            FileUtils.copyInputStreamToFile(conn.getInputStream(), file);
        } catch (MalformedURLException e) {
            throw new PluginExecutionException("illegal plugin url", e);
        } catch (IOException e) {
            throw new PluginExecutionException("failed to download plugin: " + jarUrl);
        }
        PluginManager.loadExternalPlugin(file);
        return null;
    }
}
