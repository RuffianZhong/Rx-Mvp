package com.rx.mvp.cn.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;

import java.util.Stack;


/**
 * Activity栈管理
 *
 * @author ZhongDaFeng
 */
public class ActivityStackManager {

    private static ActivityStackManager instance = new ActivityStackManager();
    private static Stack<Activity> activityStack;// 栈
    private Context currentContext;// 当前上下文对象

    /**
     * 私有构造
     */
    private ActivityStackManager() {
        activityStack = new Stack<Activity>();
    }

    /**
     * 单例实例
     *
     * @return
     */
    public static ActivityStackManager getManager() {
        return instance;
    }

    /**
     * 压栈
     *
     * @param activity
     */
    public void push(Activity activity) {
        activityStack.push(activity);
    }

    /**
     * 出栈
     *
     * @return
     */
    public Activity pop() {
        if (activityStack.isEmpty())
            return null;
        return activityStack.pop();
    }

    /**
     * 栈顶
     *
     * @return
     */
    public Activity peek() {
        if (activityStack.isEmpty())
            return null;
        return activityStack.peek();
    }

    /**
     * 当前上下文对象，用于弹框使用
     *
     * @return
     */
    public Context currentContext() {
        return currentContext;
    }


    /**
     * 设置当前上下文对象
     *
     * @param context
     */
    public void setCurrentContext(Context context) {
        currentContext = context;
    }

    /**
     * 用于异地登录或者退出时清除activity
     */
    public void clearActivity() {
        while (!activityStack.isEmpty()) {
            Activity activity = activityStack.pop();
            /*if (activity instanceof LoginActivity) {
            } else {
                activity.finish();
            }*/
        }
    }

    /**
     * 移除
     *
     * @param activity
     */
    public void remove(Activity activity) {
        if (activityStack.size() > 0 && activity == activityStack.peek())
            activityStack.pop();
        else
            activityStack.remove(activity);
    }

    /**
     * 是否存在栈
     *
     * @param activity
     * @return
     */
    public boolean contains(Activity activity) {
        return activityStack.contains(activity);
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        while (!activityStack.isEmpty()) {
            activityStack.pop().finish();
        }
    }

    /**
     * 退出应用程序
     *
     * @param context
     */
    public void exitApp(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());
            //清除通知栏
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
            //MobclickAgent.onKillProcess(context);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
        }
    }
}