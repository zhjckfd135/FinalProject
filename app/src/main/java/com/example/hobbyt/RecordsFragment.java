package com.example.hobbyt;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.hobbyt.modules.Record;
import com.example.hobbyt.utils.UrlMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecordsFragment extends Fragment {
    ArrayList<Record> records = new ArrayList<>();
    RecyclerView recyclerView;
    ProgressBar progressBar;
    SharedPreferences loginSharedPreferences;

    public RecordsFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();

        getRecords();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginSharedPreferences = getActivity().getSharedPreferences(LoginFragment.LOGIN_PREFERENCES, Context.MODE_PRIVATE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_records, container, false);
        recyclerView = v.findViewById(R.id.recordsRecyclerView);
        progressBar = v.findViewById(R.id.progressBarRecords);

        return v;
    }

    private void getRecords(){
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String urlBase = UrlBase.GET_RECORD;

        UrlMap urlMap = new UrlMap(urlBase);
        urlMap.put("user_id", loginSharedPreferences.getString(LoginFragment.USER_ID_PREFERENCES, ""));
        urlMap.put("token", loginSharedPreferences.getString(LoginFragment.TOKEN_PREFERENCES, ""));

        queue.add(getRequestJsonRecords(urlMap.generateUrl()));
    }

    private @NonNull JsonArrayRequest getRequestJsonRecords(String url) {
        return new JsonArrayRequest(url, new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response){
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);

                        Record record = new Record();
                        record.setId(object.getInt("record_id"));
                        record.setData(object.getString("data"));
                        record.setTitle(object.getString("title"));

                        records.add(record);
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
        RecordRecyclerViewAdapter adapter = new RecordRecyclerViewAdapter(getContext(), records);
        recyclerView.setAdapter(adapter);
    }
}