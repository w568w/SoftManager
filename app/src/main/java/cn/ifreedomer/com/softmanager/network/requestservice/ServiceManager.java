package cn.ifreedomer.com.softmanager.network.requestservice;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import cn.ifreedomer.com.softmanager.bean.RespResult;
import cn.ifreedomer.com.softmanager.bean.json.Authority;
import cn.ifreedomer.com.softmanager.bean.json.PayInfo;
import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author:eavawu
 * @since: 14/11/2017.
 * TODO:
 */

public class ServiceManager {
    private static final String BASE_URL = "http://192.168.0.105:8080/";
    private static Retrofit sStringRetrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();


    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
            .build();


    public static Observable<RespResult<PayInfo>> getPayInfo(String imei) {
        PayService payService = retrofit.create(PayService.class);
        Observable<RespResult<PayInfo>> payInfoObservable = payService.getPayInfo(imei);
        return payInfoObservable;
    }


    public static Observable<RespResult<Authority>> getTime(String imei) {
        AuthorityService authorityService = retrofit.create(AuthorityService.class);
        Observable<RespResult<Authority>> authorityServicePayInfo = authorityService.getTime(imei);
        return authorityServicePayInfo;
    }

    public static Observable<RespResult<Integer>> getChannelState(String version, String channel) {
        ChannelService channelService = retrofit.create(ChannelService.class);
        return channelService.getChannelState(version, channel);
    }

    public static void main(String[] args) {
        Observable<RespResult<Integer>> channelStateObserver = getChannelState("1", "vivo");
        channelStateObserver.subscribe(s -> System.out.println(s));
    }

//    public static void main(String[] args) {
//        Observable<RespResult<Authority>> payInfo = getTime("1234567222");
//        System.out.println(payInfo.blockingFirst().toString());
////        payInfo.subscribe(s -> System.out.println(s));
//    }

}
