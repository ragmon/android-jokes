package com.ragmon.jokes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
     * P.S. And don't forget for "assets/sql/upgrade-v{VERSION}.sql" file.
     ******************************************************************************/
    public static final int VERSION = 1;


    private Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(_TAG, "-- onCreate database --");
        try {
            String sql = loadSqlDump(0);        // "0" - it's an inital version of DB
            sqLiteDatabase.execSQL(sql);
        } catch (IOException e) {
            Log.d(_TAG, "Can\'t import the database inital structure.", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {
            for (int version = i + 1;                 // start from next version, not current
                 i <= i1; version++)
            {
                String sql = loadSqlDump(version);
                sqLiteDatabase.execSQL(sql);
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
        InputStream in = context.getAssets().open("upgrade-v" + version);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();

        return sb.toString();
    }

    public static SQLiteDatabase getReadableDatabase(Context context) {
        return (new DBHelper(context)).getReadableDatabase();
    }

    public static SQLiteDatabase getWritableDatabase(Context context) {
        return (new DBHelper(context)).getWritableDatabase();
    }

    public static ArrayList<Category> getCategoryList(SQLiteDatabase db) {
        return getCategoryList(db, null);
    }

    public static ArrayList<Category> getCategoryList(SQLiteDatabase db, Integer parentId) {
        Cursor cur = db.rawQuery("select * from category where parent_id = ?",
                new String[]{ String.valueOf(parentId) });

        ArrayList<Category> categories = new ArrayList<Category>();
        if (cur != null) {
            int colId = cur.getColumnIndex("id");
            int colTitle = cur.getColumnIndex("title");

            cur.moveToFirst();

            // iterate rows
            for (int i = 0; i < cur.getCount(); i++) {
                int id = cur.getInt(colId);
                String title = cur.getString(colTitle);

                Category category = new Category(id, title, parentId);
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

            cur.moveToFirst();

            // iterate rows
            for (int i = 0; i < cur.getCount(); i++) {
                int id = cur.getInt(colId);
                String title = cur.getString(colTitle);
                String content = cur.getString(colContent);

                Joke joke = new Joke(id, title, categoryId, content);
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

                String title = cur.getString(colTitle);
                int categoryId = cur.getInt(colCategoryId);
                String content = cur.getString(colContent);

                joke = new Joke(id, title, categoryId, content);
            }
        }

        return joke;
    }

}
