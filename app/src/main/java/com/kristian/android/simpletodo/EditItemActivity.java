package com.kristian.android.simpletodo;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditItemActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private String itemPosition;
    private String itemId;
    private String itemValue;
    private String itemComment;
    private String itemDate;
    private String itemStatus;
    private String itemPriority;

    private EditText etItemText;
    private EditText etItemComment;
    private EditText etItemDate;
    private EditText etItemPriority;
    private EditText etItemStatus;

    private Calendar calendar;
    private Date date;
    private int day;
    private int month;
    private int year;
    private List<String> list;
    private Spinner spinnerPriority;
    private Spinner spinnerStatus;

    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private ItemDao itemDao;
    private DaoMaster.DevOpenHelper helper;
    private SQLiteDatabase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolBar);

        itemPosition = getIntent().getStringExtra("itemPosition");
        itemId = getIntent().getStringExtra("itemId");
        itemValue = getIntent().getStringExtra("itemValue");
        itemComment = getIntent().getStringExtra("itemComment");
        itemDate = getIntent().getStringExtra("itemDate");
        //Toast.makeText(this,"Date2: "+itemDate,Toast.LENGTH_LONG).show();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
        calendar = Calendar.getInstance();

        try {
            date = dateFormat.parse(itemDate);
            calendar.setTime(date);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            month++;
            year = calendar.get(Calendar.YEAR);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String date = new Utils().getFormatDate(calendar,true);

        itemStatus = getIntent().getStringExtra("itemStatus");
        itemPriority = getIntent().getStringExtra("itemPriority");

        etItemText = (EditText) findViewById(R.id.et_item_text);
        etItemText.setText(itemValue);
        etItemComment = (EditText) findViewById(R.id.et_item_comment);
        etItemComment.setText(itemComment);
        etItemDate = (EditText) findViewById(R.id.et_item_date);
        etItemDate.setText(date);

        spinnerPriority = (Spinner) findViewById(R.id.spinner_priorities);
        spinnerPriority = setSpinner(itemPriority,R.array.priorities,spinnerPriority);
        spinnerStatus = (Spinner) findViewById(R.id.spinner_status);
        spinnerStatus = setSpinner(itemStatus,R.array.status,spinnerStatus);

        setListeners();
    }

    public Spinner setSpinner(String item, @ArrayRes int arrayId, Spinner spinner){
        String[] array = getResources().getStringArray(arrayId);
        list = new ArrayList<>();
        list.add(item);
        for (String s : array) {
            if(!s.equalsIgnoreCase(item)){
                list.add(s);
            }
        }

        ArrayAdapter<String> arrayAdapterPriorities = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        arrayAdapterPriorities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapterPriorities);
        return spinner;
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
            //Toast.makeText(this,String.valueOf(month),Toast.LENGTH_LONG).show();
            dialogFragment = DatePickerFragment.newInstance(String.valueOf(day),String.valueOf(month),
                    String.valueOf(year));
        }
        else{
            dialogFragment = new DatePickerFragment();
        }
        dialogFragment.show(getFragmentManager(), "datePicker");
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        calendar.set(year,month,day);
        month++;
        //Toast.makeText(this,"MontValue 1: "+month,Toast.LENGTH_LONG).show();
        this.year=year;
        this.month=month;
        this.day=day;
        date = calendar.getTime();
        etItemDate.setText(""+month+"/"+day+"/"+year);
        //Toast.makeText(this, "Date: "+date.toString(), Toast.LENGTH_LONG).show();
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
                editItem();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void editItem() {
        //We are going to get all the values from our form
        EditText etItemText = (EditText) findViewById(R.id.et_item_text);
        String itemText = etItemText.getText().toString();
        EditText etItemComment = (EditText) findViewById(R.id.et_item_comment);
        String itemComment = etItemComment.getText().toString();
        EditText etItemDate = (EditText) findViewById(R.id.et_item_date);
        String itemDate = etItemDate.getText().toString();

        String priority = spinnerPriority.getSelectedItem().toString();
        String status = spinnerStatus.getSelectedItem().toString();

        if(itemText!=null && calendar!=null){
            Log.d("AddItemActivity","Add Item: "+itemId+" - "+itemText+" - "+itemComment+" - "+priority+" - "+status);
            openDataBase();
            List items = itemDao.queryBuilder().where(ItemDao.Properties.Id.eq(itemId)).list();
            Item item = (Item) items.get(0);

            item.setText(itemText);
            item.setComment(itemComment);
            //Toast.makeText(this, "Date Almacenado: "+date.toString(), Toast.LENGTH_SHORT).show();
            item.setDate(date);
            item.setPriority(priority);
            item.setStatus(status);
            itemDao.update(item);
            closeDataBase();

            Intent intent = new Intent();
            intent.putExtra("itemPosition", String.valueOf(itemPosition));
            intent.putExtra("itemId", String.valueOf(item.getId()));
            intent.putExtra("itemValue", item.getText());
            intent.putExtra("itemComment", item.getComment());
            intent.putExtra("itemDate", date.toString());
            intent.putExtra("itemStatus", item.getStatus());
            intent.putExtra("itemPriority", item.getPriority());
            intent.putExtra("code", 200);
            setResult(RESULT_OK,intent);
            finish();
        }
        else{
            Toast.makeText(this,R.string.add_item_fail,Toast.LENGTH_LONG).show();
        }

    }

    public void onEdit(View v){
        Intent intent = new Intent();
        intent.putExtra("newValue",etItemText.getText().toString());
        intent.putExtra("code",90);
        intent.putExtra("itemPosition", itemPosition);
        setResult(RESULT_OK,intent);
        finish();
    }

    public void openDataBase(){
        helper = new DaoMaster.DevOpenHelper(this,"im-tiedup-db",null);
        if(dataBase == null){
            dataBase = helper.getWritableDatabase();
            Log.e("DATA BASE: ", helper.getDatabaseName());
        }
        else{
            if(!dataBase.isOpen()){
                dataBase = helper.getWritableDatabase();
                Log.e("DATA BASE","Is open: "+dataBase.isOpen());
            }
            else{
                Log.e("DATA BASE","Is open: "+dataBase.isOpen());
            }

        }
        daoMaster = new DaoMaster(dataBase);
        daoSession = daoMaster.newSession();
        itemDao = daoSession.getItemDao();
    }

    public void closeDataBase() {
        daoSession.clear();
        dataBase.close();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }

}
