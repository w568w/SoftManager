package cn.ifreedomer.com.softmanager.network.requestservice;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

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
    private static final String BASE_URL = "http://192.168.0.107:8080/";
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


    public static Observable<String> getPayInfo(String imei) {
        PayService payService = sStringRetrofit.create(PayService.class);
        Observable<String> payInfoObservable = payService.getPayInfo(imei);
        return payInfoObservable;
    }

    public static void main(String[] args) {
        Observable<String> payInfo = getPayInfo("12345");
        payInfo.subscribe(s -> System.out.println(s));
    }

}
