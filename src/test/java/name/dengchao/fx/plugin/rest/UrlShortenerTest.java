package name.dengchao.fx.plugin.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class UrlShortenerTest {

    @Test
    public void testShorten() throws Exception {
        UrlShortener shortener = new UrlShortener();
        shortener.setParameters(new String[]{"http://www.baidu.com"});
        InputStream inputStream = shortener.execute();
        String content = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        Object object = mapper.readValue(content, Object.class);
        String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        System.out.println(indented);
        System.out.println(content);
    }
}
