package cn.ifreedomer.com.softmanager.network.requestservice;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import cn.ifreedomer.com.softmanager.bean.RespResult;
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
    private static final String BASE_URL = "http://www.ifreedomer.com/";
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
        return payService.getPayInfo(imei);
    }

    public static void main(String[] args) {
//        Observable<RespResult<PayInfo>> payInfo = getPayInfo("12345678910");
        RespResult<PayInfo> payInfoRespResult = new RespResult<>();
//        System.out.println(payInfo.blockingFirst().toString());
//        payInfo.subscribe(s -> System.out.println(s));
    }

}
