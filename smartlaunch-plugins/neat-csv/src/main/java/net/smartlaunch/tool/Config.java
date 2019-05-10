package net.smartlaunch.tool;

public class Config {

    protected Integer columnCnt;
    protected String separator = ",";
    protected String outputDir;

    public Integer getColumnCnt() {
        return columnCnt;
    }

    public void setColumnCnt(Integer columnCnt) {
        this.columnCnt = columnCnt;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }
}
