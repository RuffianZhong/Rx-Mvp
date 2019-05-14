package com.rx.mvp.cn.base;

import android.support.annotation.NonNull;

import com.r.mvp.cn.root.IMvpModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Biz工厂类
 *
 * @author ZhongDaFeng
 */
public class BizFactory {

    /**
     * 全局存储Biz
     */
    private static Map<String, Object> modelMap = new HashMap<>();

    /**
     * 获取Biz
     * 查询Map中是否存在Biz实例,不存在时动态创建
     *
     * @param cls 类
     * @param <T> model
     * @return
     */
    public static <T> T getBiz(@NonNull Class<T> cls) {
        String className = cls.getName();//类名
        T model = (T) modelMap.get(className);
        if (model == null) {//不存在
            model = getModelReflex(className);
            modelMap.put(className, model);
        }
        return model;
    }

    /**
     * 反射获取Biz
     *
     * @param className 包含完整路径的类名称 com.ruffian.cn.UserBiz
     * @param <T>
     * @return
     */
    private static <T> T getModelReflex(@NonNull String className) {
        T result = null;
        try {
            result = (T) Class.forName(className).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
