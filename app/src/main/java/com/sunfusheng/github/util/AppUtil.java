package com.sunfusheng.github.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Stack;

/**
 * @author sunfusheng on 2018/1/23.
 */
@SuppressLint("StaticFieldLeak")
public class AppUtil {

    private static Application application;
    private static Context context;

    public static ActivityManager sActivityManager;
    private static Stack<Activity> activityStack = new Stack<>();

    private static ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            activityStack.push(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            activityStack.remove(activity);
        }
    };

    public static Application getApp() {
        if (application != null) {
            return application;
        }
        throw new NullPointerException("AppUtil should be initialized first!");
    }

    public static Context getContext() {
        if (context != null) {
            return context;
        }
        throw new NullPointerException("AppUtil should be initialized first!");
    }

    public static void init(@NonNull final Application app) {
        application = app;
        context = app.getApplicationContext();
        app.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    public static void exitApp() {
        while (activityStack.size() > 0) {
            Activity activity = activityStack.pop();
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
        activityStack.clear();
        System.exit(0);
    }

    public static String getPackageName() {
        return getApp().getPackageName();
    }

    public static String getName() {
        return getName(getApp().getPackageName());
    }

    public static String getName(final String packageName) {
        try {
            PackageManager pm = getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Drawable getIcon() {
        return getIcon(getApp().getPackageName());
    }

    public static Drawable getIcon(final String packageName) {
        try {
            PackageManager pm = getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getVersionCode() {
        return getVersionCode(getPackageName());
    }

    public static int getVersionCode(final String packageName) {
        try {
            PackageManager pm = getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getVersionName() {
        return getVersionName(getPackageName());
    }

    public static String getVersionName(final String packageName) {
        try {
            PackageManager pm = getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPath() {
        return getPath(getApp().getPackageName());
    }

    public static String getPath(final String packageName) {
        try {
            PackageManager pm = getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isAppForeground() {
        if (sActivityManager == null) {
            sActivityManager = (ActivityManager) getApp().getSystemService(Context.ACTIVITY_SERVICE);
        }
        if (sActivityManager == null) {
            return false;
        }

        List<ActivityManager.RunningAppProcessInfo> appProcesses = sActivityManager.getRunningAppProcesses();
        if (CollectionUtil.isEmpty(appProcesses)) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo processInfo : appProcesses) {
            if (processInfo == null) continue;
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return processInfo.processName.equals(getApp().getPackageName());
            }
        }
        return false;
    }

    public static void installApk(String apkPath) {
        if (TextUtils.isEmpty(apkPath)) {
            return;
        }

        try {
            File apkFile = new File(apkPath);
            if (!apkFile.exists()) {
                throw new FileNotFoundException("Apk file does not exist!");
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri apkUri;
            if (OsVersionUtil.hasN()) {
                apkUri = FileProvider.getUriForFile(getContext(), getPackageName() + ".file_provider", apkFile);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                apkUri = Uri.fromFile(apkFile);
            }
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            getContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
