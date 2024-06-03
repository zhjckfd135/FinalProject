package com.example.hobbyt;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hobbyt.modules.Record;

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
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    class RecordView extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView data;

        public RecordView(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleTextElement);
            data = itemView.findViewById(R.id.dataTextElement);
        }
    }

    private void goToEditor(View view, Record record){
        Intent intent = new Intent(view.getContext(), EditorActivity.class);
        intent.putExtra("record", record);
        view.getContext().startActivity(intent);
    }


}
