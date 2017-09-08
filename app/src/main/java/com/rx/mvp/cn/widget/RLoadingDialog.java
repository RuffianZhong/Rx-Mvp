package com.rx.mvp.cn.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * 自定义Loading弹窗
 *
 * @author ZhongDafeng
 */
public class RLoadingDialog extends Dialog {

    public RLoadingDialog(Context context, boolean cancelable) {
        super(context);
        setCancelable(cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LinearLayout linearLayout = new LinearLayout(getContext());
        ProgressBar progressBar = new ProgressBar(getContext());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(125,
                125);
        params.setMargins(100, 20, 100, 20);
        progressBar.setLayoutParams(params);
        linearLayout.addView(progressBar);

        setContentView(linearLayout);
    }

}
