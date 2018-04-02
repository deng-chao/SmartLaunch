package name.dengchao.test.fx.plugin.builtin;

import java.io.InputStream;

import lombok.Data;
import name.dengchao.test.fx.plugin.DisplayType;

@Data
public class Exit extends BuiltinPlugin {

    private String description;

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public DisplayType getDisplayType() {
        return DisplayType.NONE;
    }

    @Override
    public void setParameters(String... parameters) {

    }

    @Override
    public InputStream execute() {
        System.exit(0);
        return null;
    }
}
