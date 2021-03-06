package com.dy.baseutils.utils.system;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

/**
 * 设备信息相关帮助类
 * 
 * @author maozhihong
 * 
 */
public class SystemUtils {


	/**
	 * 获取设备的序列号
	 */
	@SuppressLint("MissingPermission")
	public static String getDeviceId(Context context) {
		String DeviceID = "";
		try{
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (telephonyManager.getDeviceId() == null) {
				DeviceID = Secure.getString(context.getContentResolver(),
						Secure.ANDROID_ID);
			} else {
				DeviceID = telephonyManager.getDeviceId(); // 用于获取无IMEI设备的序列号
			}
		}catch (Exception e){
		}
		return DeviceID;
	}

	/**
	 * 获取APP的版本名称
	 */
	public static String getVersionName(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(null != packInfo){
			String version = packInfo.versionName;
			return version;
		}
		return "";
	}

	/**
	 * 获取APP的版本号
	 */
	public static int getVersionCode(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(null != packInfo){
			int version = packInfo.versionCode;
			return version;
		}
		return 0;
	}

	/**
	 * 获取手机mac地址 错误返回12个0
	 */
	public static String getMacAddress(Context context) {
		// 获取mac地址：
		String macAddress = "000000000000";
		try {
			WifiManager wifiMgr = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			@SuppressLint("MissingPermission") WifiInfo info = (null == wifiMgr ? null : wifiMgr
					.getConnectionInfo());
			if (null != info) {
				if (!TextUtils.isEmpty(info.getMacAddress()))
					macAddress = info.getMacAddress().replace(":", "");
				else
					return macAddress;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return macAddress;
		}
		return macAddress;
	}

	/**
	 * 封装UUID
	 * 
	 * @param context
	 * @return
	 */
	public static String pkUUId(Context context) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getDeviceId(context)).append("_")
				.append(getMacAddress(context));
		return buffer.toString();
	}

	/**
	 * get phone type,like :GSM,CDMA,SIP,NONE
	 * 
	 * @param context
	 * @return 手机的制式类型，GSM OR CDMA 手机 1
	 */
	public static int getPhoneType(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Activity.TELEPHONY_SERVICE);
		return manager.getPhoneType();
	}

	/**
	 * get phone sys version
	 * 
	 * @return 手机的系统版本信息. 9
	 */
	public static String getSysVersion() {
		return Build.VERSION.RELEASE;
	}

	/**
	 * Returns the ISO country code equivalent of the current registered
	 * operator's MCC (Mobile Country Code).
	 * 
	 * @param context
	 * @return 手机网络国家编码 cn
	 */
	public static String getNetWorkCountryIso(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Activity.TELEPHONY_SERVICE);
		return manager.getNetworkCountryIso();
	}

	/**
	 * Returns the numeric name (MCC+MNC) of current registered operator.may not
	 * work on CDMA phone
	 * 
	 * @param context
	 * @return 手机网络运营商ID。46001
	 */
	public static String getNetWorkOperator(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Activity.TELEPHONY_SERVICE);
		return manager.getNetworkOperator();
	}

	/**
	 * Returns the alphabetic name of current registered operator.may not work
	 * on CDMA phone
	 * 
	 * @param context
	 * @return 手机网络运营商名称 china unicom
	 */
	public static String getNetWorkOperatorName(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Activity.TELEPHONY_SERVICE);
		return manager.getNetworkOperatorName();
	}

	/**
	 * get type of current network
	 * 
	 * @param context
	 * @return 手机的数据链接类型 3
	 */
	public static int getNetworkType(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Activity.TELEPHONY_SERVICE);

		return manager.getNetworkType();
	}

	/**
	 * is webservice aviliable
	 * 
	 * @param context
	 * @return 是否有可用数据链接 true
	 */
	public static boolean isOnline(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			return true;
		}
		return false;
	}

	/**
	 * get current data connection type name ,like ,Mobile/WIFI/OFFLINE
	 * 
	 * @param context
	 * @return 当前的数据链接类型 wifi
	 */
	public static String getConnectTypeName(Context context) {
		if (!isOnline(context)) {
			return "OFFLINE";
		}
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Activity.CONNECTIVITY_SERVICE);

		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null) {
			return info.getTypeName();
		} else {
			return "OFFLINE";
		}
	}

	/**
	 * get free memory of phone, in M
	 * 
	 * @param context
	 * @return 手机剩余内存 1024M
	 */
	public static long getFreeMem(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Activity.ACTIVITY_SERVICE);

		MemoryInfo info = new MemoryInfo();
		manager.getMemoryInfo(info);
		long free = info.availMem / 1024 / 1024;

		return free;
	}

	/**
	 * get product name of phone
	 * 
	 * @return 手机名称 libra_mione_plus
	 */
	public static String getProductName() {
		return Build.PRODUCT;
	}

	/**
	 * get model of phone
	 * 
	 * @return 手机型号 MI-ONE Plus
	 */
	public static String getModelName() {

		return Build.MODEL;
	}

	/**
	 * get Manufacturer Name of phone
	 * 
	 * @return 手机设备制造商名称 xiaomi
	 */
	public static String getManufacturerName() {
		return Build.MANUFACTURER;
	}

	// 获取应用名
	public static String getAppName(Context context) {
		return context.getApplicationInfo()
				.loadLabel(context.getPackageManager()).toString();
	}
	/**
	 * 是否有虚拟导航栏
	 * @param activity
	 * @return
	 */
	public static boolean checkDeviceHasNavigationBar(Context activity) {
        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar  
        boolean hasMenuKey = ViewConfiguration.get(activity)
                .hasPermanentMenuKey();  
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);
  
        if (!hasMenuKey && !hasBackKey) {  
            // 做任何你需要做的,这个设备有一个导航栏  
            return true;  
        }
        return false;
    }
	/**
	 * 虚拟导航栏高度
	 * @param activity
	 * @return
	 */
	public static int getNavigationBarHeight(Activity activity) {
	    Resources resources = activity.getResources();
	    int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
	    //获取NavigationBar的高度
	    int height = resources.getDimensionPixelSize(resourceId);
	    return height;
	}

	public static void toPhone(Context context, String phone) {
		try {
			Intent intent = new Intent(Intent.ACTION_DIAL);
			Uri data = Uri.parse("tel:" + phone);
			intent.setData(data);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
