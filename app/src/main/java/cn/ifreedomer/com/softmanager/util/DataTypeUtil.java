package cn.ifreedomer.com.softmanager.util;

import java.math.BigDecimal;

/**
 * @author:eavawu
 * @since: 30/10/2017.
 * TODO:
 */

public class DataTypeUtil {
    public static float getTwoFloat(float param) {
        BigDecimal b = new BigDecimal(param);
        int roundingMode = 4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等
        return b.setScale(2, roundingMode).floatValue();
    }

    public static String getTextBySize(float size) {
        if (size < FileUtil.KB) {
            return DataTypeUtil.getTwoFloat(size) + " B";
        } else if (size > FileUtil.KB && size < FileUtil.MB) {
            return DataTypeUtil.getTwoFloat(size / FileUtil.KB) + " KB";
        } else if (size > FileUtil.MB && size < FileUtil.MB * 1000) {
            return DataTypeUtil.getTwoFloat(size / FileUtil.MB) + " MB";
        } else if (size > FileUtil.MB * 1000) {
            return DataTypeUtil.getTwoFloat(size / (FileUtil.MB*1000)) + " GB";
        }
        return "";
    }
}
