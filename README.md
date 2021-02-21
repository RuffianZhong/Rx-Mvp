# 请关注我最新的架构模式 [JetpackMVVM](https://github.com/RuffianZhong/JetpackMVVM) 基于Rx-Mvp全方位升级

> Rx-Mvp 项目包含两个部分功能/模块
> 
> RHttp : 基于 RxJava2 + Retrofit2 + OkHttp3 + RxLifecycle2 框架整合封装的网络请求框架
> 
> MVP :  MVC 框架的升级版本，通过 Presenter 桥梁连接 View 和 Model，使得模块之间更好的解耦
> 
> app ： Demo 代码，示例如何使用 MVP 架构项目；示例如何使用 RHttp 进行网络数据请求

## RHttp

> RHttp 是基于 RxJava2 + Retrofit2 + OkHttp3 + RxLifecycle2 框架整合封装的网络请求框架

- 基本的get、post、put、delete、4种请求
- 单/多文件上传 
- 断点续传下载
- 基本回调包含 onSuccess、onError、onCancel、onProgress（上传/下载带进度）
- 支持自定义Callback
- 支持https
- 支持tag取消，支持取消全部请求
- 支持绑定组件生命周期自动管理网络请求   
- 支持链式调用
- 支持表单格式，String，json格式数据提交请求

RHttp 很屌吗？算不上，基本满足应用开发的所有需求，代码很简洁，体积小，功能齐全。这就够了

1. 基于主流网络框架 Retrofit2 + OkHttp3 作为底层框架
2. 使用 RxJava2 处理异步事件，线程间切换逻辑
3. 使用 RxLifecycle2 管理生命周期，再也不用担心 RxJava 使用不当造成内存泄漏
4. 基础网络请求，数据转换为 String 并提供 onConvert 方法提供开发者自定义转化数据
5. 上传带进度回调，多文件情况下，可以区分当前文件下标
6. 断点续传大文件，多种下载状态满足不同下载需求

##### RHttp-初始化(全局配置)
```
        //初始化RHttp
        RHttp.Configure.get().init(this);


        //初始化RHttp（全局配置）
        RHttp.Configure.get()
                .timeout(30)//请求超时时间
                .timeUnit(TimeUnit.SECONDS)//超时时间单位
                .baseHeader(new HashMap<String, Object>())//全局基础header，所有请求会包含
                .baseParameter(new HashMap<String, Object>())//全局基础参数，所有请求会包含
                .baseUrl("http://com.ruffian.http.cn")//基础URL
                .showLog(true)//是否显示调试log
                .init(this);//初始化，必须调用
```
##### RHttp-简单使用
```
        new RHttp.Builder()
                .post()                     //请求方式
                .apiUrl("user/login")       //接口地址
                .addParameter(parameter)    //参数
                .addHeader(header)          //请求头
                .lifecycle(this)            //自动管理生命周期，可以不传，如果未及时取消RxJava可能内存泄漏
                .build()
                .execute(new RHttpCallback<UserBean>() {
                    @Override
                    public UserBean onConvert(String data) {
                        /*数据解析转换*/
						//String 转为 Response（自定义）
                        Response response = new Gson().fromJson(data, Response.class);
						// Response 转为 JavaBean（目标对象）
                        UserBean userBean = new Gson().fromJson(response.getResult(), UserBean.class);
                        return userBean;
                    }

                    @Override
                    public void onSuccess(UserBean value) {
                        //do sth.
                    }

                    @Override
                    public void onError(int code, String desc) {
                        //do sth.
                    }

                    @Override
                    public void onCancel() {
  						//do sth.
                    }
                });


        RHttp http = new RHttp.Builder()
                .tag("login_request")//设置请求tag，以便后续根据tag取消请求
                .build();//构建http对象
        
        http.execute(new RHttpCallback() {});//执行请求（get/post.delete/put）
        http.execute(new RUploadCallback() {});//执行请求（文件上传）
        http.cancel();//取消当前网络请求
        http.isCanceled();//当前网络请求是否已经取消

        //静态方法
        RHttp.cancel("login_request");//根据tag取消指定网络请求
        RHttp.cancelAll();//取消所有网络请求
```

![](upload.gif) ![](download.gif) 


## [RHttp 使用 demo文档](https://github.com/RuffianZhong/Rx-Mvp/blob/master/RHttp_demo.md)

## [RHttp 详解文档](https://github.com/RuffianZhong/Rx-Mvp/blob/master/RHttp.md)



## MVP

> MVP 是 MVC 框架的升级版本，通过 Presenter 桥梁连接 View 和 Model，使得模块之间更好的解耦

### MVP -> P

>  Presenter 桥梁连接 View 和 Model，使得模块之间更好的解耦。 主要职责绑定/解绑View/销毁时释放资源
>  通过动态代理方式解决 getView 判空和容错问题

#### 1.Presenter基类定义
```

/**
 * MVP  根Presenter
 */
public interface IMvpPresenter<V extends IMvpView> {

    /**
     * 将 View 添加到当前 Presenter
     */
    @UiThread
    void attachView(@NonNull V view);

    /**
     * 将 View 从 Presenter 移除
     */
    @UiThread
    void detachView();

    /**
     * 销毁 V 实例
     */
    @UiThread
    void destroy();

}


/**
 * Presenter基础实现
 *
 * @param <V>
 */
public abstract class MvpPresenter<V extends IMvpView> implements IMvpPresenter<V> {

    protected V mView;

    //View代理对象
    protected MvpViewProxy<V> mMvpViewProxy;

    /**
     * 获取view
     *
     * @return
     */
    @UiThread
    public V getView() {
        return mView;
    }

    /**
     * 判断View是否已经添加
     *
     * @return
     */
    @UiThread
    public boolean isViewAttached() {
        return mView != null;
    }

    /**
     * 绑定View
     *
     * @param view
     */
    @UiThread
    @Override
    public void attachView(V view) {
        mMvpViewProxy = new MvpViewProxy<V>();
        mView = (V) mMvpViewProxy.newProxyInstance(view);
    }

    /**
     * 移除View
     */
    @Override
    public void detachView() {
        if (mMvpViewProxy != null) {
            mMvpViewProxy.detachView();
        }
    }

}

```

#### 2.Presenter使用

```
/**
 * 登录Presenter
 * 备注:继承 MvpPresenter 指定 View 类型
 *
 * @author ZhongDaFeng
 */
public class LoginPresenter extends MvpPresenter<AccountContract.ILoginView> {

    /**
     * 登录
     */
    public void login(String userName, String password) {

        //显示loading框
        getView().showProgressView();

        //调用model获取网络数据
        ModelFactory.getModel(AccountModel.class).login(getView().getActivity(), userName, password, getView().getRxLifecycle(), new ModelCallback.Http<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                //model数据回传

                //关闭弹窗
                getView().dismissProgressView();

                StringBuffer sb = new StringBuffer();
                sb.append("登录成功")
                        .append("\n")
                        .append("用户ID:")
                        .append(data.getUid())
                        .append("\n")
                        .append("Token:")
                        .append("\n")
                        .append(data.getToken())
                        .append("\n")
                        .append("最后登录:")
                        .append("\n")
                        .append(data.getTime());

                //用户信息展示
                getView().showResult(sb.toString());

            }

            @Override
            public void onError(int code, String desc) {
                //model数据回传

                //关闭弹窗
                getView().dismissProgressView();

                //错误信息提示
                getView().showError(code, desc);
            }

            @Override
            public void onCancel() {
                //关闭弹窗
                getView().dismissProgressView();
            }
        });

    }

    /**
     * 获取本地缓存数据
     */
    public void getLocalCache() {

        //调用model获取本地数据
        ModelFactory.getModel(AccountModel.class).getLocalCache(getView().getActivity(), getView().getRxLifecycle(), new ModelCallback.Data<String>() {
            @Override
            public void onSuccess(String object) {
                //model数据回传

                //关闭弹窗
                getView().dismissProgressView();

                //用户信息展示
                getView().showResult(object);

            }
        });
    }

    @Override
    public void destroy() {
		//一些对象的释放
    }
}

```



### MVP -> V

>  定义View接口在具体组件中实现，定义常用公用View方法，具体业务接口开发者自行定义

```
另外一套思想：MvpView 定义三个基础接口，具体组件实现
loading/data/error
1. lde 思想: 页面通用  加载中/展示数据/错误处理
2. action 方式: 考虑多个请求时 根据 action 区分处理

  void mvpLoading(String action, boolean show);
  <M> void mvpData(String action, M data);
  void mvpError(String action, int code, String msg);

作者最终放弃此方式，感兴趣查看分支 master.release.mvp_lde
```


```

	/**
	 * IMvpView
	 */
	public interface IMvpView {
	}


	/**
	 * 基础View接口
	 */
	public interface MvpView extends IMvpView {
	
	    /**
	     * RxLifecycle用于绑定组件生命周期
	     *
	     * @return
	     */
	    LifecycleProvider getRxLifecycle();
	
	    /**
	     * 获取Activity实例
	     *
	     * @return
	     */
	    Activity getActivity();
	
	    /**
	     * 展示吐司
	     *
	     * @param msg 吐司文本
	     */
	    @UiThread
	    void showToast(@NonNull String msg);
	
	    /**
	     * 显示进度View
	     */
	    @UiThread
	    void showProgressView();
	
	    /**
	     * 隐藏进度View
	     */
	    @UiThread
	    void dismissProgressView();
	
	}

	 /**------------View具体使用--------------**/

	/**
	 * 登录view
	 * 备注: MvpView 未能满足需求时新增方法
	 */
    interface ILoginView extends MvpView {

        /*登录成功展示结果*/
        @UiThread
        void showResult(String data);

        /*登录错误处理逻辑*/
        @UiThread
        void showError(int code, String msg);
    }
```



### MVP -> M

>  Model 这里认为只负责获取和解析数据，再回调给Presenter

```
	/**
	 * MVP  根Model
	 * MvpModel创建之后全局静态持有，因此不能持有短生命周期的对象，避免内存泄漏
	 *
	 * @author ZhongDaFeng
	 */
	public interface IMvpModel {
	
	}
	
	
	/**
	 * 模块数据回调接口
	 *
	 * @author ZhongDaFeng
	 */
	public interface ModelCallback {
	
	    /**
	     * 网络数据回调，泛指http
	     *
	     * @param <T>
	     */
	    interface Http<T> {
	
	        public void onSuccess(T object);
	
	        public void onError(int code, String desc);
	
	        public void onCancel();
	    }
	
	    /**
	     * 其他数据回调<本地数据，数据库等>
	     *
	     * @param <T>
	     */
	    interface Data<T> {
	
	        public void onSuccess(T object);
	    }
	
	}

	 /**------------Model具体使用--------------**/

    /**
     * 登录模块model接口.此处根据具体项目决定是否需要此接口层
     */
    interface LoginModel extends IMvpModel {
        /**
         * 用户密码登录
         *
         * @param lifecycle     组件生命周期
         * @param modelCallback model回调接口(网络)
         */
        void login(final Context context, String userName, String password, LifecycleProvider lifecycle, ModelCallback.Http<UserBean> modelCallback);

        /**
         * 获取本地缓存数据
         *
         * @param modelCallback model回调接口(普通数据)
         */
        void getLocalCache(Context context, LifecycleProvider lifecycle, ModelCallback.Data<String> modelCallback);

        /**
         * 缓存数据
         */
        void saveLocalCache(Context context, UserBean data);
    }

	/**
	 * AccountModel
	 *
	 * @author ZhongDaFeng
	 */
	public class AccountModel implements AccountContract.LoginModel {
	
	    private final String key_user_cache = "key_user_info";
	
	    @Override
	    public void login(final Context context, String userName, String password, LifecycleProvider lifecycle, final ModelCallback.Http<UserBean> modelCallback) {
	
	        //Biz发起网络请求
	        BizFactory.getBiz(UserBiz.class).login(userName, password, lifecycle, new RHttpCallback<UserBean>() {
	
	            @Override
	            public UserBean convert(JsonElement data) {
	                return new Gson().fromJson(data, UserBean.class);
	            }
	
	            @Override
	            public void onSuccess(UserBean value) {
	
	                String time = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss").format(System.currentTimeMillis());
	                value.setTime(time);
	                //回调给Presenter
	                modelCallback.onSuccess(value);
	                //保存到本地数据
	                saveLocalCache(context, value);
	            }
	
	            @Override
	            public void onError(int code, String desc) {
	                //回调给Presenter
	                modelCallback.onError(code, desc);
	            }
	
	            @Override
	            public void onCancel() {
	                //回调给Presenter
	                modelCallback.onCancel();
	            }
	        });
	
	    }
	
	    @Override
	    public void getLocalCache(final Context context, LifecycleProvider lifecycle, final ModelCallback.Data<String> modelCallback) {
	        //RxJava异步解析本地数据
	        Observable.create(new ObservableOnSubscribe<String>() {
	            @Override
	            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
	
	                //模拟工作线程获取并解析数据
	                String userInfo = SpUtils.getSpUtils(context).getSPValue(key_user_cache, "");
	
	                e.onNext(userInfo);
	                e.onComplete();
	
	            }
	        }).subscribeOn(Schedulers.io())//工作线程
	                .observeOn(AndroidSchedulers.mainThread())
	                .compose(lifecycle.<String>bindToLifecycle())//绑定生命周期
	                .subscribe(new Observer<String>() {
	                    @Override
	                    public void onSubscribe(@NonNull Disposable d) {
	
	                    }
	
	                    @Override
	                    public void onNext(@NonNull String userBean) {
	                        modelCallback.onSuccess(userBean);  //回调给Presenter
	                    }
	
	                    @Override
	                    public void onError(@NonNull Throwable e) {
	                        modelCallback.onSuccess("");
	                    }
	
	                    @Override
	                    public void onComplete() {
	
	                    }
	                });
	    }
	
	    @Override
	    public void saveLocalCache(Context context, UserBean data) {
	        StringBuffer sb = new StringBuffer();
	        sb.append("用户ID:")
	                .append(data.getUid())
	                .append("\n")
	                .append("Token:")
	                .append("\n")
	                .append(data.getToken())
	                .append("\n")
	                .append("最后登录:")
	                .append("\n")
	                .append(data.getTime());
	
	        SpUtils.getSpUtils(context).putSPValue(key_user_cache, sb.toString());
	    }
	}


```


#### MvpActivity/MvpFragment

```
public abstract class MvpActivity<V extends IMvpView, P extends IMvpPresenter<V>> extends RxActivity
 implements IMvpView, MvpDelegateCallback<V, P> {

    /**
     * 获取 Presenter 数组
     */
    protected abstract P[] getPresenterArray();
}

```

```
 P[] getPresenterArray() 返回 Presenter 数组，可用于一个Activity 对应多个 Presenter 问题
```

#### Activity使用

```
public class LoginActivity extends BaseActivity implements AccountContract.ILoginView {

    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_result)
    TextView tvResult;

    private LoginPresenter mLoginPresenter = new LoginPresenter();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initBundleData() {
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        //获取缓存数据
        mLoginPresenter.getLocalCache();
    }

    @OnClick({R.id.login})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                    return;
                }
                //登录
                mLoginPresenter.login(userName, password);
                break;
        }
    }

    @Override
    protected IMvpPresenter[] getPresenterArray() {
        return new IMvpPresenter[]{mLoginPresenter};
    }

    @Override
    public LifecycleProvider getRxLifecycle() {
        return this;
    }

    @Override
    public void showResult(String data) {
        tvResult.setText(data);
    }

    @Override
    public void showError(int code, String msg) {
        showToast(msg);
    }
}
```

### MVP -> 文件过多？

> 这里为了解决MVP模式创建过多的接口类，引入Contract(协议)

```
/**
 * Account业务的Contract(协议)
 * 目的：避免mvp架构 view/model 文件过多
 * 综合管理某业务的 view/model 接口
 *
 * @author ZhongDaFeng
 */
public interface AccountContract {

    /*登录模块View接口*/
    interface ILoginView extends MvpView {

        /*登录成功展示结果*/
        @UiThread
        void showResult(String data);

        /*登录错误处理逻辑*/
        @UiThread
        void showError(int code, String msg);
    }

    /*登录模块model接口.此处根据具体项目决定是否需要此接口层*/
    interface LoginModel extends IMvpModel {
        /**
         * 用户密码登录
         *
         * @param lifecycle     组件生命周期
         * @param modelCallback model回调接口(网络)
         */
        void login(final Context context, String userName, String password, LifecycleProvider lifecycle, ModelCallback.Http<UserBean> modelCallback);

        /**
         * 获取本地缓存数据
         *
         * @param modelCallback model回调接口(普通数据)
         */
        void getLocalCache(Context context, LifecycleProvider lifecycle, ModelCallback.Data<String> modelCallback);

        /**
         * 缓存数据
         */
        void saveLocalCache(Context context, UserBean data);
    }

}
```
## 注意事项
### 混淆代码
```
# OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**

# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# Gson
-keep public class com.google.gson.**
-keep public class com.google.gson.** {public private protected *;}
-keepattributes Signature
-keepattributes *Annotation*
-keep public class com.project.mocha_patient.login.SignResponseData { private *; }

##特殊提醒

#API接口不混淆
-keep class com.r.http.cn.api.**{*;}

#RHttp实体不混淆
-keep class com.r.http.cn.model.**{*;}
#下载相关实体不混淆（如果有自定义下载实体的情况下）
#com.rx.mvp.cn.model.load.DownloadBean => you  package
-keep class com.rx.mvp.cn.model.load.DownloadBean { *; }

# LiteOrm (DB库混淆配置，后续将会移除第三方DB库)
-keep public class com.litesuits.orm.LiteOrm { *; }
-keep public class com.litesuits.orm.db.* { *; }
-keep public class com.litesuits.orm.db.model.** { *; }
-keep public class com.litesuits.orm.db.annotation.** { *; }
-keep public class com.litesuits.orm.db.enums.** { *; }
-keep public class com.litesuits.orm.log.* { *; }
-keep public class com.litesuits.orm.db.assit.* { *; }

# 实体类解析字段使用 @SerializedName("XXX") 可以忽略，如果不是使用 @SerializedName 则自己的实体类不需要混淆
# 使用Gson时需要配置Gson的解析对象及变量都不混淆。不然Gson会找不到变量。
# 将下面替换成自己的实体类 com.rx.mvp.cn.model.account.entity => you  package
-keep class com.rx.mvp.cn.model.account.entity.** { *; }

```

## Tips


```
   /**
     * 文档说明有限
     *
     * 强烈建议阅读代码，在此基础上改造成适用自己项目的框架
     *
     * 欢迎提供建议/意见，不断完善框架
     *
     * 喜欢就star吧，收获知识激励别人
     */

```


## License

```
MIT License

Copyright (c) 2018 Ruffian-痞子
```
