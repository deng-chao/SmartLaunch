package name.dengchao.fx.hotkey.handler;

import static name.dengchao.fx.PublicComponent.getShade;
import static name.dengchao.fx.PublicComponent.getTextField;

import org.springframework.util.StringUtils;

import javafx.scene.input.KeyEvent;

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
