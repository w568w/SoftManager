package cn.ifreedomer.com.softmanager.bean;

/**
 * @author wuyihua
 * @Date 2017/10/27
 * @todo 大文件信息记录
 */

public class FileInfo {
    public static final long BIG_FILE_SIZE = 1024;
    private String path;
    private String name;
    private String type;
    private double size;

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

    public void setSize(double size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", type='" + type + '\'' +
                ", size=" + size +
                '}';
    }
}
