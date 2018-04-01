package name.dengchao.test.fx.plugin.builtin;

import java.io.InputStream;

import javafx.application.Platform;
import name.dengchao.test.fx.plugin.DisplayType;

public class Exit extends BuiltinPlugin {

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
