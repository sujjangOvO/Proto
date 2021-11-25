package com.example.proto;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class FriendListView extends LinearLayout {
    TextView textView;

    public FriendListView(Context context) {
        super(context);
        init(context);
    }

    public FriendListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.friends_list, this, true);
        textView = (TextView) findViewById(R.id.id);
    }
    public void setName(String name) {
        textView.setText(name);
    }
}
