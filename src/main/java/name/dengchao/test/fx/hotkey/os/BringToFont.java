package name.dengchao.test.fx.hotkey.os;


import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Data;

@Data
public class BringToFont implements Action {

    public BringToFont(Stage stage, TextField textField) {
        this.primaryStage = stage;
        this.textField = textField;
    }

    private Stage primaryStage;
    private TextField textField;

    private volatile boolean running;

    @Override
    public void act() {
        if (running) {
            return;
        }
        running = true;
        if (primaryStage != null) {
            Platform.runLater(() -> {
                primaryStage.show();
                textField.selectAll();
            });
        }
        running = false;
    }
}
