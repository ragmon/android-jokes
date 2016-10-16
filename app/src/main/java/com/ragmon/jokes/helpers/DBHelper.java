package com.ragmon.jokes.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ragmon.jokes.category.Category;
import com.ragmon.jokes.favorite.Favorite;
import com.ragmon.jokes.joke.Joke;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {
    private static final String _TAG = DBHelper.class.getSimpleName();


    public static final String DB_NAME = "jokes.db";


    /******************************************************************************
     * !!! CHANGE THIS VERSION FOR APPLICATION UPGRADE !!!
     * P.S. And don't forget for "assets/upgrade-v{VERSION}.sql" file.
     * Other previous versions sql files also must be exists from zero to current version number.
     ******************************************************************************/
    public static final int VERSION = 1;


    /**
     * Table names constants.
     */
    public static final String TABLE_JOKES = "joke";
    public static final String TABLE_FAVORITE = "favorite";
    public static final String TABLE_CATEGORY = "category";


    private Context context;
    private static SQLiteDatabase current;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(_TAG, "-- onCreate database --");
        try {
            String sql = loadSqlDump(0);                            // "0" - it's an inital version of DB
            sqLiteDatabase.beginTransaction();

            Log.d(_TAG, "Start import sql: " + sql);
            execMultilineSql(sqLiteDatabase, sql);
            onUpgrade(sqLiteDatabase, 0, VERSION);                  // process next DB migrations

            sqLiteDatabase.setTransactionSuccessful();

        } catch (IOException e) {
            Log.d(_TAG, "Can\'t import the database inital structure.", e);
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {
            for (int version = i + 1;                 // start from next version, not current
                 version <= i1; version++)
            {
                String sql = loadSqlDump(version);

                Log.d(_TAG, "Import sql: " + sql);
                execMultilineSql(sqLiteDatabase, sql);
            }
        } catch (IOException e) {
            Log.d(_TAG, "Can\'t success upgrade the database to VERSION #" + VERSION, e);
        }
    }

    /**
     * Load Sql code from dump file by version.
     *
     * @param version Sql dump file version.
     * @return String
     * @throws IOException
     */
    protected String loadSqlDump(int version) throws IOException {
        Log.d(_TAG, "Load Sql dump for VERSION #" + version);

        StringBuilder sb = new StringBuilder();
        InputStream in = context.getAssets().open("upgrade-v" + version + ".sql");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();

        return sb.toString();
    }

    protected static void execMultilineSql(SQLiteDatabase db, String sqlLines) {
        if (sqlLines != null) {
            String[] lines = sqlLines.split(";");
            for (String line: lines) {
                if (!line.isEmpty()) {
                    Log.d(_TAG, "Exec line sql: " + line);
                    db.execSQL(line);
                }
            }
        }
    }

    public static SQLiteDatabase getReadableDatabase(Context context) {
        return current = (new DBHelper(context)).getReadableDatabase();
    }

    public static SQLiteDatabase getWritableDatabase(Context context) {
        return current = (new DBHelper(context)).getWritableDatabase();
    }

    public static ArrayList<Category> getCategoryList(SQLiteDatabase db) {
        return getCategoryList(db, 0);
    }

    public static SQLiteDatabase getCurrentDB() {
        return current;
    }

    public static ArrayList<Category> getCategoryList(SQLiteDatabase db, int parentId) {
        Cursor cur = db.rawQuery("select * from category where parent_id = ?",
                new String[]{ String.valueOf(parentId) });

        ArrayList<Category> categories = new ArrayList<Category>();
        if (cur != null) {
            int colId = cur.getColumnIndex("id");
            int colTitle = cur.getColumnIndex("title");
            int colIcon = cur.getColumnIndex("icon");

            cur.moveToFirst();

            // iterate rows
            for (int i = 0; i < cur.getCount(); i++) {
                int id = cur.getInt(colId);
                String title = cur.getString(colTitle);
                String icon = cur.getString(colIcon);

                Category category = new Category(id, title, parentId, icon);
                categories.add(category);

                cur.moveToNext();
            }

            cur.close();
        }

        return categories;
    }

    public static ArrayList<Joke> getJokeList(SQLiteDatabase db, int categoryId) {
        Cursor cur = db.rawQuery("select * from joke where category_id = ?",
                new String[] { String.valueOf(categoryId) });

        ArrayList<Joke> jokes = new ArrayList<Joke>();
        if (cur != null) {
            int colId = cur.getColumnIndex("id");
            int colTitle = cur.getColumnIndex("title");
            int colContent = cur.getColumnIndex("content");
            int colIsViewed = cur.getColumnIndex("is_viewed");
            int colContentType = cur.getColumnIndex("content_type");

            cur.moveToFirst();

            // iterate rows
            for (int i = 0; i < cur.getCount(); i++) {
                int id = cur.getInt(colId);
                String title = cur.getString(colTitle);
                String content = cur.getString(colContent);
                boolean isViewed = cur.getInt(colIsViewed) != 0;
                String contentType = cur.getString(colContentType);

                Joke joke = new Joke(id, title, categoryId, content, isViewed, contentType);
                jokes.add(joke);

                cur.moveToNext();
            }

            cur.close();
        }

        return jokes;
    }

    public static Joke getJoke(SQLiteDatabase db, int id) {
        Cursor cur = db.rawQuery("select * from joke where id = ? limit 1",
                new String[] { String.valueOf(id) });

        Joke joke = null;
        if (cur != null) {
            if (cur.moveToFirst()) {
                int colTitle = cur.getColumnIndex("title");
                int colCategoryId = cur.getColumnIndex("category_id");
                int colContent = cur.getColumnIndex("content");
                int colIsViewed = cur.getColumnIndex("is_viewed");
                int colContentType = cur.getColumnIndex("content_type");

                String title = cur.getString(colTitle);
                int categoryId = cur.getInt(colCategoryId);
                String content = cur.getString(colContent);
                boolean isViewed = cur.getInt(colIsViewed) != 0;
                String contentType = cur.getString(colContentType);

                joke = new Joke(id, title, categoryId, content, isViewed, contentType);
            }
        }

        return joke;
    }

    public static boolean addFavorite(SQLiteDatabase db, Favorite favorite) {
        ContentValues v = new ContentValues();
        v.put("joke_id", favorite.jokeId);

        return db.insert("favorite", null, v) > 0;
    }

    public static ArrayList<Favorite> getFavoriteList(SQLiteDatabase db) {
        Cursor cur = db.rawQuery("select * from favorite", null);

        ArrayList<Favorite> favorites = new ArrayList<Favorite>();
        if (cur != null) {
            int colId = cur.getColumnIndex("id");
            int colJokeId = cur.getColumnIndex("joke_id");

            cur.moveToFirst();

            // iterate rows
            for (int i = 0; i < cur.getCount(); i++) {
                int id = cur.getInt(colId);
                int jokeId = cur.getInt(colJokeId);

                Favorite favorite = new Favorite(id, jokeId);
                favorites.add(favorite);

                cur.moveToNext();
            }

            cur.close();
        }

        return favorites;
    }

    public static Favorite getFavorite(SQLiteDatabase db, int id) {
        Cursor cur = db.rawQuery("select * from favorite where id = ? limit 1",
                new String[] { String.valueOf(id) });

        Favorite favorite = null;
        if (cur != null) {
            if (cur.moveToFirst()) {
                int colJokeId = cur.getColumnIndex("joke_id");

                int jokeId = cur.getInt(colJokeId);

                favorite = new Favorite(id, jokeId);
            }
        }

        return favorite;
    }

    public static Favorite getFavoriteByJokeId(SQLiteDatabase db, int jokeId) {
        Cursor cur = db.rawQuery("select * from favorite where joke_id = ? limit 1",
                new String[] { String.valueOf(jokeId) });

        Favorite favorite = null;
        if (cur != null) {
            if (cur.moveToFirst()) {
                int colId = cur.getColumnIndex("id");

                int id = cur.getInt(colId);

                favorite = new Favorite(id, jokeId);
            }
        }

        return favorite;
    }

    public static boolean deleteFavorite(SQLiteDatabase db, int id) {
        return db.delete("favorite", "id=?", new String[]{ String.valueOf(id) }) > 0;
    }

    public static ArrayList<Joke> getJokeListFromFavoritesList(ArrayList<Favorite> favorites) {
        ArrayList<Joke> jokes = new ArrayList<Joke>();
        for (Favorite favorite: favorites) {
            jokes.add(favorite.getJoke());
        }

        return jokes;
    }

    public static boolean setJokeViewedStatus(SQLiteDatabase db, Joke joke, boolean isViewed) {
        ContentValues v = new ContentValues();
        v.put("is_viewed", isViewed);

        return db.update(TABLE_JOKES, v, "id=?", new String[]{ String.valueOf(joke.id) }) > 0;
    }


    /**
     * DatabaseManager helper method.
     * Using only in DEBUG_MODE.
     *
     * @param Query
     * @return
     */
    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }

}
