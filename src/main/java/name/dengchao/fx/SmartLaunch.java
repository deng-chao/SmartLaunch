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
import javafx.scene.text.Font;
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

        ListView<Plugin> listView = new ListView();
        listView.setCellFactory(lst -> new ListViewCellFactory());
        listView.setLayoutY(Constants.INTERACT_WINDOW_Y);
        listView.setLayoutX(Constants.INTERACT_WINDOW_X);
        listView.setMaxWidth(Constants.PREF_WIDTH);
        listView.setPrefWidth(Constants.PREF_WIDTH);
        listView.setMaxHeight(Constants.INTERACT_WINDOW_HEIGHT);
        listView.setPrefHeight(Constants.INTERACT_WINDOW_HEIGHT);
        listView.setVisible(false);
        PublicComponent.setListView(listView);

        TextField textField = createDefaultTextField();
        PublicComponent.setTextField(textField);

        Pane pane = new Pane();
        pane.getChildren().add(textField);
        pane.setBorder(new Border(new BorderStroke(Color.valueOf("#1B1B1B"), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
        pane.setPadding(new Insets(0, 0, 0, 0));
        pane.setStyle("-fx-background-color: #1B1B1B;");


        pane.getChildren().add(listView);
        pane.setPrefHeight(450);

        Scene scene = new Scene(pane, 800, 56);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());
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
        textField.setMinHeight(50);
        textField.setPrefHeight(50);
        textField.setMaxHeight(50);
        textField.setLayoutX(3);
        textField.setLayoutY(3);
        textField.setPrefWidth(Constants.PREF_WIDTH);
        return textField;
    }
}
