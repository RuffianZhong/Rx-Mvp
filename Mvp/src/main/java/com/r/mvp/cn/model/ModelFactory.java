package com.r.mvp.cn.model;

import android.support.annotation.NonNull;

import com.r.mvp.cn.root.IMvpModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Model工厂类
 *
 * @author ZhongDaFeng
 * {@link # https://github.com/RuffianZhong/Rx-Mvp}
 */
public class ModelFactory {

    /**
     * 全局存储Model
     */
    private static Map<String, IMvpModel> modelMap = new HashMap<>();

    /**
     * 获取model
     * 查询Map中是否存在model实例,不存在时动态创建
     *
     * @param cls 类
     * @param <T> model
     * @return
     */
    public static <T extends IMvpModel> T getModel(@NonNull Class<T> cls) {
        String className = cls.getName();//类名
        T model = (T) modelMap.get(className);
        if (model == null) {//不存在
            model = getModelReflex(className);
            modelMap.put(className, model);
        }
        return model;
    }

    /**
     * 反射获取Model
     *
     * @param className 包含完整路径的类名称 com.ruffian.cn.User
     * @param <T>
     * @return
     */
    private static <T extends IMvpModel> T getModelReflex(@NonNull String className) {
        T result = null;
        try {
            result = (T) Class.forName(className).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
