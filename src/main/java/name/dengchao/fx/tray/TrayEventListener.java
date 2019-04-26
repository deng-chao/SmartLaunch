package name.dengchao.fx.tray;

import name.dengchao.fx.hotkey.os.BringToFont;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TrayEventListener implements MouseListener {

    private BringToFont bringToFont = new BringToFont();

    @Override
    public void mouseClicked(MouseEvent e) {
        bringToFont.act();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
