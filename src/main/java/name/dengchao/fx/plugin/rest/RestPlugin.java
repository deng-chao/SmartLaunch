package name.dengchao.fx.plugin.rest;

import org.apache.http.client.fluent.Request;

import name.dengchao.fx.plugin.Plugin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class RestPlugin implements Plugin {

    abstract protected Request getRequest();

    abstract protected String[] getFinalParameters();

    @Override
    public InputStream execute() {
        try {
            return getRequest().execute().returnContent().asStream();
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
