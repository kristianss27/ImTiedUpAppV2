package com.kristian.android.simpletodo;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.kristian.android.activities.AddItemActivity;
import com.kristian.android.activities.DisplayItemActivity;

import org.greenrobot.greendao.query.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MyDialogFragment.MyDialogListener{
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ArrayList<Item> arrayItem;
    private ItemAdapter itemAdapter;

    private List<Item> listItem;
    private ListView lvItems;
    private Utils utils = new Utils();
    private final int REQUEST_CODE = 90;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private ItemDao itemDao;
    private Item item;
    private DaoMaster.DevOpenHelper helper;
    private SQLiteDatabase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolBar);
        setupListView("db");
        //We setUp our List View Listener
        setupListViewListener();
        if(getIntent().getStringExtra("itemPosition")!=null){
            String position = getIntent().getStringExtra("itemPosition");
            item = (Item) arrayItem.get(Integer.parseInt(position));
            deleteItem();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.delete_item).setVisible(false);
        menu.findItem(R.id.edit_item).setVisible(false);
        menu.findItem(R.id.save_item).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_item:
                goToAddItemLayout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setupListView(String file){
        if(file.equalsIgnoreCase("db")){
            populateItems(file);
            // I've created the adapter to convert the array to views
            itemAdapter = new ItemAdapter(this, arrayItem);
            // Attach the adapter to a ListView
            lvItems = (ListView) findViewById(R.id.lvItems);
            lvItems.setAdapter(itemAdapter);
        }
        else {
            populateItems(file);
            //We need to create an adapter to display the items
            itemsAdapter = new ArrayAdapter<String>(this, R.layout.list_todo_item, items);
            //We should set our list view
            lvItems = (ListView) findViewById(R.id.lvItems);

            lvItems.setAdapter(itemsAdapter);
        }
    }

    public void populateItems(String way){
        if(way.equalsIgnoreCase("db")){
            items = new ArrayList<String>();
            arrayItem = new ArrayList<Item>();
            openDataBase();
            Query<Item> query = itemDao.queryBuilder().build();
            List<Item> list = query.list();
            closeDataBase();
            list.size();
            Log.d("MainActiviy", "Item list, size: " + list.size());
            Iterator<Item> iterator = list.iterator();
            while(iterator.hasNext()){
                Item item = iterator.next();
                String id = String.valueOf(item.getId());
                String text = item.getText();
                arrayItem.add(item);
            }
        }
        else {
            //We should validate if the file exist to populate the list with. If not We'll have a new List
            //Here is the way to populate the listView from a txt file
            items = (utils.readItem(getFilesDir()) != null) ? utils.readItem(getFilesDir()) : new ArrayList<String>();
        }
    }

    public void onAddItemImportant(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        utils.writeItem(getFilesDir(), items);

        //Toast.makeText(this, "Value Added!", Toast.LENGTH_LONG);
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        Calendar date = Calendar.getInstance();
        Item item = new Item(null,itemText,"",date.getTime(),"TO DO","HIGH");
        openDataBase();
        itemDao.insert(item);
        Log.d("MainActiviy", "Inserted new item, ID: " + item.getId());
        closeDataBase();
        itemAdapter.add(item);
        etNewItem.setText("");
        //Toast.makeText(this, "Value Added!", Toast.LENGTH_LONG);
    }

    public void deleteItem(){
        openDataBase();
        itemDao.delete(item);
        closeDataBase();
        itemAdapter.remove(item);
        updateListView();
    }


    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        item = (Item) arrayItem.get(position);
                        showAlertDialog();
                        return true;
                    }
                });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {

                Intent intent = new Intent(MainActivity.this, DisplayItemActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Item item = arrayItem.get(position);
                if(item != null){
                    Log.d("CURRENT ITEM","Posicion:"+position+" Id:"+item.getId());
                    intent.putExtra("itemPosition", String.valueOf(position));
                    intent.putExtra("itemId", String.valueOf(item.getId()));
                    intent.putExtra("itemValue", item.getText());
                    intent.putExtra("itemComment", item.getComment());
                    intent.putExtra("itemDate", item.getDate().toString());
                    intent.putExtra("itemStatus", item.getStatus());
                    intent.putExtra("itemPriority", item.getPriority());
                    intent.putExtra("code", REQUEST_CODE);
                    //startActivityForResult(intent, REQUEST_CODE);
                    startActivity(intent);
                }
                else{
                    Log.e("ITEM IS NULL","Item is null");
                }
            }


        });

    }

    private void setupListViewListenerTxt(){
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        return true;
                    }
                });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {

                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                Log.d("CURRENT ITEM: ", items.get(position));
                intent.putExtra("itemPosition", String.valueOf(position));
                intent.putExtra("itemValue", items.get(position));
                intent.putExtra("code", REQUEST_CODE);
                startActivityForResult(intent, REQUEST_CODE);
            }


        });

    }

    public void updateListView(){
        itemAdapter.notifyDataSetChanged();
    }

    public void updateListViewTxt(){
        itemsAdapter.notifyDataSetChanged();
        utils.writeItem(getFilesDir(), items);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Date date = new Date();

        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            //Add Item
            if(data.getStringExtra("itemText")!=null){
                item = new Item();

                String itemText = data.getStringExtra("itemText");
                String itemComment = data.getStringExtra("itemComment");
                String itemDate = data.getStringExtra("date");
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
                Calendar calendar = Calendar.getInstance();

                try {
                    date = dateFormat.parse(itemDate);
                    calendar.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String priority = data.getStringExtra("priority");
                String status = data.getStringExtra("status");
                item.setText(itemText);
                item.setComment(itemComment);
                item.setDate(date);
                item.setPriority(priority);
                item.setStatus(status);
                openDataBase();
                itemDao.insert(item);
                closeDataBase();
            }
            setupListView("db");
            //We setUp our List View Listener
            setupListViewListener();
            updateListView();
        }
    }

    public void goToAddItemLayout(){
        Intent intent = new Intent(this, AddItemActivity.class);
        intent.putExtra("code", REQUEST_CODE);
        startActivityForResult(intent,REQUEST_CODE);
    }

    public void showAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        DialogFragment alertDialog = MyDialogFragment.newInstance("Delete Task",getResources().getString(R.string.delete_message));
        alertDialog.show(fm, "fragment_alert");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog){ deleteItem();}
    @Override
    public void onDialogNegativeClick(DialogFragment dialog){
        dialog.dismiss();
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

}
