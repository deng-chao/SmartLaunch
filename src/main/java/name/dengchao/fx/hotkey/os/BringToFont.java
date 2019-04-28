package name.dengchao.fx.hotkey.os;


import lombok.Data;

import javafx.application.Platform;
import name.dengchao.fx.PublicComponent;

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
//                    PublicComponent.getShade().setText("");
                }
            });
        }
        running = false;
    }
}
