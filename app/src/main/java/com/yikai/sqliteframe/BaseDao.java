package com.yikai.sqliteframe;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yikai.sqliteframe.annotation.DbField;
import com.yikai.sqliteframe.annotation.DbTable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2019/4/8.
 */

public class BaseDao<T> implements IBaseDao<T> {

    private Class<T> mEntityClass;
    private SQLiteDatabase mSqLiteDatabase;
    private String mTableName;
    //存放表的列名和类的属性
    private HashMap<String, Field> mCacheMap;


    private boolean isInit = false;

    private Map<String, Object> mHashMap;

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
        Field[] fields = mEntityClass.getDeclaredFields();

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
        Field[] fields = mEntityClass.getDeclaredFields();

        for (Field field : fields) {
            Class type = field.getType();
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

        Map<String, Object> map = getMap(entity);
        ContentValues contentValues = getContentValues(map);
        long insert = mSqLiteDatabase.insert(mTableName, null, contentValues);
        return insert;
    }

    @Override
    public int delete(T entity) {
        Map<String, Object> entityMap = getMap(entity);
        String whereClause = getWhereClause(entityMap);
        String[] whereArgs = getWhereArgs(entityMap);
        int delete = mSqLiteDatabase.delete(mTableName, whereClause, whereArgs);
        return delete;
    }

    @Override
    public int update(T entity, T where) {

        Map<String, Object> entityMap = getMap(entity);
        ContentValues contentValues = getContentValues(entityMap);
        Map<String, Object> whereMap = getMap(where);

        String whereClause = getWhereClause(whereMap);
        String[] whereArgs = getWhereArgs(whereMap);


        int update = mSqLiteDatabase.update(mTableName, contentValues, whereClause, whereArgs);
        return update;
    }

    @Override
    public List<T> query(T where) {

        return query(where, null, null, null);
    }

    @Override
    public List<T> query(T where, String selection, String[] selectionArgs, String limit) {

        Cursor cursor = mSqLiteDatabase.query(mTableName, null, selection, selectionArgs, null, null, limit);


        


        return null;
    }

    private String[] getWhereArgs(Map<String, Object> whereMap) {

        List<String> listWhereArgs = new ArrayList<>();
        Set<Map.Entry<String, Object>> entries = whereMap.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            if (next.getValue() != null) {
                listWhereArgs.add((String) next.getValue());
            }
        }

        return listWhereArgs.toArray(new String[listWhereArgs.size()]);


    }

    private String getWhereClause(Map<String, Object> whereMap) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1=1");
        Set<String> set = whereMap.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key != null) {
                stringBuilder.append(" and " + key + "=?");
            }
        }
        return stringBuilder.toString();
    }

    private ContentValues getContentValues(Map<String, Object> map) {
        ContentValues contentValues = new ContentValues();
        Set<Map.Entry<String, Object>> entries = map.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> next = iterator.next();
            String key = next.getKey();
            Object value = next.getValue();
            if (value instanceof String) {
                contentValues.put(key, (String) value);
            } else if (value instanceof Long) {
                contentValues.put(key, (Long) value);
            } else if (value instanceof Integer) {
                contentValues.put(key, (Integer) value);
            }
        }
        return contentValues;
    }

    private Map<String, Object> getMap(T entity) {
        //获取entity里面的值并将值存到user对象中
        Set<Map.Entry<String, Field>> entrieSet = mCacheMap.entrySet();
        mHashMap = new HashMap<>();
        Iterator<Map.Entry<String, Field>> iterator = entrieSet.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Field> next = iterator.next();
            String key = next.getKey();
            Field filed = next.getValue();
            try {
                String value = (String) filed.get(entity);
                if (value != null) {
                    mHashMap.put(key, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return mHashMap;
    }
}
