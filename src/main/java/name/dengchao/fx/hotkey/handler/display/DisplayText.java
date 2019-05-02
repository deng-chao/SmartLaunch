package name.dengchao.fx.hotkey.handler.display;

import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import name.dengchao.fx.Constants;
import name.dengchao.fx.PublicComponent;
import name.dengchao.fx.utils.Utils;

import java.io.InputStream;

public class DisplayText implements DisplayResult {

    private static TextArea textArea = new TextArea();

    protected String format(String in) {
        return in;
    }

    @Override
    public void display(InputStream inputStream) {
        if (inputStream == null) {
            return;
        }
        if (!PublicComponent.getDisplayNodes().contains(textArea)) {
            PublicComponent.getDisplayNodes().add(textArea);
            textArea.setEditable(false);
            textArea.setLayoutY(Constants.INTERACT_WINDOW_Y);
            textArea.setLayoutX(Constants.INTERACT_WINDOW_X);
            textArea.setMaxWidth(Constants.PREF_WIDTH);
            textArea.setPrefWidth(Constants.PREF_WIDTH);
            textArea.setFont(Font.font(20));

            textArea.setMaxHeight(Constants.INTERACT_WINDOW_HEIGHT);
            textArea.setPrefHeight(Constants.INTERACT_WINDOW_HEIGHT);
            textArea.setWrapText(true);
            ((Pane) PublicComponent.getPrimaryStage().getScene().getRoot()).getChildren().add(textArea);
        }
        PublicComponent.getListView().setVisible(false);
        String content = Utils.streamToStr(inputStream);
        textArea.setText(format(content));
        textArea.setVisible(true);
    }
}
