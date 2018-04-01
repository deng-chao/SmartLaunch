package name.dengchao.test.fx.hotkey.handler;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class SelectListView {

    public SelectListView(ListView<String> listView) {
        this.listView = listView;
    }

    private ListView<String> listView;

    public void execute(KeyEvent event) {
        if (event.getCode() == KeyCode.UP) {
            ObservableList<Integer> indices = listView.getSelectionModel().getSelectedIndices();
            int index = indices.get(0);
            if (index > 0) {
                listView.getSelectionModel().select(index - 1);
            }
            event.consume();
        } else if (event.getCode() == KeyCode.DOWN) {
            ObservableList<Integer> indices = listView.getSelectionModel().getSelectedIndices();
            int index = indices.get(0);
            if (index < listView.getItems().size()) {
                listView.getSelectionModel().select(index + 1);
            }
            event.consume();
        }
    }
}
