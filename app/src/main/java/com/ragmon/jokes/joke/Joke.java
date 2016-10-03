package com.ragmon.jokes.joke;

import com.ragmon.jokes.DBHelper;
import com.ragmon.jokes.favorite.Favorite;

import java.io.Serializable;

public class Joke implements Serializable {

    public int id;
    public String title;
    public int categoryId;
    public String content;
    public boolean isViewed;

    public Joke(int id, String title, int categoryId, String content, boolean isViewed) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.content = content;
        this.isViewed = isViewed;
    }

    public Favorite getFavorite() {
        return DBHelper.getFavoriteByJokeId(DBHelper.getCurrentDB(), id);
    }

}
