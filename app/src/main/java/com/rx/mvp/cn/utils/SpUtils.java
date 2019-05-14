package com.rx.mvp.cn.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * SP数据操作工具类
 *
 * @author ZhongDaFeng
 */
public class SpUtils {


    private static final String SP_FILE_KEY = "RxMvp";
    private static SpUtils spUtil;
    private static SharedPreferences sp;

    private SpUtils(Context context, String fileKey, int mode) {
        sp = context.getSharedPreferences(fileKey, mode);
    }

    public static SpUtils getSpUtil(Context context, String fileKey, int mode) {
        if (spUtil == null) {
            spUtil = new SpUtils(context, fileKey, mode);
        } else {
            sp = context.getSharedPreferences(fileKey, mode);
        }
        return spUtil;
    }

    public static SpUtils getSpUtils(Context context) {
        return getSpUtil(context, SP_FILE_KEY, Context.MODE_PRIVATE);
    }

    public void putSPValue(String valueKey, String value) {
        sp.edit().putString(valueKey, value).commit();
    }

    public void putSPValue(String valueKey, int value) {
        sp.edit().putInt(valueKey, value).apply();
    }

    public void putSPValue(String valueKey, float value) {
        sp.edit().putFloat(valueKey, value).apply();
    }

    public void putSPValue(String valueKey, boolean value) {
        sp.edit().putBoolean(valueKey, value).apply();
    }

    public void putSPValue(String valueKey, long value) {
        sp.edit().putLong(valueKey, value).apply();
    }

    public int getSPValue(String valueKey, int value) {
        return sp.getInt(valueKey, value);
    }

    public float getSPValue(String valueKey, float value) {
        return sp.getFloat(valueKey, value);
    }

    public String getSPValue(String valueKey, String value) {
        return sp.getString(valueKey, value);
    }

    public boolean getSPValue(String valueKey, boolean value) {
        return sp.getBoolean(valueKey, value);
    }

    public long getSPValue(String valueKey, long value) {
        return sp.getLong(valueKey, value);
    }

    public void remove(String key) {
        sp.edit().remove(key).apply();
    }

    public void clear() {
        sp.edit().clear().apply();
    }


    /**
     * 保存List
     */
    public <T> void putSPList(String key, List<T> list) {
        if (null == list || list.size() <= 0) {
            sp.edit().putString(key, "").apply();
            return;
        }
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(list);
        sp.edit().putString(key, strJson).apply();
    }

    /**
     * 获取List
     */
    public <T> List<T> getSPList(String tag, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        String strJson = sp.getString(tag, null);
        if (TextUtils.isEmpty(strJson)) {
            return list;
        }
        Gson gson = new Gson();
        JsonArray array = new JsonParser().parse(strJson).getAsJsonArray();
        for (JsonElement jsonElement : array) {
            list.add(gson.fromJson(jsonElement, cls));
        }
        return list;
    }

}
