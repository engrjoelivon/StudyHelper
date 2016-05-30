package aivco.com.studyhelper;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import backend.TitleInfo;

/*
* Content tab will display the groups from the Titleinfo_db inner class,through its outer class titleinfo
* If one of the item is clicked execution moves to TitleFrgment this activity class takes care of the titles from the groups
*
* */

public class ContentTab extends AppCompatActivity implements AdapterView.OnItemClickListener,TitleFragmentFragment.StartTitleContent,FragmentForTitles.StartTitleContent,FragmentForTitleContent.OnFragmentInteractionListener {
    private static final String HOMEFRAGMENT = "homefragment";
    TitleInfo titleInfo;
    ListView listView;
    List<String> list;
    static SharedPreferences sp;
    DbManager  dbManager;
    public static final String KEY_CHOSEN_GROUP="ContentTab_GROUP";
    public static final String TITLECONTENTSELECTIONKEY="titlecontent";
    public static AppCompatActivity finishAcitivty;
    public static final String START_ACTIVITY_STRING="aivco.com.studyhelper.ContentTab";
    public String tag="ContentTabactivity";
    public static boolean loggoutIn;
    android.support.v4.app.FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_tab);
        fm=getSupportFragmentManager();
        finishAcitivty=this;
        Log.d(tag, "onCreate");
        titleInfo=new TitleInfo(this);
        dbManager=new DbManager(this);
        loggoutIn=true;



    }

    @Override
    protected void onResume() {
        super.onResume();
        listView=(ListView)findViewById(R.id.grplistView);
        listView.setOnItemClickListener(this);
        if(loggoutIn)

        { new GroupLoader().execute();}


    }

    //method from titlefragmentfragment
    @Override
    public void startSecActivity(String title)
    {
        System.out.println("..............title chosen.................." +title);
        fm.popBackStack();
        SharedPreferenceHelper.setData(ContentTab.TITLECONTENTSELECTIONKEY, title);
        startContentFragment();
    }

    @Override
    public void closeFragment()
    {
        fm.popBackStack();
            System.out.println("..............closeing fragment..................");

    }

    //method from fragment for title content
    @Override
    public void onFragmentInteraction(String message)
    {

    }

    @Override
    public void closeContent()
    {
        fm.popBackStack();
        startTitleFragment();
    }

    private class GroupLoader extends AsyncTask
    {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            ProgressDialog pd=new ProgressDialog(ContentTab.this);



        }

        @Override
        protected Object doInBackground(Object[] objects)


        {
            //Perform a query on the database to obtain names of groups


            Log.d(tag,"do in backgroud");
            //return titleInfo.collectGroups();

            return titleInfo.collectUniqueGn();
        }

        //add the cursor object to adaptor
        @Override
        protected void onPostExecute(Object o) {
            Log.d(tag, "onpostExecute");


            super.onPostExecute(o);
           list=(List)o;


            List<Form> adaptorlist=new ArrayList<>();

            for(String gp:list)
            {
                Form form=new Form();
                form.setGroup(gp);
                adaptorlist.add(form);
            }





            CustomAdaptorForListView  arr=new CustomAdaptorForListView(ContentTab.this,R.layout.background_for_group_content,adaptorlist);
            listView.setAdapter(arr);
            Log.d(tag, "end of post execute");

        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)

    {



        String ans=list.get(i);
        sp=getSharedPreferences("content",MODE_PRIVATE);
        SharedPreferences.Editor editer=sp.edit();
        editer.putString(KEY_CHOSEN_GROUP,ans);
        editer.apply();
        startTitleFragment();
        //startTitleClass();




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_tab_menu, menu);
        return super.onCreateOptionsMenu(menu);


    }

    public static void logout(){
if(finishAcitivty != null)
        finishAcitivty.finish();
    }


    private void startTitleFragment()
    {
        startFragment(new FragmentForTitles(),HOMEFRAGMENT);

    }

    private void startContentFragment()
    {
        startFragment(new FragmentForTitleContent(),HOMEFRAGMENT);
    }

    private void startFragment(Fragment fragment,String backstack)
    {
        android.support.v4.app.FragmentTransaction ft=fm.beginTransaction();
        ft.addToBackStack(backstack);
        ft.replace(android.R.id.content,fragment);
        ft.commit();


    }

    //start class to display title all titles available in the clicked group
    private void startTitleClass()
    {
        Intent intent=new Intent();
        intent.setClass(this, TitleFragment.class);
        //intent.putExtra(START_ACTIVITY_STRING,ans);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);

    }
}
