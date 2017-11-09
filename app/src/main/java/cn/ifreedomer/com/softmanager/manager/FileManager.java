package cn.ifreedomer.com.softmanager.manager;

import java.util.List;

/**
 * @author:eavawu
 * @since: 08/11/2017.
 * TODO:
 */

public class FileManager {
    private static FileManager mFileManager = new FileManager();

    private FileManager() {
    }

    public static FileManager getInstance() {
        return mFileManager;
    }

    public void analysisSDCard() {

    }
}
