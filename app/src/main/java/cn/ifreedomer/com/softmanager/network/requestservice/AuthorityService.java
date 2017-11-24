package cn.ifreedomer.com.softmanager.network.requestservice;

import cn.ifreedomer.com.softmanager.bean.RespResult;
import cn.ifreedomer.com.softmanager.bean.json.Authority;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @atuhor :eavawu
 * time:22/11/2017
 * todo:
 */
public interface AuthorityService {
    @GET("authority/getTime")
    io.reactivex.Observable<RespResult<Authority>> getTime(@Query("imei") String imei);


}
