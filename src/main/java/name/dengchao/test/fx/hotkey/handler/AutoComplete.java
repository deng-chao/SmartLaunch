package name.dengchao.test.fx.hotkey.handler;

import org.springframework.util.StringUtils;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class AutoComplete {

    public AutoComplete(Stage primaryStage, TextField shade, TextField textField) {
        this.primaryStage = primaryStage;
        this.shade = shade;
        this.textField = textField;
    }

    private Stage primaryStage;
    private TextField shade;
    private TextField textField;

    public void execute(KeyEvent event) {
        if (StringUtils.isEmpty(shade.getText())) {
            event.consume();
        } else {
            textField.setText(shade.getText() + " ");
            textField.positionCaret(textField.getText().length());
            event.consume();
        }
    }
}
