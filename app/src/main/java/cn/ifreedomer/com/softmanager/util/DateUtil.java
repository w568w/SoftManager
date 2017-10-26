package cn.ifreedomer.com.softmanager.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author:eavawu
 * @since: 25/10/2017.
 * TODO:
 */

public class DateUtil {
    public static String timeStamp2DateString(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        date.setTime(time);
        simpleDateFormat.format(date);
        return simpleDateFormat.format(date);
    }


    public static String timeStamp2DayString(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d天H时mm分ss秒");
        Date date = new Date();
        date.setTime(time);
        simpleDateFormat.format(date);
        return simpleDateFormat.format(date);
    }
}
