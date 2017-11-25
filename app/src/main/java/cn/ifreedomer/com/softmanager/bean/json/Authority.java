package cn.ifreedomer.com.softmanager.bean.json;

/**
 * @atuhor :eavawu
 * time:21/11/2017
 * todo:鉴权
 */

public class Authority {

    long id;
    private long activeTime;
    private long expirdTime;
    private String imei;
    private String price;


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(long activeTime) {
        this.activeTime = activeTime;
    }

    public long getExpirdTime() {
        return expirdTime;
    }

    public void setExpirdTime(long expirdTime) {
        this.expirdTime = expirdTime;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }


    @Override
    public String toString() {
        return "Authority{" +
                "id=" + id +
                ", activeTime=" + activeTime +
                ", expirdTime=" + expirdTime +
                ", imei='" + imei + '\'' +
                '}';
    }
}
