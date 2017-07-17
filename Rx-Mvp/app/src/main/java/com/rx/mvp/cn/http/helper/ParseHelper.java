package com.rx.mvp.cn.http.helper;

import com.google.gson.JsonElement;

/**
 * 数据解析helper
 *
 * @author ZhongDaFeng
 */
public interface ParseHelper {
    Object[] parse(JsonElement jsonElement);
}
