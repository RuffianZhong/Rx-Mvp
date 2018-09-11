package com.rx.mvp.cn.base;

import java.util.TreeMap;

/**
 * 基础业务类
 *
 * @author ZhongDaFeng
 */
public class BaseBiz {


    public final String appKey = "1889b37351288";
    public final String k_key = "key";

    /**
     * 获取基础request参数
     */
    public TreeMap<String, Object> getBaseRequest() {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put(k_key, appKey);
        return map;
    }


}
