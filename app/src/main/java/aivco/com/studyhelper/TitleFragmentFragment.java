package aivco.com.studyhelper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import backend.Title;
import backend.TitleInfo;


/**
 * Recieves execution order from Titlefragment loads the title from the titleinfo db
 */
public class TitleFragmentFragment extends Fragment implements AdapterView.OnItemClickListener {
    private String log="TitleFragmentFragment";
    private ListView ls;
    private StartTitleContent startTitleContent;
    TitleInfo ti;
    List<String> list;
    DbManager dbManager;
    static View view;

    public TitleFragmentFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null)
        return inflater.inflate(R.layout.fragment_title_fragment, container, false);
    return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbManager=new DbManager(getActivity());
        ti=new TitleInfo(getActivity());
        new TitleLoader().execute();
        ls=(ListView)getView().findViewById(R.id.titlelistView);
        ls.setOnItemClickListener(this);

    }


    /////////////responds when an item is clicked,does not perform any function,first get the string that was clicked fron dbmanager///////////
   //////////////after which passes it to the activity that it is attached to //////////////////
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        Log.d(log,"onitemclicked");
        String ans=list.get(i);
       // Cursor s=(Cursor) ls.getItemAtPosition(i);
        //String ans=dbManager.getSelectedTitleRow(s,i) ;
      if(startTitleContent != null)
        startTitleContent.startSecActivity(ans);

    }
    class TitleLoader extends AsyncTask
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object[] objects)
        {


         ///calls collectUnique from titleInfo class,this will return only unique rows from the table
            //return ti.collectUniqueTn(ContentTab.sp.getString(ContentTab.KEY_CHOSEN_GROUP, ""));
            return ti.collectTitles(ContentTab.sp.getString(ContentTab.KEY_CHOSEN_GROUP, ""));
        }
        //add the cursor object to adaptor
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        ///set the adaptor here
            //setList(o);
            setListFromTitle(o);


        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        startTitleContent=(StartTitleContent)activity;
    }

    public interface StartTitleContent
    {

      void startSecActivity(String title);

    }

    public void setList(Object o){
        list=(List)o;
        List<Form> adaptorlist=new ArrayList<>();


        for(String formElement:list)
        {
            Form form=new Form();
            form.setGroup(formElement);
            adaptorlist.add(form);

        }
        CustomAdaptorForListView  arr=new CustomAdaptorForListView(getActivity(),
                R.layout.background_for_group_content,adaptorlist);
        ls.setAdapter(arr);

    }
    public void setListFromTitle(Object o)
    {
        list=new ArrayList<>();
      List<Title>   titles=(List)o;
        List<Form> adaptorlist=new ArrayList<>();
        for(Title title:titles)
        {
            Form form=new Form();
            form.setGroup(title.getTitle_name());
            list.add(title.getUnique_Key());
            adaptorlist.add(form);

        }
        CustomAdaptorForListView  arr=new CustomAdaptorForListView(getActivity(),
                R.layout.background_for_group_content,adaptorlist);
        ls.setAdapter(arr);


    }



}
