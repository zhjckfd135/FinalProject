package com.example.hobbyt;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.hobbyt.modules.Record;
import com.example.hobbyt.modules.User;
import com.example.hobbyt.utils.UrlMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {
    List<User> users = new ArrayList<>();

    TextView textViewFirstName;
    TextView textViewLastName;
    Button userExit;
    RecyclerView recyclerView;
    ProgressBar progressBar;

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
        recyclerView = v.findViewById(R.id.userRecyclerView);
        userExit = v.findViewById(R.id.exitUserButton);
        progressBar = v.findViewById(R.id.progressBarUser);

        textViewFirstName.setText(loginSharedPreferences.getString(LoginFragment.USER_FIRST_NAME, ""));
        textViewLastName.setText(loginSharedPreferences.getString(LoginFragment.USER_LAST_NAME, ""));
        userExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSharedPreferences.edit().clear().apply();
                GoToAuthorization();
            }
        });

        updateRecycleView();
        initUsers();

        return v;
    }

    private void initUsers(){
        RequestQueue queue = Volley.newRequestQueue(getContext());

        queue.add(getRequestJsonUsers(UrlBase.GET_USERS));
    }

    private @NonNull JsonArrayRequest getRequestJsonUsers(String url) {
        return new JsonArrayRequest(url, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response){
                try {
                    int user_id = Integer.parseInt( loginSharedPreferences.getString(LoginFragment.USER_ID_PREFERENCES, "0"));
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);

                        User user = new User(object.getInt("user_id"),
                                            object.getString("first_name"),
                                            object.getString("last_name"),
                                            object.getString("email"));

                        if(user.getUser_id() == user_id)
                            continue;

                        users.add(user);
                    }

                    progressBar.setVisibility(View.INVISIBLE);
                    updateRecycleView();
                }
                catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                Log.w("Login" ,e.toString());
            }
        });
    }

    private void updateRecycleView(){
        UserRecyclerViewAdapter userRecyclerViewAdapter = new UserRecyclerViewAdapter(getContext(), users);
        recyclerView.setAdapter(userRecyclerViewAdapter);
    }

    private void GoToAuthorization(){
        Intent intent = new Intent(this.getContext(), AuthorizationActivity.class);
        getActivity().startActivity(intent);
    }
}