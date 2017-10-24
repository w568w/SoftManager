package cn.ifreedomer.com.softmanager;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import cn.ifreedomer.com.softmanager.factory.DefaultThreadFactory;
import cn.ifreedomer.com.softmanager.util.ShellUtils;
/**
 * @author wuyihua
 * @Date 2017/10/24
 * @todo 全局数据管理
 */

public class GlobalDataManager {

    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(5,
            new DefaultThreadFactory());


    private static GlobalDataManager globalDataManager = new GlobalDataManager();
    private boolean isRequestedRoot;
    private boolean hasRootPermission = false;

    public static GlobalDataManager getInstance() {
        return globalDataManager;
    }


    public ScheduledExecutorService getThreadPool(){
        return executorService;
    }

    public boolean isRequestedRoot() {
        return isRequestedRoot;
    }

    public void setRequestedRoot(boolean isRequestedRoot) {
        this.isRequestedRoot = isRequestedRoot;
    }

    /**
     * 检查或者请求root权限
     *
     * @return 是否获取到了root
     */
    public boolean checkOrRequestedRootPermission() {
        if (!isRequestedRoot) {
            isRequestedRoot = true;
            //申请root
            hasRootPermission = ShellUtils.checkRootPermission();
        }
        return hasRootPermission;
    }

}
