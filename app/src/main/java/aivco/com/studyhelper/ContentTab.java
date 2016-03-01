package aivco.com.studyhelper;

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
import java.util.List;
import java.util.Set;

import backend.TitleInfo;

/*
* Content tab will display the groups from the Titleinfo_db inner class,through its outer class titleinfo
* If one of the item is clicked execution moves to TitleFrgment this activity class takes care of the titles from the groups
*
* */

public class ContentTab extends AppCompatActivity implements AdapterView.OnItemClickListener {
    TitleInfo titleInfo;
    ListView listView;
    List<String> list;
    static SharedPreferences sp;
    DbManager  dbManager;
    public static final String KEY_CHOSEN_GROUP="ContentTab_GROUP";
    public static final String TITLECONTENTSELECTIONKEY="titlecontent";

    public static final String START_ACTIVITY_STRING="aivco.com.studyhelper.ContentTab";
    public String tag="ContentTabactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_tab);
        Log.d(tag, "onCreate");
        titleInfo=new TitleInfo(this);
        dbManager=new DbManager(this);




    }

    @Override
    protected void onResume() {
        super.onResume();
        listView=(ListView)findViewById(R.id.grplistView);
        listView.setOnItemClickListener(this);
        new GroupLoader().execute();


    }

    class GroupLoader extends AsyncTask
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
            return titleInfo.collectGroups();

            //return titleInfo.collectUniqueGn();
        }

        //add the cursor object to adaptor
        @Override
        protected void onPostExecute(Object o) {
            Log.d(tag, "onpostExecute");

            super.onPostExecute(o);
            Cursor c=(Cursor)o;


            List<Form> adaptorlist=new ArrayList<>();
            list=new ArrayList<>();


            while(c.moveToNext())
            {
                Form form=new Form();
                form.setGroup(c.getString(c.getColumnIndex(TitleInfo.groupname_col)));
                list.add(c.getString(c.getColumnIndex(TitleInfo.groupname_col)));
               // form.setTitle(c.getString(c.getColumnIndex(TitleInfo.date_col)));
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
        Log.d(tag, "item clicked");

        String ans=list.get(i);
        sp=getSharedPreferences("content",MODE_PRIVATE);
        SharedPreferences.Editor editer=sp.edit();
        editer.putString(KEY_CHOSEN_GROUP,ans);
        editer.commit();

        System.out.println(ans + "........................................................." + i);
        Intent intent=new Intent();
        intent.setClass(this, TitleFragment.class);
        intent.putExtra(START_ACTIVITY_STRING,ans);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_tab_menu,menu);
        return super.onCreateOptionsMenu(menu);


    }
}
