package cn.ifreedomer.com.softmanager.manager;

/**
 * @author:eavawu
 * @since: 27/10/2017.
 * TODO:
 */

public class CleanManager {
    private static CleanManager cleanManager = new CleanManager();

    public static CleanManager getInstance() {
        return cleanManager;
    }

    public float queryTotalPkgCacheSize() {
        return 0;
    }


}
