package aivco.com.studyhelper;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import backend.Title;
import backend.TitleInfo;
/*
recieves execution order from Content tab
* moves execution to its titlefragmentfragment where titles from the selected group is retrieved from the db and displayes as list
*Saves the Title name selected by the user from the list item as a sharepreference object then passes execution to TitleContentActivity
*
* */

public class TitleFragment extends AppCompatActivity implements TitleFragmentFragment.StartTitleContent,View.OnClickListener,AdapterView.OnItemClickListener {

    public static final String EXTRA_FOR_ACTIVITY_CONTENT = "aivco.com.studyhelper_TitleFragment";
    private String tag="TitleFragment";
    public static final String ACTION_BAR_KEY_TitleFragment="actionbarkey";
    public static AppCompatActivity finishAcitivty;
    private ImageButton ib;
    private EditText searchbar;
    private static String SEARCHRESULT="recordlist";
    public static TitleFragment titleFragment;
    static List<String> listOfRecords,uniqueKeyList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_fragment);
        finishAcitivty=this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TitleFragment.this, MainActivity.class));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleFragment=this;

    }
    @Override
    protected void onResume() {
        super.onResume();
        TextView tv=(TextView)findViewById(R.id.toolbartv);

        tv.setText(ContentTab.sp.getString(ContentTab.KEY_CHOSEN_GROUP, ""));//sets the action bar title
        tv.setSelected(true);


        ib=(ImageButton)findViewById(R.id.searchicon);
        ib.setOnClickListener(this);

        searchbar=(EditText)findViewById(R.id.searchbar);
        searchbar.setFocusableInTouchMode(false);
        searchbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchbar.setFocusableInTouchMode(true);
                searchbar.requestFocus();

                return false;

            }
        });
        Log.d(tag, "onResume TitleFragment");
    }



    //////////////saves the string that was returned from the onclicked list item,so that it can be used by the next actitity///////
    @Override
    public void startSecActivity(String title)
    {

        System.out.println("the key is*****************"+title);
        SharedPreferenceHelper.setData(ContentTab.TITLECONTENTSELECTIONKEY, title);
        Intent intent=new Intent();
        intent.setClass(this, TitleContentActivity.class);
        intent.putExtra(EXTRA_FOR_ACTIVITY_CONTENT, title);
        startActivity(new Intent(this, TitleContentActivity.class));
    }



    public static void logout(){

        if(finishAcitivty != null)
        finishAcitivty.finish();
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId()){
            case R.id.searchicon:
            {
                searchbar.setFocusableInTouchMode(false);
                searchbar.clearFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

               if(!searchbar.getText().toString().equals(""))
               {
                   new SearchClass().execute(searchbar.getText().toString(),ContentTab.sp.getString(ContentTab.KEY_CHOSEN_GROUP,""));

               }


                break;

            }


        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId())
        {


        }
        startSecActivity(uniqueKeyList.get(position));



    }

    private class SearchClass extends AsyncTask<String,String,List[] >{


         @Override
         protected List[] doInBackground(String[] params) {

             return new TitleInfo(TitleFragment.this).searchResult(params[0],params[1]);
         }

         @Override
         protected void onPostExecute(List[] recordlist) {
             super.onPostExecute(recordlist);

             uniqueKeyList=recordlist[0];
             if(  !recordlist[1].isEmpty())
             {

                 SearchFragment sf=SearchFragment.getInstance();
                 Bundle b=new Bundle();
                 b.putStringArrayList(SEARCHRESULT,(ArrayList)recordlist[1]);
                 sf.setArguments(b);
                 FragmentManager fm=getFragmentManager();
                 android.app.FragmentTransaction ft=fm.beginTransaction();
                 ft.replace(android.R.id.content,sf);
                 ft.commit();
             }

             else{

                 Toast.makeText(TitleFragment.this,getResources().getString(R.string.search_result),Toast.LENGTH_SHORT).show();

             }



         }
     }

    public static class SearchFragment extends Fragment implements AdapterView.OnItemClickListener {

        private static final SearchFragment instance=new SearchFragment();
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


            listOfRecords=getArguments().getStringArrayList(SEARCHRESULT);


        }

        public static SearchFragment getInstance(){


            return instance;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            return inflater.inflate(R.layout.list_utitlity_layout,container,false);
        }


        @Override
        public void onActivityCreated(Bundle savedInstanceState)  {
            super.onActivityCreated(savedInstanceState);
           createListVew();
           createBackButton();




        }

        public void createListVew(){

            ListView recordList=(ListView)getView().findViewById(R.id.list_utility);
            ArrayAdapter<String> listAdapter=new ArrayAdapter<>(getActivity().getBaseContext(),R.layout.textviewlayout,listOfRecords);
            recordList.setAdapter(listAdapter);
            recordList.setOnItemClickListener(titleFragment);

        }

        public void createBackButton(){

           ImageButton b=(ImageButton)getView().findViewById(R.id.backbutton);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            System.out.println("answer is "+listOfRecords.get(position));

            SharedPreferenceHelper.setData(ContentTab.TITLECONTENTSELECTIONKEY, listOfRecords.get(position));
            Intent intent=new Intent();
            intent.setClass(getActivity().getBaseContext(), TitleContentActivity.class);
            intent.putExtra(EXTRA_FOR_ACTIVITY_CONTENT, listOfRecords.get(position));
            startActivity(new Intent(getActivity().getBaseContext(), TitleContentActivity.class));

        }
    }



}
