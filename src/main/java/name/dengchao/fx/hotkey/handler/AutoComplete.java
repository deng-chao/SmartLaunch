package name.dengchao.fx.hotkey.handler;

import javafx.scene.input.KeyEvent;
import name.dengchao.fx.plugin.Plugin;
import org.springframework.util.CollectionUtils;

import static name.dengchao.fx.PublicComponent.getListView;
import static name.dengchao.fx.PublicComponent.getTextField;

public class AutoComplete {

    public void execute(KeyEvent event) {

        if (CollectionUtils.isEmpty(getListView().getItems())) {
            event.consume();
            return;
        }
        complete();
        event.consume();
    }

    public static void complete() {
        Plugin mostMatch = getListView().getSelectionModel().getSelectedItems().get(0);
        if (mostMatch == null) {
            return;
        }
        String currInput = getTextField().getText();
        int index = currInput.lastIndexOf('|');
        boolean hasPipe = currInput.lastIndexOf('|') >= 0;
        String preCommand = hasPipe ? currInput.substring(0, index) : "";
        String completed = (hasPipe ? preCommand + "| " : "") + mostMatch.getName() + " ";
        getTextField().setText(completed);
        getTextField().positionCaret(completed.length());
    }
}
