package com.kristian.android.simpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kristianss27 on 9/14/16.
 */
public class ItemAdapter extends ArrayAdapter<Item> {
    public ItemAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_todo_item, parent, false);
        }
        // Lookup view for data population
        TextView textView = (TextView) convertView.findViewById(R.id.list_todo_textview);
        TextView priorityTextView = (TextView) convertView.findViewById(R.id.list_todo_priority);
        //TextView tvHome = (TextView) convertView.findViewById(R.id.tvHome);

        // Populate the data into the template view using the data object
        textView.setText(item.getText());
        if(item.getPriority().equalsIgnoreCase(getContext().getResources().getString(R.string.priority_1))){
            priorityTextView.setText(R.string.priority_1);
            priorityTextView.setTextColor(getContext().getResources().getColor(R.color.priority_1_color));
        }
        else if(item.getPriority().equalsIgnoreCase(getContext().getResources().getString(R.string.priority_2))){
            priorityTextView.setText(R.string.priority_2);
            priorityTextView.setTextColor(getContext().getResources().getColor(R.color.priority_2_color));
        }
        else if(item.getPriority().equalsIgnoreCase(getContext().getResources().getString(R.string.priority_3))){
            priorityTextView.setText(R.string.priority_3);
            priorityTextView.setTextColor(getContext().getResources().getColor(R.color.priority_3_color));
        }
        else{
            priorityTextView.setText(R.string.priority_1);
            priorityTextView.setTextColor(getContext().getResources().getColor(R.color.priority_3_color));
        }
        //textView.setText(item.getText());


        // Return the completed view to render on screen

        return convertView;
    }
}
