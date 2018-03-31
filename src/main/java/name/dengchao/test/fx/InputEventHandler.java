package name.dengchao.test.fx;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_ADDPeer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import name.dengchao.test.fx.plugin.DisplayType;
import name.dengchao.test.fx.plugin.Plugin;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InputEventHandler implements EventHandler<KeyEvent> {

    public InputEventHandler(Stage primaryStage, TextField shade, TextField textField) {
        this.primaryStage = primaryStage;
        this.shade = shade;
        this.textField = textField;
    }

    private Stage primaryStage;
    private TextField shade;
    private TextField textField;
    private Tooltip tooltip = new Tooltip();
    private ListView<String> listView = new ListView();
    private boolean added = false;

    @Override
    public void handle(KeyEvent event) {

        if (!listView.getSelectionModel().getSelectedIndices().contains(0)) {

        }

        String input = textField.getText() + event.getText();
        if (event.getCode() == KeyCode.ENTER) {
            String[] inputs = input.split(" ");
            Plugin activePlugin = PluginManager.pluginMap.get(inputs[0]);
            InputStream inputStream = null;
            if (activePlugin != null) {
                String[] params = new String[inputs.length - 1];
                System.arraycopy(inputs, 1, params, 0, params.length);
                activePlugin.setParameters(params);
                inputStream = activePlugin.execute();
            }
            event.consume();
            if (activePlugin.getDisplayType() == DisplayType.NONE) {
                Platform.exit();
            } else if (activePlugin.getDisplayType() == DisplayType.NUM) {
                try {
                    String numStr = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
                    NumberFormat format = new DecimalFormat("###,###,###,###,###.###");
                    double value = Double.valueOf(numStr);
                    tooltip.setText(format.format(value));
                    Point2D point2D = textField.localToScreen(0, 0);
                    if (!tooltip.isShowing()) {
                        tooltip.setY(point2D.getY() + 3);
                        tooltip.setFont(Font.font(16));
                    }
                    tooltip.hide();
                    tooltip.show(primaryStage);
                    tooltip.setX(point2D.getX() + primaryStage.getWidth() - tooltip.getWidth());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (event.getCode() == KeyCode.ESCAPE) {
            Platform.exit();
        } else if (event.getCode() == KeyCode.TAB) {
            textField.setText(shade.getText() + " ");
            textField.positionCaret(textField.getText().length());
            event.consume();
        } else if (event.getCode() == KeyCode.UP) {
            ObservableList<Integer> indices = listView.getSelectionModel().getSelectedIndices();
            int index = indices.get(0);
            if (index > 0) {
                listView.getSelectionModel().select(index - 1);
            }
            event.consume();
        } else if (event.getCode() == KeyCode.DOWN) {
            ObservableList<Integer> indices = listView.getSelectionModel().getSelectedIndices();
            int index = indices.get(0);
            if (index < listView.getItems().size()) {
                listView.getSelectionModel().select(index + 1);
            }
            event.consume();
        } else {
            // autocomplete
            if (!added) {
                ((Pane) primaryStage.getScene().getRoot()).getChildren().add(listView);
                added = true;
            }
            Set<String> pluginNames = PluginManager.pluginMap.keySet();
            List<String> candidates = pluginNames.stream().
                    filter(e -> e.startsWith(input)).
                    collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(candidates)) {
                shade.setText(candidates.get(0));
                listView.setCellFactory(lst ->
                        new ListCell<String>() {
                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                setPrefHeight(45.0);
                                setFont(Font.font(20));
                                setPadding(new Insets(0, 0, 0, 17));
                                if (empty) {
                                    setText(null);
                                } else {
                                    setText(item);
                                }
                            }
                        });
                ObservableList<String> items = FXCollections.observableArrayList(candidates);
                listView.setItems(items);
                listView.setLayoutY(55);
                listView.setLayoutX(3);
                listView.setMaxWidth(594);
                listView.setPrefWidth(594);
                listView.setMaxHeight(candidates.size() * 45.0 + 3);
                listView.getSelectionModel().select(0);
                primaryStage.setMaxHeight(500);
                primaryStage.setHeight(63 + candidates.size() * 45.0);
            }
        }
    }
}
