package com.example.hobbyt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class UserFragment extends Fragment {

    TextView textViewFirstName;
    TextView textViewLastName;
    Button userExit;

    SharedPreferences loginSharedPreferences;

    public UserFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginSharedPreferences = getActivity().getSharedPreferences(LoginFragment.LOGIN_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user, container, false);
        textViewFirstName = v.findViewById(R.id.userFirstName);
        textViewLastName = v.findViewById(R.id.userLastName);
        userExit = v.findViewById(R.id.exitUserButton);

        textViewFirstName.setText(loginSharedPreferences.getString(LoginFragment.USER_FIRST_NAME, ""));
        textViewLastName.setText(loginSharedPreferences.getString(LoginFragment.USER_LAST_NAME, ""));
        userExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSharedPreferences.edit().clear().apply();
                GoToAuthorization();
            }
        });
        return v;
    }

    private void GoToAuthorization(){
        Intent intent = new Intent(this.getContext(), AuthorizationActivity.class);
        getActivity().startActivity(intent);
    }
}