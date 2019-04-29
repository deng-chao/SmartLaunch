package name.dengchao.fx.hotkey.handler.display;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class DisplayJson extends DisplayText {

    @Override
    protected String format(String in) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(in, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }
}
