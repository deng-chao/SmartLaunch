package net.smartlaunch.plugin.tray;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public abstract class Tray {

    private static TrayIcon trayIcon;

    public static void createTray() {
        try (InputStream inputStream = Tray.class.getClassLoader().getResourceAsStream("icon.jpg")) {
            //Check the SystemTray is supported
            if (!SystemTray.isSupported()) {
                log.info("SystemTray is not supported");
                return;
            }
            final PopupMenu popup = new PopupMenu();
            BufferedImage image = ImageIO.read(inputStream);
            trayIcon = new TrayIcon(image);
            final SystemTray tray = SystemTray.getSystemTray();

            // Create a pop-up menu components
            CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
            CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");

            MenuItem aboutItem = new MenuItem("About");
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
            trayIcon.addMouseListener(new TrayEventListener());
            tray.add(trayIcon);

        } catch (AWTException e) {
            log.info("TrayIcon could not be added.");
        } catch (IOException e) {
            log.error("Failed to read icon file.", e);
        }
    }

    public static void notify(String title, String content, TrayIcon.MessageType type) {
        trayIcon.displayMessage(title, content, type);
    }
}
