package name.dengchao.fx.plugin.rest;

import com.alibaba.fastjson.JSON;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.util.StreamUtils;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import name.dengchao.fx.PublicComponent;
import name.dengchao.fx.plugin.DisplayType;
import name.dengchao.fx.plugin.Plugin;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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

    private HttpClient client = HttpClientBuilder.create().build();

    @Override
    public InputStream execute() {
        System.out.println("upload to dfs");
        if (filePath == null) {
            interactChooseFile();
        }
        String url = "http://dfs.hjfile.cn/v2/token?accessKey=HJTAKAMUDItVGjbuvQWqUDbmKeZWIkIx&timestamp="
            + "621355968000000810&sign=72c10527cdd0500f57c2b0467b8cba73&uid=0&fileType=&maxSize=0&rawFileName=";
        try {
            HttpGet getToken = new HttpGet(url);
            InputStream tokenIn = client.execute(getToken).getEntity().getContent();
            String tokenResp = StreamUtils.copyToString(tokenIn, StandardCharsets.UTF_8);
            String token = JSON.parseObject(tokenResp).getString("data");
            HttpPost upload = new HttpPost("http://dfs.hjfile.cn/v1/file");
            upload.setHeader("token", token);
            HttpEntity entity = MultipartEntityBuilder.create()
                .setMode(HttpMultipartMode.RFC6532)
                .addBinaryBody("file", new File(filePath))
                .build();
            upload.setEntity(entity);
            InputStream dfsInfoIn = client.execute(upload).getEntity().getContent();
            String dfsRawResp = StreamUtils.copyToString(dfsInfoIn, StandardCharsets.UTF_8);
            String publishUrl = JSON.parseObject(dfsRawResp).getJSONArray("data")
                .getJSONObject(0).getString("publishUrl");
            return new ByteArrayInputStream(publishUrl.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
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
        txt.setBorder(
            new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(1))));
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
        dialog.showAndWait();
    }
}
