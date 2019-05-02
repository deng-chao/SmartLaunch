package name.dengchao.fx.plugin.rest;


import com.alibaba.fastjson.JSON;
import name.dengchao.fx.utils.StreamUtils;
import org.junit.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class QrCodeTest {

    @Test
    public void testGenerateQrCode() throws Exception {
        QrCode qrCode = new QrCode();
        qrCode.setParameters(new String[]{"https://www.baidu.com"});
        InputStream inputStream = qrCode.execute();
        String content = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        System.out.println(JSON.toJSONString(JSON.parseObject(content), true));
    }
}
