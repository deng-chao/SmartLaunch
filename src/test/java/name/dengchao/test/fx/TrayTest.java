package name.dengchao.test.fx;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class TrayTest {

    @Test
    public void testName() throws Exception {
        Resource resource = new ClassPathResource("icon.jpg");
        try (InputStream inputStream = resource.getInputStream()) {
            //Check the SystemTray is supported
            if (!SystemTray.isSupported()) {
                System.out.println("SystemTray is not supported");
                return;
            }
            final PopupMenu popup = new PopupMenu();
            BufferedImage image = ImageIO.read(inputStream);
            final TrayIcon trayIcon = new TrayIcon(image);
            final SystemTray tray = SystemTray.getSystemTray();

            // Create a pop-up menu components
            MenuItem aboutItem = new MenuItem("About");
            CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
            CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
            Menu displayMenu = new Menu("Display");
            MenuItem errorItem = new MenuItem("Error");
            MenuItem warningItem = new MenuItem("Warning");
            MenuItem infoItem = new MenuItem("Info");
            MenuItem noneItem = new MenuItem("None");
            MenuItem exitItem = new MenuItem("Exit");

            //Add components to pop-up menu
            popup.add(aboutItem);
            popup.addSeparator();
            popup.add(cb1);
            popup.add(cb2);
            popup.addSeparator();
            popup.add(displayMenu);
            displayMenu.add(errorItem);
            displayMenu.add(warningItem);
            displayMenu.add(infoItem);
            displayMenu.add(noneItem);
            popup.add(exitItem);

            trayIcon.setPopupMenu(popup);
            trayIcon.setImageAutoSize(true);

            trayIcon.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("Abbbd");
                    tray.remove(trayIcon);
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
            });

            tray.add(trayIcon);

        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
        Thread.currentThread().join();
    }
}
