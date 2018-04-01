package name.dengchao.test.fx.hotkey.handler;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lombok.Data;
import name.dengchao.test.fx.plugin.PluginManager;

@Data
public class TypeSuggestion {

    public TypeSuggestion(Stage primaryStage, TextField shade, TextField textField, ListView<String> listView) {
        this.primaryStage = primaryStage;
        this.shade = shade;
        this.textField = textField;
        this.listView = listView;
    }

    private Stage primaryStage;
    private TextField shade;
    private TextField textField;
    private ListView<String> listView;

    public void suggest(KeyEvent event) {
        String input = textField.getText();
        if (event.getCode() == KeyCode.BACK_SPACE && input.length() > 0) {
            input = input.substring(0, input.length() - 1);
        } else {
            input = input + event.getText();
        }
        while (input.contains("  ")) {
            input = input.replaceAll("  ", " ");
        }
        String[] parts = input.split(" ");
        if (parts.length == 1) {
            suggestCommand(input);
        } else {

        }
    }

    private void suggestCommand(String input) {
        Set<String> pluginNames = PluginManager.pluginMap.keySet();
        List<String> candidates = new ArrayList<>();
        for (String pluginName : pluginNames) {
            if (pluginName.startsWith(input)) {
                candidates.add(pluginName);
            }
        }
        if (!CollectionUtils.isEmpty(candidates)) {
            listView.setVisible(true);
            shade.setText(candidates.get(0));
            ObservableList<String> items = FXCollections.observableArrayList(candidates);
            listView.setItems(items);
        } else {
            if (input.contains(" ")) {
                listView.setVisible(true);
                listView.getItems().clear();
                listView.getItems().add("No command match");
                shade.setText("");
            } else {
                listView.setVisible(true);
                listView.getItems().clear();
                listView.getItems().add("Search '" + input + "' in google.");
                shade.setText("");
            }
        }
        listView.setMaxHeight(listView.getItems().size() * 45.0 + 3);
        listView.getSelectionModel().select(0);
        primaryStage.setHeight(63 + listView.getItems().size() * 45.0);
    }
}
