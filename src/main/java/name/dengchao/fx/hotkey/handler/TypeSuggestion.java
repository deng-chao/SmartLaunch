package name.dengchao.fx.hotkey.handler;

import lombok.Data;

import org.springframework.util.CollectionUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import name.dengchao.fx.PublicComponent;
import name.dengchao.fx.plugin.Plugin;
import name.dengchao.fx.plugin.PluginManager;
import name.dengchao.fx.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        String[] parts = input.split(" ");
        if (parts.length == 1) {
            suggestCommand(input);
        } else {

        }
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
        if (!CollectionUtils.isEmpty(candidates)) {
            PublicComponent.getListView().setVisible(true);
//            PublicComponent.getShade().setText(candidates.get(0).getName());
            ObservableList<Plugin> items = FXCollections.observableArrayList(candidates);
            PublicComponent.getListView().setItems(items);
        } else {
            if (input.contains(" ")) {
                PublicComponent.getListView().setVisible(true);
                PublicComponent.getListView().getItems().clear();
//                PublicComponent.getListView().getItems().add("No command match");
//                PublicComponent.getShade().setText("");
            } else {
                PublicComponent.getListView().setVisible(true);
                PublicComponent.getListView().getItems().clear();
//                PublicComponent.getListView().getItems().add("Search '" + input + "' in google.");
//                PublicComponent.getShade().setText("");
            }
        }
//        PublicComponent.getListView().setMaxHeight(PublicComponent.getPrimaryStage().getMaxHeight());
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
