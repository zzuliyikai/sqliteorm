package com.yikai.sqliteframe.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.yikai.sqliteframe.BaseDao;
import com.yikai.sqliteframe.IBaseDao;

/**
 * Created by 15738 on 2019/4/10.
 */

public class BaseDaoFactory {

    private static BaseDaoFactory mBaseDaoFactory = new BaseDaoFactory();

    /**
     * 存入的路径
     */
    private String mPath;
    /**
     * 数据库操作对象
     */
    private SQLiteDatabase mSqLiteDatabase;

    private BaseDaoFactory(){
        mPath = Environment.getDataDirectory().getAbsolutePath()+"yikai.db";
        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(mPath, null);

    }


    public synchronized <T extends BaseDao<M>,M> T getDataHlper(Class<T> entityDao,Class<M> entity){

        //打开数据库

        BaseDao baseDao = null;
        try {
            baseDao = entityDao.newInstance();
            baseDao.init(entity,mSqLiteDatabase);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return (T) baseDao;


    }


    public BaseDaoFactory getInstance(){

        return mBaseDaoFactory;
    }





}
