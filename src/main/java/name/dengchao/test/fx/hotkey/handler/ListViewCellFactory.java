package name.dengchao.test.fx.hotkey.handler;

import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.text.Font;

public class ListViewCellFactory extends ListCell<String> {
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
}
