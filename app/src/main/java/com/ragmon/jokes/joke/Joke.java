package com.ragmon.jokes.joke;

import java.io.Serializable;

public class Joke implements Serializable {

    public int id;
    public String title;
    public int categoryId;
    public String content;

    public Joke(int id, String title, int categoryId, String content) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.content = content;
    }

}