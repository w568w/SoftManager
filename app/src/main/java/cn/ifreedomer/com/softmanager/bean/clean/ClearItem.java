package cn.ifreedomer.com.softmanager.bean.clean;

public class ClearItem {
	private String packageName;
	private boolean isChecked;
	private String filePath = "";
	private long fileSize;
	private boolean includeEBook;
	private boolean includeAudio;
	private boolean includeApk;
	private String name;
	private String md5Path;
	private String md5PackageName;
	
	
	public String getMd5Path() {
		return md5Path;
	}

	public void setMd5Path(String md5Path) {
		this.md5Path = md5Path;
	}

	public String getMd5PackageName() {
		return md5PackageName;
	}

	public void setMd5PackageName(String md5PackageName) {
		this.md5PackageName = md5PackageName;
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

	public boolean isIncludeApk() {
		return includeApk;
	}

	public void setIncludeApk(boolean includeApk) {
		this.includeApk = includeApk;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

}
