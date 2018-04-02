package name.dengchao.test.fx.hotkey.handler;

import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import name.dengchao.test.fx.plugin.Plugin;

public class ListViewCellFactory extends ListCell<Plugin> {

    @Override
    protected void updateItem(Plugin item, boolean empty) {
        super.updateItem(item, empty);
        setPrefHeight(55.0);
        setPadding(new Insets(0, 0, 0, 17));
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {

//            setText(item.getName());
//            setFont(Font.font("Courier New", 25));

            VBox vBox = new VBox();

            Text name = new Text();
            Text desc = new Text();

            if (vBox.getChildren().size() == 0) {
                vBox.getChildren().add(name);
                vBox.getChildren().add(desc);
            }

            name.setFont(Font.font("Courier New", 25));
            desc.setFont(Font.font("Courier New", 15));

            name.setText(item.getName());
            desc.setText(item.getDescription());

            setGraphic(vBox);
        }
    }


}
