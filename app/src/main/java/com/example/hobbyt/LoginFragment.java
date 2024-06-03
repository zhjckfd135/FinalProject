package com.example.hobbyt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hobbyt.modules.User;
import com.example.hobbyt.utils.UrlMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {
    public static final String LOGIN_PREFERENCES = "login";
    public static final String USER_ID_PREFERENCES = "user_id";
    public static final String TOKEN_PREFERENCES = "token";
    public static final String USER_FIRST_NAME = "first_name";
    public static final String USER_LAST_NAME = "last_name";

    private View view;
    private TextView signupText;
    private EditText emailEdit;
    private EditText passwordEdit;
    private Button loginButton;

    private SharedPreferences sharedPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE);

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

    private void loginUser(){
        if(!checkFields()){
            Toast.makeText(getActivity(), "Please Complete all Fields", Toast.LENGTH_LONG).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        UrlMap urlMap = new UrlMap(UrlBase.LOGIN_USER);
        urlMap.put("email", emailEdit.getText().toString());
        urlMap.put("password", passwordEdit.getText().toString());

        loginButton.setText("Loading...");
        loginButton.setClickable(false);

        queue.add(getRequestTokenUser(urlMap.generateUrl()));
    }

    private void loginTokenUser(){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        UrlMap urlMap = new UrlMap(UrlBase.LOGIN_TOKEN);
        urlMap.put("id", sharedPreferences.getString(USER_ID_PREFERENCES, ""));
        urlMap.put("token", sharedPreferences.getString(TOKEN_PREFERENCES, ""));

        queue.add(getRequestJsonUser(urlMap.generateUrl()));
    }

    private StringRequest getRequestTokenUser(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.isEmpty()) errorLogin("No response");

                String[] data = response.split("\n");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TOKEN_PREFERENCES, data[0]);
                editor.putString(USER_ID_PREFERENCES, data[1]);
                editor.apply();

                loginTokenUser();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorResponse(error);
            }
        });

        return stringRequest;
    }

    private @NonNull JsonObjectRequest getRequestJsonUser(String url) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response == null) errorLogin("No response");

                try {

                    User user = new User(response.getInt("user_id"),
                                        response.getString("first_name"),
                                        response.getString("last_name"),
                                        response.getString("email")
                                            );

                    GoToMain(user);

                } catch (JSONException e) {
                    errorLogin("Error");
                    Log.w("Login" ,e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorResponse(error);
            }
        });
        return jsonRequest;
    }

    private void GoToMain(User user) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void errorResponse(VolleyError error) {
        String response;
        try {
            response = new String(error.networkResponse.data,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            response = "UnsupportedEncodingException";
            Log.e("SignIn", e.toString());
        }
        errorLogin(response);
        Log.w("Login" , error.toString());
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