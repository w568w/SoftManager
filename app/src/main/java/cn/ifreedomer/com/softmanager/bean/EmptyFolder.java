package cn.ifreedomer.com.softmanager.bean;

import java.util.ArrayList;

public class EmptyFolder {
	private long totalSize;
	private ArrayList<String> pathList;
	public long getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}
	public ArrayList<String> getPathList() {
		return pathList;
	}
	public void setPathList(ArrayList<String> pathList) {
		this.pathList = pathList;
	}
}
