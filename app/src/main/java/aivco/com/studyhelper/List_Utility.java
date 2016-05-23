package aivco.com.studyhelper;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import backend.TitleInfo;

/**
 * Created by joel on 4/6/16.
 */
public class List_Utility extends DialogFragment  {
    private EditText edittext;
    private Groupname groupname;
    ListView ls;
    String this_id;
    List<String> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b=getArguments();
        this_id=b.getString("key");
        list=new ArrayList<>();

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.activity_content_tab, container, false);

        getDialog().setTitle("Create New Group ");




        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        ls=(ListView)getView().findViewById(R.id.grplistView);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(this_id != null)
                    new TitleInfo(getActivity().getApplicationContext()).updateGroupName(this_id,list.get(position));
                getDialog().dismiss();
            }


        });
        new GroupLoader().execute();





    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //groupname=(Groupname)activity;

    }





    public interface Groupname
    {
        public void name(String name);

    }

    class GroupLoader extends AsyncTask
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();






        }

        @Override
        protected Object doInBackground(Object[] objects)


        {
            return new TitleInfo(getActivity().getApplicationContext()).collectUniqueGn();
        }

        //add the cursor object to adaptor
        @Override
        protected void onPostExecute(Object o)
        {



            super.onPostExecute(o);
            list=(List)o;


            List<Form> adaptorlist=new ArrayList<>();


            for(String gp:list)
            {
                Form form=new Form();
                form.setGroup(gp);
                adaptorlist.add(form);
            }





            CustomAdaptorForListView  arr=new CustomAdaptorForListView(getActivity(),R.layout.background_for_group_content,adaptorlist);
            ls.setAdapter(arr);


        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }
    }
}
