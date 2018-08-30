package com.r.http.cn.utils;

import android.content.Context;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBaseConfig;
import com.r.http.cn.RHttp;

import java.util.ArrayList;

/**
 * 数据库辅助类
 *
 * @author ZhongDaFeng
 * @https://github.com/litesuits/android-lite-orm
 */
public class DBHelper {

    /*数据库名称*/
    private final String DB_NAME = "com-r-mvp-cn.db";
    private final int DB_VERSION = 1;
    private static DBHelper instance;
    private static LiteOrm db;
    private Context context;

    private DBHelper() {
        if (RHttp.Configure.get().getContext() == null) {
            throw new NullPointerException("RHttp not init!");
        }
        context = RHttp.Configure.get().getContext();
        initDB(context);
    }

    public static DBHelper get() {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化数据库
     *
     * @param context
     */
    private void initDB(Context context) {
        if (db == null) {
            DataBaseConfig config = new DataBaseConfig(context, DB_NAME);
            config.debugged = RHttp.Configure.get().isShowLog(); // open the log
            config.dbVersion = DB_VERSION; // set database version
            config.onUpdateListener = null; // set database update listener
            db = LiteOrm.newSingleInstance(config);
        }
    }

    /**
     * 插入或者更新对象
     * 备注:有则更新，无则插入
     *
     * @param object
     * @return
     */
    public long insertOrUpdate(Object object) {
        long count = 0;
        if (db != null) {
            count = db.save(object);
        }
        return count;
    }

    /**
     * 删除对象
     *
     * @param var1
     * @return
     */
    public int delete(Object var1) {
        int count = 0;
        if (db != null) {
            count = db.delete(var1);
            LogUtils.e("count======" + count);
        }
        return count;
    }

    /**
     * 查询数据总数
     *
     * @param var1
     * @param <T>
     * @return
     */
    public <T> long queryCount(Class<T> var1) {
        long count = 0;
        if (db != null) {
            count = db.queryCount(var1);
        }
        return count;
    }

    /**
     * 查询列表
     *
     * @param var1
     * @param <T>
     * @return
     */
    public <T> ArrayList<T> query(Class<T> var1) {
        ArrayList<T> list = new ArrayList<>();
        if (db != null) {
            list = db.query(var1);
        }
        return list;
    }

    /**
     * 根据ID查询数据
     *
     * @param var1
     * @param var2
     * @param <T>
     * @return
     */
    public <T> T queryById(long var1, Class<T> var2) {
        T t = null;
        if (db != null) {
            t = db.queryById(var1, var2);
        }
        return t;
    }

}
