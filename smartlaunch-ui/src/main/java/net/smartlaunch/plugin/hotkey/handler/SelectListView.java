package net.smartlaunch.plugin.hotkey.handler;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import net.smartlaunch.base.plugin.Plugin;
import net.smartlaunch.ui.PublicComponent;

public class SelectListView {

    private final ListView<Plugin> listView = PublicComponent.getListView();

    private int fromTop = 0;

    public void execute(KeyEvent event) {
        if (event.getCode() == KeyCode.UP) {
            ObservableList<Integer> indices = listView.getSelectionModel().getSelectedIndices();
            int index = indices.get(0);
            if (index > 0) {
                if (fromTop > 1) {
                    fromTop = fromTop - 1;
                } else {
                    listView.scrollTo(index - 1);
                }
                listView.getSelectionModel().select(index - 1);
            }
            event.consume();
        } else if (event.getCode() == KeyCode.DOWN) {
            ObservableList<Integer> indices = listView.getSelectionModel().getSelectedIndices();
            int index = indices.get(0);
            if (index < listView.getItems().size()) {
                if (fromTop < 6) {
                    fromTop = fromTop + 1;
                } else {
                    listView.scrollTo(index - 5);
                }
                listView.getSelectionModel().select(index + 1);
            }
            event.consume();
        }
    }
}
