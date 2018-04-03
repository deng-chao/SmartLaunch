package name.dengchao.test.fx.plugin.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import name.dengchao.test.fx.plugin.DisplayType;
import org.apache.http.client.fluent.Request;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Data
@EqualsAndHashCode(callSuper = false)
public class UrlShortener extends RestPlugin {

    private String name = "hjvc";
    private String description = "短链接服务";
    private String[] parameterNames = {"url"};
    private String[] parameters;
    private String restApi = "https://hj.vc/v1/shorten";

    @Override
    public DisplayType getDisplayType() {
        return DisplayType.JSON;
    }

    @Override
    protected Request getRequest() {
        String requestUrl = restApi;
        if (parameterNames.length > 0) {
            requestUrl = requestUrl + "?";
        }
        for (int i = 0; i < parameterNames.length; i++) {
            requestUrl = requestUrl + parameterNames[i] + "=" + parameters[i];
            if (i < parameterNames.length - 1) {
                requestUrl = requestUrl + "&";
            }
        }
        return Request.Post(requestUrl);
    }
}
