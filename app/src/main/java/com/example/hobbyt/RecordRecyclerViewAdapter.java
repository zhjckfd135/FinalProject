package com.example.hobbyt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

public class RecordRecyclerViewAdapter extends RecyclerView.Adapter<RecordRecyclerViewAdapter.RecordView> {
    private List<Record> recordList;
    private final LayoutInflater inflater;

    public RecordRecyclerViewAdapter(Context context, List<Record> records) {
        this.recordList = records;
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public RecordView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.record_element, parent, false);
        return new RecordView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordView holder, int position) {
        Record record = recordList.get(position);
        holder.data.setText(record.getData());
        holder.title.setText(record.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditor(v, record);
            }
        });

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDeleteDialog(position).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    class RecordView extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView data;
        final ImageButton imageButton;

        public RecordView(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleTextElement);
            data = itemView.findViewById(R.id.dataTextElement);
            imageButton = itemView.findViewById(R.id.imageButtonDeleteRecordElement);
        }
    }

    private void goToEditor(View view, Record record){
        Intent intent = new Intent(view.getContext(), EditorActivity.class);
        intent.putExtra("record", record);
        view.getContext().startActivity(intent);
    }

    private void deleteRecord(int pos){
        RequestQueue queue = Volley.newRequestQueue(inflater.getContext());
        UrlMap urlMap = new UrlMap(UrlBase.DELETE_RECORD);
        urlMap.put("record_id", Integer.toString(recordList.get(pos).getId()));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlMap.generateUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase("success")){
                    Toast.makeText(inflater.getContext(), "Delete Successful", Toast.LENGTH_LONG).show();
                    recordList.remove(pos);
                    notifyItemRemoved(pos);
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

    private Dialog onCreateDeleteDialog(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());
        builder.setTitle("Delete?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteRecord(pos);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
