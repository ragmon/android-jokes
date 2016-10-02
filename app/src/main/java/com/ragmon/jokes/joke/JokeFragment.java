package com.ragmon.jokes.joke;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockFragment;
import com.ragmon.jokes.OnSwipeTouchListener;
import com.ragmon.jokes.R;

import java.util.ArrayList;


public class JokeFragment extends SherlockFragment {
    private static final String _TAG = JokeFragment.class.getCanonicalName();


    private Joke joke;
    private ArrayList<Joke> jokesList;
    private int currentJokeIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.joke_fragment, container, false);
        view.findViewById(R.id.webView).setOnTouchListener(new OnSwipeTouchListener(getSherlockActivity()) {
            @Override
            public void onSwipeRight() {
                Log.d(_TAG, "-- onSwipeRight --");

                int nextJokeIndex;
                if (currentJokeIndex > 0) {
                    nextJokeIndex = currentJokeIndex - 1;
                } else {
                    nextJokeIndex = jokesList.size() - 1;
                }
                Joke nextJoke = jokesList.get(nextJokeIndex);

                Log.d(_TAG, "Next joke index #" + nextJokeIndex);

                getSherlockActivity().getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.contentFragment, JokeFragment.newInstance(nextJoke, jokesList, nextJokeIndex))
                        .commit();
            }

            @Override
            public void onSwipeLeft() {
                Log.d(_TAG, "-- onSwipeLeft --");

                int nextJokeIndex;
                if (currentJokeIndex < jokesList.size() - 1) {
                    nextJokeIndex = currentJokeIndex + 1;
                } else {
                    nextJokeIndex = 0;
                }
                Joke nextJoke = jokesList.get(nextJokeIndex);

                Log.d(_TAG, "Next joke index #" + nextJokeIndex);

                getSherlockActivity().getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.contentFragment, JokeFragment.newInstance(nextJoke, jokesList, nextJokeIndex))
                        .commit();
            }

            @Override
            public void onSwipeTop() {
                Log.d(_TAG, "-- onSwipeTop --");
            }

            @Override
            public void onSwipeBottom() {
                Log.d(_TAG, "-- onSwipeBottom --");
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        WebView webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setDefaultTextEncodingName("utf-8");  // maybe this fix bag with charset on android 2.3
        webView.loadDataWithBaseURL("file:///android_asset/", joke.content, "text/html; charset=utf-8", "utf-8", null);
    }

    public static JokeFragment newInstance(Joke joke, ArrayList<Joke> categoryJokesList, int currentJokeIndex) {
        JokeFragment jokeFragment = new JokeFragment();
        jokeFragment.joke = joke;
        jokeFragment.jokesList = categoryJokesList;
        jokeFragment.currentJokeIndex = currentJokeIndex;

        return jokeFragment;
    }

}
