package com.ragmon.jokes.joke;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ragmon.jokes.R;

import java.util.ArrayList;


public class JokeListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater lInflater;
    ArrayList<Joke> jokes;

    public JokeListAdapter(Context context, ArrayList<Joke> jokes) {
        this.context = context;
        this.jokes = jokes;
//        this.lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        if (view == null) {
            view = lInflater.inflate(R.layout.joke_list_item, viewGroup, false);

            Joke joke = getItem(i);

            TextView jokeTitle = (TextView) view.findViewById(R.id.jokeItemTitle);

            // Fill view
            jokeTitle.setText(joke.title);
        }

        return view;
    }
}
