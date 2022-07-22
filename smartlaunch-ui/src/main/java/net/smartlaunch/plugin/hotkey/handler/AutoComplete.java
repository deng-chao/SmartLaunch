package net.smartlaunch.plugin.hotkey.handler;

import javafx.scene.input.KeyEvent;
import net.smartlaunch.base.plugin.Plugin;
import net.smartlaunch.base.utils.CollectionUtils;
import net.smartlaunch.ui.PublicComponent;

public class AutoComplete {

    public void execute(KeyEvent event) {

        if (CollectionUtils.isEmpty(PublicComponent.getListView().getItems())) {
            event.consume();
            return;
        }
        complete();
        event.consume();
    }

    public static void complete() {
        if (CollectionUtils.isEmpty(PublicComponent.getListView().getSelectionModel().getSelectedItems())) return;
        Plugin mostMatch = PublicComponent.getListView().getSelectionModel().getSelectedItems().get(0);
        if (mostMatch == null) return;

        String currInput = PublicComponent.getTextField().getText();
        int index = currInput.lastIndexOf('|');
        boolean hasPipe = currInput.lastIndexOf('|') >= 0;
        String preCommand = hasPipe ? currInput.substring(0, index) : "";
        String completed = (hasPipe ? preCommand + "| " : "") + mostMatch.getName() + " ";
        PublicComponent.getTextField().setText(completed);
        PublicComponent.getTextField().positionCaret(completed.length());
    }
}
