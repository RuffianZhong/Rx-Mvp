package com.rx.mvp.cn.model.load.download;


import com.r.http.cn.model.Download;

/**
 * 下载实体类，继承Download
 */
public class DownloadBean extends Download {

    /**
     * 额外字段，apk图标
     */
    private String icon;

    public DownloadBean(String url,String icon,String localUrl) {
        setServerUrl(url);
        setLocalUrl(localUrl);
        setIcon(icon);
    }


    public String getIcon() {
        return icon == null ? "" : icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
