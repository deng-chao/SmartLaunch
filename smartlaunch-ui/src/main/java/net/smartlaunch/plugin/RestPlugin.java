package net.smartlaunch.plugin;

import net.smartlaunch.base.plugin.Configurable;
import net.smartlaunch.base.plugin.Plugin;
import net.smartlaunch.base.plugin.PluginExecutionException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public abstract class RestPlugin extends BuiltinPlugin implements Plugin, Configurable {

    abstract protected HttpRequestBase getRequest();

    abstract protected String[] getFinalParameters();

    private HttpClient client = HttpClientBuilder.create().build();

    @Override
    public InputStream execute() {
        try {
            return client.execute(getRequest()).getEntity().getContent();
        } catch (IOException e) {
            return new ByteArrayInputStream("Failed to call rest api".getBytes());
        }
    }

    protected String appendParameter(String requestUrl) {
        if (getParameterNames().length > 0) {
            requestUrl = requestUrl + "?";
        }
        for (int i = 0; i < getParameterNames().length; i++) {
            try {
                requestUrl = requestUrl + getParameterNames()[i] + "=" + URLEncoder.encode(getFinalParameters()[i], StandardCharsets.UTF_8.name());
                if (i < getParameterNames().length - 1) {
                    requestUrl = requestUrl + "&";
                }
            } catch (UnsupportedEncodingException e) {
                throw new PluginExecutionException("Failed to build request url.", e);
            }
        }
        return requestUrl;
    }
}
