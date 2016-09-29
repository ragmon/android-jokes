package com.ragmon.jokes.category;

import android.app.Activity;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.ragmon.jokes.DBHelper;
import com.ragmon.jokes.R;

import java.util.ArrayList;


public class CategoryListFragment extends SherlockFragment {
    private static final String _TAG = CategoryListFragment.class.getSimpleName();

    private ArrayList<Category> categories;
    OnCategoryListInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.category_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // If categories not set, that fragment init as static.
        // Then load root level categories.
        if (categories == null) {
            Log.d(_TAG, "Categories list not set, then load root categories from DB.");
            categories = DBHelper.getCategoryList(DBHelper.getCurrentDB());
        }

        // Set categories list view adapter.
        ListView listView = (ListView) view.findViewById(R.id.categoryList);
        listView.setAdapter(new CategoryListAdapter(getSherlockActivity(), categories));
        listView.setOnItemClickListener(itemClickListener);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnCategoryListInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCategoryListInteractionListener");
        }
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (mListener != null) {
                mListener.onCategoryListItemSelect((Category) adapterView.getItemAtPosition(i));
            }
        }
    };

    public static CategoryListFragment newInstance(ArrayList<Category> categories) {
        CategoryListFragment categoryListFragment = new CategoryListFragment();
        categoryListFragment.categories = categories;

        return categoryListFragment;
    }

    public interface OnCategoryListInteractionListener {
        void onCategoryListItemSelect(Category category);
    }

}
