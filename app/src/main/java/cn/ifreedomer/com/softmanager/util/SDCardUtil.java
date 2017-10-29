package cn.ifreedomer.com.softmanager.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * @author:eavawu
 * @since: 29/10/2017.
 * TODO:
 */

public class SDCardUtil {

    /**
     * @return 手机内存的总空间大小
     */
    static public float getTotalInternalMemorySize(){
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());

        long blockSize = stat.getBlockSize();
        float totalBlocks = stat.getBlockCount();

        return totalBlocks * blockSize;
    }

    /**
     *
     * @return 手机内存的可用空间大小
     */
    static public float getAvailableInternalMemorySize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());

        long blockSize = stat.getBlockSize();
        float availableBlocks = stat.getAvailableBlocks();

        return availableBlocks * blockSize;
    }


    static public  float getUsedInternalMemorySize(){
        return getTotalInternalMemorySize() - getAvailableInternalMemorySize();
    }
}
