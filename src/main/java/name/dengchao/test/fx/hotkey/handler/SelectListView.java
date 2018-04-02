package name.dengchao.test.fx.hotkey.handler;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import name.dengchao.test.fx.PublicComponent;

public class SelectListView {

    private ListView<String> listView = PublicComponent.getListView();
    private TextField textField = PublicComponent.getTextField();
    private TextField shadow = PublicComponent.getShade();

    public void execute(KeyEvent event) {
        if (event.getCode() == KeyCode.UP) {
            ObservableList<Integer> indices = listView.getSelectionModel().getSelectedIndices();
            int index = indices.get(0);
            if (index > 0) {
                listView.getSelectionModel().select(index - 1);
                textField.setText(listView.getSelectionModel().getSelectedItem());
                shadow.setText("");
            }
            event.consume();
        } else if (event.getCode() == KeyCode.DOWN) {
            ObservableList<Integer> indices = listView.getSelectionModel().getSelectedIndices();
            int index = indices.get(0);
            if (index < listView.getItems().size()) {
                listView.getSelectionModel().select(index + 1);
                textField.setText(listView.getSelectionModel().getSelectedItem());
                shadow.setText("");
            }
            event.consume();
        }
    }
}
