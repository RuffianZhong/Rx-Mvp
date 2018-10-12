# RHttp

- 基本的get、post、put、delete、4种请求
- 单/多文件上传 
- 断点续传下载
- 支持自定义Callback
- 支持https
- 支持tag取消，也可取消全部   
- 支持链式调用
- 支持表单格式，String，json格式数据提交请求

### 1.全局配置

为了 RHttp 框架需求，需要一些全局配置  **init** 必须调用

##### 1.1 如果你很懒，只为快速上手。 继承 Application 一行代码初始化

```
public class RApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
		//必须初始化RHttp
        RHttp.Configure.get().init(this);
    }

}
```

##### 1.2 如果你需求比较复杂，以下是全部可选配置，开发者按需设置
```
        RHttp.Configure.get()
                .baseUrl(baseUrl)				//基础URL
                .baseHeader(baseHeader)			//全局Header
                .baseParameter(baseParameter)	//全局参数
                .timeout(60)					//连接&读&写超时时长
                .timeUnit(TimeUnit.SECONDS)		//超时时长单位
                .showLog(true)					//是否显示log
                .init(this);					//初始化
```

### 2.使用请求

##### 2.1 先看看全部可选参数设置
```
        RHttp http = new RHttp.Builder()
                .post()// 任选一种请求方式 .post().get().put().delete()
                .baseUrl("http://apicloud.mob.com/")//基础路径 如果全局配置了，这里可以省略
                .apiUrl("user/login")//接口路径  如果不需要可以不填
                .addParameter(header)//增加参数，重复调用可叠加
                .setParameter(parameter)//设置参数，覆盖之前设置
                .addHeader(header)//增加Header，重复调用可叠加
                .setHeader(header)//设置Header，覆盖之前设置
				.setBodyString(new Gson().toJson(user), true) //String/json格式提交数据 
                .tag("someTag")//设置TAG 后续可根据tag取消请求
				.file(fileMap)//map方式设置file文件
                .file("key", list)//一个key对应多个文件
                .lifecycle(this)//设置自动管理生命周期 Activity/Fragment 继承 RxLifecycle (不设置可能导致RxJava使用内存泄漏)
                .activityEvent(ActivityEvent.STOP)//手动管理属于Activity (此属性前提必须设置lifecycle)
                .fragmentEvent(FragmentEvent.DESTROY_VIEW)//手动管理属于Fragment (此属性前提必须设置lifecycle)
                .build();
```
- `addParameter()`  表示添加参数，重复调用可叠加
- `setParameter() ` 表示重置参数，清空之前设置
- `setBodyString(String text,boolean isJson) ` （只支持POST）表示 `String/json` 格式提交数据，会使 `add/setParameter` 设置的参数无效；
  `boolean isJson` 参数意义： `true` 表示json格式 ； `false` 表示String格式   
- `baseUrl()`  基础URL，如果全局配置了，这里可以省略
- `apiUrl()`   基础路径 如果不需要可以不设置
- `lifecycle()` 自动管理生命周期，防止RxJava内存泄漏
- `activityEvent()` 适用于Activity的指定生命周期管理  依赖于 `lifecycle` 已经设置
- `fragmentEvent()` 适用于Fragment的指定生命周期管理  依赖于 `lifecycle` 已经设置
- `file(Map fileMap)` 上传文件时，文件map
- `file(String key,List<File> list)` 上传文件时，文件list 适用一个key对应多个文件

##### 2.2 发起请求/上传文件/取消请求/取消全部
```
        http.request(new HttpCallback{});//发起请求
        http.upload(new UploadCallback{});//上传文件的请求
        http.isCanceled();//是否已经取消
        http.cancel();//取消请求
        RHttp.cancel("tag");//根据tag取消请求
        RHttp.cancelAll();//取消全部请求
```

##### 2.3 回调函数

###### 2.3.1 `HttpCallback<T>` 基本网络请求回调

```
        http.request(new HttpCallback<String>() {
            @Override
            public String onConvert(String data) {
                //数据转换/解析
				//开发者自己实现如何将 data 转化为 泛型指定 数据
                return data;
            }

            @Override
            public void onSuccess(String value) {

            }

            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onCancel() {

            }
        });
```

- `public String onConvert(String data)` 是数据转化/解析的回调函数，通过指定泛型 在这里解析之后框架会将返回值 `String`（或者其他bean） 回调给 `onSuccess` 可以直接使用；如果解析数据出错，回调 `onError`
- `public void onSuccess(String value)` 成功回调 通过指定泛型得到最终结果，解析和转换交由 `onConvert` 实现
- `public void onError(int code, String desc)` 错误/失败回调  code错误码  desc错误信息
- `public void onCancel()` 取消回调


###### 2.3.2 `UploadCallback<T>` 上传文件请求回调

`UploadCallback<T>` 继承于 `HttpCallback<T>` 具有相同的基本功能，额外增加 `进度回调接口`


```
        http.upload(new UploadCallback<UserBean>(){

            @Override
            public void onProgress(File file, long currentSize, long totalSize, float progress, int currentIndex, int totalFile) {
                
            }

            @Override
            public UserBean onConvert(String data) {
				//开发者自己实现如何将 String 转化为对应的 JavaBean
                return new Gson().fromJson(data, UserBean.class);
            }

            @Override
            public void onSuccess(UserBean value) {

            }

            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onCancel() {

            }
        });
```
- `public void onProgress(File file, long currentSize, long totalSize, float progress, int currentIndex, int totalFile) ` 上传进度回调
1. `file：` 正在上传的文件
2. `currentSize：` 已上传
3. `totalSize：` 文件总大小 
4. `progress：` 进度
5. `currentIndex：` 当前上传文件下标
6. `totalFile：` 此次上传总文件数

- 其他回调与 `HttpCallback` 一样

###### 2.3.3 `XXXCallback<T>` 开发者如何自定义适用于自己项目的 Callback ??

我们看到，框架提供的 `HttpCallback<T>` 的 `onConvert(String data)` 传进来的参数是 `String` 也就是说，`RHttp` 将请求到的数据转化为 `String` 类型

将数据从 `String` 开始解析麻烦了一点，对于一些规范数据，比如：
```
{
    "code": 200
    "msg": "success",
    "result": {},   
}
```

对于这种数据格式，如果全部从 String 开始解析，那太蛋疼，进一步封装

定义一个 `Response` 用于快速解析服务器返回数据
```
public class HttpResponse implements Serializable {

	/*描述信息*/
    @SerializedName("msg")
    private String msg;

	/*状态码*/
    @SerializedName("code")
    private int code;

	/*数据对象*/
    @SerializedName("result")
    private JsonElement result;

    /**
     * 是否成功(这里可以与服务器约定)
     * 备注：这里约定 code==200 认为这次请求逻辑成功
     *
     * @return
     */
    public boolean isSuccess() {
        return code == 200;
    }

    public String toString() {
        String response = "[http response]" + "{\"code\": " + code + ",\"msg\":" + msg + ",\"result\":" + new Gson().toJson(result) + "}";
        return response;
    }

    //get/set

}
```

这里我们最终需要使用的大部分是 `result` 转成对应的 `JavaBean` 就可以了

对于 `code` 不等于 `200` 的错误逻辑，可以回调 `onError(int code, String desc)` 认为逻辑失败了，例如：登录返回 code=123 msg=密码错误

**自定义Callback示例**

```
public abstract class RHttpCallback<T> extends HttpCallback<T> {

    @Override
    public T onConvert(String data) {
        /**
         * 接口响应数据格式如@Response
         * 将result转化给success
         * 这里处理通过错误
         */
        T t = null;
        Response response = new Gson().fromJson(data, Response.class);
        int code = response.getCode();
        String msg = response.getMsg();
        JsonElement result = response.getResult();
        switch (code) {
            case 101://token过期，跳转登录页面重新登录(示例)
                break;
            case 102://系统公告(示例)
                break;
            default:
                if (response.isSuccess()) {//与服务器约定成功逻辑
                    t = convert(result);
                } else {//统一为错误处理
                    onError(code, msg);
                }
                break;
        }
        return t;
    }

    /**
     * 数据转换/解析
     *
     * @param data
     * @return
     */
    public abstract T convert(JsonElement data);

    /**
     * 成功回调
     *
     * @param value
     */
    public abstract void onSuccess(T value);

    /**
     * 失败回调
     *
     * @param code
     * @param desc
     */
    public abstract void onError(int code, String desc);

    /**
     * 取消回调
     */
    public abstract void onCancel();
}
```

###### 2.3.4 断点续传下载文件回调函数
```
public abstract class DownloadCallback<T extends Download> {

    /**
     * 进度回调
     *
     * @param state       下载状态
     * @param currentSize 当前已下载
     * @param totalSize   文件总大小
     * @param progress    进度
     */
    public abstract void onProgress(Download.State state, long currentSize, long totalSize, float progress);

    /**
     * 下载出错
     *
     * @param e
     */
    public abstract void onError(Throwable e);

    /**
     * 下载成功
     *
     * @param object
     */
    public abstract void onSuccess(T object);
}
```
- `void onProgress(Download.State state, long currentSize, long totalSize, float progress);` 下载进度回调
1. `State：` 下载状态
2. `currentSize：` 已下载
3. `totalSize：` 文件总大小 
4. `progress：` 进度
- `onSuccess(T object)` 下载成功回调 `T` 可以是 `Download` 或其子类
- `onError(Throwable e)` 下载失败


框架下载类 `Download`

```
public class Download implements Serializable {

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    @Column("id")
    private long id;

    @Column("localUrl")
    private String localUrl;//本地存储地址

    @Column("serverUrl")
    private String serverUrl;//下载地址

    @Column("totalSize")
    private long totalSize;//文件大小

    @Column("currentSize")
    private long currentSize;//当前大小

    @Column("state")
    private State state = State.NONE;//下载状态

    @Ignore
    private Api api;//接口service

    @Ignore
    private DownloadCallback callback;//回调接口

    /**
     * 枚举下载状态
     */
    public enum State {
        NONE,           //无状态
        WAITING,        //等待
        LOADING,        //下载中
        PAUSE,          //暂停
        ERROR,          //错误
        FINISH,         //完成
    }
}
```

拓展下载实体类  使用框架的 `Download` 或者其子类

```
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

	//get/set
}
```

构建下载

```
        //文件存储路径和名称
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "QQ.apk");
        //下载地址
        String url = "http://imtt.dd.qq.com/16891/FC92B1B4471DE5AAD0D009DF9BF1AD01.apk?fsname=com.tencent.mobileqq_7.7.5_896.apk&csr=1bbd";
        //额外参数icon
        String icon = "http://pp.myapp.com/ma_icon/0/icon_6633_1535456193/96";

        DownloadBean bean = new DownloadBean(url, icon, file.getAbsolutePath());
        /*设置回调监听*/
        bean.setCallback(new DownloadCallback<DownloadBean>() {
            @Override
            public void onProgress(Download.State state, long currentSize, long totalSize, float progress) {
                //State:  NONE=无状态 WAITING=等待 LOADING=下载中 ERROR=错误 FINISH=完成
                //currentSize 已下载
                //totalSize 文件大小
                //progress 进度
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(DownloadBean object) {
                //下载成功
                //object.getLocalUrl();
            }
        });

        /*开始/继续*/
        RDownLoad.get().startDownload(bean);
        /*暂停*/
        RDownLoad.get().stopDownload(bean);
        /*暂停全部*/
        RDownLoad.get().stopAllDownload();
        /*移除下载*/
        RDownLoad.get().removeDownload(bean);
        /*下载中列表*/
        RDownLoad.get().getDownloadList(DownloadBean.class);
```

- `void startDownload(Download download)` 开始/继续下载
- `void stopDownload(Download download)`  停止下载
- `void stopAllDownload()`  停止全部下载任务
- `void removeDownload(Download download)`  移除下载，正在下载中会先暂停再移除
- `public <T> List<T> getDownloadList(Class<T> tClass)`  获取正在下载列表 `<T extends Download>`  `tClass` 必须是 `Download` 或者其子类

