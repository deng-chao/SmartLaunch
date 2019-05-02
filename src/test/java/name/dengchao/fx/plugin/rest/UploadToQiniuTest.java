package name.dengchao.fx.plugin.rest;

import name.dengchao.fx.utils.QiniuAuth;
import name.dengchao.fx.utils.StreamUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class UploadToQiniuTest {

    @Test
    public void testUploadToQiniu() throws IOException {
        QiniuAuth auth = QiniuAuth.create(
                "_X-HJipezNOe7hZ7Put5g7YwKrIZ7-Zvo__yH8cN",
                "uaXmaVlFgmilj-GLGLqEb5vngfZpRneFQ--M6etL"
        );
        HttpClient client = HttpClientBuilder.create().build();
        String token = auth.uploadToken("smart-launch");

        HttpEntity entity = MultipartEntityBuilder.create()
                .addBinaryBody("file", this.getClass().getResourceAsStream("icon.png"))
                .addTextBody("key", "2019/03/29/abcd.jpg")
                .addTextBody("bucket", "smart-launch")
                .addTextBody("token", token).build();
        HttpPost post = new HttpPost("http://upload.qiniup.com/");
        post.setEntity(entity);
        HttpResponse httpResponse = client.execute(post);
        StatusLine statusLine = httpResponse.getStatusLine();
        String resp = StreamUtils.copyToString(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
        System.out.println(resp);
        System.out.println(statusLine.getStatusCode());


    }
}
