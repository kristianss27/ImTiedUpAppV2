package com.kristian.android.activities;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kristian.android.fragments.DatePickerFragment;
import com.kristian.android.simpletodo.MainActivity;
import com.kristian.android.simpletodo.R;

import java.util.Calendar;
import java.util.Date;

public class AddItemActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private Calendar calendar;
    private Date date;
    private EditText etItemDate;
    private Spinner spinnerPriority;
    private Spinner spinnerStatus;
    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        setViews();
        setListeners();
    }

    public void setViews(){
        Toolbar toolBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolBar);
        etItemDate = (EditText) findViewById(R.id.et_item_date);
        //We should get our spinners
        spinnerPriority = (Spinner) findViewById(R.id.spinner_priorities);
        spinnerStatus = (Spinner) findViewById(R.id.spinner_status);

        //Lets populate one spinner (spinnerPriority) through an ArrayAdapter. The other has been filled in the IU
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.priorities,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);
    }

    public void setListeners(){
        etItemDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
    }

    public void showDatePickerDialog(View v){
        DialogFragment dialogFragment;
        if(calendar!=null){
            dialogFragment = DatePickerFragment.newInstance(String.valueOf(day),String.valueOf(month),
                                                                                String.valueOf(year));
            }
        else{
            dialogFragment = new DatePickerFragment();
            }

            dialogFragment.show(getFragmentManager(), "datePicker");
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        calendar = Calendar.getInstance();
        calendar.set(year,month,day);
        date = calendar.getTime();
        this.year=year;
        month++;
        this.month=month;
        this.day=day;
        date = calendar.getTime();
        etItemDate.setText(""+month+"/"+day+"/"+year);
        Toast.makeText(this, "Date: "+date.toString(), Toast.LENGTH_LONG).show();
    }

    public void addItem() {
        //We are going to get all the values from our form
        EditText etItemText = (EditText) findViewById(R.id.et_item_text);
        String itemText = etItemText.getText().toString();
        EditText etItemComment = (EditText) findViewById(R.id.et_item_comment);
        String itemComment = etItemComment.getText().toString();
        EditText etItemDate = (EditText) findViewById(R.id.et_item_date);
        String itemDate = etItemDate.getText().toString();

        String priority = spinnerPriority.getSelectedItem().toString();
        String status = spinnerStatus.getSelectedItem().toString();

        if(itemText!=null && date!=null){
            Log.d("AddItemActivity","Add Item: "+itemText+" - "+itemComment+" - "+ date.toString()+" - "+priority+" - "+status);

            Intent intent = new Intent();
                intent.putExtra("itemText",itemText);
                intent.putExtra("itemComment",itemComment);
                intent.putExtra("date",date.toString());
                intent.putExtra("priority",priority);
                intent.putExtra("status",status);
                intent.putExtra("code",90);
                setResult(RESULT_OK,intent);
                finish();
            }
            else{
                Toast.makeText(this,R.string.add_item_fail,Toast.LENGTH_LONG).show();
            }
        

        //Toast.makeText(this, "Value Added!", Toast.LENGTH_LONG);
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
        menu.findItem(R.id.delete_item).setVisible(false);
        menu.findItem(R.id.edit_item).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_item:
                addItem();
                return true;

            default:
                return super.onOptionsItemSelected(item);
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
