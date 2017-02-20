package cn.ifreedomer.com.softmanager.model;

import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class AppItem {
	public static final int INSTALL_INTERNAL = 0;
	public static final int INSTALL_STORAGE = 1;
	private String appName;
	private String appPackage;
	private String codePath;
	private String odexPath;
	private long codeSize;
	private Drawable appIcon;
	private int appCode;
	private long cacheSize;
	private String appVersion;
	private int appVersionCode;
	private boolean isChecked;
	private int location; // 0是安装位置为手机 1是安装位置为sd卡
	private String filePath;
	
	private String name;
	private boolean includeEBook;
	private boolean includeAudio;
	private boolean includeObb;
	private long memorySize;
	private int pid;
	
	private boolean systemApp;
	private boolean userApp;
	private boolean chongfuApp;
	private boolean haoziyuanApp;
	
	private boolean enable;
	
	private byte[] icon;
	private long installTime;
	private long lastStartTime;
	private long startCount;
	private long spaceTime;
	
	private boolean install;
	
	private ArrayList<AppItem> cacheList;
	private boolean isGroup;
	
	private boolean keep;
	
	private ApplicationInfo applicationInfo;
	private Resources resources;
	
	//耗费流量
	private long flow;
	private int uid;
	private long tempFlow;
	private Drawable drawable;
	
	private String TypeFlag;//耗资源的类型
	
	private String groupName;
	
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getTypeFlag() {
		return TypeFlag;
	}
	
	public void setTypeFlag(String typeFlag) {
		TypeFlag = typeFlag;
	}
	
	public boolean isUserApp() {
		return userApp;
	}

	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}

	public boolean isChongfuApp() {
		return chongfuApp;
	}

	public void setChongfuApp(boolean chongfuApp) {
		this.chongfuApp = chongfuApp;
	}

	public boolean isHaoziyuanApp() {
		return haoziyuanApp;
	}

	public void setHaoziyuanApp(boolean haoziyuanApp) {
		this.haoziyuanApp = haoziyuanApp;
	}

	public ApplicationInfo getApplicationInfo() {
		return applicationInfo;
	}

	public void setApplicationInfo(ApplicationInfo applicationInfo) {
		this.applicationInfo = applicationInfo;
	}

	public Resources getResources() {
		return resources;
	}

	public void setResources(Resources resources) {
		this.resources = resources;
	}

	public boolean isKeep() {
		return keep;
	}

	public void setKeep(boolean keep) {
		this.keep = keep;
	}

	public boolean isInstall() {
		return install;
	}

	public void setInstall(boolean install) {
		this.install = install;
	}

	public ArrayList<AppItem> getCacheList() {
		return cacheList;
	}

	public void setCacheList(ArrayList<AppItem> cacheList) {
		this.cacheList = cacheList;
	}

	public boolean isGroup() {
		return isGroup;
	}

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

	public long getSpaceTime() {
		return spaceTime;
	}

	public void setSpaceTime(long spaceTime) {
		this.spaceTime = spaceTime;
	}

	public long getLastStartTime() {
		return lastStartTime;
	}

	public void setLastStartTime(long lastStartTime) {
		this.lastStartTime = lastStartTime;
	}

	public long getStartCount() {
		return startCount;
	}

	public void setStartCount(long startCount) {
		this.startCount = startCount;
	}

	public long getInstallTime() {
		return installTime;
	}

	public void setInstallTime(long installTime) {
		this.installTime = installTime;
	}

	public byte[] getIcon() {
		return icon;
	}

	public void setIcon(byte[] icon) {
		this.icon = icon;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public boolean isSystemApp() {
		return systemApp;
	}

	public void setSystemApp(boolean systemApp) {
		this.systemApp = systemApp;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public long getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(long memorySize) {
		this.memorySize = memorySize;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isIncludeEBook() {
		return includeEBook;
	}

	public void setIncludeEBook(boolean includeEBook) {
		this.includeEBook = includeEBook;
	}

	public boolean isIncludeAudio() {
		return includeAudio;
	}

	public void setIncludeAudio(boolean includeAudio) {
		this.includeAudio = includeAudio;
	}

	public boolean isIncludeObb() {
		return includeObb;
	}

	public void setIncludeObb(boolean includeObb) {
		this.includeObb = includeObb;
	}

	public int getAppVersionCode() {
		return appVersionCode;
	}

	public void setAppVersionCode(int appVersionCode) {
		this.appVersionCode = appVersionCode;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppPackage() {
		return appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}

	public String getCodePath() {
		return codePath;
	}

	public void setCodePath(String codePath) {
		this.codePath = codePath;
	}

	public long getCodeSize() {
		return codeSize;
	}

	public void setCodeSize(long codeSize) {
		this.codeSize = codeSize;
	}

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	public int getAppCode() {
		return appCode;
	}

	public void setAppCode(int appCode) {
		this.appCode = appCode;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public long getCacheSize() {
		return cacheSize;
	}

	public void setCacheSize(long cacheSize) {
		this.cacheSize = cacheSize;
	}

	public String getOdexPath() {
		return odexPath;
	}

	public void setOdexPath(String odexPath) {
		this.odexPath = odexPath;
	}
	
	//耗流量
	public Drawable getDrawable() {
		return drawable;
	}
	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}
	public long getTempFlow() {
		return tempFlow;
	}
	public void setTempFlow(long tempFlow) {
		this.tempFlow = tempFlow;
	}
	public long getFlow() {
		return flow;
	}
	public void setFlow(long flow) {
		this.flow = flow;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}

}
