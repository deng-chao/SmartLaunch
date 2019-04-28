package name.dengchao.fx.hotkey.handler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import name.dengchao.fx.plugin.Plugin;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

public class ListViewCellFactory extends ListCell<Plugin> {

    private ImageView defaultImage;

    public ListViewCellFactory() {
        try {
            InputStream fis = new ClassPathResource("icon.jpg").getInputStream();
            Image defaultIcon = new Image(fis);
            defaultImage = new ImageView(defaultIcon);
            defaultImage.setFitHeight(30);
            defaultImage.setFitWidth(30);
        } catch (IOException e) {
            e.printStackTrace();
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

            HBox vBox = new HBox();
            Text name = new Text();
            ImageView imageView = item.getIcon() == null ? defaultImage : item.getIcon();
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);
            if (vBox.getChildren().size() == 0) {
                Label label = new Label(item.getDescription(), imageView);
                label.setFont(Font.font("Courier New", 22));
                vBox.getChildren().add(label);
                vBox.getChildren().add(name);
                vBox.setAlignment(Pos.CENTER_LEFT);
            }

            name.setFont(Font.font("Courier New", 17));
            name.setText("『" + item.getName() + "』");

            setGraphic(vBox);
        }
    }


}
