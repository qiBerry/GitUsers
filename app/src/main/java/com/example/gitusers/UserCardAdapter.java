package com.example.gitusers;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class UserCardAdapter extends RecyclerView.Adapter<UserCardAdapter.UserViewHolder>{
    private Context context;
    private ArrayList<User> userList;

    public UserCardAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_card, viewGroup, false);
        UserViewHolder viewHolder = new UserViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int i) {
        //Filling card
        final User user = userList.get(i);
        userViewHolder.tvCardPrimary.setText(user.getLogin());
        userViewHolder.tvCardSub.setText("ID: " + user.getId());
        userViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InfoActivity.class);
                intent.putExtra("userLogin", user.getLogin());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    //Initialisation of card
    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvCardPrimary, tvCardSub;
        public View view;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;

            tvCardPrimary = (TextView) itemView.findViewById(R.id.tv_card_primary);
            tvCardSub = (TextView) itemView.findViewById(R.id.tv_card_sub);
        }

    }
}
