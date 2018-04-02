package name.dengchao.test.fx.hotkey.os;


import javafx.application.Platform;
import lombok.Data;
import name.dengchao.test.fx.PublicComponent;

@Data
public class BringToFont implements Action {

    private volatile boolean running;

    @Override
    public void act() {
        if (running) {
            return;
        }
        running = true;
        if (PublicComponent.getPrimaryStage() != null) {
            Platform.runLater(() -> {
                PublicComponent.getPrimaryStage().show();
                PublicComponent.getTextField().selectAll();
                PublicComponent.getShade().setText("");
            });
        }
        running = false;
    }
}
