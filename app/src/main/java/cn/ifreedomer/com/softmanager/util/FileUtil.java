package cn.ifreedomer.com.softmanager.util;

import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.ifreedomer.com.softmanager.bean.EmptyFolder;
import cn.ifreedomer.com.softmanager.bean.FileInfo;
import cn.ifreedomer.com.softmanager.service.FileScanService;

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
//
//    public static List<FileInfo> getBigFiles() {
//        String sdCard = Environment.getExternalStorageDirectory().toString();
//        return scanBigFile(new File(sdCard));
//    }

//
//    public static List<FileInfo> scanBigFile(File file) {
//        List<FileInfo> fileInfoList = new ArrayList<>();
//        if (file != null && file.exists() && file.isDirectory()) {
//            File[] files = file.listFiles();
//            for (File curFile : files) {
//                if (curFile.listFiles() == null) {
//                    if (curFile.length() > FileInfo.BIG_FILE_SIZE) {
//                        FileInfo fileInfo = new FileInfo();
//                        fileInfo.setName(curFile.getName());
//                        fileInfo.setPath(curFile.getPath());
//                        String mimeType = new MimetypesFileTypeMap().getContentType(curFile);
//                        fileInfo.setType(mimeType);
//                        fileInfo.setSize(curFile.length());
//                        fileInfoList.add(fileInfo);
//                    }
//                } else {
//                    scanBigFile(curFile);
//                }
//            }
//        }
//        return fileInfoList;
//    }


    public static List<FileInfo> scanSDCard4BigFile(String path, FileScanService.ScanListener scanListener) {

        List<FileInfo> bigFileInfoList = new ArrayList<>();
        float scanSize = 0;
        float totalSize = SDCardUtil.getUsedInternalMemorySize() / (1000 * 1000);
        Log.e(TAG, "totalSize: " + totalSize);
        int fileNum = 0, folderNum = 0;
        File file = new File(path);
        if (file.exists()) {
            LinkedList<File> list = new LinkedList<File>();
            File[] files = file.listFiles();
            for (File file2 : files) {
                if (file2.isDirectory()) {
                    System.out.println("文件夹:" + file2.getAbsolutePath());
                    list.add(file2);
                    if (file2.listFiles().length == 0) {
                        scanSize = scanSize + file2.length();
                    }

                } else {
                    System.out.println("文件:" + file2.getAbsolutePath());
                    fileNum++;
                    processBigFile(file2, bigFileInfoList);


                    scanSize = scanSize + file2.length();
                    scanListener.onScanProcess(scanSize / (1000 * 1000) * 100 / totalSize);

                }
            }
            File temp_file;
            while (!list.isEmpty()) {
                temp_file = list.removeFirst();
                files = temp_file.listFiles();
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        list.add(file2);
                        folderNum++;
                        if (file2.listFiles().length == 0) {
                            scanSize = scanSize + file2.length();
                        }


                    } else {
                        System.out.println("文件:" + file2.getAbsolutePath());
                        fileNum++;

                        processBigFile(file2, bigFileInfoList);


                        scanSize = scanSize + file2.length();

                        scanListener.onScanProcess(scanSize / (MB) * 100 / totalSize);

                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
        float bigSize = 0;
        for (int i = 0; i < bigFileInfoList.size(); i++) {
            bigSize = bigSize + bigFileInfoList.get(i).getSize();
        }

        scanListener.onScanFinish(bigSize, bigFileInfoList);
        System.out.println("文件夹共有:" + folderNum + ",文件共有:" + fileNum);
        return bigFileInfoList;
    }

    public static void processBigFile(File curFile, List<FileInfo> fileInfoList) {
        if (curFile.length() > FileInfo.BIG_FILE_SIZE) {
            FileInfo fileInfo = FileInfo.getFileInfo(curFile);
            fileInfo.setSize(DataTypeUtil.getTwoFloat(fileInfo.getSize() / (MB)));
            fileInfoList.add(fileInfo);
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


    public static List<FileInfo> getFolderFiles(String folderPath, List<FileInfo> fileInfoList) {
        File folder = new File(folderPath);
        if (!folder.exists() || folder.list().length == 0) {
            Log.e(TAG, "getFolderFiles: ");
            return null;
        }
        for (int i = 0; i < folder.list().length; i++) {
            File[] files = folder.listFiles();
            if (files[i].getName().startsWith(".")) {
                continue;
            }
            if (files[i].isDirectory()) {
                getFolderFiles(files[i].getPath(), fileInfoList);
            } else {

                FileInfo fileInfo = FileInfo.getFileInfo(folder.listFiles()[i]);
                fileInfoList.add(fileInfo);
                LogUtil.e(TAG, fileInfo.toString());
            }
        }
        return fileInfoList;
    }


    /**
     * 获取空文件夹的集合
     *
     * @return
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
     * @param folder
     * @return
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


}
