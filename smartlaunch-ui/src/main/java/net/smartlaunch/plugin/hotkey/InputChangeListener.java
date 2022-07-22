package net.smartlaunch.plugin.hotkey;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import net.smartlaunch.base.plugin.Plugin;
import net.smartlaunch.base.plugin.PluginManager;
import net.smartlaunch.base.utils.CollectionUtils;
import net.smartlaunch.base.utils.Utils;
import net.smartlaunch.ui.PublicComponent;

import java.util.*;

public class InputChangeListener implements ChangeListener<String> {

    private Stack<String> inputs = new Stack<>();
    private final Object lock = new Object();
    private boolean running = false;

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//        for (Node node : PublicComponent.getDisplayNodes()) {
//            node.setVisible(false);
//        }
        String input = Utils.removeSurplusSpace(newValue);
        String[] parts = input.split("\\|");
        suggestCommand(parts[parts.length - 1].trim());
    }

    private void handleInput() {
        running = true;
        String newValue;
        synchronized (lock) {
            newValue = inputs.peek();
            inputs.clear();
        }
        for (Node node : PublicComponent.getDisplayNodes()) {
            node.setVisible(false);
        }
        String input = Utils.removeSurplusSpace(newValue);
        String[] parts = input.split("\\|");
        suggestCommand(parts[parts.length - 1].trim());
        try {
            Thread.sleep(10);
        } catch (Exception e) {
            // not
        }
        running = false;
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
        candidates.sort(Comparator.comparingInt(e -> e.getName().length()));
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
        PublicComponent.getPrimaryStage().setHeight(PublicComponent.getListView().getHeight() + 60);
        PublicComponent.getListView().getSelectionModel().select(0);
    }
}
