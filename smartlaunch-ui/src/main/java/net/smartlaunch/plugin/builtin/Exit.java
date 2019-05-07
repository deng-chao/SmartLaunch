package net.smartlaunch.plugin.builtin;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import net.smartlaunch.base.plugin.Configurable;
import net.smartlaunch.base.plugin.DisplayType;
import net.smartlaunch.base.plugin.Plugin;
import net.smartlaunch.plugin.BuiltinPlugin;

import java.io.IOException;
import java.io.InputStream;

@Data
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class Exit extends BuiltinPlugin implements Configurable {

    private String description = "exit SmartLaunch";

    private ImageView iconView;

    public Exit() {
        try (InputStream fis = Exit.class.getClassLoader().getResourceAsStream("exit.png")) {
            Image defaultIcon = new Image(fis);
            iconView = new ImageView(defaultIcon);
            iconView.setFitHeight(30);
            iconView.setFitWidth(30);
        } catch (IOException e) {
            log.error("failed to read icon image for exit.", e);
        }
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public DisplayType getDisplayType() {
        return DisplayType.NONE;
    }

    @Override
    public void setParameters(String... parameters) {

    }

    @Override
    public ImageView getIcon() {
        return iconView;
    }

    @Override
    public InputStream execute() {
        System.exit(0);
        return null;
    }
}
