package name.dengchao.fx.hotkey.handler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.Data;
import name.dengchao.fx.PublicComponent;
import name.dengchao.fx.plugin.Plugin;
import name.dengchao.fx.plugin.PluginManager;
import name.dengchao.fx.utils.Utils;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Data
public class TypeSuggestion {

    public void suggest(KeyEvent event) {
        // Prevent false suggestion
        if (event.getCode() == KeyCode.WINDOWS) {
            return;
        }
        for (Node node : PublicComponent.getDisplayNodes()) {
            node.setVisible(false);
        }
        String input = PublicComponent.getTextField().getText();
        if (event.getCode() == KeyCode.BACK_SPACE && input.length() > 0) {
            input = input.substring(0, input.length() - 1);
        } else {
            input = input + event.getText();
        }
        input = Utils.removeSurplusSpace(input);
        String[] parts = input.split("\\|");
        suggestCommand(parts[parts.length - 1].trim());
    }

    private void suggestCommand(String input) {
        Set<String> pluginNames = PluginManager.pluginMap.keySet();
        List<Plugin> candidates = new ArrayList<>();
        for (String pluginName : pluginNames) {
            if (pluginName.toLowerCase().contains(input.toLowerCase())) {
                Plugin plugin = PluginManager.pluginMap.get(pluginName);
                if (!candidates.contains(plugin)) {
                    candidates.add(PluginManager.pluginMap.get(pluginName));
                }
            }
        }
        Collections.sort(candidates, Comparator.comparingInt(e -> e.getName().length()));
        if (!CollectionUtils.isEmpty(candidates)) {
            PublicComponent.getListView().setVisible(true);
            ObservableList<Plugin> items = FXCollections.observableArrayList(candidates);
            PublicComponent.getListView().setItems(items);
        } else {
            if (input.contains(" ")) {
                PublicComponent.getListView().setVisible(true);
                PublicComponent.getListView().getItems().clear();
            } else {
                PublicComponent.getListView().setVisible(true);
                PublicComponent.getListView().getItems().clear();
            }
        }
        PublicComponent.getListView().getSelectionModel().select(0);
        PublicComponent.getPrimaryStage().setHeight(PublicComponent.getListView().getHeight() + 60);
    }

    private void suggestParameter(Plugin plugin, String input) {
        if (plugin == null) {
            return;
        }
        String[] parts = input.split(" ");
        if (parts[parts.length - 1].trim().equals("")) {

        }
    }
}
