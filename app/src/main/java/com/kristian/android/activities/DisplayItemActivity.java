package com.kristian.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.kristian.android.simpletodo.EditItemActivity;
import com.kristian.android.simpletodo.MainActivity;
import com.kristian.android.simpletodo.MyDialogFragment;
import com.kristian.android.simpletodo.R;
import com.kristian.android.simpletodo.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DisplayItemActivity extends AppCompatActivity implements MyDialogFragment.MyDialogListener{

    private String itemPosition;
    private String itemId;
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
    private final int REQUEST_CODE = 90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_item);
        Toolbar toolBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolBar);

        itemPosition = getIntent().getStringExtra("itemPosition");
        itemId = getIntent().getStringExtra("itemId");
        itemValue = getIntent().getStringExtra("itemValue");
        itemComment = getIntent().getStringExtra("itemComment");
        itemDate = getIntent().getStringExtra("itemDate");
        //Toast.makeText(this,"Date1: "+itemDate,Toast.LENGTH_LONG).show();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
        Calendar calendar = Calendar.getInstance();

        try {
            Date date = dateFormat.parse(itemDate);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String date = new Utils().getFormatDate(calendar,true);

        itemStatus = getIntent().getStringExtra("itemStatus");
        itemPriority = getIntent().getStringExtra("itemPriority");

        tItemText = (TextView) findViewById(R.id.et_item_text);
        tItemText.setText(itemValue);
        tItemComment = (TextView) findViewById(R.id.et_item_comment);
        tItemComment.setText(itemComment);
        tItemDate = (TextView) findViewById(R.id.et_item_date);
        tItemDate.setText(date);
        tItemStatus = (TextView) findViewById(R.id.et_item_status);
        tItemStatus.setText(itemStatus);
        tItemPriority = (TextView) findViewById(R.id.et_item_priority);
        tItemPriority.setText(itemPriority);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.add_item).setVisible(false);
        menu.findItem(R.id.save_item).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_item:
                Intent intent = new Intent(DisplayItemActivity.this, EditItemActivity.class);
                intent.putExtra("itemPosition",itemPosition);
                intent.putExtra("itemId",itemId);
                intent.putExtra("itemValue", itemValue);
                intent.putExtra("itemComment", itemComment);
                //Toast.makeText(this,"Date click: "+itemDate,Toast.LENGTH_LONG).show();
                intent.putExtra("itemDate", itemDate);
                intent.putExtra("itemStatus", itemStatus);
                intent.putExtra("itemPriority", itemPriority);
                intent.putExtra("code", REQUEST_CODE);
                startActivityForResult(intent,REQUEST_CODE);
                return true;
            case R.id.delete_item:
                showAlertDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        DialogFragment alertDialog = MyDialogFragment.newInstance("Delete Task",getResources().getString(R.string.delete_message));
        alertDialog.show(fm, "fragment_alert");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("itemPosition",itemPosition);
        startActivity(intent);
    }
    @Override
    public void onDialogNegativeClick(DialogFragment dialog){
        dialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            itemPosition = data.getStringExtra("itemPosition");
            itemId = data.getStringExtra("itemId");
            itemValue = data.getStringExtra("itemValue");
            itemComment = data.getStringExtra("itemComment");
            itemDate = data.getStringExtra("itemDate");
            //Toast.makeText(this,"MontValue 1: "+itemDate,Toast.LENGTH_LONG).show();
            int code = data.getExtras().getInt("code", 0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
            Calendar calendar = Calendar.getInstance();

            try {
                Date date = dateFormat.parse(itemDate);
                calendar.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String date = new Utils().getFormatDate(calendar,true);

            itemStatus = data.getStringExtra("itemStatus");
            itemPriority = data.getStringExtra("itemPriority");

            tItemText = (TextView) findViewById(R.id.et_item_text);
            tItemText.setText(itemValue);
            tItemComment = (TextView) findViewById(R.id.et_item_comment);
            tItemComment.setText(itemComment);
            tItemDate = (TextView) findViewById(R.id.et_item_date);
            tItemDate.setText(date);
            tItemStatus = (TextView) findViewById(R.id.et_item_status);
            tItemStatus.setText(itemStatus);
            tItemPriority = (TextView) findViewById(R.id.et_item_priority);
            tItemPriority.setText(itemPriority);
            Toast.makeText(this,R.string.edit_item_success, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
