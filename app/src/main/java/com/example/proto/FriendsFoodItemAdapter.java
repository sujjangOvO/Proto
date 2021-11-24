package com.example.proto;

import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FriendsFoodItemAdapter extends RecyclerView.Adapter<FriendsFoodItemAdapter.CustomViewHolder>{

    private ArrayList<FriendsFoodItem> arrayList;
    public FriendsFoodItemAdapter(ArrayList<FriendsFoodItem> arrayList){this.arrayList=arrayList;}

    @NonNull
    @Override
    public FriendsFoodItemAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_friends_foodlist,parent,false);
        CustomViewHolder holder=new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsFoodItemAdapter.CustomViewHolder holder, int position) {
        holder.item_id.setImageResource(arrayList.get(position).getItem_id());
        holder.item_title.setText(arrayList.get(position).getItem_title());
        holder.item_addr.setText(arrayList.get(position).getItem_addr());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),ListDetail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                remove(holder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public void remove(int position){
        try{
            arrayList.remove(position); // 리스트뷰 지우기
            notifyItemRemoved(position); // 새로고침
        }catch(IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView item_id;
        protected TextView item_title;
        protected TextView item_addr;

        public CustomViewHolder(View itemview) {
            super(itemview);

            this.item_id=(ImageView) itemView.findViewById(R.id.listImage);
            this.item_title=(TextView) itemView.findViewById(R.id.listTitle);
            this.item_addr=(TextView) itemView.findViewById(R.id.listAddr);
        }
    }
}