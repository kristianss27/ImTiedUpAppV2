package com.kristian.android.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    private String itemValue;
    private String itemPosition;
    private EditText etCurrentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        itemValue = getIntent().getStringExtra("itemValue");
        itemPosition = getIntent().getStringExtra("itemPosition");

        etCurrentItem = (EditText) findViewById(R.id.etCurrentItem);
        etCurrentItem.setText(itemValue);
    }

    public void onEdit(View v){
        Intent intent = new Intent();
        intent.putExtra("newValue",etCurrentItem.getText().toString());
        intent.putExtra("code",90);
        intent.putExtra("itemPosition", itemPosition);
        setResult(RESULT_OK,intent);
        finish();
    }

}
