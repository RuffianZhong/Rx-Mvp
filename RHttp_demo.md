# RHttp

- 基本的get、post、put、delete、4种请求
- 单/多文件上传 
- 断点续传下载
- 支持自定义Callback
- 支持https
- 支持tag取消，也可取消全部   
- 支持链式调用
- 支持表单格式，String，json格式数据提交请求


> RHttp使用文档（示例）


### 1.全局配置

初始化 RHttp 必须调用 **init**

```
public class RApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

		//必须初始化RHttp
        RHttp.Configure.get()
                .baseUrl(baseUrl)  //基础URL
                .init(this);       //初始化
    }

}
```

### 2.使用请求

基础Http请求使用示例

```
        //Header参数
        TreeMap<String, Object> header = new TreeMap<>();
        header.put("H1", "H100");
        header.put("H2", "H1000");

        //请求参数
        TreeMap<String, Object> parameter = new TreeMap<>();
        parameter.put("P1", "P100");
        parameter.put("P2", "P1000");

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
                        //数据解析转换
                        Response response = new Gson().fromJson(data, Response.class);//String 转为 Response（自定义）
                        UserBean userBean = new Gson().fromJson(response.getResult(), UserBean.class);// Response 转为 JavaBean（目标对象）
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

                    }
                });
```

### 3.上传文件

上传文件示例   支持多文件上传，可以获取到总文件数，当前文件处于第几个
```
        //Header参数
        TreeMap<String, Object> header = new TreeMap<>();
        header.put("H1", "H100");
        header.put("H2", "H1000");

        //请求参数
        TreeMap<String, Object> parameter = new TreeMap<>();
        parameter.put("P1", "P100");
        parameter.put("P2", "P1000");

        //文件参数
        TreeMap<String, File> fileMap = new TreeMap<>();
        parameter.put("icon", new File("/file/xx.png"));
        parameter.put("file", new File("/file/yy.txt"));

        new RHttp.Builder()
                .post()                     //请求方式
                .apiUrl("user/login")       //接口地址
                .addParameter(parameter)    //参数
                .addHeader(header)          //请求头
                .file(fileMap)              //文件集合
                .lifecycle(this)            //自动管理生命周期，可以不传，如果未及时取消RxJava可能内存泄漏
                .build()
                .upload(new UploadCallback<UserBean>() {
                    @Override
                    public void onProgress(File file, long currentSize, long totalSize, float progress, int currentIndex, int totalFile) {
                        //当前文件 file
                        //当前文件下标  currentIndex   总文件数  totalFile
                    }

                    @Override
                    public UserBean onConvert(String data) {
                        //数据解析转换
                        Response response = new Gson().fromJson(data, Response.class);//String 转为 Response（自定义）
                        UserBean userBean = new Gson().fromJson(response.getResult(), UserBean.class);// Response 转为 JavaBean（目标对象）
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

                    }
                });
```

### 4.断点续传下载

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


