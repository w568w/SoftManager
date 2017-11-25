package cn.ifreedomer.com.softmanager.fragment.device;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.DeviceInfoWrap;
import cn.ifreedomer.com.softmanager.bean.DeviceTitle;
import cn.ifreedomer.com.softmanager.bean.FourValue;
import cn.ifreedomer.com.softmanager.bean.TwoValue;
import cn.ifreedomer.com.softmanager.util.CameraUtils;
import cn.ifreedomer.com.softmanager.util.DateUtil;
import cn.ifreedomer.com.softmanager.util.HardwareUtil;
import cn.ifreedomer.com.softmanager.util.IPAddressUtil;
import cn.ifreedomer.com.softmanager.util.NetworkUtil;
import cn.ifreedomer.com.softmanager.util.ScreenUtil;
import cn.ifreedomer.com.softmanager.util.SystemUtil;
import cn.ifreedomer.com.softmanager.widget.HardwareHeadView;
import cn.ifreedomer.com.softmanager.widget.device.DeviceInfoFourValueItemDelegate;
import cn.ifreedomer.com.softmanager.widget.device.DeviceInfoTitleItemDelegate;
import cn.ifreedomer.com.softmanager.widget.device.DeviceInfoTwoValueItemDelegate;

import static android.content.Context.TELEPHONY_SERVICE;


/**
 * @author wuyihua
 * @Date 2017/10/23
 * @todo
 */

public class DeviceInfoFragment extends Fragment {
    public static final String TAG = DeviceInfoFragment.class.getSimpleName();
    @InjectView(R.id.hardware_rv)
    RecyclerView hardwareRv;


    private MultiItemTypeAdapter mMultiAdapter;
    private List<DeviceInfoWrap> mDatalist;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_info, container, false);
        ButterKnife.inject(this, view);
        view.setVisibility(View.VISIBLE);
        initAdapter();
        initHeadView();
        return view;
    }

    private void initAdapter() {
        mDatalist = new ArrayList<>();
        mMultiAdapter = new MultiItemTypeAdapter<>(getActivity(), mDatalist);
        mMultiAdapter.addItemViewDelegate(new DeviceInfoFourValueItemDelegate());
        mMultiAdapter.addItemViewDelegate(new DeviceInfoTitleItemDelegate());
        mMultiAdapter.addItemViewDelegate(new DeviceInfoTwoValueItemDelegate());
        hardwareRv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }




    @Override
    public void onHiddenChanged(boolean ishide){
        super.onHiddenChanged(ishide);
        if (!ishide) {
            //相当于Fragment的onResume
            RxPermissions rxPermissions = new RxPermissions(getActivity());
            rxPermissions
                    .request(Manifest.permission.CAMERA)
                    .subscribe(granted -> {
                            initData();

                    });
        }
    }


    private void initData() {
        //基本信息
        DeviceInfoWrap<DeviceTitle> basicInfoWrap = DeviceInfoWrap.createTitle(getString(R.string.basic_info));
        mDatalist.add(basicInfoWrap);

        //启动时间
        String lastLauncherTitle = getString(R.string.last_launch);
        String lastLauncherValue = DateUtil.timeStamp2DateString(SystemUtil.getBootTime());
        DeviceInfoWrap<TwoValue> lastLauncherWrap = DeviceInfoWrap.createTwoValue(lastLauncherTitle, lastLauncherValue);
        mDatalist.add(lastLauncherWrap);

        //运行时间
        String runTimeTitle = getString(R.string.run_time);
        String runTimeValue = DateUtil.timeStamp2DayString(SystemClock.elapsedRealtimeNanos() / 1000000);
        DeviceInfoWrap<TwoValue> runTimeWrap = DeviceInfoWrap.createTwoValue(runTimeTitle, runTimeValue);
        mDatalist.add(runTimeWrap);

        //网络信息
        DeviceInfoWrap<DeviceTitle> networkInfoWrap = DeviceInfoWrap.createTitle(getString(R.string.network_info));
        mDatalist.add(networkInfoWrap);


        //WIFI网络和IP地址
        boolean wifiConnected = NetworkUtil.isWifiConnected(getContext());
        String connectStr = wifiConnected ? getString(R.string.has_connected) : getString(R.string.not_connected);
        String wifiIp = IPAddressUtil.getIPAddress(true);
        DeviceInfoWrap<FourValue> wifiNetworkFourWrap = DeviceInfoWrap.createFourValue(getString(R.string.wifi_network), connectStr, getString(R.string.ip_address), wifiIp);
        mDatalist.add(wifiNetworkFourWrap);

        //硬件特性
        DeviceInfoWrap<DeviceTitle> hardwareWrap = DeviceInfoWrap.createTitle(getString(R.string.hard_title));
        mDatalist.add(hardwareWrap);

        //处理器
        String processorTitle = getString(R.string.processor);
        String processorValue = HardwareUtil.getCpuName();
        DeviceInfoWrap<TwoValue> processorWrap = DeviceInfoWrap.createTwoValue(processorTitle, processorValue);
        mDatalist.add(processorWrap);

        //尺寸
        String sizeValue = ScreenUtil.getRealHeight(getActivity()) + " x " + ScreenUtil.getRealWidth(getActivity());
        String sizeTitle = getString(R.string.size);
        DeviceInfoWrap<TwoValue> sizeWrap = DeviceInfoWrap.createTwoValue(sizeTitle, sizeValue);
        mDatalist.add(sizeWrap);


        //像素密度
        String densityValue = ScreenUtil.getDensityDpi(getActivity()) + "";
        String densityTitle = getString(R.string.density);
        DeviceInfoWrap<TwoValue> densityWrap = DeviceInfoWrap.createTwoValue(densityTitle, densityValue);
        mDatalist.add(densityWrap);


        //电池容量
        String batteryValue = HardwareUtil.getBatteryCapacity(getContext()) + " mAh";
        String batteryTitle = getString(R.string.battery);
        DeviceInfoWrap<TwoValue> batteryWrap = DeviceInfoWrap.createTwoValue(batteryTitle, batteryValue);
        mDatalist.add(batteryWrap);


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            String imeiValue = ((TelephonyManager) getContext().getSystemService(TELEPHONY_SERVICE))
                    .getDeviceId();
            String imeiTitle = getString(R.string.imei);
            DeviceInfoWrap<TwoValue> imeiWrap = DeviceInfoWrap.createTwoValue(imeiTitle, imeiValue);
            mDatalist.add(imeiWrap);
        }


        //后置摄像头
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            String backCameraValue = CameraUtils.getCameraPixels(CameraUtils.CAMERA_FACING_BACK);
            String backCameraTitle = getString(R.string.back_camera);
            DeviceInfoWrap<TwoValue> backCameraWrap = DeviceInfoWrap.createTwoValue(backCameraTitle, backCameraValue);
            mDatalist.add(backCameraWrap);


            //后置摄像头
            String foreCameraValue = CameraUtils.getCameraPixels(CameraUtils.CAMERA_FACING_FRONT);
            String foreCameraTitle = getString(R.string.fore_camera);
            DeviceInfoWrap<TwoValue> foreCameraWrap = DeviceInfoWrap.createTwoValue(foreCameraTitle, foreCameraValue);
            mDatalist.add(foreCameraWrap);

        }
        //三轴陀螺仪
        String gyroValue = HardwareUtil.hasSensor(getContext(), Sensor.TYPE_GYROSCOPE) ? getString(R.string.has) : getString(R.string.do_has);
        String gyroTitle = getString(R.string.gyro);

        DeviceInfoWrap<TwoValue> gyroWrap = DeviceInfoWrap.createTwoValue(gyroTitle, gyroValue);
        mDatalist.add(gyroWrap);


        //方向传感器
        String directionSensorValue = HardwareUtil.hasSensor(getContext(), Sensor.TYPE_ORIENTATION) ? getString(R.string.has) : getString(R.string.do_has);
        ;
        String directionSensorTitle = getString(R.string.direction_sensor);
        DeviceInfoWrap<TwoValue> directionSensorWrap = DeviceInfoWrap.createTwoValue(directionSensorTitle, directionSensorValue);
        mDatalist.add(directionSensorWrap);


        //距离传感器
        String distanceValue = HardwareUtil.hasSensor(getContext(), Sensor.TYPE_PROXIMITY) ? getString(R.string.has) : getString(R.string.do_has);
        ;
        String distanceTitle = getString(R.string.distance_sensor);
        DeviceInfoWrap<TwoValue> distanceSensorWrap = DeviceInfoWrap.createTwoValue(distanceTitle, distanceValue);
        mDatalist.add(distanceSensorWrap);

        //环境光线传感器
        String lightSensorValue = HardwareUtil.hasSensor(getContext(), Sensor.TYPE_LIGHT) ? getString(R.string.has) : getString(R.string.do_has);
        ;
        String lightSensorTitle = getString(R.string.light_sensor);
        DeviceInfoWrap<TwoValue> lightSensorWrap = DeviceInfoWrap.createTwoValue(lightSensorTitle, lightSensorValue);
        mDatalist.add(lightSensorWrap);


        //气压计
        String barometerValue = HardwareUtil.hasSensor(getContext(), Sensor.TYPE_PRESSURE) ? getString(R.string.has) : getString(R.string.do_has);
        ;
        String barometerTitle = getString(R.string.barometer);
        DeviceInfoWrap<TwoValue> barometerWrap = DeviceInfoWrap.createTwoValue(barometerTitle, barometerValue);
        mDatalist.add(barometerWrap);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }


    //
    private void initHeadView() {
        HardwareHeadView headerView = new HardwareHeadView(getActivity());
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mMultiAdapter);
        mHeaderAndFooterWrapper.addHeaderView(headerView);
        hardwareRv.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
