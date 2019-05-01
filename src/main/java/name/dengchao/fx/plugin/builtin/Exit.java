package name.dengchao.fx.plugin.builtin;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import name.dengchao.fx.plugin.DisplayType;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Data
@EqualsAndHashCode(callSuper = false)
public class Exit extends BuiltinPlugin {

    private String description = "Exit SmartLaunch";

    private ImageView iconView;

    public Exit() {
        try (InputStream fis = new ClassPathResource("exit.png").getInputStream()) {
            Image defaultIcon = new Image(fis);
            iconView = new javafx.scene.image.ImageView(defaultIcon);
            iconView.setFitHeight(30);
            iconView.setFitWidth(30);
        } catch (IOException e) {
            e.printStackTrace();
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
