package cn.ifreedomer.com.softmanager.bean.json;

/**
 * @atuhor :eavawu
 * time:18/11/2017
 * todo:
 */
public class PayInfo {
    private String payInfo;
    private int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }

    @Override
    public String toString() {
        return "PayInfo{" +
                "payInfo='" + payInfo + '\'' +
                '}';
    }
}
