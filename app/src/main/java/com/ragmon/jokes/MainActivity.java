package com.ragmon.jokes;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends SherlockFragmentActivity {

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = DBHelper.getReadableDatabase(this);

        // Init SlideMenu
        SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.sidemenu);
        menu.setBehindWidthRes(R.dimen.slidingmenu_behind_width);
        
//        String[] items = {"Первый фрагмент","Второй фрагмент"};
//        ((ListView) findViewById(R.id.sidemenu)).setAdapter(
//                category
////                new ArrayAdapter<Object>(
////                        this,
////                        R.layout.sidemenu_item,
////                        R.id.text,
////                        items
////                )
//        );
    }
}
