package name.dengchao.test.fx.plugin.builtin;

import lombok.NoArgsConstructor;
import name.dengchao.test.fx.plugin.DisplayType;
import org.springframework.util.StreamUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@NoArgsConstructor
public class Calculator extends BuiltinPlugin {

    private String expression;

    @Override
    public InputStream execute() {
        try {
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

    public static void main(String[] args) throws IOException {
        Calculator calculator = new Calculator();
        calculator.setParameters("1+2");
        String in = StreamUtils.copyToString(calculator.execute(), StandardCharsets.UTF_8);
        System.out.println(in);
    }
}
