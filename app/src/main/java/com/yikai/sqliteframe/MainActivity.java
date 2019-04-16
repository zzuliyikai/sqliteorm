package com.yikai.sqliteframe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.yikai.sqliteframe.db.BaseDao;
import com.yikai.sqliteframe.db.BaseDaoFactory;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BaseDao mBaseDao;
    private UserDao mUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createDatabase(View view) {
        //基本用法
      //  mBaseDao = BaseDaoFactory.getInstance().getDataHlper(User.class);

        //可扩展的用法，例如想多表查询

        mUserDao = BaseDaoFactory.getInstance().getDataHlper2(UserDao.class, User.class);





    }


    public void insert(View view) {
/*        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setName("yikai"+i);
            user.setPassword("123456");
            user.setId(i);
            mBaseDao.insert(user);
        }*/

        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setName("yikai" + i);
            user.setPassword("123456");
            user.setId(i);
            mUserDao.insert(user);
        }




    }

    public void query(View view) {

        User user = new User();
        user.setName("yikai");
        List<User> query = mUserDao.query(user,null,0,10);
        Toast.makeText(this, "size"+query.size(), Toast.LENGTH_SHORT).show();

    }

    public void delete(View view) {
        User user = new User();
        user.setName("易凯");
        mBaseDao.delete(user);
    }

    public void update(View view) {
        User where = new User();
        where.setName("yikai");
        User user = new User();
        user.setName("易凯");
        int update = mBaseDao.update(user, where);
        Toast.makeText(this, "update"+update, Toast.LENGTH_SHORT).show();

    }
}
