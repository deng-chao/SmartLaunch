package name.dengchao.fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import name.dengchao.fx.hotkey.InputEventHandler;
import name.dengchao.fx.hotkey.handler.ListViewCellFactory;
import name.dengchao.fx.plugin.Plugin;
import name.dengchao.fx.plugin.PluginManager;

public class SmartLaunch extends Application {


    public static void main(String[] args) {
        PreStart.check();
        PluginManager.load();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        PublicComponent.setPrimaryStage(primaryStage);

        Pane pane = new Pane();
        pane.setId("main-pane");

        TextField textField = createDefaultTextField();
        pane.getChildren().add(textField);
        PublicComponent.setTextField(textField);

        ListView<Plugin> listView = new ListView();
        listView.setCellFactory(lst -> new ListViewCellFactory());
        listView.setLayoutY(Constants.INTERACT_WINDOW_Y);
        listView.setLayoutX(Constants.INTERACT_WINDOW_X);
        listView.setVisible(false);
        pane.getChildren().add(listView);
        PublicComponent.setListView(listView);

        Scene scene = new Scene(pane, 800, 56);
        scene.getStylesheets().add(loadCss("constants.css"));
        scene.getStylesheets().add(loadCss("application.css"));
        primaryStage.setMaxHeight(500);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();

        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            textField.requestFocus();
            InputEventHandler handler = new InputEventHandler();
            textField.addEventHandler(KeyEvent.KEY_PRESSED, handler);
        });
    }

    private TextField createDefaultTextField() {
        TextField textField = new TextField();
        textField.setLayoutX(3);
        textField.setLayoutY(3);
        return textField;
    }

    private String loadCss(String cssFileName) {
        return getClass().getClassLoader().getResource(cssFileName).toExternalForm();
    }
}
