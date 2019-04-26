package name.dengchao.fx.plugin.builtin;

import lombok.Data;
import lombok.EqualsAndHashCode;

import name.dengchao.fx.plugin.DisplayType;

import java.io.InputStream;

@Data
@EqualsAndHashCode(callSuper = false)
public class Exit extends BuiltinPlugin {

    private String description = "Exit SmartLaunch";

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
