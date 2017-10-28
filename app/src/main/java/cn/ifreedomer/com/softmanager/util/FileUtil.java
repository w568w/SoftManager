package cn.ifreedomer.com.softmanager.util;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import cn.ifreedomer.com.softmanager.bean.FileInfo;

/**
 * @author wuyihua
 * @Date 2017/10/27
 * @todo
 */

public class FileUtil {


    public static List<FileInfo> getBigFiles() {
        String sdCard = Environment.getExternalStorageDirectory().toString();
        return scanBigFile(new File(sdCard));
    }


    public static List<FileInfo> scanBigFile(File file) {
        List<FileInfo> fileInfoList = new ArrayList<>();
        if (file != null && file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File curFile : files) {
                if (curFile.listFiles() == null) {
                    if (curFile.length() > FileInfo.BIG_FILE_SIZE) {
                        FileInfo fileInfo = new FileInfo();
                        fileInfo.setName(curFile.getName());
                        fileInfo.setPath(curFile.getPath());
                        String mimeType = new MimetypesFileTypeMap().getContentType(curFile);
                        fileInfo.setType(mimeType);
                        fileInfo.setSize(curFile.length());
                        fileInfoList.add(fileInfo);
                    }
                } else {
                    scanBigFile(curFile);
                }
            }
        }
        return fileInfoList;
    }
}
