package com.ragmon.jokes.category;

public class Category {

    public int id;
    public String title;
    public int parentId;

    public Category(int id, String title, int parentId) {
        this.id = id;
        this.title = title;
        this.parentId = parentId;
    }

}
