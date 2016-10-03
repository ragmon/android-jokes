package com.ragmon.jokes.favorite;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.ragmon.jokes.R;

import java.util.ArrayList;


public class FavoriteListFragment extends SherlockFragment {
    private static final String _TAG = FavoriteListFragment.class.getSimpleName();

    private ArrayList<Favorite> favorites;
    OnFavoriteListInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favorite_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set favorites list view adapter
        ListView listView = (ListView) view.findViewById(R.id.favoriteList);
        listView.setAdapter(new FavoriteListAdapter(getSherlockActivity(), favorites, itemRemoveClickListener));
        listView.setOnItemClickListener(itemClickListener);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFavoriteListInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFavoriteListInteractionListener");
        }
    }

    /**
     * On favorites list item click.
     */
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (mListener != null) {
                Favorite favorite = (Favorite) adapterView.getItemAtPosition(i);
                mListener.onFavoriteListItemSelect(favorite, favorites);
            }
        }
    };

    /**
     * On favorites list item button remove click.
     */
    View.OnClickListener itemRemoveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                Favorite favorite = (Favorite) view.getTag(R.id.TAG_FAVORITE);
                ArrayList<Favorite> favoriteList = (ArrayList) view.getTag(R.id.TAG_FAVORITE_LIST);
                FavoriteListAdapter adapter = (FavoriteListAdapter) view.getTag(R.id.TAG_ADAPTER);
                mListener.onFavoriteListItemRemoveClick(favorite, favoriteList, adapter);
            }
        }
    };

    public static FavoriteListFragment newInstance(ArrayList<Favorite> favorites) {
        FavoriteListFragment fragment = new FavoriteListFragment();
        fragment.favorites = favorites;

        return fragment;
    }

    public interface OnFavoriteListInteractionListener {
        void onFavoriteListItemSelect(Favorite favorite, ArrayList<Favorite> favorites);
        void onFavoriteListItemRemoveClick(Favorite favorite, ArrayList<Favorite> favoriteList, FavoriteListAdapter adapter);
    }
}
