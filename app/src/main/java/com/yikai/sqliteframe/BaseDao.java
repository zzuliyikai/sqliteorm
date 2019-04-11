package com.yikai.sqliteframe;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yikai.sqliteframe.annotation.DbField;
import com.yikai.sqliteframe.annotation.DbTable;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by Administrator on 2019/4/8.
 */

public abstract class BaseDao<T> implements IBaseDao<T> {

    private Class<T> mEntityClass;
    private SQLiteDatabase mSqLiteDatabase;
    private String mTableName;
    //存放表的列名和类的属性
    private HashMap<String, Field> mCacheMap;


    private boolean isInit = false;

    public synchronized boolean init(Class<T> entityClass, SQLiteDatabase sqLiteDatabase) {
        if (!isInit) {
            this.mEntityClass = entityClass;
            this.mSqLiteDatabase = sqLiteDatabase;

            mTableName = mEntityClass.getAnnotation(DbTable.class).value();

            if (mTableName == null) {
                mTableName = mEntityClass.getSimpleName();
            }

            //判断数据库是否打开，如果没有打开直接初始化失败
            if (!mSqLiteDatabase.isOpen()) {
                return false;
            }

            //创建数据库表
            if (isCreateTable()) {
                mCacheMap = new HashMap<>();
                //查看数据库表
                initCacheMap();
                isInit = true;
            }

        }
        return isInit;
    }

    private void initCacheMap() {

        String sql = "select * from " + mTableName + " limit 1,0";

        Cursor cursor = mSqLiteDatabase.rawQuery(sql, null);

        String[] columnNames = cursor.getColumnNames();
        Field[] fields = mEntityClass.getFields();

        for (Field field : fields) {
            field.setAccessible(true);
        }


        for (String columnName : columnNames) {

            Field colmunFilde = null;

            for (Field field : fields) {
                String value;
                if (field.getAnnotation(DbField.class).value() == null) {
                    value = field.getName();
                } else {
                    value = field.getAnnotation(DbField.class).value();
                }
                if (columnName.equals(value)) {
                    colmunFilde = field;
                    break;
                }
            }

            if (colmunFilde != null) {
                mCacheMap.put(columnName, colmunFilde);
            }

        }
        cursor.close();
    }

    public boolean isCreateTable() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("create table if not exists ");
        stringBuffer.append(mTableName + " (");
        Field[] fields = mEntityClass.getFields();

        for (Field field : fields) {
            Class<?> type = field.getType();
            if (type == String.class) {
                String value = field.getAnnotation(DbField.class).value();
                stringBuffer.append(value + " TEXT,");
            } else if (type == Double.class) {
                String value = field.getAnnotation(DbField.class).value();
                stringBuffer.append(value + " DOUBLE,");
            } else if (type == Integer.class) {
                String value = field.getAnnotation(DbField.class).value();
                stringBuffer.append(value + " INTEGER,");
            } else if (type == Long.class) {
                String value = field.getAnnotation(DbField.class).value();
                stringBuffer.append(value + " BIGINT,");
            } else if (type == Byte[].class) {
                String value = field.getAnnotation(DbField.class).value();
                stringBuffer.append(value + " BLOB,");
            } else {
                /**
                 * 暂时不支持
                 */
                continue;
            }
        }
        if (stringBuffer.charAt(stringBuffer.length() - 1) == ',') {

            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        stringBuffer.append(")");
        try {

            mSqLiteDatabase.execSQL(stringBuffer.toString());

        } catch (Exception e) {
            return false;
        }


        return true;
    }


    @Override
    public long insert(T entity) {

        return 0;
    }

}
