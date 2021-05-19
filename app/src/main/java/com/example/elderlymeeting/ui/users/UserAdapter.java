package com.example.elderlymeeting.ui.users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.elderlymeeting.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<Users> mUsers;

    public UserAdapter(Context context, List<Users> mUsers) {
        this.context = context;
        this.mUsers = mUsers;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = mUsers.get(position);
        holder.fullName.setText(users.getFullName());

        if(users.getPicture().equals("default")){
            holder.picture.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Glide.with(context).load(users.getPicture()).into(holder.picture);
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView fullName;
        public TextView age;
        public TextView email;
        public TextView bio;
        public TextView hobby;
        public ImageView picture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fullName = itemView.findViewById(R.id.fullName);
            picture = itemView.findViewById(R.id.profilePicture);

        }

    }
}
