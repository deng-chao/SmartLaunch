package net.smartlaunch.plugin.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import net.smartlaunch.base.plugin.ConfigManager;
import net.smartlaunch.ui.PublicComponent;

public class ConfigWindow {

    public static void show(String pluginName) {

        JSONObject configJSON = ConfigManager.getConfig(pluginName);
        if (configJSON == null) {
            return;
        }
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(PublicComponent.getPrimaryStage());
        VBox dialogVBox = new VBox(5);
        dialogVBox.setPadding(new Insets(10, 5, 5, 5));
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(1))));
        textArea.setPrefWidth(1000);
        textArea.setPrefHeight(500);
        textArea.setFont(new Font(15));
        textArea.setPadding(new Insets(1, 5, 1, 5));
        textArea.setText(JSON.toJSONString(configJSON, true));

        HBox menuHBox = new HBox();
        menuHBox.setAlignment(Pos.BOTTOM_RIGHT);
        Button buttonSave = new Button("Save");
        buttonSave.setOnAction((evt) -> ConfigManager.saveConfig(pluginName, textArea.getText()));
        menuHBox.getChildren().add(buttonSave);
        Button buttonClose = new Button("Close");
        buttonClose.setOnAction(evt -> dialog.hide());
        menuHBox.getChildren().add(buttonClose);

        dialogVBox.getChildren().add(textArea);
        dialogVBox.getChildren().add(menuHBox);

        Scene dialogScene = new Scene(dialogVBox);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
