package net.smartlaunch.plugin;

import net.smartlaunch.base.plugin.Configurable;
import net.smartlaunch.base.plugin.Plugin;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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
            requestUrl = requestUrl + getParameterNames()[i] + "=" + getFinalParameters()[i];
            if (i < getParameterNames().length - 1) {
                requestUrl = requestUrl + "&";
            }
        }
        return requestUrl;
    }
}
