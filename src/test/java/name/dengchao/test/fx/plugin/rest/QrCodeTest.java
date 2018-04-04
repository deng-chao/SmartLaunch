package name.dengchao.test.fx.plugin.rest;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class QrCodeTest {

    @Test
    public void testGenerateQrCode() throws Exception {
        QrCode qrCode = new QrCode();
        qrCode.setParameters(new String[]{"https://www.hujiang.com"});
        InputStream inputStream = qrCode.execute();
        String content = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        Object object = mapper.readValue(content, Object.class);
        String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        System.out.println(indented);
    }
}
