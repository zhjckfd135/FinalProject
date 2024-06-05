package com.example.hobbyt;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.UserView> {
    public interface ServerCallbackRecordList{
        void onSuccess(Record record);
    }

    List<User> users;
    private final LayoutInflater inflater;
    SharedPreferences loginSharedPreferences;

    public UserRecyclerViewAdapter(Context context, List<User> users) {
        this.users = users;
        this.inflater = LayoutInflater.from(context);
        loginSharedPreferences = inflater.getContext().getSharedPreferences(LoginFragment.LOGIN_PREFERENCES, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public UserView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.user_element, parent, false);
        return new UserView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserView holder, int position) {
        User user = users.get(position);
        holder.firstAndLastName.setText(user.getFirst_name() + " " + user.getLast_name());
        holder.sendRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Record> records = new ArrayList<>();
                AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());
                View vDialogue = inflater.inflate(R.layout.add_primary_dialogue, null);

                builder.setTitle("Add Primary");
                builder.setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.cancel();
                });

                builder.setView(vDialogue);
                AlertDialog dialog = builder.create();
                dialog.show();




                RecyclerView recyclerView = vDialogue.findViewById(R.id.dialogueRecordElementRecyclerView);
                DialogueRecordRecyclerViewAdapter adapter = new DialogueRecordRecyclerViewAdapter(v.getContext(), records, user.getUser_id(), dialog);
                recyclerView.setAdapter(adapter);

                getJsonArrayRequest(new ServerCallbackRecordList(){
                    @Override
                    public void onSuccess(Record response){
                        records.add(response);

                        RecyclerView recyclerView = vDialogue.findViewById(R.id.dialogueRecordElementRecyclerView);
                        DialogueRecordRecyclerViewAdapter adapter = new DialogueRecordRecyclerViewAdapter(v.getContext(), records, user.getUser_id(),dialog);
                        recyclerView.setAdapter(adapter);
                    }
                });

            }
        });
    }

    private  void getJsonArrayRequest(ServerCallbackRecordList callback) {
        RequestQueue queue = Volley.newRequestQueue(inflater.getContext());
        String urlBase = UrlBase.GET_RECORD;

        UrlMap urlMap = new UrlMap(urlBase);
        urlMap.put("user_id", loginSharedPreferences.getString(LoginFragment.USER_ID_PREFERENCES, ""));
        urlMap.put("token", loginSharedPreferences.getString(LoginFragment.TOKEN_PREFERENCES, ""));

        JsonArrayRequest request = new JsonArrayRequest(urlMap.generateUrl(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);

                        Record record = new Record();

                        record.setData(object.getString("data"));
                        record.setTitle(object.getString("title"));
                        record.setId(object.getInt("record_id"));

                        callback.onSuccess(record);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserView extends RecyclerView.ViewHolder{
        TextView firstAndLastName;
        ImageButton sendRecord;

        public UserView(@NonNull View itemView) {
            super(itemView);

            firstAndLastName = itemView.findViewById(R.id.userFirstAndLastNameElement);
            sendRecord = itemView.findViewById(R.id.imageButtonSendRecord);


        }
    }
}
