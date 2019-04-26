package name.dengchao.fx.hotkey.handler;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import name.dengchao.fx.PublicComponent;
import name.dengchao.fx.plugin.Plugin;

public class SelectListView {

    private ListView<Plugin> listView = PublicComponent.getListView();
    private TextField textField = PublicComponent.getTextField();
    private TextField shadow = PublicComponent.getShade();

    public void execute(KeyEvent event) {
        if (event.getCode() == KeyCode.UP) {
            ObservableList<Integer> indices = listView.getSelectionModel().getSelectedIndices();
            int index = indices.get(0);
            if (index > 0) {
                listView.getSelectionModel().select(index - 1);
                listView.scrollTo(index - 5);
                textField.setText(listView.getSelectionModel().getSelectedItem().getName());
                textField.selectAll();
                shadow.setText("");
            }
            event.consume();
        } else if (event.getCode() == KeyCode.DOWN) {
            ObservableList<Integer> indices = listView.getSelectionModel().getSelectedIndices();
            int index = indices.get(0);
            if (index < listView.getItems().size()) {
                listView.scrollTo(index - 5);
                listView.getSelectionModel().select(index + 1);
                textField.setText(listView.getSelectionModel().getSelectedItem().getName());
                textField.selectAll();
                shadow.setText("");
            }
            event.consume();
        }
    }
}
