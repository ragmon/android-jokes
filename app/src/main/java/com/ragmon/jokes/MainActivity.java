package com.ragmon.jokes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.ragmon.jokes.category.Category;
import com.ragmon.jokes.category.CategoryListFragment;
import com.ragmon.jokes.favorite.Favorite;
import com.ragmon.jokes.favorite.FavoriteListAdapter;
import com.ragmon.jokes.favorite.FavoriteListFragment;
import com.ragmon.jokes.joke.Joke;
import com.ragmon.jokes.joke.JokeFragment;
import com.ragmon.jokes.joke.JokeListAdapter;
import com.ragmon.jokes.joke.JokeListFragment;

import java.util.ArrayList;

public class MainActivity extends SherlockFragmentActivity
        implements CategoryListFragment.OnCategoryListInteractionListener,
                   JokeListFragment.OnJokeListInteractionListener,
                   FavoriteListFragment.OnFavoriteListInteractionListener
//        , FragmentManager.OnBackStackChangedListener
{
    private static final String _TAG = MainActivity.class.getSimpleName();


    /**
     * Request codes.
     */
    public static final int REQUEST_LICENSE_CONFIRMATION = 1;

    /**
     * Fields name constants.
     */
    public static final String FIELD_IS_LICENSE_CONFIRMED = "isLicenseConfirmed";


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
        db = DBHelper.getWritableDatabase(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Listen for changes in the back stack
//        getSupportFragmentManager().addOnBackStackChangedListener(this);
//        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                Log.d(_TAG, "onBackStackChanged");
//                MainActivity.this.invalidateOptionsMenu();
//            }
//        });
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
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.navbar_shadow);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentFragment, new CategoryListFragment())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check is user confirmed application license.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean confirmLicense = prefs.getBoolean(FIELD_IS_LICENSE_CONFIRMED, false);
        if (!confirmLicense) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivityForResult(intent, REQUEST_LICENSE_CONFIRMATION);
        }
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LICENSE_CONFIRMATION:
                if (resultCode == RESULT_OK) {
                    Log.d(_TAG, "User agree license, share to preferences.");
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putBoolean(FIELD_IS_LICENSE_CONFIRMED, true);
                    edit.commit();

                    Log.d(_TAG, "Success confirmed license.");
                    Toast.makeText(MainActivity.this, getString(R.string.welcome_message), Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
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

            case R.id.mmItemFavorites:
                ArrayList<Favorite> favorites = DBHelper.getFavoriteList(db);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentFragment, FavoriteListFragment.newInstance(favorites))
                        .addToBackStack(null)
                        .commit();
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
                .addToBackStack(JokeFragment.TAG)
                .commit();
    }

    @Override
    public void onJokeListItemBtnFavoriteClick(Joke joke, boolean isInFavorites,
                                               ArrayList<Joke> jokesList, JokeListAdapter adapter)
    {
        // Remove from favorites
        if (isInFavorites) {
            Favorite favorite = DBHelper.getFavoriteByJokeId(db, joke.id);
            if (favorite != null) {
                if (DBHelper.deleteFavorite(db, favorite.id)) {
                    Log.d(_TAG, "Success deleted favorite with ID: " + favorite.id);
                } else {
                    Log.e(_TAG, "Error delete favorite with ID: " + favorite.id);
                }
            } else {
                Log.e(_TAG, "Can\'t find favorite row in DB with Joke ID: " + joke.id);
            }
        }
        // Add to favorites
        else {
            Favorite favorite = new Favorite(0, joke.id);
            if (DBHelper.addFavorite(db, favorite)) {
                Log.d(_TAG, "Success added favorite row to DB for Joke #" + joke.id);
            } else {
                Log.e(_TAG, "Error add favorite row to DB for Joke #" + joke.id);
            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFavoriteListItemSelect(Favorite favorite, ArrayList<Favorite> favorites) {
        Log.d(_TAG, "Favorite item selected with ID: " + favorite.id);

        Joke joke = favorite.getJoke();
        ArrayList<Joke> favoriteJokesList = DBHelper.getJokeListFromFavoritesList(favorites);
        int currentJokeIndex = favoriteJokesList.indexOf(joke);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentFragment, JokeFragment.newInstance(joke, favoriteJokesList, currentJokeIndex))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onFavoriteListItemRemoveClick(Favorite favorite, ArrayList<Favorite> favoriteList, FavoriteListAdapter adapter) {
        Log.d(_TAG, "Delete request for favorite item with ID: " + favorite.id);

        if (DBHelper.deleteFavorite(db, favorite.id)) {
            Log.d(_TAG, "Success delete from favorites.");

            favoriteList.remove(favorite);
            adapter.notifyDataSetChanged();
        } else {
            Log.e(_TAG, "Error delete from favorites.");
            Toast.makeText(MainActivity.this, getString(R.string.error_delete_from_favorites), Toast.LENGTH_LONG).show();
        }
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
