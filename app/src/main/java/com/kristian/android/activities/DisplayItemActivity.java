package com.kristian.android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.kristian.android.simpletodo.R;

public class DisplayItemActivity extends AppCompatActivity {

    private String itemPosition;
    private String itemValue;
    private String itemComment;
    private String itemDate;
    private String itemStatus;
    private String itemPriority;

    private TextView tItemText;
    private TextView tItemComment;
    private TextView tItemDate;
    private TextView tItemStatus;
    private TextView tItemPriority;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_item);

        itemPosition = getIntent().getStringExtra("itemPosition");
        itemValue = getIntent().getStringExtra("itemValue");
        itemComment = getIntent().getStringExtra("itemComment");
        itemDate = getIntent().getStringExtra("itemDate");
        itemStatus = getIntent().getStringExtra("itemStatus");
        itemPriority = getIntent().getStringExtra("itemPriority");

        tItemText = (TextView) findViewById(R.id.t_item_text);
        tItemText.setText(itemValue);
        tItemComment = (TextView) findViewById(R.id.t_item_comment);
        tItemComment.setText(itemComment);
        tItemDate = (TextView) findViewById(R.id.t_item_date);
        tItemDate.setText(itemDate);
        tItemStatus = (TextView) findViewById(R.id.t_item_status);
        tItemStatus.setText(itemStatus);
        tItemPriority = (TextView) findViewById(R.id.t_item_priority);
        tItemPriority.setText(itemPriority);
    }
}
