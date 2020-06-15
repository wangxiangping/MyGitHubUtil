package com.wxp.wxputil.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

public class AndroidInfoGetter {

    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getDeviceSN() {
        String ret;
        try {
            Method systemProperties_get = Class.forName("android.os.SystemProperties")
                    .getMethod("get", String.class);
            if ((ret = (String) systemProperties_get.invoke(null, "ro.boot.serialno")) != null)
                return ret;
        } catch (Exception e) {
            return null;
        }
        return "";
    }

    public static String getDeviceType() {
        String ret;
        try {
            Method systemProperties_get = Class.forName("android.os.SystemProperties")
                    .getMethod("get", String.class);
            if ((ret = (String) systemProperties_get.invoke(null, "ro.product.brand")) != null)
                return ret;
        } catch (Exception e) {
            return null;
        }
        return "";
    }

    public static String getDeviceType1() {
        String ret;
        try {
            Method systemProperties_get = Class.forName("android.os.SystemProperties")
                    .getMethod("get", String.class);
            if ((ret = (String) systemProperties_get.invoke(null, "ro.product.primodel")) != null)
                return ret;
        } catch (Exception e) {
            return null;
        }
        return "";
    }

    public static String getA9SN() {
        InputStreamReader isr = null;
        BufferedReader br = null;
        char[] buffer = new char[64];
        File file = new File("data/data/sn");
        try {
            isr = new InputStreamReader(new FileInputStream(file));
            br = new BufferedReader(isr);
            br.read(buffer, 0, buffer.length);
            br.close();
            isr.close();
            return new String(buffer).trim();
        } catch (IOException e) {
            return "";
        }
    }

    public static float getDensity(Activity activity) {
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }

//    public static int getNetworkType(Context context) {
//        ConnectivityManager connectivityManager = (ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        if (networkInfo == null) {
//            return -1;
//        }
//        return networkInfo.getType();
//    }


    public static boolean isServiceRunning(Context context, Class serviceClass) {
        ActivityManager manager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service
                : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public static void toggleComponent(Context context, Class componentClass, boolean enable) {
        ComponentName componentName = new ComponentName(context, componentClass);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(componentName,
                enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}
