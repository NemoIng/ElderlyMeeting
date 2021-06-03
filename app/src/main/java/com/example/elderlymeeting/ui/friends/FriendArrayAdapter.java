package com.example.elderlymeeting.ui.friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.elderlymeeting.R;

import java.util.ArrayList;

public class FriendArrayAdapter extends ArrayAdapter<FriendsList> {
    public FriendArrayAdapter(@NonNull Context context, int resource,
                              @NonNull ArrayList<FriendsList> friendsLists) {
        super(context, resource, friendsLists);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.user_item, parent, false);
        }

        //get instance from friendList
        FriendsList currentItem = getItem(position);

        //assign values to the layout fields
        ImageView profilePicture = listItemView.findViewById(R.id.profilePicture);
        Glide.with(getContext())
                .load(currentItem.getImageUrl())
                .centerCrop()
                .override(300, 300)
                .into(profilePicture);

        TextView fullName = listItemView.findViewById(R.id.fullName);
        fullName.setText(currentItem.getFullName());

        TextView age = listItemView.findViewById(R.id.age);
        age.setText(currentItem.getAge() + " years old");

        return listItemView;
    }
}


