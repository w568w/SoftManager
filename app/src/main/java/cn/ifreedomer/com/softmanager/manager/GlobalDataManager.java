package cn.ifreedomer.com.softmanager.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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


    public ScheduledExecutorService getThreadPool() {
        return executorService;
    }


    private Map<String, String> actionMap = null;

    public Map<String, String> getActionMap() {
        return actionMap;
    }

    public void setActionMap(Map<String, String> actionMap) {
        this.actionMap = actionMap;
    }

    private ConcurrentHashMap<String, Object> tempMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, Object> getTempMap() {
        return tempMap;
    }

    public void setTempMap(ConcurrentHashMap<String, Object> tempMap) {
        this.tempMap = tempMap;
    }
}
