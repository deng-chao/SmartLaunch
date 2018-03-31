package name.dengchao.test.fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

public class HelloWorld extends Application {


    public static void main(String[] args) {

        String home = System.getProperties().getProperty("user.home");
        String appHome = home + "/AppData/Local/QuickLaunch";
        String pluginConfigPath = appHome + "/plugins/config";
        File configDir = new File(pluginConfigPath);
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        PluginManager.load();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.UNDECORATED);

        TextField shade = new TextField();
        shade.setFont(Font.font(25));
        shade.setMinHeight(50);
        shade.setPrefHeight(50);
        shade.setMaxHeight(50);
        shade.setLayoutX(3);
        shade.setLayoutY(3);
        shade.setPrefWidth(594);
        shade.setStyle("-fx-background-color: transparent;");
        shade.setStyle("-fx-text-inner-color: gray;");

        TextField textField = new TextField();
        textField.setFont(Font.font(25));
        textField.setMinHeight(50);
        textField.setPrefHeight(50);
        textField.setMaxHeight(50);
        textField.setLayoutX(3);
        textField.setLayoutY(3);
        textField.setPrefWidth(594);
        textField.setStyle("-fx-background-color: transparent;");
        textField.setPrefWidth(Double.MAX_VALUE);
        InputEventHandler handler = new InputEventHandler(primaryStage, shade, textField);
        textField.addEventHandler(KeyEvent.KEY_PRESSED, handler);

        Pane vBox = new Pane();
        vBox.getChildren().add(shade);
        vBox.getChildren().add(textField);
        vBox.setStyle("-fx-background-color: blue;");
        vBox.setStyle("-fx-border-radius: 2;");
        vBox.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
        vBox.setStyle("-fx-padding: 10;");

        primaryStage.setScene(new Scene(vBox, 600, 56));
        primaryStage.show();

        Platform.runLater(() -> textField.requestFocus());
    }
}
