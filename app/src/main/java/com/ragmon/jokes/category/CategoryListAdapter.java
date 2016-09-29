package com.ragmon.jokes.category;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ragmon.jokes.R;

import java.util.ArrayList;


public class CategoryListAdapter extends BaseAdapter {
    private static final String _TAG = CategoryListAdapter.class.getSimpleName();


    Context context;
    LayoutInflater lInflater;
    ArrayList<Category> categories;

    public CategoryListAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
//        this.lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); // maybe error here?
        this.lInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Category getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return categories.get(i).id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = lInflater.inflate(R.layout.category_list_item, viewGroup, false);

            Category category = getItem(i);

            TextView categoryTitle = (TextView) view.findViewById(R.id.categoryItemTitle);

            // Fill view
            categoryTitle.setText(category.title);
        }

        return view;
    }
}
