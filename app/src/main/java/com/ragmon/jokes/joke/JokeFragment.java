package com.ragmon.jokes.joke;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.ragmon.jokes.DBHelper;
import com.ragmon.jokes.OnSwipeTouchListener;
import com.ragmon.jokes.R;
import com.ragmon.jokes.favorite.Favorite;

import java.util.ArrayList;


public class JokeFragment extends SherlockFragment {
    private static final String _TAG = JokeFragment.class.getCanonicalName();


    public static final String TAG = "joke";


    private Joke joke;
    private ArrayList<Joke> jokesList;
    private int currentJokeIndex;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

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

                getSherlockActivity().getSupportFragmentManager().popBackStack(TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSherlockActivity().getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.contentFragment, JokeFragment.newInstance(nextJoke, jokesList, nextJokeIndex))
                        .addToBackStack(TAG)
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

                getSherlockActivity().getSupportFragmentManager().popBackStack(TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSherlockActivity().getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.contentFragment, JokeFragment.newInstance(nextJoke, jokesList, nextJokeIndex))
                        .addToBackStack(TAG)
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
        webView.loadDataWithBaseURL("file:///android_asset/", joke.content, "text/html", "UTF-8", null);

        // Set joke status "VIEWED"
        DBHelper.setJokeViewedStatus(DBHelper.getCurrentDB(), joke, true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(_TAG, "Create options menu for joke #" + joke.id);

        MenuItem mItemAddRemoveFavorite = menu.findItem(R.id.mmItemAddRemoveFavorite);
        if (joke.getFavorite() != null) {
            mItemAddRemoveFavorite.setIcon(R.drawable.heart_active);
        } else {
            mItemAddRemoveFavorite.setIcon(R.drawable.heart_inactive);
        }

        menu.setGroupVisible(R.id.mmGroupJoke, true);
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String textContent;
        switch (item.getItemId()) {
            case R.id.mmItemAddRemoveFavorite:
                Log.d(_TAG, "Joke menu item add/remove from favorites click.");

                Favorite favorite = joke.getFavorite();
                if (favorite != null) {
                    if (DBHelper.deleteFavorite(DBHelper.getCurrentDB(), favorite.id)) {
                        Log.d(_TAG, "Success remove joke #" + favorite.jokeId + " from favorites.");
                    } else {
                        Log.e(_TAG, "Error remove joke #" + favorite.jokeId + " from favorites.");
                    }
                } else {
                    favorite = new Favorite(0, joke.id);
                    if (DBHelper.addFavorite(DBHelper.getCurrentDB(), favorite)) {
                        Log.d(_TAG, "Success add joke #" + favorite.jokeId + " to favorites.");
                    } else {
                        Log.e(_TAG, "Error add joke #" + favorite.jokeId + " to favorites.");
                    }
                }
                getSherlockActivity().invalidateOptionsMenu();
                return true;

            case R.id.mmItemShare:
                Log.d(_TAG, "Joke menu item \"Share\" click.");

                textContent = Html.fromHtml(joke.content).toString();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, joke.title);
                intent.putExtra(Intent.EXTRA_TEXT, textContent);
                startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
                return true;

            case R.id.mmItemCopyToClipboard:
                Log.d(_TAG, "Copy joke #" + joke.id + " to clipboard.");

                textContent = Html.fromHtml(joke.content).toString();

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                            getSherlockActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(TAG, textContent);
                    clipboard.setPrimaryClip(clip);
                } else {
                    getSherlockActivity();
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                            getSherlockActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(textContent);
                }

                Toast.makeText(getSherlockActivity(), getString(R.string.text_copied_to_clipboard), Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static JokeFragment newInstance(Joke joke, ArrayList<Joke> categoryJokesList, int currentJokeIndex) {
        JokeFragment jokeFragment = new JokeFragment();
        jokeFragment.joke = joke;
        jokeFragment.jokesList = categoryJokesList;
        jokeFragment.currentJokeIndex = currentJokeIndex;

        return jokeFragment;
    }

}
