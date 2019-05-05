package name.dengchao.test.fx.hotkey.handler;

import org.springframework.util.StringUtils;

import javafx.scene.input.KeyEvent;

import static name.dengchao.test.fx.PublicComponent.getShade;
import static name.dengchao.test.fx.PublicComponent.getTextField;

public class AutoComplete {

    public void execute(KeyEvent event) {
        if (StringUtils.isEmpty(getShade().getText())) {
            event.consume();
        } else {
            getTextField().setText(getShade().getText() + " ");
            getTextField().positionCaret(getTextField().getText().length());
            event.consume();
        }
    }
}
