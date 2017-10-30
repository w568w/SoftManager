package cn.ifreedomer.com.softmanager.bean;

import java.io.File;

import cn.ifreedomer.com.softmanager.util.FileUtil;

/**
 * @author wuyihua
 * @Date 2017/10/27
 * @todo 大文件信息记录
 */

public class FileInfo {
    public static final long BIG_FILE_SIZE = 1024 * 1024 * 10;
    private String path;
    private String name;
    private String type;
    private float size;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSize(float size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", size=" + size +
                ", isChecked=" + isChecked +
                '}';
    }

    public static FileInfo getFileInfo(File file) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setName(file.getName());
        fileInfo.setPath(file.getPath());
        fileInfo.setSize(file.length());
        fileInfo.setType(FileUtil.getMimeType(file.getPath()));
        return fileInfo;
    }

    public float getSize() {
        return size;
    }
}
