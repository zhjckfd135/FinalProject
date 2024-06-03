package com.example.hobbyt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hobbyt.databinding.ActivityMainBinding;
import com.example.hobbyt.modules.User;
import com.example.hobbyt.utils.UrlMap;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPreferences loginSharedPreferences;

    private void GoToActivity(Class<?> c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavigationViewListener();
        binding.addPostButton.setOnClickListener(v -> {
            GoToActivity(EditorActivity.class);
        });

        loginSharedPreferences = getSharedPreferences(LoginFragment.LOGIN_PREFERENCES, Context.MODE_PRIVATE);

        if(!loginSharedPreferences.contains(LoginFragment.USER_ID_PREFERENCES)) {
            GoToActivity(AuthorizationActivity.class);
        }
        loginTokenUser();
    }

    private void NavigationViewListener() {
        setFragment(new RecordsFragment());


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.navigation_recommendations){
                setFragment(new RecordsFragment());
            }
            if(item.getItemId() == R.id.navigation_user){
                setFragment(new UserFragment());
            }

            return true;
        });
    }

    public void setFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayoutMain, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void loginTokenUser(){
        RequestQueue queue = Volley.newRequestQueue(this);
        UrlMap urlMap = new UrlMap(UrlBase.LOGIN_TOKEN);
        urlMap.put("id", loginSharedPreferences.getString(LoginFragment.USER_ID_PREFERENCES, ""));
        urlMap.put("token", loginSharedPreferences.getString(LoginFragment.TOKEN_PREFERENCES, ""));

        queue.add(getRequestJsonUser(urlMap.generateUrl()));
    }

    private @NonNull JsonObjectRequest getRequestJsonUser(String url) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    User user = new User(response.getInt("user_id"),
                            response.getString("first_name"),
                            response.getString("last_name"),
                            response.getString("email"),
                            response.getString("password")

                    );
                } catch (JSONException e) {
                    Log.w("Login" ,e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        return jsonRequest;
    }
}