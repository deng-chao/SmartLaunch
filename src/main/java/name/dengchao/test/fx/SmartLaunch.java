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
import name.dengchao.test.fx.hotkey.os.BringToFont;
import name.dengchao.test.fx.hotkey.os.GlobalKeyListener;
import name.dengchao.test.fx.plugin.PluginManager;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class SmartLaunch extends Application {


    public static void main(String[] args) {
        PreStart.check();
        PluginManager.load();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        PublicComponent.setPrimaryStage(primaryStage);

        ListView<String> listView = new ListView();
        listView.setCellFactory(lst -> new ListViewCellFactory());
        listView.setLayoutY(55);
        listView.setLayoutX(3);
        listView.setMaxWidth(594);
        listView.setPrefWidth(594);
        listView.setVisible(false);
        PublicComponent.setListView(listView);

        TextField shade = createDefaultTextField();
        shade.setStyle("-fx-text-inner-color: gray;");
        PublicComponent.setShade(shade);

        TextField textField = createDefaultTextField();
        PublicComponent.setTextField(textField);

        Pane vBox = new Pane();
        vBox.getChildren().add(shade);
        vBox.getChildren().add(textField);
        vBox.setStyle("-fx-background-color: blue;");
        vBox.setStyle("-fx-border-radius: 2;");
        vBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2))));
        vBox.setStyle("-fx-padding: 8;");
        vBox.setStyle("-fx-background-color: transparent;");

        vBox.getChildren().add(listView);

        Scene scene = new Scene(vBox, 600, 56);
        try {
            scene.getStylesheets().add(new ClassPathResource("application.css").getURL().toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
        }
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setMaxHeight(500);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();

        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            textField.requestFocus();
            GlobalKeyListener.register("-49-3675", new BringToFont(primaryStage, textField));
            InputEventHandler handler = new InputEventHandler();
            textField.addEventHandler(KeyEvent.KEY_PRESSED, handler);
        });
    }

    private TextField createDefaultTextField() {
        TextField textField = new TextField();
        textField.setFont(Font.font(25));
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
