package com.rx.mvp.cn.model.load.upload;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.ImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.r.http.cn.RHttp;
import com.r.http.cn.utils.LogUtils;
import com.r.mvp.cn.root.IMvpPresenter;
import com.rx.mvp.cn.R;
import com.rx.mvp.cn.base.BaseActivity;
import com.rx.mvp.cn.core.bitmap.ImageLoaderUtils;
import com.rx.mvp.cn.core.net.http.RUploadCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 文件上传演示
 *
 * @author ZhongDaFeng
 */
public class UploadActivity extends BaseActivity {


    private final int IMAGE_PICKER = 1001;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.tv_index)
    TextView tvIndex;

    @BindView(R.id.tv_result)
    TextView tvResult;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_upload;
    }

    @Override
    protected void initBundleData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        /**
         * 图片选择配置
         */
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(false);  //显示拍照按钮
        imagePicker.setCrop(false);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(3);    //选中数量限制
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素

    }

    @OnClick(R.id.tv_select)
    public void onClick() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, IMAGE_PICKER);
    }

    /**
     * 上传文件
     *
     * @param fileMap
     */
    private void upload(Map<String, File> fileMap, List<File> list) {

        //基本参数
        TreeMap<String, Object> parameter = new TreeMap<>();
        parameter.put("id", 10001);
        parameter.put("name", "Ruffian");

        //Header参数
        TreeMap<String, Object> header = new TreeMap<>();
        header.put("headerId", 101);
        header.put("headerName", "Ruffian-痞子");

        //随意获取两张相片
        // File userIcon = new File("/storage/emulated/0/DCIM/camera/IMG_20180815_181425.jpg");
        // File otherIcon = new File("/storage/emulated/0/DCIM/camera/IMG_20180815_202702.jpg");

        //文件参数
        //TreeMap<String, File> fileMap = new TreeMap<>();
        // fileMap.put("userIcon", userIcon);
        // fileMap.put("otherIcon", otherIcon);

        /**
         * 发送请求
         */
        new RHttp.Builder()
                .post()
                .baseUrl("http://192.168.102.224:8080/")
                .apiUrl("PureFood/api/upload/image")
                .addParameter(parameter)
                .addHeader(header)
                .file(fileMap)
                // .file("key", list)
                .lifecycle(this)
                .build()
                .upload(new RUploadCallback<String>() {
                    @Override
                    public String convert(JsonElement data) {
                        return new Gson().toJson(data);
                    }

                    @Override
                    public void onProgress(File file, long currentSize, long totalSize, float progress, int currentIndex, int totalFile) {
                        progressBar.setProgress((int) (progress * 100));
                        tvIndex.setText("正在上传第:  " + currentIndex + "  张，总共:  " + totalFile + "   " + (progress * 100) + "%");
                        // tvResult.setText("正在上传中\n文件名称:" + file.getName() + "\n已上传:" + currentSize + "  总大小:" + totalSize + "\n当前文件序号:" + currentIndex + "  文件总数:" + totalFile);
                        tvResult.setText("正在上传中\n文件名称:" + file.getName() + "\n已上传:" + currentSize + "  总大小:" + totalSize);

                    }

                    @Override
                    public void onSuccess(String value) {
                        tvResult.setText("上传成功\n" + value);
                    }

                    @Override
                    public void onError(int code, String desc) {
                        tvResult.setText("上传失败   \n错误码:" + code + "\n错误信息:" + desc);
                    }

                    @Override
                    public void onCancel() {
                        tvResult.setText("请求取消了");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                /**
                 * 获取选择图片之后上传
                 */
                File file;
                Map<String, File> fileMap = new IdentityHashMap<>();// IdentityHashMap 一个key多个文件
                String key;
                String path;
                List<File> list = new ArrayList<>();
                for (int i = 0; i < images.size(); i++) {
                    key = images.get(i).name;
                    path = images.get(i).path;
                    file = new File(path);
                    //fileMap.put(new String("key"), file);//一个key对应多个文件上传 IdentityHashMap  关键是对象 new String("key")
                    fileMap.put(key, file);
                    list.add(file);
                }
                try {
                    Thread.sleep(500);//延时0.5秒看效果
                    //上传图片
                    upload(fileMap, list);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected IMvpPresenter[] getPresenterArray() {
        return new IMvpPresenter[0];
    }

    /**
     * 图片加载器
     */
    public class GlideImageLoader implements ImageLoader {

        @Override
        public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
            Uri uri = Uri.fromFile(new File(path));
            Glide.with(activity).load(uri).into(imageView);
        }

        @Override
        public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
            Uri uri = Uri.fromFile(new File(path));
            Glide.with(activity).load(uri).into(imageView);
        }

        @Override
        public void clearMemoryCache() {
        }
    }

}
