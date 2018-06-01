package name.dengchao.fx.plugin.builtin;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import name.dengchao.fx.plugin.DisplayType;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Calculator extends BuiltinPlugin {

    private String expression;
    private String description = "内置计算器，直接输入表达式";

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
    public String[] getParameterNames() {
        return new String[]{"expression"};
    }
}
