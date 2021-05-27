package com.example.elderlymeeting.ui.friends;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.elderlymeeting.R;
import com.example.elderlymeeting.ui.messaging.MessageFragment;
import com.example.elderlymeeting.ui.users.UserAdapter;
import com.example.elderlymeeting.ui.users.Users;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<Users> mFriends;
    private  boolean isChat;


    //constructor
    public FriendsAdapter(Context context, List<Users> friends, boolean isChat) {
        this.context = context;
        this.mFriends = friends;
        this.isChat = isChat;
    }

    @NonNull
    @NotNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item,
                parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserAdapter.ViewHolder holder, int position) {
        final Users friends = mFriends.get(position);
        holder.fullName.setText(friends.getFullName());

        if(friends.getPicture().equals("default")){
            holder.picture.setImageResource(R.mipmap.ic_launcher);
        }else{
            //using glide
            Glide.with(context).load(friends.getPicture()).into(holder.picture);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(context, MessageFragment.class);
                i.putExtra("userid", friends.getId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        public TextView fullname;
        public ImageView picture;

        public Viewholder(@NonNull @NotNull View itemView) {
            super(itemView);

            fullname = itemView.findViewById(R.id.fullName);
            picture = itemView.findViewById(R.id.profilePicture);
        }
    }
}
