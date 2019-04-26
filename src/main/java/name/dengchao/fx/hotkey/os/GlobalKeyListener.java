package name.dengchao.fx.hotkey.os;

import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.TreeSet;

public class GlobalKeyListener implements NativeKeyListener {

    private Set<Integer> pressedKeys = new TreeSet<>();

    private BringToFont bringToFont = new BringToFont();

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
        // Currently do nothing now.
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        boolean isAltPressed = (e.getModifiers() & NativeKeyEvent.ALT_MASK) != 0;
        if (e.getKeyCode() == NativeKeyEvent.VC_SPACE && isAltPressed) {
            bringToFont.act();
            consumeIfUsingHotKey(e);
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
    }

    private void consumeIfUsingHotKey(NativeKeyEvent e) {
        try {
            Field f = NativeInputEvent.class.getDeclaredField("reserved");
            f.setAccessible(true);
            f.setShort(e, (short) 0x01);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
