package net.smartlaunch.plugin.builtin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.smartlaunch.base.plugin.DisplayType;
import net.smartlaunch.plugin.RestPlugin;
import org.apache.http.client.methods.HttpPost;

@Data
@EqualsAndHashCode(callSuper = false)
public class UrlShortener extends RestPlugin {

    private String name = "url-shortener";
    private String description = "generate short url";
    private String[] parameterNames = {"url"};
    private String[] parameters;
    private String restApi = "https://hj.vc/v1/shorten";

    @Override
    public DisplayType getDisplayType() {
        return DisplayType.JSON;
    }

    @Override
    protected String[] getFinalParameters() {
        return parameters;
    }

    @Override
    protected HttpPost getRequest() {
        String requestUrl = restApi;
        requestUrl = appendParameter(requestUrl);
        return new HttpPost(requestUrl);
    }
}
