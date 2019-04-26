package name.dengchao.fx.plugin.builtin;

import name.dengchao.fx.plugin.DisplayType;

import javax.xml.bind.DatatypeConverter;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                System.out.println(text);
                byte[] output = DatatypeConverter.parseBase64Binary(text);
                String outputPath = "F:\\dengchao\\Desktop\\壁纸\\" + sdf.format(new Date()) + ".jpg";
                System.out.println(outputPath);
                File outputFile = new File(outputPath);
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                }
                try (OutputStream os = new FileOutputStream(outputPath)) {
                    os.write(output);
                }
            } catch (UnsupportedFlavorException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
