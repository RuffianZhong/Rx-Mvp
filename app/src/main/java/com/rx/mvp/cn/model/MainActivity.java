package com.rx.mvp.cn.model;

import android.Manifest;
import android.content.Intent;
import android.view.View;

import com.r.http.cn.RDownLoad;
import com.r.http.cn.model.Download;
import com.rx.mvp.cn.R;
import com.rx.mvp.cn.base.BaseActivity;
import com.rx.mvp.cn.base.BaseBiz;
import com.rx.mvp.cn.model.account.activity.LoginActivity;
import com.rx.mvp.cn.model.multiple.MultipleActivity;
import com.rx.mvp.cn.model.other.activity.PhoneAddressActivity;
import com.rx.mvp.cn.model.load.download.DownloadActivity;
import com.rx.mvp.cn.model.load.upload.UploadActivity;
import com.rx.mvp.cn.utils.LogUtils;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initBundleData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        String[] per = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, per)) {
            EasyPermissions.requestPermissions(this, "申请读写权限", 101, per);
        }

        TreeMap<String, Object> request = new TreeMap<>();
        request.put("index", "100");
        request.put(BaseBiz.API_URL, "PureFood/api/upload/userImage");

        TreeMap<String, Object> request2 = new TreeMap<>();
        request2.put("index", "100");
        request2.put(BaseBiz.API_URL, "api/upload/userImage2");

        File file1 = new File("/storage/emulated/0/DCIM/camera/IMG_20180815_181425.jpg");
        File file = new File("/storage/emulated/0/DCIM/camera/IMG_20180815_202702.jpg");

        TreeMap<String, File> fileTreeMap = new TreeMap<>();
        fileTreeMap.put("userIcon", file);
        fileTreeMap.put("otherIcon", file1);

        TreeMap<String, Object> header = new TreeMap<>();
        header.put("head1", "Ruffian-痞子");
        header.put("head2", "android");


   /*     new RHttp.Builder().addParameter(request).addHeader(header).file(fileTreeMap).build().upload(new UploadCallback() {
            @Override
            public void onProgress(File file, long currentSize, long totalSize, float progress, int currentIndex, int totalFile) {
                LogUtils.e(" file:" + file.getName() + "  progress:" + progress + "  currentIndex:" + currentIndex);
            }

            @Override
            public Object[] onConvert(JsonElement data) {
                return new Object[0];
            }

            @Override
            public void onSuccess(Object... object) {
                LogUtils.e("onSuccess");
            }

            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onCancel() {

            }
        });*/



      /*  new RHttp.Builder().post().parameter(request2).header(header).file(fileTreeMap).build().upload(new UploadCallback() {
            @Override
            public void onProgress(File file, long currentSize, long totalSize, float progress, int currentIndex, int totalFile) {
                LogUtils.e("22222 file:" + file.getName() + "  progress:" + progress + "  currentIndex:" + currentIndex);
            }

            @Override
            public Object[] onConvert(JsonElement data) {
                return new Object[0];
            }

            @Override
            public void onSuccess(Object... object) {
                LogUtils.e("2222 onSuccess");
            }

            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onCancel() {

            }
        });

*/

/*        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "test" + (System.currentTimeMillis() / 1000) + ".apk");
        String url = "http://imtt.dd.qq.com/16891/50CC095EFBE6059601C6FB652547D737.apk?fsname=com.tencent.mm_6.6.7_1321.apk&csr=1bbd";
        apkApi = new Download(url);
        apkApi.setSavePath(outputFile.getAbsolutePath());
        //apkApi.setBaseUrl("http://www.izaodao.com/");
        apkApi.setListener(httpProgressOnNextListener);*/

    }

    Download apkApi;
    // DownLoadManager manager = DownLoadManager.getInstance();
    RDownLoad manager = null;

    @Override
    protected void onStop() {
        super.onStop();
        //  manager.stopDown(apkApi);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // manager.startDown(apkApi);
    }

/*
    HttpDownOnNextListener<Download> httpProgressOnNextListener = new HttpDownOnNextListener<Download>() {
        @Override
        public void onNext(Download baseDownEntity) {
            Toast.makeText(MainActivity.this, baseDownEntity.getSavePath(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStart() {
            LogUtils.e("提示:开始下载");
        }

        @Override
        public void onComplete() {
            LogUtils.e("提示：下载完成");
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            LogUtils.e("失败:" + e.toString());
        }


        @Override
        public void onPuase() {
            super.onPuase();
            LogUtils.e("提示:暂停");
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void updateProgress(long readLength, long countLength) {
            LogUtils.e("提示:下载中 countLength:" + countLength + "  readLength:" + readLength);
        }
    };*/


    @OnClick({R.id.login, R.id.phone_address, R.id.multiple, R.id.upload, R.id.download})
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.login:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.phone_address:
                intent = new Intent(this, PhoneAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.multiple:
                intent = new Intent(this, MultipleActivity.class);
                startActivity(intent);
                break;
            case R.id.upload:
                intent = new Intent(this, UploadActivity.class);
                startActivity(intent);
                break;
            case R.id.download:
                intent = new Intent(this, DownloadActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        LogUtils.e("申请权限成功");
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        LogUtils.e("申请权限失败");
    }
}

