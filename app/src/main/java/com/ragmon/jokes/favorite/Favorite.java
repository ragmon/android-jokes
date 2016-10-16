package com.ragmon.jokes.favorite;


import com.ragmon.jokes.helpers.DBHelper;
import com.ragmon.jokes.joke.Joke;

import java.io.Serializable;

public class Favorite implements Serializable {

    public int id;
    public int jokeId;
    private Joke joke;

    public Favorite(int id, int jokeId) {
        this.id = id;
        this.jokeId = jokeId;
    }

    public Joke getJoke() {
        if (joke != null) {
            return joke;
        } else {
            return joke = DBHelper.getJoke(DBHelper.getCurrentDB(), jokeId);
        }
    }

}
