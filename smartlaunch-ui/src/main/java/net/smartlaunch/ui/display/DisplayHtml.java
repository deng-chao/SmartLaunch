package net.smartlaunch.ui.display;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import net.smartlaunch.base.utils.Utils;

import java.io.InputStream;

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
        String html = Utils.streamToStr(inputStream);
        webEngine.loadContent(html);
        getChildren().add(browser);
    }
}
