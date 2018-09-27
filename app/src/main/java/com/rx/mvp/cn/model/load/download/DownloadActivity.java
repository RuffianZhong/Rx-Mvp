package com.rx.mvp.cn.model.load.download;

import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.r.http.cn.RDownLoad;
import com.r.http.cn.load.download.DownloadCallback;
import com.r.http.cn.model.Download;
import com.r.mvp.cn.root.IMvpPresenter;
import com.rx.mvp.cn.R;
import com.rx.mvp.cn.base.BaseActivity;
import com.rx.mvp.cn.core.bitmap.ImageLoaderUtils;
import com.rx.mvp.cn.utils.LogUtils;
import com.rx.mvp.cn.utils.ToastUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 下载示例
 *
 * @author ZhongDaFeng
 */
public class DownloadActivity extends BaseActivity {

    @BindView(R.id.listView)
    ListView listView;
    private DataAdapter adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_download;
    }

    @Override
    protected void initBundleData() {
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        adapter = new DataAdapter(this, getList());
        listView.setAdapter(adapter);
    }

    /**
     * 重新下载
     */
    private void reStartDownload(DownloadBean bean) {
        RDownLoad.get().removeDownload(bean, true);//移除下载
        RDownLoad.get().startDownload(bean);//开始下载
    }

    @Override
    protected IMvpPresenter[] getPresenterArray() {
        return new IMvpPresenter[0];
    }

    /**
     * 适配器
     */
    class DataAdapter extends CommonAdapter<DownloadBean> {

        public DataAdapter(Context context, List<DownloadBean> data) {
            super(context, R.layout.item_download, data);
        }

        @Override
        protected void convert(final ViewHolder holder, final DownloadBean item, int position) {
            //设置回调(需要在 RDownLoad.get() 的所有操作之前，才能正确回调)
            item.setCallback(new DownloadCallback<DownloadBean>() {
                @Override
                public void onProgress(Download.State state, long currentSize, long totalSize, float progress) {
                    LogUtils.d("download progress currentSize:" + currentSize + "  totalSize:" + totalSize);
                    int progressInt = (int) (progress * 100);
                    holder.setText(R.id.tv_progress, progressInt + "%");
                    holder.setProgress(R.id.progress, progressInt);
                    holder.setText(R.id.start, getStateText(state));
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    LogUtils.e("下载失败");
                    //  holder.setText(R.id.start, "下载失败");
                }

                @Override
                public void onSuccess(DownloadBean object) {
                    LogUtils.e("下载成功");
                    // holder.setText(R.id.start, "下载成功");
                }
            });

            //下载状态
            holder.setText(R.id.start, getStateText(item.getState()));
            if (item.getState() == Download.State.LOADING) {//正在下载中，恢复下载进度
                RDownLoad.get().startDownload(item);
            }

            //进度
            float progress = ((float) item.getCurrentSize() / (float) item.getTotalSize()) * 100;
            holder.setProgress(R.id.progress, (int) progress);
            holder.setText(R.id.tv_progress, (int) progress + "%");
            //图标
            ImageLoaderUtils.load(mContext, item.getIcon(), (ImageView) holder.getView(R.id.iv_icon));

            holder.setOnClickListener(R.id.start, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (item.getState()) {
                        case NONE:
                        case PAUSE:
                            RDownLoad.get().startDownload(item);
                            break;
                        case WAITING:
                        case LOADING:
                        case ERROR:
                            RDownLoad.get().stopDownload(item);
                            break;
                        case FINISH:
                            ToastUtils.showToast(mContext, "文件已下载完成");
                            break;
                    }
                }
            });
            holder.setOnClickListener(R.id.delete, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reStartDownload(item);
                }
            });

        }

        private String getStateText(Download.State state) {
            String stateText = "下载";
            switch (state) {
                case NONE:
                    stateText = "下载";
                    break;
                case WAITING:
                    stateText = "等待中";
                    break;
                case LOADING:
                    stateText = "下载中";
                    break;
                case PAUSE:
                    stateText = "暂停中";
                    break;
                case ERROR:
                    stateText = "错误";
                    break;
                case FINISH:
                    stateText = "完成";
                    break;
            }
            return stateText;
        }
    }

    /**
     * 获取源数据
     *
     * @return
     */
    private List<DownloadBean> getSourceList() {
        List<DownloadBean> list = new ArrayList<>();

        File file1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "WEIXIN" + ".apk");
        File file2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "QQ" + ".apk");
        String url1 = "http://imtt.dd.qq.com/16891/50CC095EFBE6059601C6FB652547D737.apk?fsname=com.tencent.mm_6.6.7_1321.apk&csr=1bbd";
        String url2 = "http://imtt.dd.qq.com/16891/FC92B1B4471DE5AAD0D009DF9BF1AD01.apk?fsname=com.tencent.mobileqq_7.7.5_896.apk&csr=1bbd";

        String icon1 = "http://pp.myapp.com/ma_icon/0/icon_10910_1534495359/96";
        String icon2 = "http://pp.myapp.com/ma_icon/0/icon_6633_1535456193/96";

        DownloadBean bean1 = new DownloadBean(url1, icon1, file1.getAbsolutePath());
        DownloadBean bean2 = new DownloadBean(url2, icon2, file2.getAbsolutePath());

        list.add(bean1);
        list.add(bean2);
        return list;
    }

    /**
     * 获取数据
     *
     * @return
     */
    private List<DownloadBean> getList() {
        List<DownloadBean> localList = RDownLoad.get().getDownloadList(DownloadBean.class);
        List<DownloadBean> sourceList = getSourceList();
        if (localList != null && localList.size() > 0) {
            DownloadBean bean;
            DownloadBean sourceBean;
            for (int i = 0; i < sourceList.size(); i++) {
                sourceBean = sourceList.get(i);
                for (int j = 0; j < localList.size(); j++) {
                    bean = localList.get(j);
                    if (bean.getServerUrl().equals(sourceBean.getServerUrl())) {
                        sourceBean = bean;//替换数据
                        sourceList.set(i, sourceBean);
                    }
                }
            }
        }
        return sourceList;
    }

}
