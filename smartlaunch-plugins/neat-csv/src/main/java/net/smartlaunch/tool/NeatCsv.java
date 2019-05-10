package net.smartlaunch.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import net.smartlaunch.base.plugin.ConfigManager;
import net.smartlaunch.base.plugin.Configurable;
import net.smartlaunch.base.plugin.DisplayType;
import net.smartlaunch.base.plugin.Plugin;
import net.smartlaunch.base.plugin.PluginExecutionException;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class NeatCsv implements Plugin, Configurable {

    private String csvPath;

    @Override
    public JSONObject defaultConfig() {
        return JSON.parseObject(JSON.toJSONString(new Config()));
    }

    @Override
    public String getName() {
        return "neat-csv";
    }

    @Override
    public String getSummary() {
        return "nest csv file";
    }

    @Override
    public DisplayType getDisplayType() {
        return DisplayType.NONE;
    }

    @Override
    public void setParameters(String... parameters) {
        if (parameters == null || parameters.length == 0) {
            csvPath = null;
            return;
        }
        csvPath = parameters[0];
    }

    @Override
    public InputStream execute() {
        JSONObject configJson = ConfigManager.getConfig(getName());
        Config config = configJson.toJavaObject(Config.class);
        File f = new File(csvPath);
        File outputFile = new File(csvPath + ".neat");
        final Integer columnCnt = config.columnCnt;
        try (BufferedReader br = new BufferedReader(new FileReader(f));
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String actualLine = line;
                String[] parts = line.split(config.separator, -1);
                int actualColumnCnt = parts.length;
                while (actualColumnCnt < columnCnt) {
                    actualLine = actualLine + "<br>" + br.readLine();
                    parts = actualLine.split(config.separator, -1);
                    actualColumnCnt = parts.length;
                }
                if (columnCnt < actualColumnCnt) {
                    throw new PluginExecutionException("unable to deal with line: " + actualLine);
                }
                // 使用~作为分隔符，去掉数据中的 ~
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].replaceAll("~", "");
                }
                String outputLine = StringUtils.join(parts, "~");
                bw.write(outputLine);
                bw.write(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
