package com.ragmon.jokes.joke;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.ragmon.jokes.R;

import java.util.ArrayList;


public class JokeListFragment extends SherlockFragment {
    private static final String _TAG = JokeListFragment.class.getSimpleName();

    private ArrayList<Joke> jokes;
    private JokeListAdapter adapter;
    OnJokeListInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.joke_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set jokes list view adapter
        ListView listView = (ListView) view.findViewById(R.id.jokeList);
        listView.setAdapter(adapter = new JokeListAdapter(getSherlockActivity(), jokes, itemClickListener, itemFavoriteBtnListener));
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

    /**
     * On joke list item select click.
     */
    View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                Joke joke = (Joke) view.getTag(R.id.TAG_JOKE);
                int currentJokeIndex = (Integer) view.getTag(R.id.TAG_JOKE_INDEX);

                mListener.onJokeListItemSelect(joke, jokes, currentJokeIndex);
            }
        }
    };

    /**
     * On joke list item favorite (add/remove) button click.
     */
    View.OnClickListener itemFavoriteBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                Joke joke = (Joke) view.getTag(R.id.TAG_JOKE);
                boolean isInFavorites = (Boolean) view.getTag(R.id.TAG_FAVORITE_STATUS);

                mListener.onJokeListItemBtnFavoriteClick(joke, isInFavorites, jokes, adapter);
            }
        }
    };

//    public ArrayList<Joke> getJokesList() {
//        return jokes;
//    }

    public static JokeListFragment newInstance(ArrayList<Joke> jokes) {
        JokeListFragment jokeListFragment = new JokeListFragment();
        jokeListFragment.jokes = jokes;

        return jokeListFragment;
    }

    public interface OnJokeListInteractionListener {
        void onJokeListItemSelect(Joke joke, ArrayList<Joke> categoryJokesList, int currentJokeIndex);
        void onJokeListItemBtnFavoriteClick(Joke joke, boolean isInFavorites, ArrayList<Joke> jokesList, JokeListAdapter adapter);
    }

}
