package cn.ifreedomer.com.softmanager.network.requestservice;

import cn.ifreedomer.com.softmanager.bean.RespResult;
import cn.ifreedomer.com.softmanager.bean.json.PayInfo;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author:eavawu
 * @since: 14/11/2017.
 * TODO:
 */

public interface PayService {
    @GET("pay/getPayInfo")
    io.reactivex.Observable<RespResult<PayInfo>> getPayInfo(@Query("imei") String imei);

}
