package name.dengchao.test.fx.hotkey;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import name.dengchao.test.fx.hotkey.handler.AutoComplete;
import name.dengchao.test.fx.hotkey.handler.CommandExecutor;
import name.dengchao.test.fx.hotkey.handler.ListViewCellFactory;
import name.dengchao.test.fx.hotkey.handler.SelectListView;
import name.dengchao.test.fx.hotkey.handler.TypeSuggestion;

public class InputEventHandler implements EventHandler<KeyEvent> {

    public InputEventHandler(Stage primaryStage, TextField shade, TextField textField) {
        this.primaryStage = primaryStage;
//        this.shade = shade;
        this.textField = textField;
        listView = new ListView();

        Platform.runLater(() -> {
            ((Pane) primaryStage.getScene().getRoot()).getChildren().add(listView);
            listView.setCellFactory(lst -> new ListViewCellFactory());
            listView.setLayoutY(55);
            listView.setLayoutX(3);
            listView.setMaxWidth(594);
            listView.setPrefWidth(594);
            primaryStage.setMaxHeight(500);
            listView.setVisible(false);
        });

        ts = new TypeSuggestion(primaryStage, shade, textField, listView);
        ac = new AutoComplete(primaryStage, shade, textField);
        sl = new SelectListView(listView);
        ce = new CommandExecutor(primaryStage, textField);
    }


    private Stage primaryStage;
    //    private TextField shade;
    private TextField textField;

    private Tooltip tooltip = new Tooltip();
    private ListView<String> listView;
    private TypeSuggestion ts;
    private AutoComplete ac;
    private SelectListView sl;
    private CommandExecutor ce;

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
