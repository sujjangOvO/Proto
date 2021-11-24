package com.example.proto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FriendsAdapter extends BaseAdapter { //친구 리스트

    private ArrayList<FriendsItem> items = new ArrayList<FriendsItem>();
    private TextView id;
    String user_id;

    public FriendsAdapter(String user_id){
        this.user_id=user_id;
    }


    public View getView(int position, View convertView, ViewGroup parent){
        final int pos=position;
        final Context context= parent.getContext();
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.friends_list,parent,false);
        }
        id=(TextView) convertView.findViewById(R.id.id);

        FriendsItem friendsItem=items.get(position);

        id.setText(friendsItem.getName());

        LinearLayout layout=(LinearLayout) convertView.findViewById(R.id.layout);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //친구 이름 클릭했을 때
//                Toast.makeText(v.getContext(),items.get(pos).getName(),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context,FriendProfile.class);
                intent.putExtra("friend_id",items.get(pos).getName());
                intent.putExtra("user_id",user_id);
                context.startActivity(intent);
            }
        });


        return convertView;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(String str){
        FriendsItem item=new FriendsItem(str);
        items.add(item);
    }


    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




}
