package com.example.hobbyt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hobbyt.modules.Record;
import com.example.hobbyt.utils.UrlMap;

import java.util.List;

public class DialogueRecordRecyclerViewAdapter extends RecyclerView.Adapter<DialogueRecordRecyclerViewAdapter.RecordTitleView>{
    List<Record> records;
    private final int user_id;
    private final AlertDialog dialog;
    private final LayoutInflater inflater;

    public DialogueRecordRecyclerViewAdapter(Context context, List<Record> records, int user_id, AlertDialog dialog){
        this.records = records;
        this.inflater = LayoutInflater.from(context);
        this.user_id = user_id;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public RecordTitleView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.record_title_element, parent, false);
        return new RecordTitleView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordTitleView holder, int position) {
        Record record = records.get(position);
        holder.title.setText(record.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPrimary(position);
                dialog.dismiss();
            }
        });
    }

    private void addPrimary(int pos){
        RequestQueue queue = Volley.newRequestQueue(inflater.getContext());
        UrlMap urlMap = new UrlMap(UrlBase.ADD_PRIMARY_TO_RECORD);
        urlMap.put("user_id", Integer.toString(user_id));
        urlMap.put("record_id", Integer.toString(records.get(pos).getId()));
        urlMap.put("access_level", Integer.toString(2));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlMap.generateUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase("success")){
                    Toast.makeText(inflater.getContext(), "Add Primary Successful", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(inflater.getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    class RecordTitleView extends RecyclerView.ViewHolder {
        final TextView title;

        public RecordTitleView(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.recordTitleElement);
        }
    }
}
