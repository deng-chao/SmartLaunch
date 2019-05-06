package net.smartlaunch.plugin.hotkey.handler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.Data;
import net.smartlaunch.base.plugin.Plugin;
import net.smartlaunch.base.utils.CollectionUtils;
import net.smartlaunch.base.utils.Utils;
import net.smartlaunch.plugin.PluginManager;
import net.smartlaunch.ui.PublicComponent;

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
        PublicComponent.getListView().setVisible(true);
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
            ObservableList<Plugin> items = FXCollections.observableArrayList(candidates);
            PublicComponent.getListView().setItems(items);
        } else {
            if (input.contains(" ")) {
                PublicComponent.getListView().getItems().clear();
            } else {
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
