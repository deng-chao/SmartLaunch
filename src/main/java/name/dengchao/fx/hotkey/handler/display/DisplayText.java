package name.dengchao.fx.hotkey.handler.display;

import org.apache.commons.io.IOUtils;
import org.springframework.util.StreamUtils;

import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import name.dengchao.fx.PublicComponent;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DisplayText implements DisplayResult {

    private static TextArea textArea = new TextArea();

    protected String format(String in) {
        return in;
    }

    @Override
    public void display(InputStream inputStream) {
        try {
            if (!PublicComponent.getDisplayNodes().contains(textArea)) {
                PublicComponent.getDisplayNodes().add(textArea);
                textArea.setEditable(false);
                textArea.setLayoutY(53);
                textArea.setLayoutX(3);
                textArea.setMaxWidth(794);
                textArea.setPrefWidth(794);
                textArea.setFont(Font.font("Courier New", 20));
                textArea.setMaxHeight(400);
                textArea.setPrefHeight(400);
                textArea.setWrapText(true);
                ((Pane) PublicComponent.getPrimaryStage().getScene().getRoot()).getChildren().add(textArea);
            }
            PublicComponent.getListView().setVisible(false);
            String content = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            textArea.setText(format(content));
            textArea.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
