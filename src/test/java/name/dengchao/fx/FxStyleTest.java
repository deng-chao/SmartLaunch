package name.dengchao.fx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FxStyleTest extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {
        Pane r1 = new Pane();
        r1.setPrefSize(200, 150);
        r1.setStyle("-fx-background-color: palegreen;");

        StackPane r2 = new StackPane();
        r2.setPrefSize(200, 150);
        r2.setStyle("-fx-background-color: coral;");

        SplitPane split = new SplitPane();
        split.getItems().setAll(r1, r2);
        split.setStyle("-fx-box-border: transparent;");

        StackPane layout = new StackPane();
        layout.getChildren().setAll(split);
//        layout.setStyle("-fx-padding: 20px; -fx-background-color: cornsilk");
        layout.setPadding(new Insets(-5));

        stage.setScene(new Scene(layout));
        stage.show();
    }
}
