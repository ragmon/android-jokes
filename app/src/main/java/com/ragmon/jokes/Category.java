package com.ragmon.jokes;

public class Category {

    public int id;
    public String title;
    public Integer parentId = null;

    public Category(int id, String title, Integer parentId) {
        this.id = id;
        this.title = title;
        this.parentId = parentId;
    }

}
