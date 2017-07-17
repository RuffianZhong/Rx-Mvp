package com.rx.mvp.cn.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * toast工具类
 *
 * @author ZhongDaFeng
 */
public class ToastUtils {

    private static Toast toast;

    /**
     * 显示提示信息
     */
    public static void showToast(Context context, String text) {
        if (TextUtils.isEmpty(text)) return;
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();

    }

    /**
     * 显示提示信息
     */
    public static void showToast(Context context, int rId) {
        if (toast == null) {
            toast = Toast.makeText(context, rId, Toast.LENGTH_SHORT);
        } else {
            toast.setText(rId);
        }
        toast.show();

    }

    /**
     * 显示提示信息(时间较长)
     */
    public static void showLongToast(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        } else {
            toast.setText(text);
        }
        toast.show();

    }

    /**
     * 显示提示信息(时间较长)
     */
    public static void showLongToast(Context context, int rId) {
        if (toast == null) {
            toast = Toast.makeText(context, rId, Toast.LENGTH_LONG);
        } else {
            toast.setText(rId);
        }
        toast.show();

    }

}
