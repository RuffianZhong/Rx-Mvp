package com.r.http.cn.utils;

import android.text.TextUtils;

import java.io.File;

/**
 * 计算工具类
 *
 * @author ZhongDaFeng
 */
public class ComputeUtils {

    /**
     * 计算进度值
     *
     * @param current
     * @param total
     * @return
     */
    public static float getProgress(long current, long total) {
        float progress = (float) current / (float) total;
        return progress;
    }

    /**
     * 文件是否存在
     *
     * @param fileUrl 文件路径
     * @return
     */
    public static boolean isFileExists(String fileUrl) {
        boolean flag = false;
        File file = new File(fileUrl);
        if (file.exists() && file.isFile()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) return false;

        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + filePath + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + filePath + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + filePath + "不存在！");
            return false;
        }
    }
}
