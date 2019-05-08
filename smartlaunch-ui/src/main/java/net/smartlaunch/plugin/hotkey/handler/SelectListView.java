package net.smartlaunch.plugin.hotkey.handler;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import net.smartlaunch.base.plugin.Plugin;
import net.smartlaunch.ui.PublicComponent;

public class SelectListView {

    public void resetIndex() {
    }

    private int topIndex = 0;
    private static final int PAGE_ITEM_SIZE = 7;

    private ListView<Plugin> listView = PublicComponent.getListView();

    public void execute(KeyEvent event) {
        if (event.getCode() == KeyCode.UP) {
            ObservableList<Integer> indices = listView.getSelectionModel().getSelectedIndices();
            int index = indices.get(0);
            if (index > 0) {
                listView.getSelectionModel().select(index - 1);
//                listView.scrollTo(index - 5);
            }
            event.consume();
        } else if (event.getCode() == KeyCode.DOWN) {
            ObservableList<Integer> indices = listView.getSelectionModel().getSelectedIndices();
            int index = indices.get(0);
            if (index < listView.getItems().size()) {
                listView.scrollTo(index - 5);
                listView.getSelectionModel().select(index + 1);
            }
            event.consume();
        }
    }
}
