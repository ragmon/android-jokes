package com.ragmon.jokes.welcome;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockFragment;
import com.ragmon.jokes.R;


public class LicenseFragment extends SherlockFragment {
    private static final String _TAG = LicenseFragment.class.getSimpleName();


    public static final String licenseFilename = "file:///android_asset/license.html";


    public LicenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_license, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        WebView licWebView = (WebView) view.findViewById(R.id.licenseWebView);
        licWebView.loadUrl(licenseFilename);
    }

    public static LicenseFragment newInstance() {
        LicenseFragment fragment = new LicenseFragment();
        return fragment;
    }

}
