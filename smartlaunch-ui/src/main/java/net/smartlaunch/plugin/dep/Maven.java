package net.smartlaunch.plugin.dep;

import com.alibaba.fastjson.JSONObject;

import net.smartlaunch.base.utils.Constants;
import net.smartlaunch.base.utils.Utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Maven implements Dependency {

    private static final String MVN_URL = "http://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.6.1/binaries/apache-maven-3.6.1-bin.zip";

    @Override
    public JSONObject defaultConfig() {
        JSONObject json = new JSONObject();
        String mvnHome = System.getenv("M2_HOME");
        json.put("mvnHome", mvnHome);
        json.put("settings", Constants.USER_HOME + File.separator + ".m2" + File.separator + "settings.xml");
        json.put("repository", Constants.USER_HOME + File.separator + ".m2" + File.separator + "repository");
        return json;
    }

    public String install() {
        InputStream inputStream = null;
        ZipInputStream zin = null;
        try {
            URL url = new URL(MVN_URL);
            inputStream = url.openStream();
            File dir = new File(
                Utils.getAppHome().concat(File.separator).concat("tmp").concat(File.separator).concat("apache-maven"));
            if (dir.exists()) {
                dir.delete();
            }
            dir.mkdirs();
            zin = new ZipInputStream(inputStream);
            ZipEntry zipEntry = zin.getNextEntry();
            while (zipEntry != null) {
                if (zipEntry.isDirectory()) {
                    zipEntry = zin.getNextEntry();
                    continue;
                }
                int indexOfLastSlash = zipEntry.getName().lastIndexOf("/");
                String folderPath = indexOfLastSlash > 0 ? dir.getAbsolutePath() + File.separator + zipEntry.getName()
                    .substring(0, indexOfLastSlash) : dir.getAbsolutePath();
                String fileName = zipEntry.getName().substring(indexOfLastSlash + 1);
                File folder = new File(folderPath);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                File dest = new File(folder, fileName);
                dest.createNewFile();
                FileUtils.copyToFile(zin, dest);
                zipEntry = zin.getNextEntry();
            }
            zin.closeEntry();
            zin.close();
            File projectMvn = new File(Utils.getAppHome().concat(File.separator).concat("maven"));
            if (projectMvn.exists()) {
                projectMvn.delete();
            }
            FileUtils.moveDirectory(dir, projectMvn);
            return projectMvn.getAbsolutePath();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(zin);
        }
        return null;
    }
}
