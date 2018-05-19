package cn.ifreedomer.com.softmanager.bean;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Mount {
    protected final File mDevice;
    protected final Set<String> mFlags;
    protected final File mMountPoint;
    protected final String mType;

    public Mount(File device, File path, String type, String flagsStr) {
        this.mDevice = device;
        this.mMountPoint = path;
        this.mType = type;
        this.mFlags = new HashSet(Arrays.asList(flagsStr.split(",")));
    }

    public File getDevice() {
        return this.mDevice;
    }

    public File getMountPoint() {
        return this.mMountPoint;
    }

    public String getType() {
        return this.mType;
    }

    public Set<String> getFlags() {
        return this.mFlags;
    }

    public String toString() {
        return String.format("%s on %s type %s %s", new Object[]{this.mDevice, this.mMountPoint, this.mType, this.mFlags});
    }
}