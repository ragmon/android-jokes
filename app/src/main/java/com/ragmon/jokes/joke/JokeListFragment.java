package com.ragmon.jokes.joke;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.ragmon.jokes.R;

import java.util.ArrayList;


public class JokeListFragment extends SherlockFragment {
    private static final String _TAG = JokeListFragment.class.getSimpleName();

    private ArrayList<Joke> jokes;
    OnJokeListInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.joke_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set jokes list view adapter
        ListView listView = (ListView) view.findViewById(R.id.jokeList);
        listView.setAdapter(new JokeListAdapter(getSherlockActivity(), jokes));
        listView.setOnItemClickListener(itemClickListener);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnJokeListInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnJokeListInteractionListener");
        }
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (mListener != null) {
                Joke joke = (Joke) adapterView.getItemAtPosition(i);
                mListener.onJokeListItemSelect(joke, jokes, i);
            }
        }
    };

    public ArrayList<Joke> getJokesList() {
        return jokes;
    }

    public static JokeListFragment newInstance(ArrayList<Joke> jokes) {
        JokeListFragment jokeListFragment = new JokeListFragment();
        jokeListFragment.jokes = jokes;

        return jokeListFragment;
    }

    public interface OnJokeListInteractionListener {
        void onJokeListItemSelect(Joke joke, ArrayList<Joke> categoryJokesList, int currentJokeIndex);
    }

}
