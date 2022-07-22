package net.smartlaunch.plugin.hotkey.handler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import net.smartlaunch.base.plugin.Plugin;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ListViewCellFactory extends ListCell<Plugin> {

    private ImageView defaultImage;


    public ListViewCellFactory() {
        try (InputStream fis = this.getClass().getClassLoader().getResourceAsStream("icon.jpg")) {
            assert fis != null;
            Image defaultIcon = new Image(fis);
            defaultImage = new ImageView(defaultIcon);
            defaultImage.setFitHeight(35);
            defaultImage.setFitWidth(35);
        } catch (IOException e) {
            log.error("failed to read default image file.", e);
        }
    }

    @Override
    public void updateSelected(boolean selected) {
        super.updateSelected(selected);

    }

    @Override
    protected void updateItem(Plugin item, boolean empty) {
        super.updateItem(item, empty);
        setPrefHeight(55.0);
        setPadding(new Insets(0));
        if (empty) {
            setGraphic(null);
        } else {

            HBox hBox = new HBox();

            Text desc = new Text();
            desc.setFont(Font.font(18));
            desc.setText(" - " + item.getSummary());
            desc.setFill(Color.WHITE);
            desc.setFontSmoothingType(FontSmoothingType.LCD);

            Text name = new Text();
            name.setFont(Font.font(18));
            name.setText(item.getName());
            name.setFill(Color.WHITE);
            name.setFontSmoothingType(FontSmoothingType.LCD);

            VBox vBox = new VBox();
            HBox main = new HBox();
            main.getChildren().add(name);
            main.getChildren().add(desc);
            vBox.getChildren().add(main);
            Text path = new Text();
            path.setText(item.getPath() == null ? "unknown" : item.getPath());
            path.setFont(Font.font(12));
            path.setFill(Color.WHITE);
            path.setTextOrigin(VPos.TOP);
            path.setFontSmoothingType(FontSmoothingType.LCD);
            vBox.getChildren().add(path);
            vBox.setPadding(new Insets(10, 0, 0, 10));

            ImageView imageView = item.getIcon() == null ? defaultImage : item.getIcon();
            imageView.setFitHeight(35);
            imageView.setFitWidth(35);
            if (hBox.getChildren().size() == 0) {
                Label label = new Label("", imageView);
                label.setPrefHeight(35);
                label.setPrefWidth(35);
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.getChildren().add(label);
                hBox.getChildren().add(vBox);
            }
            setGraphic(hBox);
        }
    }


}
