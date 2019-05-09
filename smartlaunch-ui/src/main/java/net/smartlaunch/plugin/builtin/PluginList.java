package net.smartlaunch.plugin.builtin;

import lombok.extern.slf4j.Slf4j;
import net.smartlaunch.base.plugin.DisplayType;
import net.smartlaunch.plugin.BuiltinPlugin;
import net.smartlaunch.plugin.exception.PluginExecutionException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class PluginList extends BuiltinPlugin {


    @Override
    public String getName() {
        return "ps";
    }

    @Override
    public String getSummary() {
        return "search plugin";
    }

    @Override
    public DisplayType getDisplayType() {
        return DisplayType.LIST_VIEW;
    }

    @Override
    public void setParameters(String... parameters) {

    }

    private static final String ERR_MSG = "failed to list repository plugins";

    @Override
    public InputStream execute() {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet("http://plugin-manager.smart-launch.net/v1/plugins");
        try {
            HttpResponse response = client.execute(get);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode / 100 == 2) {
                return response.getEntity().getContent();
            } else {
                throw new PluginExecutionException(ERR_MSG + ", " + statusCode);
            }
        } catch (IOException e) {
            throw new PluginExecutionException(ERR_MSG, e);
        }
    }
}
