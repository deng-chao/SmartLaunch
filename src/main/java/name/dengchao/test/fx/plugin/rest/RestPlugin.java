package name.dengchao.test.fx.plugin.rest;

import name.dengchao.test.fx.plugin.Plugin;
import org.apache.http.client.fluent.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class RestPlugin implements Plugin {

    abstract protected Request getRequest();

    @Override
    public InputStream execute() {
        try {
            return getRequest().execute().returnContent().asStream();
        } catch (IOException e) {
            return new ByteArrayInputStream("Failed to call rest api".getBytes());
        }
    }
}
