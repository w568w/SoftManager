package cn.ifreedomer.com.softmanager.util;

import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import cn.ifreedomer.com.softmanager.bean.EmptyFolder;

/**
 * @author wuyihua
 * @Date 2017/10/27
 * @todo
 */

public class FileUtil {
    public static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String THUMBNAIL_PATH = SD_PATH + "/DCIM/.thumbnails/";
    public static final String TAG = FileUtil.class.getSimpleName();
    public static final float MB = 1000 * 1000.0f;
    public static final float B = 1.0f;
    public static final float KB = 1000.0f;

    public static boolean isStopScan;
    public static void scanFile(String path, ScanListener scanListener) {
        if (scanListener != null) {
            scanListener.onScanStart();
        }
        LinkedList<File> folderList = new LinkedList<>();

        File file = new File(path);
        if (file.exists()) {
            //跟目录下的列表
            File[] rootFiles = file.listFiles();
            for (File rootFile : rootFiles) {
                if (rootFile.isDirectory()) {
                    folderList.add(rootFile);
                } else {
                    processFile(rootFile, scanListener);
                }
            }

            while (!folderList.isEmpty()) {
                if (isStopScan) {
                    return;
                }
                File curFile = folderList.removeFirst();
                File[] listFiles = curFile.listFiles();
                if (listFiles == null || listFiles.length == 0) {
                    continue;
                }
                for (int i = 0; i < listFiles.length; i++) {
                    if (listFiles[i].isDirectory()) {
                        folderList.add(listFiles[i]);
                    } else {
                        processFile(listFiles[i], scanListener);
                    }
                }
            }

        }

        if (scanListener != null) {
            scanListener.onScanFinish();
        }
    }


    public interface ScanListener {
        void onScanStart();

        void onScanProcess(File file);

        void onScanFinish();
    }


    public static void processFile(File file, ScanListener scanListener) {
        if (scanListener != null) {
            LogUtil.d(TAG, "processFile = " + file.getPath());
            scanListener.onScanProcess(file);
        }
    }


    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }



    /**
     * 获取空文件夹的集合
     *
     * @return 空文件夹集合
     */
    public static EmptyFolder getEmptyFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            EmptyFolder emptyFolder = new EmptyFolder();
            long size = 0;
            ArrayList<String> arrayList = new ArrayList<String>();
            File[] files = new File(SD_PATH).listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        if (file.getName().equals(".android_secure")) {
                            continue;
                        }
                        boolean isEmpty = isEmpty(file);
                        if (isEmpty) {
                            arrayList.add(file.getAbsolutePath());
                            size += file.length();
                        }
                    }
                }
            }
            emptyFolder.setPathList(arrayList);
            emptyFolder.setTotalSize(size);
            return emptyFolder;
        }
        return null;
    }


    /**
     * 判断一个文件夹是不是空文件夹
     *
     * @param folder 文件夹
     * @return 是否为空
     */
    public static boolean isEmpty(File folder) {
        File[] files = folder.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    boolean isEmpty = isEmpty(file);
                    if (!isEmpty) {
                        return false;
                    }
                } else if (file.isFile()) {
                    return false;
                }
            }
        }
        return true;
    }



    /**
     * 根据目录删除所有的文件
     *
     * @param filePath 文件路径
     */
    public static void deleteFileByPath(String filePath) {
        File rootfile = new File(filePath);
        if (rootfile != null && rootfile.exists()) {
            if (rootfile.isDirectory()) {
                File[] files = rootfile.listFiles();
                if (files != null) {
                    for (File childFile : files) {
                        deleteFileByPath(childFile.getAbsolutePath());
                    }
                }
                Log.e(TAG, "delete folder = " + filePath);
                rootfile.delete();

            } else if (rootfile.isFile()) {
                Log.e(TAG, "delete file = " + filePath);

                rootfile.delete();
            }
        }
    }



    public static boolean deleteDir(File dir) {
        return ShellUtils.execCommand("rm -r " + dir.getPath(), false).result == 0;

    }

    public static ShellUtils.CommandResult deleteFolderByRoot(String path) {
        return ShellUtils.execCommand("rm -r " + path, true);
    }


    public  static void inputStream2File(InputStream ins, File file) {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int read;
            while ((read = ins.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.close();
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
