# Rx-Mvp

# 项目使用 RHttp + MVP 模式构建 #


## RHttp

> RHttp 是基于 RxJava2 + Retrofit2 + OkHttp3 + RxLifecycle2 框架整合封装的网络请求框架

- 基本的get、post、put、delete、4种请求
- 单/多文件上传 
- 断点续传下载
- 基本回调包含 onSuccess、onError、onCancel、onProgress（上传/下载）
- 支持自定义Callback
- 支持https
- 支持tag取消，也可取消全部   
- 支持链式调用

RHttp 很屌吗？算不上，基本满足应用开发的所有需求，代码很简洁，体积小，功能齐全。这就够了

1. 基于主流网络框架 Retrofit2 + OkHttp3 作为底层框架
2. 使用 RxJava2 处理异步事件，线程间切换逻辑
3. 使用 RxLifecycle2 管理生命周期，再也不用担心 RxJava 使用不当造成内存泄漏
4. 基础网络请求，数据转换为 String 并提供 onConvert 方法提供开发者自定义转化数据
5. 上传带进度回调，多文件情况下，可以区分当前文件下标
6. 断点续传大文件，多种下载状态满足不同下载需求

```
        new RHttp.Builder()
                .post()                     //请求方式
                .apiUrl("user/login")       //接口地址
                .addParameter(parameter)    //参数
                .addHeader(header)          //请求头
                .lifecycle(this)            //自动管理生命周期，可以不传，如果未及时取消RxJava可能内存泄漏
                .build()
                .request(new HttpCallback<UserBean>() {
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
```

![](upload.gif) ![](download.gif) 


## [RHttp 使用 demo文档](https://github.com/RuffianZhong/Rx-Mvp/blob/master/RHttp_demo.md)

## [RHttp 详解文档](https://github.com/RuffianZhong/Rx-Mvp/blob/master/RHttp.md)



## MVP

> MVP 是 MVC 框架的升级版本，通过 Presenter 桥梁连接 View 和 Model，使得模块之间更好的解耦

#### 定义BasePresenter<V, T>

```

public class BasePresenter<V, T> {

    protected Reference<V> mViewRef;
    protected V mView;
    protected Reference<T> mActivityRef;
    protected T mActivity;


    public BasePresenter(V view, T activity) {
        attachView(view);
        attachActivity(activity);
        setListener(activity);
    }

   
	/*关联*/	
    private void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
        mView = mViewRef.get();
    }

	/*关联*/	
    private void attachActivity(T activity) {
        mActivityRef = new WeakReference<T>(activity);
        mActivity = mActivityRef.get();
    }

    /*解绑*/	
    private void detachView() {
        if (isViewAttached()) {
            mViewRef.clear();
            mViewRef = null;
        }
    }
    
	/*解绑*/	
    private void detachActivity() {
        if (isActivityAttached()) {
            mActivityRef.clear();
            mActivityRef = null;
        }
    }

    public V getView() {
        if (mViewRef == null) {
            return null;
        }
        return mViewRef.get();
    }

    public T getActivity() {
        if (mActivityRef == null) {
            return null;
        }
        return mActivityRef.get();
    }
}

```

**XXXPresenter**

```
    class LoginPresenter extends BasePresenter<ILoginView, LifecycleProvider> {

        public LoginPresenter(ILoginView view, LifecycleProvider activity) {
            super(view, activity);
        }

		/*登录逻辑*/
        public void login(){
            new RHttp.Builder()
                    .post()
                    .apiUrl("user/login")
                    .addParameter(parameterMap)
                    .lifecycle(getActivity())
                    .build()
                    .request(new HttpCallback<UserBean>(){});
        }

    }
```


#### 定义View接口 
```
	public interface ILoginView {

	    //显示结果
	    void showResult(UserBean bean);

	}
```
#### Activity使用

```
    class XActivity extends RxActivity implements ILoginView{
        //登录Presenter
        LoginPresenter loginPresenter = new LoginPresenter(this, this);

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            loginPresenter.login();
        }

        @Override
        public void showResult(UserBean bean) {
            //update ui
        }
    }
```

```

1. 有部分开发者可能会认为把 `UserBean` 实体传递给 `View(activity/fragment)` 还是会出现 `model` 跟 `view` 的耦合

2. 个人认为，不一定死脑筋非要完全解耦才是实现了 `MVP` 的模式，这里已经实现不关心 `UserBean` 的来源和构建就行了

3. 如果非要完全解耦也是可以的，将 UserBean 拆解成"基本数据类型" 
  `void showResult(String userName,int userId....)` 要用几个参数就定义几个参数，但是感觉太麻烦了

```

```
   /**
     * 文档说明有限
     *
     * 强烈建议阅读代码，在此基础上改造成适用自己项目的框架
     *
     * 欢迎提供建议/意见，不断完善框架
     */

```

> 原始构造想法参考博客
> **CSDN博客地址：http://blog.csdn.net/u014702653/article/details/75268919**


## License

```
MIT License

Copyright (c) 2018 Ruffian-痞子

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```