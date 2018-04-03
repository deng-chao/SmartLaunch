package name.dengchao.test.fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import name.dengchao.test.fx.hotkey.InputEventHandler;
import name.dengchao.test.fx.hotkey.handler.ListViewCellFactory;
import name.dengchao.test.fx.plugin.Plugin;
import name.dengchao.test.fx.plugin.PluginManager;

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
        listView.setLayoutY(53);
        listView.setLayoutX(3);
        listView.setMaxWidth(594);
        listView.setPrefWidth(594);
        listView.setMaxHeight(400);
        listView.setPrefHeight(400);
        listView.setVisible(false);
        PublicComponent.setListView(listView);

        TextField shade = createDefaultTextField();
        shade.setStyle("-fx-text-inner-color: gray;");
        PublicComponent.setShade(shade);

        TextField textField = createDefaultTextField();
        PublicComponent.setTextField(textField);

        Pane pane = new Pane();
        pane.getChildren().add(shade);
        pane.getChildren().add(textField);
        pane.setStyle("-fx-background-color: blue;");
        pane.setStyle("-fx-border-radius: 2;");
        pane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
        pane.setStyle("-fx-padding: 8;");
        pane.setStyle("-fx-background-color: transparent;");

        pane.getChildren().add(listView);
        pane.setPrefHeight(450);

        Scene scene = new Scene(pane, 600, 56);
//        scene.setFill(Color.TRANSPARENT);

        primaryStage.setMaxHeight(500);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
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
        textField.setFont(Font.font("Courier New", 25));
        textField.setMinHeight(50);
        textField.setPrefHeight(50);
        textField.setMaxHeight(50);
        textField.setLayoutX(3);
        textField.setLayoutY(3);
        textField.setPrefWidth(594);
        textField.setStyle("-fx-background-color: transparent;");
        return textField;
    }
}
