package com.example.dell.app5;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AboutUsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AboutUsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutUsFragment extends Fragment {
    TextView textViewVersionInfo;
    private static final String TAG = "Fragment";
    View root;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    public static AboutUsFragment newInstance(String param1, String param2) {
        AboutUsFragment fragment = new AboutUsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater; ViewGroup container;
//        ViewGroup container;
//        View view=getLayoutInflater().inflate(R.layout.fragment_about,container,false);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        Log.e("im","here too");
        root=inflater.inflate(R.layout.fragment_about_us,container,false);
        textViewVersionInfo = (TextView) root.findViewById(R.id.textview_version_info);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getVersionInfo();
        checkAppVersion();

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void checkAppVersion() {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        int currentAppVersionCode = getCurrentAppVersionCode();
        int oldAppVersion = prefs.getInt("pilotzai_app_version", 0);
        if (oldAppVersion < currentAppVersionCode) {
            try {
                if (oldAppVersion > 0)
                    Toast.makeText(getContext(), String.format(Locale.ENGLISH,"App updated from version %d", oldAppVersion), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "App started for the first time", Toast.LENGTH_SHORT).show();
            } finally {
                SharedPreferences.Editor preferencesEditor = prefs.edit();
                preferencesEditor.putInt("pilotzai_app_version", currentAppVersionCode);
                preferencesEditor.apply();
                preferencesEditor.commit();
            }
        }
    }

    private int getCurrentAppVersionCode() {
        int versionCode = -1;
        try {
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    private void getVersionInfo() {
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        textViewVersionInfo.setText(String.format(Locale.ENGLISH,"Version name = %s \nVersion code = %d", versionName, versionCode));

    }
}
