package com.r.http.cn.load.upload;


import com.r.http.cn.RHttp;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 上传RequestBody
 *
 * @author ZhongDaFeng
 */
public class UploadRequestBody extends RequestBody {

    //实际的待包装请求体
    private final RequestBody requestBody;
    //进度回调接口
    private final UploadProgressCallback progressCallback;
    //包装完成的BufferedSink
    private BufferedSink bufferedSink;
    //源文件
    private File file;
    //当前下标
    private int current;
    //上传总文件
    private int totalFile;


    public UploadRequestBody(RequestBody requestBody, File file, int current, int totalFile, UploadProgressCallback progressCallback) {
        this.file = file;
        this.current = current;
        this.totalFile = totalFile;
        this.requestBody = requestBody;
        this.progressCallback = progressCallback;
    }

    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     * @throws IOException 异常
     */
    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    /**
     * 重写writeTo
     *
     * @param sink BufferedSink
     * @throws IOException 异常
     */
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (null == bufferedSink) {
            bufferedSink = Okio.buffer(sink(sink));
        }
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    /**
     * 写入，回调进度接口
     *
     * @param sink Sink
     * @return Sink
     */
    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long writtenBytesCount = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long totalBytesCount = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                //增加当前写入的字节数
                writtenBytesCount += byteCount;
                //获得contentLength的值，后续不再调用
                if (totalBytesCount == 0) {
                    totalBytesCount = contentLength();
                }
                //回调接口
                if (progressCallback != null) {
                    RHttp.Configure.get().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            float progress = (float) writtenBytesCount / (float) totalBytesCount;
                            progressCallback.progress(file, writtenBytesCount, totalBytesCount, progress, current, totalFile);
                        }
                    });
                }
            }
        };
    }
}
