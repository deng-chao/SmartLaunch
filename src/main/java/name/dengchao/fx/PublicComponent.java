package name.dengchao.fx;

import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import name.dengchao.fx.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;


public class PublicComponent {

    private static TextField textField;
    private static TextField shade;
    private static Stage primaryStage;
    private static ListView<Plugin> listView;

    private static List<Node> displayNodes = new ArrayList<>();

    public static TextField getTextField() {
        return textField;
    }

    public static void setTextField(TextField textField) {
        PublicComponent.textField = textField;
    }

    public static TextField getShade() {
        return shade;
    }

    public static void setShade(TextField shade) {
        PublicComponent.shade = shade;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        PublicComponent.primaryStage = primaryStage;
    }

    public static ListView<Plugin> getListView() {
        return listView;
    }

    public static void setListView(ListView<Plugin> listView) {
        PublicComponent.listView = listView;
    }

    public static List<Node> getDisplayNodes() {
        return displayNodes;
    }

    public static void setDisplayNodes(List<Node> displayNodes) {
        PublicComponent.displayNodes = displayNodes;
    }
}
