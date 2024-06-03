package com.example.hobbyt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hobbyt.modules.Record;
import com.example.hobbyt.modules.User;

import java.util.ArrayList;
import java.util.List;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.UserView> {
    List<User> users;
    private final LayoutInflater inflater;

    public UserRecyclerViewAdapter(Context context, List<User> users) {
        this.users = users;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public UserView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.record_element, parent, false);
        return new UserView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserView holder, int position) {
        User user = users.get(position);
        holder.firstAndLastName.setText(user.getFirst_name() + " " + user.getLast_name());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserView extends RecyclerView.ViewHolder{
        TextView firstAndLastName;
        Button sendRecord;

        public UserView(@NonNull View itemView) {
            super(itemView);

            firstAndLastName = itemView.findViewById(R.id.userFirstAndLastNameElement);
            sendRecord = itemView.findViewById(R.id.imageButtonSendRecord);


        }
    }
}
