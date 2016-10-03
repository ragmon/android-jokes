package com.ragmon.jokes.favorite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ragmon.jokes.R;

import java.util.ArrayList;


public class FavoriteListAdapter extends BaseAdapter {
    private static final String _TAG = FavoriteListAdapter.class.getSimpleName();


    Context context;
    LayoutInflater lInflater;
    ArrayList<Favorite> favorites;
    View.OnClickListener onRemoveListener;

    public FavoriteListAdapter(Context context, ArrayList<Favorite> favorites, View.OnClickListener onRemoveListener) {
        this.context = context;
        this.favorites = favorites;
        this.onRemoveListener = onRemoveListener;
        this.lInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return favorites.size();
    }

    @Override
    public Favorite getItem(int i) {
        return favorites.get(i);
    }

    @Override
    public long getItemId(int i) {
        return favorites.get(i).id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = lInflater.inflate(R.layout.favorite_list_item, viewGroup, false);

            Favorite favorite = getItem(i);

            TextView favoriteTitle = (TextView) view.findViewById(R.id.favoriteItemTitle);
            ImageView favoriteRemoveBtn = (ImageView) view.findViewById(R.id.favoriteItemRemove);

            // Fill view
            favoriteTitle.setText(favorite.getJoke().title);
            favoriteRemoveBtn.setImageResource(R.drawable.heart_active);
            favoriteRemoveBtn.setTag(R.id.TAG_FAVORITE, favorite);
            favoriteRemoveBtn.setTag(R.id.TAG_FAVORITE_LIST, favorites);
            favoriteRemoveBtn.setTag(R.id.TAG_ADAPTER, FavoriteListAdapter.this);
            favoriteRemoveBtn.setOnClickListener(onRemoveListener);
        }

        return view;
    }
}
