package net.smartlaunch.plugin.hotkey;

import net.smartlaunch.base.plugin.Plugin;
import net.smartlaunch.plugin.hotkey.handler.AutoComplete;
import net.smartlaunch.plugin.hotkey.handler.CommandExecutor;
import net.smartlaunch.plugin.hotkey.handler.SelectListView;
import net.smartlaunch.ui.PublicComponent;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputEventHandler implements EventHandler<KeyEvent> {

    private AutoComplete ac = new AutoComplete();
    private SelectListView sl = new SelectListView();
    private CommandExecutor ce = new CommandExecutor();

    public InputEventHandler() {
        ListView<Plugin> listView = PublicComponent.getListView();
        Platform.runLater(() -> listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                ce.execute(event);
            }
        }));
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getCode() != KeyCode.UP && event.getCode() != KeyCode.DOWN) {
            sl.resetIndex();
        }
        if (event.getCode() == KeyCode.ENTER) {
            ce.execute(event);
        } else if (event.getCode() == KeyCode.TAB) {
            ac.execute(event);
        } else if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
            sl.execute(event);
        }
    }
}
