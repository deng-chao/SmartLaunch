package name.dengchao.fx.plugin.builtin;

import name.dengchao.fx.plugin.DisplayType;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.InputStream;

public class Copy extends BuiltinPlugin {

    private String content;

    @Override
    public String getName() {
        return "cp";
    }

    @Override
    public String getDescription() {
        return "cp input";
    }

    @Override
    public DisplayType getDisplayType() {
        return DisplayType.NONE;
    }

    @Override
    public void setParameters(String... parameters) {
        if (parameters != null && parameters.length != 0 && parameters[0] != null) {
            this.content = parameters[0];
        }
    }

    @Override
    public InputStream execute() {
        StringSelection selection = new StringSelection(content);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
        return null;
    }
}
