package name.dengchao.fx.plugin.rest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
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
import name.dengchao.fx.utils.QiniuAuth;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UploadToQiniu implements Plugin {

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

    private String domain = "http://pqr4zmlfn.bkt.clouddn.com";

    private static final String defaultBucket = "smart-launch";
    private static final String uploadUrl = "http://upload.qiniup.com/";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");

    @Override
    public InputStream execute() {

        System.out.println("upload to dfs");
        if (filePath == null) {
            interactChooseFile();
        }
        try {
            // the ak & sk is for personal use.
            QiniuAuth auth = QiniuAuth.create(
                "_X-HJipezNOe7hZ7Put5g7YwKrIZ7-Zvo__yH8cN",
                "uaXmaVlFgmilj-GLGLqEb5vngfZpRneFQ--M6etL"
            );
            String token = auth.uploadToken(defaultBucket);
            int indexOfDot = filePath.lastIndexOf('.');
            String fileExt = indexOfDot > 0 ? filePath.substring(indexOfDot) : "";
            String key = sdf.format(new Date()) + UUID.randomUUID().toString() + fileExt;
            HttpEntity entity = MultipartEntityBuilder.create()
                .addBinaryBody("file", new File(filePath))
                .addTextBody("key", key)
                .addTextBody("bucket", defaultBucket)
                .addTextBody("token", token).build();
            HttpPost post = new HttpPost(uploadUrl);
            post.setEntity(entity);
            HttpResponse httpResponse = client.execute(post);
            StatusLine statusLine = httpResponse.getStatusLine();
            if (statusLine.getStatusCode() / 100 == 2) {
                String resp = StreamUtils.copyToString(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
                System.out.println(resp);
                return new ByteArrayInputStream((domain + "/" + key).getBytes(StandardCharsets.UTF_8));
            }
            System.out.println(statusLine.getStatusCode());
            return null;
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
