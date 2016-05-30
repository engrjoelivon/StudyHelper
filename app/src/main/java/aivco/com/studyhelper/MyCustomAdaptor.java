package aivco.com.studyhelper;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by joel on 12/28/15.
 */
public class MyCustomAdaptor extends ArrayAdapter {

    public List<String> data;
    public Resources res;
    public Activity spinnerWithCustomAdapter;
    private int resource;
    LayoutInflater inflater;
    public MyCustomAdaptor(Activity activitySpinner, int textViewResourceId, List objects)
    {
        super(activitySpinner, textViewResourceId, objects);
        inflater = (LayoutInflater)activitySpinner.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        spinnerWithCustomAdapter=activitySpinner;
        this.data=objects;
        this.resource=textViewResourceId;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return customrow(position,convertView,parent);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return customrow(position,convertView,parent);
    }

    public View customrow(int position, View convertView, ViewGroup parent){
   View row=inflater.inflate(resource,parent,false);
        TextView name=(TextView)row.findViewById(R.id.custom_layout_tv);
        name.setClickable(false);
        name.setText(data.get(position));


        return row;
    }




}
