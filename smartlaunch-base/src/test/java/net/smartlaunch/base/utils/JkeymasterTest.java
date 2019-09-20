package net.smartlaunch.base.utils;

import com.tulskiy.keymaster.common.Provider;
import org.apache.pivot.wtk.Keyboard;
import org.junit.Test;

import javax.swing.*;
import java.awt.event.InputEvent;

public class JkeymasterTest {

    @Test
    public void name() {
        final Provider provider = Provider.getCurrentProvider(false);
        Runtime.getRuntime().addShutdownHook(new Thread("shutdown-hook") {
            @Override
            public void run() {
                provider.reset();
                provider.stop();
            }
        });
        provider.reset();
        provider.register(
                KeyStroke.getKeyStroke(Keyboard.KeyCode.SPACE, InputEvent.ALT_MASK),
                hotKey -> System.out.println(hotKey)
        );
    }
}
