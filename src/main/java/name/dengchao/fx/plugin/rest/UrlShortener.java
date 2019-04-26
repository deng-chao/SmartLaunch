package name.dengchao.fx.plugin.rest;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.http.client.fluent.Request;

import name.dengchao.fx.plugin.DisplayType;

@Data
@EqualsAndHashCode(callSuper = false)
public class UrlShortener extends RestPlugin {

    private String name = "url-shortener";
    private String description = "短链接服务";
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
    protected Request getRequest() {
        String requestUrl = restApi;
        requestUrl = appendParameter(requestUrl);
        return Request.Post(requestUrl);
    }
}
