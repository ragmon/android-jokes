package com.ragmon.jokes.category;

public class Category {

    public int id;
    public String title;
    public int parentId;
    public String icon;

    public Category(int id, String title, int parentId, String icon) {
        this.id = id;
        this.title = title;
        this.parentId = parentId;
        this.icon = icon;
    }

}
