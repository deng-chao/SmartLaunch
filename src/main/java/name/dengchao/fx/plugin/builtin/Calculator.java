package name.dengchao.fx.plugin.builtin;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import name.dengchao.fx.plugin.DisplayType;
import org.springframework.core.io.ClassPathResource;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Data
@EqualsAndHashCode(callSuper = false)
public class Calculator extends BuiltinPlugin {

    private String expression;
    private String description = "内置计算器，直接输入表达式";

    private ImageView iconView;

    public Calculator() {
        try (InputStream fis = new ClassPathResource("calc.png").getInputStream()) {
            Image defaultIcon = new Image(fis);
            iconView = new ImageView(defaultIcon);
            iconView.setFitHeight(30);
            iconView.setFitWidth(30);
        } catch (IOException e) {
            e.printStackTrace();
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
            return new ByteArrayInputStream("wrong express".getBytes());
        }
    }

    @Override
    public String getName() {
        return "calc";
    }

    @Override
    public DisplayType getDisplayType() {
        return DisplayType.NUM;
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
