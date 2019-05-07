package net.smartlaunch.ui;

import net.smartlaunch.base.plugin.Plugin;
import net.smartlaunch.base.utils.Constants;
import net.smartlaunch.plugin.PluginManager;
import net.smartlaunch.plugin.hotkey.InputChangeListener;
import net.smartlaunch.plugin.hotkey.InputEventHandler;
import net.smartlaunch.plugin.hotkey.handler.ListViewCellFactory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SmartLaunch extends Application {


    public static void main(String[] args) {
        PreStart.check();
        PluginManager.init();
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
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("icon.jpg")));
        primaryStage.setTitle("SmartLaunch");
        primaryStage.show();

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                primaryStage.hide();
            }
        });

        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            textField.requestFocus();
            textField.textProperty().addListener(new InputChangeListener());
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
