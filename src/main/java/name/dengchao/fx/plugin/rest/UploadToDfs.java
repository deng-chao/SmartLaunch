package name.dengchao.fx.plugin.rest;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import name.dengchao.fx.PublicComponent;
import name.dengchao.fx.plugin.DisplayType;
import name.dengchao.fx.plugin.Plugin;

import java.io.File;
import java.io.InputStream;

public class UploadToDfs implements Plugin {

    private String filePath;

    @Override
    public String getName() {
        return "upload";
    }

    @Override
    public String getDescription() {
        return "upload file to dfs, return file access url";
    }

    @Override
    public DisplayType getDisplayType() {
        return DisplayType.TEXT;
    }

    @Override
    public void setParameters(String... parameters) {
        this.filePath = parameters[0];
    }

    @Override
    public InputStream execute() {
        System.out.println("upload to dfs");
        if (filePath == null) {
            interactChooseFile();
        }
        return null;
    }

    private void interactChooseFile() {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(PublicComponent.getPrimaryStage());
        HBox dialogHBox = new HBox(5);
        dialogHBox.setPadding(new Insets(10, 5, 5, 5));
        Button buttonLoad = new Button("Choose File");
        Label txt = new Label("No file selected");
        txt.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(1))));
        txt.setPrefWidth(400);
        txt.setFont(new Font(15));
        txt.setPadding(new Insets(1, 5, 1, 5));
        buttonLoad.setOnAction((evt) -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ALL files (*.*)", "*.*");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(PublicComponent.getPrimaryStage());
            if (file != null) {
                txt.setText(file.getName());
                filePath = file.getAbsolutePath();
            }
        });
        Button buttonOk = new Button("Ok");
        buttonOk.setOnAction(evt -> dialog.hide());
        dialogHBox.getChildren().add(txt);
        dialogHBox.getChildren().add(buttonLoad);
        dialogHBox.getChildren().add(buttonOk);
        Scene dialogScene = new Scene(dialogHBox);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}
