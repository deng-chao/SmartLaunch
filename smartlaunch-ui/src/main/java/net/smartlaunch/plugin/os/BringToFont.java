package net.smartlaunch.plugin.os;


import javafx.application.Platform;
import lombok.Data;
import net.smartlaunch.ui.PublicComponent;

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
                if (PublicComponent.getPrimaryStage().isShowing()) {
                    PublicComponent.getPrimaryStage().hide();
                } else {
                    PublicComponent.getPrimaryStage().show();
                    PublicComponent.getTextField().selectAll();
                    PublicComponent.getTextField().requestFocus();
                }
            });
        }
        running = false;
    }
}
