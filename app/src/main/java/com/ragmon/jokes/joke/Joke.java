package com.ragmon.jokes.joke;

import com.ragmon.jokes.helpers.DBHelper;
import com.ragmon.jokes.favorite.Favorite;

import java.io.Serializable;

public class Joke implements Serializable {

    public int id;
    public String title;
    public int categoryId;
    public String content;
    public boolean isViewed;
    public String contentType;

    public Joke(int id, String title, int categoryId, String content, boolean isViewed, String contentType) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.content = content;
        this.isViewed = isViewed;
        this.contentType = contentType;
    }

    public Favorite getFavorite() {
        return DBHelper.getFavoriteByJokeId(DBHelper.getCurrentDB(), id);
    }

}
