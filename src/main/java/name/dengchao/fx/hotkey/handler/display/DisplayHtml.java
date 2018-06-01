package name.dengchao.fx.hotkey.handler.display;

import org.apache.commons.io.IOUtils;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class DisplayHtml extends Region implements DisplayResult {

    private static final WebView browser = new WebView();
    private static final WebEngine webEngine = browser.getEngine();

    @Override
    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
    }

    @Override
    protected double computePrefWidth(double height) {
        return 750;
    }

    @Override
    protected double computePrefHeight(double width) {
        return 500;
    }

    @Override
    public void display(InputStream inputStream) {
        try {
            StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            webEngine.loadContent("<html><body><b>JavaFX</b></body></html>");
            getChildren().add(browser);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
