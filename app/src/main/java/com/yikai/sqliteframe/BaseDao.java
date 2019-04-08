package com.yikai.sqliteframe;

import android.database.sqlite.SQLiteDatabase;

import com.yikai.sqliteframe.annotation.DbField;
import com.yikai.sqliteframe.annotation.DbTable;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2019/4/8.
 */

public class BaseDao<T> implements IBaseDao<T> {

    private Class<T> mEntityClass;
    private SQLiteDatabase mSqLiteDatabase;
    private String mTableName;


    private boolean isInit = false;

    public synchronized boolean init(Class<T> entityClass, SQLiteDatabase sqLiteDatabase) {
        if (!isInit) {
            this.mEntityClass = entityClass;
            this.mSqLiteDatabase = sqLiteDatabase;

            mTableName = mEntityClass.getAnnotation(DbTable.class).value();

            //判断数据库是否打开，如果没有打开直接初始化失败
            if (!mSqLiteDatabase.isOpen()) {
                return false;
            }
            //创建表是否成功，如果不成功直接返回false
            if (!isCreateTable()) {
                return false;
            }

            isInit = true;
        }
        return isInit;
    }

    @Override
    public long insert(T entity) {

        return 0;
    }

    public boolean isCreateTable() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("create table if not exists ");
        stringBuffer.append(mTableName +" (");
        Field[] fields = mEntityClass.getFields();

        for (Field field :fields) {
            Class<?> type = field.getType();
            if (type == String.class){
                String value = field.getAnnotation(DbField.class).value();
                stringBuffer.append(value + " TEXT,");
            }else if (type == Double.class){
                String value = field.getAnnotation(DbField.class).value();
                stringBuffer.append(value + " DOUBLE,");
            }else if (type == Integer.class){
                String value = field.getAnnotation(DbField.class).value();
                stringBuffer.append(value + " INTEGER,");
            }else if (type == Long.class){
                String value = field.getAnnotation(DbField.class).value();
                stringBuffer.append(value + " BIGINT,");
            }else if (type == Byte[].class){
                String value = field.getAnnotation(DbField.class).value();
                stringBuffer.append(value + " BLOB,");
            }else {

                /**
                 * 暂时不支持
                 */
                continue;
            }


        }


        return false;
    }
}
