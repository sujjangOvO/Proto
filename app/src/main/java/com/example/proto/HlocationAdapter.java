package com.example.proto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;
import java.util.ArrayList;

public class HlocationAdapter extends RecyclerView.Adapter<HlocationAdapter.CustomViewHolder> {

    private ArrayList<HlocationItem> arrayList;

    public HlocationAdapter(ArrayList<HlocationItem> arrayList){ this.arrayList=arrayList;}

    @NonNull
    @Override
    public HlocationAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.hlocation_item,parent,false);
        CustomViewHolder holder = new CustomViewHolder(v);

        return holder;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // onBindViewHolder
    public void onBindViewHolder(@NonNull HlocationAdapter.CustomViewHolder holder, int position){
        holder.store_name.setText(arrayList.get(position).getStore_name());
        holder.store_location.setText(arrayList.get(position).getStore_location());
        holder.btn.setImageResource(arrayList.get(position).getPhoto());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // 지도 출력
            }
        });
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView store_name;
        private TextView store_location;
        private ImageButton btn;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.store_name=itemView.findViewById(R.id.storeName);
            this.store_location=itemView.findViewById(R.id.storeLocation);
            this.btn=itemView.findViewById(R.id.btn_location);
        }
    }


}
