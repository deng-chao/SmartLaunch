package name.dengchao.fx.plugin.rest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import name.dengchao.fx.plugin.DisplayType;
import org.apache.http.client.methods.HttpPost;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class QrCode extends RestPlugin {

    private String name = "qrcode";
    private String description = "沪江二维码服务";
    private String[] parameterNames = {"text", "size", "format", "referrer"};
    private String[] parameters;

    private String restApi = "https://qrcode.hjapi.com/v1/qrcode";
    private String[] defaultParameters = {"1024", "png", "SmartLaunch"};


    @Override
    protected String[] getFinalParameters() {
        List<String> a = Arrays.asList(parameters);
        List<String> b = Arrays.asList(defaultParameters);
        List<String> finalParameters = new ArrayList<>();
        finalParameters.addAll(a);
        finalParameters.addAll(b);
        return finalParameters.toArray(new String[finalParameters.size()]);
    }

    @Override
    protected HttpPost getRequest() {
        return new HttpPost(restApi);
    }

    @Override
    public DisplayType getDisplayType() {
        return DisplayType.JSON;
    }
}
