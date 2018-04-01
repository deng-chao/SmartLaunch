package name.dengchao.test.fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
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
import name.dengchao.test.fx.hotkey.os.BringToFont;
import name.dengchao.test.fx.hotkey.os.GlobalKeyListener;
import name.dengchao.test.fx.plugin.PluginManager;

public class SmartLaunch extends Application {


    public static void main(String[] args) {
        PreStart.check();
        PluginManager.load();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.UNDECORATED);

        TextField shade = new TextField();
        setTextField(shade);
        shade.setStyle("-fx-text-inner-color: gray;");

        TextField textField = new TextField();
        setTextField(textField);
        InputEventHandler handler = new InputEventHandler(primaryStage, shade, textField);
        textField.addEventHandler(KeyEvent.KEY_PRESSED, handler);

        Pane vBox = new Pane();
        vBox.getChildren().add(shade);
        vBox.getChildren().add(textField);
        vBox.setStyle("-fx-background-color: blue;");
        vBox.setStyle("-fx-border-radius: 2;");
        vBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2))));
        vBox.setStyle("-fx-padding: 10;");
        vBox.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(vBox, 600, 56);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();

        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            textField.requestFocus();
            GlobalKeyListener.register("-49-3675", new BringToFont(primaryStage, textField));
        });
    }

    private void setTextField(TextField shade) {
        shade.setFont(Font.font(25));
        shade.setMinHeight(50);
        shade.setPrefHeight(50);
        shade.setMaxHeight(50);
        shade.setLayoutX(3);
        shade.setLayoutY(3);
        shade.setPrefWidth(594);
        shade.setStyle("-fx-background-color: transparent;");
    }


}
