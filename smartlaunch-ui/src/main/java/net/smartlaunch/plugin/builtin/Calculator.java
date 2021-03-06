package net.smartlaunch.plugin.builtin;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import net.smartlaunch.base.plugin.DisplayType;
import net.smartlaunch.plugin.BuiltinPlugin;
import net.smartlaunch.base.plugin.PluginExecutionException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Data
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class Calculator extends BuiltinPlugin {

    private String expression;
    private String summary = "内置计算器，直接输入表达式";

    private ImageView iconView;

    public Calculator() {
        try (InputStream fis = this.getClass().getClassLoader().getResourceAsStream("calc.png")) {
            Image defaultIcon = new Image(fis);
            iconView = new ImageView(defaultIcon);
            iconView.setFitHeight(30);
            iconView.setFitWidth(30);
        } catch (IOException e) {
            log.error("failed to read icon image for calc.", e);
        }
    }

    @Override
    public InputStream execute() {
        try {
            // use js engine directly.
            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");
            Object result = engine.eval(expression);
            return new ByteArrayInputStream(result.toString().getBytes());
        } catch (ScriptException e) {
            throw new PluginExecutionException("wrong express", e);
        }
    }

    @Override
    public String getName() {
        return "calc";
    }

    @Override
    public DisplayType getDisplayType() {
        return DisplayType.TIP;
    }

    @Override
    public void setParameters(String... parameters) {
        if (parameters == null || parameters.length == 0) {
            return;
        }
        this.expression = parameters[0];
    }

    @Override
    public ImageView getIcon() {
        return iconView;
    }

    @Override
    public String[] getParameterNames() {
        return new String[]{"expression"};
    }
}
