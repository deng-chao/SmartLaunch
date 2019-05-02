package name.dengchao.fx.plugin.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import name.dengchao.fx.utils.StreamUtils;
import org.junit.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class UrlShortenerTest {

    @Test
    public void testShorten() throws Exception {
        UrlShortener shortener = new UrlShortener();
        shortener.setParameters(new String[]{"http://www.baidu.com"});
        InputStream inputStream = shortener.execute();
        String content = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        JSONObject object = JSON.parseObject(content);
        System.out.println(object);
        System.out.println(content);
    }
}
