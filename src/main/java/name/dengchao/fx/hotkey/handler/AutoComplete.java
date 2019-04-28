package name.dengchao.fx.hotkey.handler;

import static name.dengchao.fx.PublicComponent.getListView;
import static name.dengchao.fx.PublicComponent.getTextField;

import org.springframework.util.CollectionUtils;

import javafx.scene.input.KeyEvent;
import name.dengchao.fx.plugin.Plugin;

public class AutoComplete {

    public void execute(KeyEvent event) {

        if (CollectionUtils.isEmpty(getListView().getItems())) {
            event.consume();
            return;
        }
        Plugin mostMatch = getListView().getItems().get(0);
        getTextField().setText(mostMatch.getName());
        getTextField().positionCaret(mostMatch.getName().length());
        event.consume();
    }
}
