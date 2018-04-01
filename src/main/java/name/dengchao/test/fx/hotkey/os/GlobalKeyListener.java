package name.dengchao.test.fx.hotkey.os;

import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class GlobalKeyListener implements NativeKeyListener {

    private Set<Integer> pressedKeys = new TreeSet<>();

    private static Map<String, Action> key2Action = new ConcurrentHashMap<>();

    public static void register(String key, Action action) {
        key2Action.put(key, action);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
        // Currently do nothing now.
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        pressedKeys.add(e.getKeyCode());
        consumeIfUsingHotKey(e);
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        invokeAction();
        pressedKeys.remove(nativeKeyEvent.getKeyCode());
    }

    private volatile AtomicBoolean running = new AtomicBoolean(false);

    private void consumeIfUsingHotKey(NativeKeyEvent e) {
        Action action = key2Action.get(concatKeyCode());
        if (action != null) {
            try {
                Field f = NativeInputEvent.class.getDeclaredField("reserved");
                f.setAccessible(true);
                f.setShort(e, (short) 0x01);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void invokeAction() {
        if (running.get()) {
            return;
        }
        running.set(true);
        Action action = key2Action.get(concatKeyCode());
        if (action != null) {
            action.act();
        }
        running.set(false);
    }

    private String concatKeyCode() {
        String key = "";
        for (Integer pressedKey : pressedKeys) {
            key = key.concat("-").concat(pressedKey.toString());
        }
        return key;
    }
}
