package cn.ifreedomer.com.softmanager.bean;


/**
 * @atuhor :eavawu
 * time:05/12/2017
 * todo:
 */

public class Channel {
    public static final int CLOSE = 0;
    public static final int OPEN = 1;
    long id;
    private String version;
    private String channel_name;
    private Integer state;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
