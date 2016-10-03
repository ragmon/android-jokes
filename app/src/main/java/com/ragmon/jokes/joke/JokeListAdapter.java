package com.ragmon.jokes.joke;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ragmon.jokes.R;

import java.util.ArrayList;


public class JokeListAdapter extends BaseAdapter {
    private static final String _TAG = JokeListAdapter.class.getSimpleName();


    Context context;
    LayoutInflater lInflater;
    ArrayList<Joke> jokes;
    View.OnClickListener onSelectListener;
    View.OnClickListener onFavoriteListener;


    public JokeListAdapter(Context context, ArrayList<Joke> jokes,
                           View.OnClickListener onSelectListener,
                           View.OnClickListener onFavoriteListener
    ) {
        this.context = context;
        this.jokes = jokes;
        this.onSelectListener = onSelectListener;
        this.onFavoriteListener = onFavoriteListener;
        this.lInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return jokes.size();
    }

    @Override
    public Joke getItem(int i) {
        return jokes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return jokes.get(i).id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Joke joke = getItem(i);
        boolean isInFavorites = joke.getFavorite() != null;

        if (view == null) {
            view = lInflater.inflate(R.layout.joke_list_item, viewGroup, false);

            LinearLayout infoLayout = (LinearLayout) view.findViewById(R.id.jokeItemInfoLayout);
            TextView title = (TextView) view.findViewById(R.id.jokeItemTitle);
            ImageView favoriteBtn = (ImageView) view.findViewById(R.id.jokeItemBtnFavorite);

            // Fill view
            title.setText(joke.title);

            infoLayout.setTag(R.id.TAG_JOKE, joke);
            infoLayout.setTag(R.id.TAG_JOKE_INDEX, i);
            infoLayout.setOnClickListener(onSelectListener);

            favoriteBtn.setTag(R.id.TAG_JOKE, joke);
            favoriteBtn.setOnClickListener(onFavoriteListener);
        }

        TextView isViewed = (TextView) view.findViewById(R.id.jokeItemIsViewed);
        ImageView favoriteBtn = (ImageView) view.findViewById(R.id.jokeItemBtnFavorite);

        isViewed.setText(joke.isViewed
                ? context.getString(R.string.joke_is_viewed)
                : context.getString(R.string.joke_is_not_viewed));

        favoriteBtn.setTag(R.id.TAG_FAVORITE_STATUS, isInFavorites);
        favoriteBtn.setImageResource(isInFavorites
                ? R.drawable.heart_active
                : R.drawable.heart_inactive);

        return view;
    }
}
