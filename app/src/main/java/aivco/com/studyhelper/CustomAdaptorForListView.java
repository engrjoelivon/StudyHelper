package aivco.com.studyhelper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnanderson1 on 1/4/16.
 */
public class CustomAdaptorForListView extends ArrayAdapter<Form> {

    List<Form> form;
    Activity activity;
    LayoutInflater inflater;


    public CustomAdaptorForListView(Activity activity, int resource, List<Form> objects) {
        super(activity, resource, objects);
        this.form=objects;
        this.activity=activity;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position,convertView,parent);
    }

    public View createView(int position, View convertView, ViewGroup parent){
   System.out.println("position is..........................................................."+position);
        View view=inflater.inflate(R.layout.background_for_group_content,parent,false) ;
           final   TextView groupname=(TextView)view.findViewById(R.id.groupname);
              TextView date=(TextView)view.findViewById(R.id.date);
              TextView titlename=(TextView)view.findViewById(R.id.titlesname);
        groupname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("setOnClickListener is..........................................................."+groupname.getText().toString());



            }
        });
        Form myform=form.get(position);
        groupname.setText(myform.getGroup());
       date.setText(myform.getTitle());
        titlename.setText("");

        return view;
    }

}
