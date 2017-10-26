package cn.ifreedomer.com.softmanager.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author wuyihua
 * @Date 2017/10/26
 * @todo
 */

public class HardwareUtil {

    /**
     * 获取CPU型号 * @return
     */
    public static String getCpuName() {

        String str1 = "/proc/cpuinfo";
        String str2 = "";

        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr);
            while ((str2 = localBufferedReader.readLine()) != null) {
                if (str2.contains("Hardware")) {
                    return str2.split(":")[1];
                }
            }
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return null;

    }


    public static double getBatteryCapacity(Context context) {
        Object mPowerProfile_ = null;

        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            double batteryCapacity = (Double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile_, "battery.capacity");
            return batteryCapacity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean hasSensor(Context context, int type) {
        SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor defaultSensor = mSensorManager.getDefaultSensor(type);
        return defaultSensor != null;
    }
}
