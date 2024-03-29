package com.example.elderlymeeting.ui.messaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.elderlymeeting.R;

import java.util.ArrayList;

public class MessageArrayAdapter extends ArrayAdapter<MessageList> {

    public MessageArrayAdapter(@NonNull Context context, int resource,
                               @NonNull ArrayList<MessageList> messageList) {
        super(context, resource, messageList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.message_item, parent, false);
        }

        //get instances from the messageList
        MessageList currentItem = getItem(position);

        //assign values to the layout fields
        TextView message = listItemView.findViewById(R.id.messageOutput);
        message.setText(currentItem.getMessage());

        TextView name = listItemView.findViewById(R.id.sender);
        name.setText(currentItem.getName());

        TextView date = listItemView.findViewById(R.id.messageDate);
        date.setText(currentItem.getDate());

        return listItemView;
    }
}
