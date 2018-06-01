package name.dengchao.fx.hotkey.handler.display;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import name.dengchao.fx.PublicComponent;

public class DisplayJson implements DisplayResult {

    private static TextArea textArea = new TextArea();

    @Override
    public void display(InputStream inputStream) {
        try {
            if (!PublicComponent.getDisplayNodes().contains(textArea)) {
                PublicComponent.getDisplayNodes().add(textArea);
                textArea.setEditable(false);
                textArea.setLayoutY(53);
                textArea.setLayoutX(3);
                textArea.setMaxWidth(594);
                textArea.setPrefWidth(594);
                textArea.setFont(Font.font("Courier New", 20));
                textArea.setMaxHeight(400);
                textArea.setPrefHeight(400);
                ((Pane) PublicComponent.getPrimaryStage().getScene().getRoot()).getChildren().add(textArea);
            }
            PublicComponent.getListView().setVisible(false);
            String content = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(content, Object.class);
            String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            textArea.setText(indented);
            textArea.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
