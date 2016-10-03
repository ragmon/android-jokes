package com.ragmon.jokes;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.ragmon.jokes.welcome.WelcomeFragment;

public class WelcomeActivity extends SherlockFragmentActivity
        implements WelcomeFragment.OnWelcomeFragmentInteraction
{
    private static final String _TAG = WelcomeActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.welcomeContent, WelcomeFragment.newInstance())
                .commit();
    }

    public void onBtnStartUsing_Click(View view, boolean isConfirmedLicense) {
        if (isConfirmedLicense) {
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(WelcomeActivity.this, getString(R.string.you_must_agree_license), Toast.LENGTH_LONG).show();
        }
    }
}
