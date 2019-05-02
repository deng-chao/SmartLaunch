package name.dengchao.fx.hotkey.handler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import name.dengchao.fx.plugin.Plugin;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ListViewCellFactory extends ListCell<Plugin> {

    private ImageView defaultImage;

    public ListViewCellFactory() {
        try (InputStream fis = this.getClass().getClassLoader().getResourceAsStream("icon.jpg")) {
            Image defaultIcon = new Image(fis);
            defaultImage = new ImageView(defaultIcon);
            defaultImage.setFitHeight(30);
            defaultImage.setFitWidth(30);
        } catch (IOException e) {
            log.error("failed to read default image file.", e);
        }
    }

    @Override
    protected void updateItem(Plugin item, boolean empty) {
        super.updateItem(item, empty);
        setPrefHeight(55.0);
        setPadding(new Insets(0, 0, 0, 17));
        if (empty) {
            setGraphic(null);
        } else {

            HBox hBox = new HBox();
//            Text name = new Text();
            Text desc = new Text();
            desc.setFont(Font.font("Courier New", 20));
            desc.setText(item.getDescription());
//            name.setFont(Font.font("Courier New", 12));
//            name.setText(item.getName());

            VBox vBox = new VBox();
            vBox.getChildren().add(desc);
//            vBox.getChildren().add(name);
            vBox.setPadding(new Insets(17, 0, 0, 10));


            ImageView imageView = item.getIcon() == null ? defaultImage : item.getIcon();
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);
            if (hBox.getChildren().size() == 0) {
                Label label = new Label("", imageView);
                label.setFont(Font.font("Courier New", 22));

                hBox.getChildren().add(label);
                hBox.getChildren().add(vBox);
                hBox.setAlignment(Pos.CENTER_LEFT);
            }
            setGraphic(hBox);
        }
    }


}
