package cn.ifreedomer.com.softmanager.bean.clean;

import cn.ifreedomer.com.softmanager.bean.GarbageInfo;

/**
 * @author:eavawu
 * @since: 09/11/2017.
 * TODO:
 */

public class EmptyFolderInfo {
    private String emptyTitle;
    private float emptySize;
    private int emptyCount;

    public static GarbageInfo<EmptyFolderInfo> create(float emptySize, int emptyCount, String emptyTitle) {
        EmptyFolderInfo emptyFolderInfo = new EmptyFolderInfo();
        emptyFolderInfo.setEmptySize(emptySize);
        emptyFolderInfo.setEmptyCount(emptyCount);
        emptyFolderInfo.setEmptyTitle(emptyTitle);
        GarbageInfo<EmptyFolderInfo> garbageInfo = new GarbageInfo<>();
        garbageInfo.setType(GarbageInfo.TYPE_EMPTY_FILE);
        garbageInfo.setData(emptyFolderInfo);
        return garbageInfo;
    }

    public String getEmptyTitle() {
        return emptyTitle;
    }

    public void setEmptyTitle(String emptyTitle) {
        this.emptyTitle = emptyTitle;
    }

    public float getEmptySize() {
        return emptySize;
    }

    public void setEmptySize(float emptySize) {
        this.emptySize = emptySize;
    }

    public int getEmptyCount() {
        return emptyCount;
    }

    public void setEmptyCount(int emptyCount) {
        this.emptyCount = emptyCount;
    }
}
