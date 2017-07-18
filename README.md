# Rx-Mvp

# RxJava2+Retrofit2+RxLifecycle2使用MVP模式构建项目 #

## Api接口 ##
    public interface UserApi {

    @GET("user/login")
    Observable<HttpResponse> login(@QueryMap Map<String, Object> request);

	}


## 适用Retrofit网络请求Observable(被订阅者) ##
	public class HttpRxObservable {

		    /**
		     * 获取被订阅者
		     * 备注:网络请求Observable构建
		     * <h1>补充说明</h1>
		     * 传入LifecycleProvider自动管理生命周期,避免内存泄漏
		     * 备注:需要继承RxActivity.../RxFragment...
		     */
		    public static Observable getObservable(Observable<HttpResponse> apiObservable, LifecycleProvider lifecycle) {
		        Observable observable;
		        observable = apiObservable
		                .map(new ServerResultFunction())
		                .compose(lifecycle.bindToLifecycle())//随生命周期自动管理.eg:onCreate(start)->onStop(end)
		                .onErrorResumeNext(new HttpResultFunction<>())
		                .subscribeOn(Schedulers.io())
		                .observeOn(AndroidSchedulers.mainThread());
		        return observable;
		    }
		
		    /**
		     * 获取被订阅者
		     * 备注:网络请求Observable构建
		     * <h1>补充说明</h1>
		     * 传入LifecycleProvider<ActivityEvent>手动管理生命周期,避免内存泄漏
		     * 备注:需要继承RxActivity,RxAppCompatActivity,RxFragmentActivity
		     */
		    public static Observable getObservable(Observable<HttpResponse> apiObservable, LifecycleProvider<ActivityEvent> lifecycle, ActivityEvent event) {
		        Observable observable;
		        observable = apiObservable
		                .map(new ServerResultFunction())
		                .compose(lifecycle.bindUntilEvent(event))//手动管理移除监听生命周期.eg:ActivityEvent.STOP
		                .onErrorResumeNext(new HttpResultFunction<>())
		                .subscribeOn(Schedulers.io())
		                .observeOn(AndroidSchedulers.mainThread());
		        return observable;
		    }

	}

## 适用Retrofit网络请求Observer(订阅者) ##
    public abstract class HttpRxObserver<T> implements Observer<T>, HttpRequestListener {

		    private String mTag;//请求标识
		
		    public HttpRxObserver() {
		    }
		
		    public HttpRxObserver(String tag) {
		        this.mTag = tag;
		    }
		
		    @Override
		    public void onError(Throwable e) {
		        RxActionManagerImpl.getInstance().remove(mTag);
		        if (e instanceof ApiException) {
		            onError((ApiException) e);
		        } else {
		            onError(new ApiException(e, ExceptionEngine.UN_KNOWN_ERROR));
		        }
		    }
		
		    @Override
		    public void onComplete() {
		    }
		
		    @Override
		    public void onNext(@NonNull T t) {
		        if (!TextUtils.isEmpty(mTag)) {
		            RxActionManagerImpl.getInstance().remove(mTag);
		        }
		        onSuccess(t);
		    }
		
		    @Override
		    public void onSubscribe(@NonNull Disposable d) {
		        if (!TextUtils.isEmpty(mTag)) {
		            RxActionManagerImpl.getInstance().add(mTag, d);
		        }
		        onStart(d);
		    }
		
		    @Override
		    public void cancel() {
		        if (!TextUtils.isEmpty(mTag)) {
		            RxActionManagerImpl.getInstance().cancel(mTag);
		        }
		    }
		
		
		    protected abstract void onStart(Disposable d);
		
		    protected abstract void onError(ApiException e);
		
		    protected abstract void onSuccess(T response);

	}
## 使用 ##
       public void login(RxActivity activity, String phone, String psw) {

		        //构建请求数据
		        Map<String, Object> request = HttpRequest.getRequest();
		        request.put("phone", phone);
		        request.put("psw", psw);

		        //设置唯一TAG
		        HttpRxObserver httpRxObserver = new HttpRxObserver("xxx_login") {
		            @Override
		            protected void onStart(Disposable d) {
		            }
		
		            @Override
		            protected void onError(ApiException e) {
		                LogUtils.w("onError code:" + e.getCode() + " msg:" + e.getMsg());
		            }
		
		            @Override
		            protected void onSuccess(Object response) {
		                LogUtils.w("onSuccess response:" + response.toString());
		            }
		        };

		        /**
		         * 获取请求Observable
		         * 1.RxActivity,RxFragment...所在页面继承RxLifecycle支持的组件
		         * 2.ActivityEvent指定监听函数解绑的生命周期（手动管理,未设置则自动管理）
		         * 以上两点作用防止RxJava监听没解除导致内存泄漏,ActivityEvent若未指定则按照activity/fragment的生命周期
		         */
		
		        HttpRxObservable.getObservable(ApiUtils.getUserApi().login(request), activity).subscribe(httpRxObserver);
		        //HttpRxObservable.getObservable(ApiUtils.getUserApi().login(request), activity, ActivityEvent.PAUSE).subscribe(httpRxObserver);
		        
		        //取消请求
		        /*if(!httpRxObserver.isDisposed()){
		            httpRxObserver.cancel();
		        }*/
    }



**CSDN博客地址，文章说明：http://blog.csdn.net/u014702653/article/details/75268919**