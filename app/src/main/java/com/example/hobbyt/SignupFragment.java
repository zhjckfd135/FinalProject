package com.example.hobbyt;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SignupFragment extends Fragment {

    private View view;
    private TextView backText;

    public SignupFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_signup, container, false);
        backText = (TextView) view.findViewById(R.id.textBackSignUp);
        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthorizationActivity authorizationActivity = (AuthorizationActivity)getActivity();
                authorizationActivity.setFragment(new LoginFragment());
            }
        });

        return view;
    }
}