package name.dengchao.test.fx;

import javafx.application.Application;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloWorld extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello World!");

        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction((event) -> System.out.println("Hello World!"));

        VBox root = new VBox();
        root.getChildren().add(btn);

        TextArea textArea = new TextArea();
        textArea.setPrefRowCount(1);
        textArea.setBackground(Background.EMPTY);
        root.getChildren().add(textArea);

        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
