package com.example.hobbyt;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment {

    private View view;
    private TextView signupText;
    private EditText emailEdit;
    private EditText passwordEdit;
    private Button signupButton;

    public LoginFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initFields(inflater, container);
        setListeners();

        return view;
    }

    private void setListeners(){
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSignupFragment();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void setSignupFragment(){
        AuthorizationActivity authorizationActivity = (AuthorizationActivity)getActivity();
        authorizationActivity.setFragment(new SignupFragment());
    }

    private View initFields(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        signupText = (TextView) view.findViewById(R.id.textRegisterLogin);
        emailEdit = (EditText) view.findViewById(R.id.editTextEmailLogin);
        passwordEdit = (EditText) view.findViewById(R.id.editTextPasswordLogin);
        signupButton = (Button) view.findViewById(R.id.buttonSignup);

        return view;
    }

    public void loginUser(){
        if(!checkFields()){
            Toast.makeText(getActivity(), "Please Complete all Fields", Toast.LENGTH_LONG).show();
            return;
        }


    }

    private boolean checkFields(){
        return !(emailEdit.getText().toString().isEmpty() ||
                passwordEdit.getText().toString().isEmpty());
    }
}