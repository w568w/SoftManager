package cn.ifreedomer.com.softmanager.bean;

/**
 * @author:eavawu
 * @since: 25/10/2017.
 * TODO:
 */

public class DeviceInfoWrap<T> {
    public static final int TWO_VALUE = 1;
    public static final int FOUR_VALUE = 2;
    public static final int TITLE = 3;
    private int type;
    private T data;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static DeviceInfoWrap<DeviceTitle> createTitle(String string) {
        DeviceInfoWrap<DeviceTitle> basicInfo = new DeviceInfoWrap<>();
        DeviceTitle title = DeviceTitle.create(string);
        basicInfo.setType(DeviceInfoWrap.TITLE);
        basicInfo.setData(title);
        return basicInfo;
    }

    public static DeviceInfoWrap<TwoValue> createTwoValue(String title, String value) {
        DeviceInfoWrap<TwoValue> twoValueDeviceInfoWrap = new DeviceInfoWrap<>();
        TwoValue twoValue = new TwoValue(title, value);
        twoValueDeviceInfoWrap.setType(DeviceInfoWrap.TWO_VALUE);
        twoValueDeviceInfoWrap.setData(twoValue);
        return twoValueDeviceInfoWrap;
    }

    public static DeviceInfoWrap<FourValue> createFourValue(String title, String value, String subTitle, String subValue) {
        DeviceInfoWrap<FourValue> fourValueDeviceInfoWrap = new DeviceInfoWrap<>();
        fourValueDeviceInfoWrap.setType(DeviceInfoWrap.FOUR_VALUE);
        FourValue fourValue = new FourValue(title, value, subTitle, subValue);
        fourValueDeviceInfoWrap.setData(fourValue);
        return fourValueDeviceInfoWrap;
    }
}
