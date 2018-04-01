package name.dengchao.test.fx.hotkey;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import name.dengchao.test.fx.PublicComponent;
import name.dengchao.test.fx.hotkey.handler.*;

public class InputEventHandler implements EventHandler<KeyEvent> {

    private Stage primaryStage = PublicComponent.getPrimaryStage();
    private TypeSuggestion ts = new TypeSuggestion();
    private AutoComplete ac = new AutoComplete();
    private SelectListView sl = new SelectListView();
    private CommandExecutor ce = new CommandExecutor();

    public InputEventHandler() {
        ListView<String> listView = PublicComponent.getListView();
        Platform.runLater(() -> {
            listView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    ce.execute(event);
                }
            });
        });
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            ce.execute(event);
        } else if (event.getCode() == KeyCode.ESCAPE) {
            primaryStage.hide();
        } else if (event.getCode() == KeyCode.TAB) {
            ac.execute(event);
        } else if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
            sl.execute(event);
        } else {
            ts.suggest(event);
        }
    }
}
