package net.smartlaunch.plugin.builtin;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;
import net.smartlaunch.base.plugin.Configurable;
import net.smartlaunch.base.plugin.DisplayType;
import net.smartlaunch.base.plugin.Plugin;
import net.smartlaunch.plugin.BuiltinPlugin;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class Copy extends BuiltinPlugin implements Configurable {

    private String content;

    private ImageView iconView;

    public Copy() {
        try (InputStream fis = this.getClass().getClassLoader().getResourceAsStream("copy.png")) {
            Image defaultIcon = new Image(fis);
            iconView = new ImageView(defaultIcon);
            iconView.setFitHeight(30);
            iconView.setFitWidth(30);
        } catch (IOException e) {
            log.error("failed to read icon image for copy.", e);
        }
    }

    @Override
    public String getName() {
        return "cp";
    }

    @Override
    public String getDescription() {
        return "copy input";
    }

    @Override
    public DisplayType getDisplayType() {
        return DisplayType.NONE;
    }

    @Override
    public ImageView getIcon() {
        return iconView;
    }

    @Override
    public void setParameters(String... parameters) {
        if (parameters != null && parameters.length != 0 && parameters[0] != null) {
            this.content = parameters[0];
        } else {
            this.content = null;
        }
    }

    @Override
    public InputStream execute() {
        StringSelection selection = new StringSelection(content);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
        return null;
    }
}
