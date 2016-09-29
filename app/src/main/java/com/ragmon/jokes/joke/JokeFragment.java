package com.ragmon.jokes.joke;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockFragment;
import com.ragmon.jokes.R;


public class JokeFragment extends SherlockFragment {

    private Joke joke;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.joke_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        WebView webView = (WebView) view.findViewById(R.id.webView);
        webView.loadData(joke.content, "text/html; charset=utf-8", "utf-8");
    }

    public static JokeFragment newInstance(Joke joke) {
        JokeFragment jokeFragment = new JokeFragment();
        jokeFragment.joke = joke;

        return jokeFragment;
    }

}
