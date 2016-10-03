package com.ragmon.jokes.welcome;


import android.app.Activity;
import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.ragmon.jokes.R;
import com.ragmon.jokes.helpers.ViewHtmlClickable;


public class WelcomeFragment extends SherlockFragment {
    private static final String _TAG = WelcomeFragment.class.getSimpleName();


    /**
     * Link name constants.
     */
    public static final String LINK_VIEW_LICENSE_TEXT = "view_license_text";


    OnWelcomeFragmentInteraction mListener;
    CheckBox agreeLicense;

    public WelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        Log.d(_TAG, getString(R.string.license_agree_label));
        // Set license agree label html with link click events listener.
        TextView licenseAgreeLabel = (TextView) view.findViewById(R.id.licenseAgreeLabel);
        ViewHtmlClickable.setTextViewHTML(licenseAgreeLabel, getString(R.string.license_agree_label),
                textViewLinkClickListener);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(_TAG, "onViewCreated");

        Button btnStartUsingApp = (Button) view.findViewById(R.id.btnStartUsingApp);
        btnStartUsingApp.setOnClickListener(btnStartUsingAppClickListener);

        agreeLicense = (CheckBox) view.findViewById(R.id.agreeLicense);
    }

    /**
     * Button start using application listener.
     */
    View.OnClickListener btnStartUsingAppClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d(_TAG, "Button \"Start using application\" click.");
            if (mListener != null) {
                boolean isConfirmedLicense = agreeLicense.isChecked();
                mListener.onBtnStartUsing_Click(view, isConfirmedLicense);
            }
        }
    };

    /**
     * Text view link click listener.
     */
    ViewHtmlClickable.LinkClickListener textViewLinkClickListener = new ViewHtmlClickable.LinkClickListener() {
        public void onClick(View view, URLSpan span) {
            String url = span.getURL();
            Log.d(_TAG, "Click on TextView link with Url: " + url);

            // Show license text.
            if (url.equalsIgnoreCase(LINK_VIEW_LICENSE_TEXT)) {
                getSherlockActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.welcomeContent, LicenseFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnWelcomeFragmentInteraction) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnWelcomeFragmentInteraction");
        }
    }

    public static WelcomeFragment newInstance() {
        WelcomeFragment fragment = new WelcomeFragment();
        return fragment;
    }

    public interface OnWelcomeFragmentInteraction {
        void onBtnStartUsing_Click(View view, boolean isConfirmedLicense);
    }

}
