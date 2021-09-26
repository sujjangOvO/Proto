package com.example.proto;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.CustomViewHolder>{

    private ArrayList<Item> arrayList;

    public ItemAdapter(ArrayList<Item> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ItemAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        CustomViewHolder holder=new CustomViewHolder(view);

        return holder;
    }

    @Override
    // 추가될 때에 대한 생명주기
    public void onBindViewHolder(@NonNull ItemAdapter.CustomViewHolder holder, int position) {
        holder.item_id.setImageResource(arrayList.get(position).getItem_id());
        holder.item_title.setText(arrayList.get(position).getItem_title());
        holder.item_user.setText(arrayList.get(position).getItem_user());
        holder.item_addr.setText(arrayList.get(position).getItem_addr());

        // listview가 클릭이 되었을 때
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
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
        protected TextView item_user;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.item_id=(ImageView) itemView.findViewById(R.id.listImage);
            this.item_title=(TextView) itemView.findViewById(R.id.listTitle);
            this.item_addr=(TextView) itemView.findViewById(R.id.listAddr);
            this.item_user=(TextView) itemView.findViewById(R.id.listUser);
        }
    }
}
