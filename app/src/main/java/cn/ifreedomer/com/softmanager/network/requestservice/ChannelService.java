package cn.ifreedomer.com.softmanager.network.requestservice;

import cn.ifreedomer.com.softmanager.bean.RespResult;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author:eavawu
 * @since: 14/11/2017.
 * TODO:
 */

public interface ChannelService {
    @GET("channel/getChannel")
    io.reactivex.Observable<RespResult<Integer>> getChannelState(@Query("version") String version, @Query("channel") String channel);

}
