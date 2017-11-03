package cn.ifreedomer.com.softmanager;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import cn.ifreedomer.com.softmanager.factory.DefaultThreadFactory;
/**
 * @author wuyihua
 * @Date 2017/10/24
 * @todo 全局数据管理
 */

public class GlobalDataManager {

    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(5,
            new DefaultThreadFactory());


    private static GlobalDataManager globalDataManager = new GlobalDataManager();


    public static GlobalDataManager getInstance() {
        return globalDataManager;
    }


    public ScheduledExecutorService getThreadPool(){
        return executorService;
    }


}
