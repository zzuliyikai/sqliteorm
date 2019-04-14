package com.yikai.sqliteframe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.yikai.sqliteframe.db.BaseDaoFactory;
import com.yikai.sqliteframe.db.User;

public class MainActivity extends AppCompatActivity {

    private BaseDao mBaseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createDatabase(View view) {

        mBaseDao = BaseDaoFactory.getInstance().getDataHlper(User.class);

    }


    public void insert(View view) {
        User user = new User();
        user.setName("yikai");
        user.setPassword("123456");
        mBaseDao.insert(user);
    }

    public void query(View view) {
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
