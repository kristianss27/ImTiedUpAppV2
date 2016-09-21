package com.kristian.android.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //showAlertDialog();
        setUpDataBase();
        setupListView("db");
        //We setUp our List View Listener
        setupListViewListener();
    }

    public void setUpDataBase(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"simple-todo-db",null);
        Log.d("KRISTIAN DATABASES", helper.getDatabaseName());
        System.out.println("KRISTIAN DB: "+ helper.getDatabaseName());
        daoMaster = new DaoMaster(helper.getWritableDatabase());
        daoSession = daoMaster.newSession();
        itemDao = daoSession.getItemDao();
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
            Query<Item> query = itemDao.queryBuilder().build();
            List<Item> list = query.list();
            list.size();
            Log.d("MainActiviy", "Item list, size: " + list.size());
            Iterator<Item> iterator = list.iterator();
            while(iterator.hasNext()){
                Item item = iterator.next();
                Log.d("MainActiviy", "Item id: " + item.getId().toString());
                Log.d("MainActiviy", "Item text: " + item.getText());
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
        Item item = new Item(null,itemText,"test",new Date(),"A");
        itemDao.insert(item);
        Log.d("MainActiviy", "Inserted new item, ID: " + item.getId());
        Item itemResult = new Item(item.getId(),itemText,"test",new Date(),"A");
        itemAdapter.add(item);
        etNewItem.setText("");
        //Toast.makeText(this, "Value Added!", Toast.LENGTH_LONG);
    }


    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        Item item = (Item) parent.getItemAtPosition(position);
                        itemDao.delete(item);
                        itemAdapter.remove(item);
                        updateListView();
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
                Item item = arrayItem.get(position);
                if(item != null){
                    Log.d("CURRENT ITEM","Posicion:"+position+" Id:"+item.getId());
                    intent.putExtra("itemPosition", String.valueOf(position));
                    intent.putExtra("itemValue", item.getText());
                    intent.putExtra("code", REQUEST_CODE);
                    startActivityForResult(intent, REQUEST_CODE);
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
                        items.remove(position);
                        updateListView();
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
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            String newValue = data.getExtras().getString("newValue");
            String itemPosition = data.getExtras().getString("itemPosition");
            Item item = arrayItem.get(Integer.parseInt(itemPosition));
            item.setText(newValue);
            int code = data.getExtras().getInt("code",0);
            arrayItem.set(Integer.parseInt(itemPosition),item);
            updateListView();
            itemDao.update(item);

        }
    }

    public void showAlertDialog() {

        FragmentManager fm = getSupportFragmentManager();

        DialogFragment alertDialog = MyDialogFragment.newInstance("ToDoList App");

        alertDialog.show(fm, "fragment_alert");

    }


}
