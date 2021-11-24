package com.example.proto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HpostingAdapter extends RecyclerView.Adapter<HpostingAdapter.CustomViewHolder> {

    private ArrayList<HpostingItem> arrayList;

    public HpostingAdapter(ArrayList<HpostingItem> arrayList){ this.arrayList=arrayList;}

    public HpostingAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.hposting_item,parent,false);
        HpostingAdapter.CustomViewHolder holder = new HpostingAdapter.CustomViewHolder(v);

        return holder;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // onBindViewHolder
    public void onBindViewHolder(@NonNull HpostingAdapter.CustomViewHolder holder, int position){
        holder.posting_Title.setText(arrayList.get(position).getPosting_Title());
        holder.posting_Location.setText(arrayList.get(position).getPosting_Location());
        holder.posting_Contents.setText(arrayList.get(position).getPosting_Contents());
        holder.img.setImageResource(arrayList.get(position).getPhoto());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // 해당 게시글 출력
            }
        });
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView posting_Title;
        private TextView posting_Location;
        private TextView posting_Contents;
        private ImageView img;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.posting_Title=itemView.findViewById(R.id.pTitle);
            this.posting_Location=itemView.findViewById(R.id.pLocation);
            this.posting_Contents=itemView.findViewById(R.id.pContents);
            this.img=itemView.findViewById(R.id.pImage);
        }
    }
}
