package com.example.hobbyt;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hobbyt.modules.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {

    private View view;
    private TextView signupText;
    private EditText emailEdit;
    private EditText passwordEdit;
    private Button loginButton;

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

        loginButton.setOnClickListener(new View.OnClickListener(){
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
        loginButton = (Button) view.findViewById(R.id.buttonLogin);

        return view;
    }

    public void loginUser(){
        if(!checkFields()){
            Toast.makeText(getActivity(), "Please Complete all Fields", Toast.LENGTH_LONG).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String url;
        try {
            url = UrlBase.LOGIN_USER + "?email=" + URLEncoder.encode(email, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            errorLogin("Error");
            return;
        }

        loginButton.setText("Loading...");
        loginButton.setClickable(false);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response == null) errorLogin("No response");

                try {

                    User user = new User(response.getInt("user_id"),
                                        response.getString("first_name"),
                                        response.getString("last_name"),
                                        response.getString("email"),
                                        response.getString("password")

                                            );
                    Toast.makeText(getActivity(), user.getFirst_name(), Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    errorLogin("Error");
                    Log.w("Login" ,e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String response;
                try {
                    response = new String(error.networkResponse.data,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    response = "UnsupportedEncodingException";
                    Log.e("SignIn", e.toString());
                }
                errorLogin(response);
                Log.w("Login" ,error.toString());
            }
        });


        queue.add(jsonRequest);
    }

    private void errorLogin(String errorText){
        loginButton.setText("Log In");
        loginButton.setClickable(true);
        Toast.makeText(getActivity(), errorText, Toast.LENGTH_LONG).show();
    }

    private boolean checkFields(){
        return !(emailEdit.getText().toString().isEmpty() ||
                passwordEdit.getText().toString().isEmpty());
    }
}