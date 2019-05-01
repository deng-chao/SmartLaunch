package name.dengchao.fx.plugin.builtin;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import name.dengchao.fx.config.ConfigManager;
import name.dengchao.fx.plugin.DisplayType;
import org.apache.commons.lang.SystemUtils;

import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class DecodeBase64Image extends BuiltinPlugin {

    @Override
    public String getName() {
        return "decodeimage";
    }

    @Override
    public String getDescription() {
        return "decode image and save";
    }

    @Override
    public DisplayType getDisplayType() {
        return DisplayType.NONE;
    }

    @Override
    public void setParameters(String... parameters) {

    }

    @Override
    public JSONObject defaultConfig() {
        JSONObject config = new JSONObject();
        config.put("saveTo", SystemUtils.getUserHome() + "/Pictures/smart-launch");
        return config;
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public InputStream execute() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = clipboard.getContents(null);
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                String text = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                text = text.substring(text.indexOf("base64"));
                text = text.substring(text.indexOf(','));
                text = text.substring(0, text.indexOf('&'));
                log.info(text);
                byte[] output = DatatypeConverter.parseBase64Binary(text);

                String saveTo = ConfigManager.getConfig(getName()).getString("saveTo");
                String outputPath = saveTo + "\\" + sdf.format(new Date()) + ".jpg";
                log.info("outputPath:" + outputPath);
                File outputFile = new File(outputPath);
                if (!outputFile.getParentFile().exists()) {
                    outputFile.getParentFile().mkdirs();
                }
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                }
                try (OutputStream os = new FileOutputStream(outputPath)) {
                    os.write(output);
                }
            } catch (UnsupportedFlavorException | IOException e) {
                log.error("failed to decode and save image.", e);
            }
        }
        return null;
    }
}
