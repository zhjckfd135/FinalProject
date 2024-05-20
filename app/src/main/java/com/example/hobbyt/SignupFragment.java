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
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SignupFragment extends Fragment {

    private View view;
    private Button signupButton;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPasswordConfirm;
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
        view = initFields(inflater, container);
        setListeners();

        return view;
    }

    private void setListeners() {
        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { setLoginFragment(); }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { registerNewUser(); }
        });
    }

    private View initFields(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        backText = (TextView) view.findViewById(R.id.textBackSignup);
        signupButton = (Button) view.findViewById(R.id.buttonSignup);
        editTextFirstName = (EditText) view.findViewById(R.id.editTextFirstNameSignup);
        editTextLastName = (EditText) view.findViewById(R.id.editTextLastNameSignup);
        editTextEmail = (EditText) view.findViewById(R.id.editTextEmailSignup);
        editTextPassword = (EditText) view.findViewById(R.id.editTextPasswordSignup);
        editTextPasswordConfirm = (EditText) view.findViewById(R.id.editTextConfirmPasswordSignup);

        return view;
    }

    public void registerNewUser(){
        if(!checkFields()){
            Toast.makeText(getActivity(), "Please Complete all Fields", Toast.LENGTH_LONG).show();
            return;
        }
        if(!checkConfirmPassword()){
            Toast.makeText(getActivity(), "Please confirm the password is correct", Toast.LENGTH_LONG).show();
            return;
        }
        

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = UrlBase.REGISTER_NEW_USER;

        signupButton.setText("Loading...");
        signupButton.setClickable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase("success")){
                    Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_LONG).show();
                    setLoginFragment();
                }
                if(response.equalsIgnoreCase("This email is busy")){
                    Toast.makeText(getActivity(), "This email is busy", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                signupButton.setText("Sign Up");
                signupButton.setClickable(true);
                String response;
                try {
                     response = new String(error.networkResponse.data,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    response = "UnsupportedEncodingException";
                    Log.e("SignIn", e.toString());
                }
                Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                Log.w("SignUp" ,error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("first_name", editTextFirstName.getText().toString());
                params.put("last_name", editTextLastName.getText().toString());
                params.put("email", editTextEmail.getText().toString());
                params.put("password", editTextPassword.getText().toString());
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private boolean checkFields(){
        return !(editTextFirstName.getText().toString().isEmpty() ||
                editTextLastName.getText().toString().isEmpty() ||
                editTextEmail.getText().toString().isEmpty() ||
                editTextPassword.getText().toString().isEmpty() ||
                editTextPasswordConfirm.getText().toString().isEmpty());
    }

    private boolean checkConfirmPassword(){
        return editTextPasswordConfirm.getText().toString().equals(editTextPassword.getText().toString());
    }

    private void setLoginFragment(){
        AuthorizationActivity authorizationActivity = (AuthorizationActivity)getActivity();
        authorizationActivity.setFragment(new LoginFragment());
    }
}