package aivco.com.studyhelper;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by johnanderson1 on 10/29/15.
 */
public class Create_Group extends DialogFragment implements View.OnClickListener{

    private EditText edittext;
    private Groupname groupname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.create_group, container, false);

        getDialog().setTitle("Create New Group ");




        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       edittext=(EditText)getView().findViewById(R.id.new_group_name);
        Button saveButton=(Button)getView().findViewById(R.id.saveGroup);
             saveButton.setOnClickListener(this);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        groupname=(Groupname)activity;

    }

    @Override
    public void onClick(View view) {

        if(groupname != null)
        {
          groupname.name(edittext.getText().toString());

        }

    }

    public interface Groupname
    {
     public void name(String name);

    }
}
