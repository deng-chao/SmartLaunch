package name.dengchao.fx.plugin.rest;

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD:src/main/java/name/dengchao/fx/plugin/rest/UploadToQiniu.java
=======
import name.dengchao.fx.plugin.builtin.Configurable;
>>>>>>> d80c6ff... 新增插件配置管理功能
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.util.StreamUtils;

=======
import com.alibaba.fastjson.JSON;
>>>>>>> 92adb10... 1. 解决 upload 插件在 interact 模式下, 取消文件选择后, 再次选择文件并上传时, 发生卡死的问题:src/main/java/name/dengchao/fx/plugin/rest/UploadToDfs.java
=======
>>>>>>> a283383... 使用slf4j+log4j2打印日志
=======
import com.alibaba.fastjson.JSONObject;
>>>>>>> 520b98f... 调整样式
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import name.dengchao.fx.PublicComponent;
import name.dengchao.fx.config.ConfigManager;
import name.dengchao.fx.plugin.DisplayType;
import name.dengchao.fx.plugin.Plugin;
<<<<<<< HEAD
<<<<<<< HEAD:src/main/java/name/dengchao/fx/plugin/rest/UploadToQiniu.java
import name.dengchao.fx.utils.QiniuAuth;
=======
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.util.StreamUtils;
>>>>>>> 92adb10... 1. 解决 upload 插件在 interact 模式下, 取消文件选择后, 再次选择文件并上传时, 发生卡死的问题:src/main/java/name/dengchao/fx/plugin/rest/UploadToDfs.java
=======
import name.dengchao.fx.plugin.builtin.Configurable;
import name.dengchao.fx.utils.QiniuAuth;
import name.dengchao.fx.utils.StreamUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
<<<<<<< HEAD
import org.springframework.util.StreamUtils;
>>>>>>> a283383... 使用slf4j+log4j2打印日志
=======
>>>>>>> 0d4842d... 减少依赖

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class UploadToQiniu implements Plugin, Configurable {

    private String filePath;

    private ImageView iconView;

    public UploadToQiniu() {
        try (InputStream fis = getClass().getClassLoader().getResourceAsStream("upload.png")) {
            Image defaultIcon = new Image(fis);
            iconView = new ImageView(defaultIcon);
            iconView.setFitHeight(30);
            iconView.setFitWidth(30);
        } catch (IOException e) {
            log.error("failed to read icon image for upload.", e);
        }
    }

    @Override
    public String getName() {
        return "upload";
    }

    @Override
    public String getDescription() {
        return "UPLOAD file to QINIU, return access url";
    }

    @Override
    public DisplayType getDisplayType() {
        return DisplayType.TEXT;
    }

    @Override
    public void setParameters(String... parameters) {
        if (parameters != null && parameters.length > 0) {
            this.filePath = parameters[0];
        }
    }

    @Override
    public ImageView getIcon() {
        return iconView;
    }

    private HttpClient client = HttpClientBuilder.create().build();

    private String domain = "http://pqr4zmlfn.bkt.clouddn.com";

    private static final String defaultBucket = "smart-launch";
    private static final String uploadUrl = "http://upload.qiniup.com/";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");

    private static final String ak = "_X-HJipezNOe7hZ7Put5g7YwKrIZ7-Zvo__yH8cN";
    private static final String sk = "uaXmaVlFgmilj-GLGLqEb5vngfZpRneFQ--M6etL";

    @Override
    public JSONObject defaultConfig() {
        JSONObject json = new JSONObject();
        json.put("ak", ak);
        json.put("sk", sk);
        json.put("bucket", defaultBucket);
        json.put("domain", domain);
        return json;
    }

    @Override
    public InputStream execute() {
<<<<<<< HEAD
<<<<<<< HEAD:src/main/java/name/dengchao/fx/plugin/rest/UploadToQiniu.java

        System.out.println("upload to dfs");
=======
        System.out.println("upload file, Path: " + filePath);
>>>>>>> de4bf5d... 1. 修正 upload 非交互模式下, 无法上传的问题:src/main/java/name/dengchao/fx/plugin/rest/UploadToDfs.java
=======
        log.info("upload file, Path: " + filePath);
>>>>>>> a283383... 使用slf4j+log4j2打印日志
        if (filePath == null) {
            interactChooseFile();
        }
<<<<<<< HEAD:src/main/java/name/dengchao/fx/plugin/rest/UploadToQiniu.java
=======
        if (filePath == null) {
            return null;
        }
        String url = "http://dfs.hjfile.cn/v2/token?accessKey=HJTAKAMUDItVGjbuvQWqUDbmKeZWIkIx&timestamp="
                + "621355968000000810&sign=72c10527cdd0500f57c2b0467b8cba73&uid=0&fileType=&maxSize=0&rawFileName=";
>>>>>>> 92adb10... 1. 解决 upload 插件在 interact 模式下, 取消文件选择后, 再次选择文件并上传时, 发生卡死的问题:src/main/java/name/dengchao/fx/plugin/rest/UploadToDfs.java
        try {
<<<<<<< HEAD
            // the ak & sk is for personal use.
            QiniuAuth auth = QiniuAuth.create(
                    "_X-HJipezNOe7hZ7Put5g7YwKrIZ7-Zvo__yH8cN",
                    "uaXmaVlFgmilj-GLGLqEb5vngfZpRneFQ--M6etL"
            );
            String token = auth.uploadToken(defaultBucket);
=======
            // the ak & sk is for public use.
            String ak = ConfigManager.getConfig(getName()).getString("ak");
            String sk = ConfigManager.getConfig(getName()).getString("sk");
            String bucket = ConfigManager.getConfig(getName()).getString("bucket");
            String domain = ConfigManager.getConfig(getName()).getString("domain");
            QiniuAuth auth = QiniuAuth.create(ak, sk);
            String token = auth.uploadToken(bucket);
>>>>>>> 520b98f... 调整样式
            int indexOfDot = filePath.lastIndexOf('.');
            String fileExt = indexOfDot > 0 ? filePath.substring(indexOfDot) : "";
            String key = sdf.format(new Date()) + UUID.randomUUID().toString() + fileExt;
            HttpEntity entity = MultipartEntityBuilder.create()
<<<<<<< HEAD
<<<<<<< HEAD:src/main/java/name/dengchao/fx/plugin/rest/UploadToQiniu.java
                .addBinaryBody("file", new File(filePath))
                .addTextBody("key", key)
                .addTextBody("bucket", defaultBucket)
                .addTextBody("token", token).build();
=======
                    .addBinaryBody("file", new File(filePath))
                    .addTextBody("key", key)
                    .addTextBody("bucket", bucket)
                    .addTextBody("token", token).build();
>>>>>>> a283383... 使用slf4j+log4j2打印日志
            HttpPost post = new HttpPost(uploadUrl);
            post.setEntity(entity);
            HttpResponse httpResponse = client.execute(post);
            StatusLine statusLine = httpResponse.getStatusLine();
            if (statusLine.getStatusCode() / 100 == 2) {
                String resp = StreamUtils.copyToString(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
                log.info(resp);
                return new ByteArrayInputStream((domain + "/" + key).getBytes(StandardCharsets.UTF_8));
            }
            log.info("status code: " + statusLine.getStatusCode());
            return null;
=======
                    .setMode(HttpMultipartMode.RFC6532)
                    .addBinaryBody("file", new File(filePath))
                    .build();
            upload.setEntity(entity);
            InputStream dfsInfoIn = client.execute(upload).getEntity().getContent();
            String dfsRawResp = StreamUtils.copyToString(dfsInfoIn, StandardCharsets.UTF_8);
            String publishUrl = JSON.parseObject(dfsRawResp).getJSONArray("data")
                    .getJSONObject(0).getString("publishUrl");
            return new ByteArrayInputStream(publishUrl.getBytes(StandardCharsets.UTF_8));
>>>>>>> 92adb10... 1. 解决 upload 插件在 interact 模式下, 取消文件选择后, 再次选择文件并上传时, 发生卡死的问题:src/main/java/name/dengchao/fx/plugin/rest/UploadToDfs.java
        } catch (IOException e) {
            log.error("failed upload file to qiniu.", e);
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
