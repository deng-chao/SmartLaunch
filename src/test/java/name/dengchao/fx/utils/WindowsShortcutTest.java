package name.dengchao.fx.utils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public class WindowsShortcutTest {

    @Test
    public void name() throws IOException, ParseException {
        File file = new File("C:\\ProgramData\\Microsoft\\Windows\\Start Menu\\Programs\\搜狗输入法\\链接\\官方网站.lnk");
        WindowsShortcut shortcut = new WindowsShortcut(file);
        System.out.println(shortcut.getRealFilename());
    }
}
