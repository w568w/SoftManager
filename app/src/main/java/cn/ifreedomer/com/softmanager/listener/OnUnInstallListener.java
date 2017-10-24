package cn.ifreedomer.com.softmanager.listener;

import cn.ifreedomer.com.softmanager.model.AppInfo;

/**
 * @author:eavawu
 * @date: 20/02/2017.
 * @todo:
 */

public interface OnUnInstallListener {
    /**
     * 下载完成回调
     *
     * @param appInfo app信息
     */
    void onUninstall(AppInfo appInfo);

}
