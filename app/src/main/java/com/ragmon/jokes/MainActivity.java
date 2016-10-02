package com.ragmon.jokes;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.ragmon.jokes.category.Category;
import com.ragmon.jokes.category.CategoryListFragment;
import com.ragmon.jokes.joke.Joke;
import com.ragmon.jokes.joke.JokeFragment;
import com.ragmon.jokes.joke.JokeListFragment;

import java.util.ArrayList;

public class MainActivity extends SherlockFragmentActivity
        implements CategoryListFragment.OnCategoryListInteractionListener,
                   JokeListFragment.OnJokeListInteractionListener
//        , FragmentManager.OnBackStackChangedListener
{
    private static final String _TAG = MainActivity.class.getSimpleName();


    /******************************************************************
     * Debug mode allow you to use DatabaseManager
     ******************************************************************/
    private static final boolean DEBUG_MODE = false;


    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Get DB connection before init content view,
        // because in "activity_main.xml" view set fragment which use "CategoryListFragment"
        // where need load categories list from DB.
        db = DBHelper.getReadableDatabase(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Listen for changes in the back stack
//        getSupportFragmentManager().addOnBackStackChangedListener(this);
        // Handle when activity is recreated like on orientation Change
//        shouldDisplayHomeUp();

        // Init SlideMenu
        SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.sidemenu);
        menu.setBehindWidthRes(R.dimen.slidingmenu_behind_width);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentFragment, new CategoryListFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!DEBUG_MODE) {
            menu.findItem(R.id.mmItemDatabaseManager).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mmItemDatabaseManager:
                Log.d(_TAG, "-- Show Database Manager --");
                Intent dbmanager = new Intent(this, AndroidDatabaseManager.class);
                startActivity(dbmanager);
                return true;

            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Close DB connection
        if (db != null) {
            db.close();
        }
    }

    @Override
    public void onCategoryListItemSelect(Category category) {
        ArrayList<Category> categories = DBHelper.getCategoryList(db, category.id);
        Log.d(_TAG, "Loaded categories count: " + categories.size());
        // Have sub level categories
        if (categories.size() > 0) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFragment, CategoryListFragment.newInstance(categories))
                    .addToBackStack(null)
                    .commit();
        }
        // No sub level categories, load jokes list for this category
        else {
            ArrayList<Joke> jokes = DBHelper.getJokeList(db, category.id);
            Log.d(_TAG, "Loaded jokes count: " + jokes.size());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentFragment, JokeListFragment.newInstance(jokes))
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onJokeListItemSelect(Joke joke, ArrayList<Joke> categoryJokesList, int currentJokeIndex) {
        Log.d(_TAG, "Selected joke with ID: " + joke.id);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentFragment, JokeFragment.newInstance(joke, categoryJokesList, currentJokeIndex))
                .addToBackStack(null)
                .commit();
    }

//    @Override
//    public void onBackStackChanged() {
//        shouldDisplayHomeUp();
//    }

//    public void shouldDisplayHomeUp() {
//        Log.d(_TAG, "Back stack count: " + getSupportFragmentManager().getBackStackEntryCount());
//        // Enable Up button only  if there are entries in the back stack
//        boolean canback = getSupportFragmentManager().getBackStackEntryCount() > 0;
//        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
//    }

}
