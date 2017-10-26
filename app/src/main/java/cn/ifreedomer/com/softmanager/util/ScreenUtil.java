package cn.ifreedomer.com.softmanager.util;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * 版权所有：XXX有限公司</br>
 * 
 * ScreenUtil </br>
 * 
 * @author zhou.wenkai ,Created on 2014-12-2 11:09:58</br>
 * @Description Major Function：ScreenUtils </br>
 * 
 * 注:如果您修改了本类请填写以下内容作为记录，如非本人操作劳烦通知，谢谢！！！</br>
 * @author mender，Modified Date Modify Content:
 */
public class ScreenUtil {

    /** 基准分辨率的宽 */
	private double STANDARD_SCREEN_WIDTH  = 320;
    /** 基准分辨率的高 */
    protected double STANDARD_SCREEN_HEIGHT = 480;

    /**
     * 获取屏幕可操作区域宽度
     *
     * @param activity
     * @return 屏幕可操作区域宽度
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕可操作区域高度
     *
     * @param activity
     * @return 屏幕可操作区域高度
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * 获取屏幕可操作区域宽度和高度
     *
     * @param activity
     * @return 宽度和高度封装到Point对象中
     */
    public static Point getScreenSize(Activity activity) {
        return new Point(getScreenWidth(activity), getScreenHeight(activity));
    }


    /**
     * 获取屏幕真实宽度<br/>
     * 其实和 getScreenWidth()获取的值相同，相对getRealHeight()增加
     *
     * @param activity
     * @return 屏幕真实宽度
     */
    public static int getRealWidth(Activity activity) {
        int widthPixels = 0;
        Display display = activity.getWindowManager().getDefaultDisplay();
        final int VERSION = Build.VERSION.SDK_INT;

        if(VERSION < 13) {
            display.getWidth();
        }else if (VERSION == 13) {
            try {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
            } catch (Exception e) {
            }
        } else {
            Point realSize = new Point();
            try {
                Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
                widthPixels = realSize.x;
            } catch (Exception e) {
            }
        }
        return widthPixels;
    }

    /**
     * 获取屏幕真实高度
     *
     * @param activity
     * @return 屏幕真实高度
     */
    public static int getRealHeight(Activity activity) {
        int heightPixels = 0;
        Display display = activity.getWindowManager().getDefaultDisplay();
        final int VERSION = Build.VERSION.SDK_INT;

        if(VERSION < 13) {
            display.getHeight();
        }else if (VERSION == 13) {
            try {
                heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (Exception e) {
            }
        } else {
            Point realSize = new Point();
            try {
                Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
                heightPixels = realSize.y;
            } catch (Exception e) {
            }
        }
        return heightPixels;
    }

    /**
     * 获取屏幕真实宽度和高度
     *
     * @param activity
     * @return 宽度和高度封装到Point对象中
     */
    public static Point getRealSize(Activity activity) {
        return new Point(getRealWidth(activity), getRealHeight(activity));
    }

    /**
     * 获取屏幕宽度ppi
     *
     * @param activity
     * @return 屏幕宽度ppi
     */
    public static float getWidthPpi(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.xdpi;
    }

    /**
     * 获取屏幕高度ppi
     *
     * @param activity
     * @return 屏幕高度ppi
     */
    public static float getHeightPpi(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.ydpi;
    }

    /**
     * 获取屏幕ppi
     *
     * @param activity
     * @return 屏幕高度ppi
     */
    public static float getScreenPpi(Activity activity) {
        return (getWidthPpi(activity) + getHeightPpi(activity)) / 2;
    }

    /**
     * 获取屏幕宽度物理尺寸
     *
     * @param activity
     * @return
     */
    public static float getWidthInch(Activity activity) {
        int realWidth = getRealWidth(activity);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return (float)realWidth / getWidthPpi(activity);
    }

    /**
     * 获取屏幕高度物理尺寸
     *
     * @param activity
     * @return
     */
    public static float getHeightInch(Activity activity) {
        int realHeight = getRealHeight(activity);
        return (float)realHeight / getHeightPpi(activity);
    }

    /**
     * 获取屏幕物理尺寸
     *
     * @param activity
     * @return 屏幕物理尺寸
     */
    public static float getScreenInch(Activity activity) {
        return (float)Math.sqrt(Math.pow(getWidthInch(activity), 2) + Math.pow(getHeightInch(activity), 2));
    }

    /**
     * 获取屏幕密度
     *
     * @return
     */
    public static float getScreenDensity(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.density;
    }

    /**
     * 获取每英寸的点数(打印分辨率,区别PPI)
     *
     * @return
     */
    public static float getDensityDpi(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.densityDpi;
    }

    /**
     * 获取屏幕可操作区域宽度dp数目
     *
     * @param activity
     * @return
     */
    public static float getScreenWidthDp(Activity activity) {
        return getScreenWidth(activity) / getScreenDensity(activity);
    }

    /**
     * 获取屏幕高度可操作区域dp数目
     *
     * @param activity
     * @return
     */
    public static float getScreenHeightDp(Activity activity) {
        return getScreenHeight(activity) / getScreenDensity(activity);
    }

    /**
     * 获取屏幕真实宽度dp数目
     *
     * @param activity
     * @return
     */
    public static float getRealWidthDp(Activity activity) {
        return getRealWidth(activity) / getScreenDensity(activity);
    }

    /**
     * 获取屏幕真实高度dp数目
     *
     * @param activity
     * @return
     */
    public static float getRealHeightDp(Activity activity) {
        return getRealHeight(activity) / getScreenDensity(activity);
    }

    /**
     * 判断屏幕是否大于6英寸
     *
     * @param activity
     * @return
     */
    public static boolean isMoreThan6Inch(Activity activity) {
        return getScreenInch(activity) >= 6.0;
    }

    /**
     * 判断设备是否为大尺寸屏幕
     *
     * @param context
     * @return
     */
    public static boolean isScreenSizeLarge(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 判断设备是否为平板
     *
     * @param activity
     * @return true 平板;
     *          false 手机;
     */
    public static boolean isTablet(Activity activity) {
        return isMoreThan6Inch(activity) && isScreenSizeLarge(activity);
    }

    /**
     * 获取通知栏的高度
     *
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        Class<?> c;
        Object obj;
        Field field;
        int x, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = activity.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /** 
     * 获取当前屏幕截图，包含状态栏 
     *
     * @return 
     */  
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }
    
    /** 
     * 获取当前屏幕截图，不包含状态栏 
     *
     * @return 
     */  
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();  
        return bp;
    }
    
}