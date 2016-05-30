package aivco.com.studyhelper;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import aivco.com.studyhelper.TitleFragmentFragment.HandleSearch;
/*
recieves execution order from Content tab
* moves execution to its titlefragmentfragment where titles from the selected group is retrieved from the db and displayed as list
*Saves the Title name selected by the user from the list item as a sharepreference object then passes execution to TitleContentActivity
*
* Class ui is handled by titlefragmentfragment,the fragment is directy added to activitytitlefragment.
* */

public class TitleFragment extends AppCompatActivity implements TitleFragmentFragment.StartTitleContent,AdapterView.OnItemClickListener {

    private static final String EXTRA_FOR_ACTIVITY_CONTENT = "aivco.com.studyhelper_TitleFragment";
    private String tag="TitleFragment";
    public static final String ACTION_BAR_KEY_TitleFragment="actionbarkey";
    private static AppCompatActivity finishAcitivty;
    private static String SEARCHRESULT="recordlist";
    public static TitleFragment titleFragment;
    static List<String> listOfRecords,uniqueKeyList;
    private TextView tv;
    private SearchView searchView;
    private HandleSearch handleSearch; //interface from titleFragmentFragment


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_fragment);
        finishAcitivty=this;

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tv=(TextView)findViewById(R.id.toolbartv);

        tv.setText(ContentTab.sp.getString(ContentTab.KEY_CHOSEN_GROUP, ""));//sets the action bar title
        tv.setSelected(true);




        searchView=(SearchView)findViewById(R.id.search_view);
        searchView.setFocusable(false);
        searchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchView.setFocusableInTouchMode(true);
                searchView.requestFocus();

                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag,"touched");
                tv.setVisibility(View.GONE);


            }
        });



        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d(tag,"closed");
                tv.setVisibility(View.VISIBLE);
                handleSearch.close();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.d(tag,"text submitted "+query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(tag,"text entered by user "+newText);
                handleSearch.searchText(newText);   //passes the text from the search bar to titlefragmentfragment handlesearch interface

                return false;
            }
        });

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


        Log.d(tag, "onResume TitleFragment");
    }






    //////////////saves the string that was returned from the onclicked list item,so that it can be used by the next actitity///////
    @Override
    public void startSecActivity(String title)
    {

        SharedPreferenceHelper.setData(ContentTab.TITLECONTENTSELECTIONKEY, title);
        Intent intent=new Intent();
        intent.setClass(this, TitleContentActivity.class);
        intent.putExtra(EXTRA_FOR_ACTIVITY_CONTENT, title);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(new Intent(this, TitleContentActivity.class));
    }



    public static void logout(){

        if(finishAcitivty != null)
        finishAcitivty.finish();
    }







    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        switch (view.getId())
        {


        }
        startSecActivity(uniqueKeyList.get(position));



    }







    //setter method to set handle search recieved from Titlefragment
    void setSearchInterface(HandleSearch searchInterface)
    {

        this.handleSearch=searchInterface;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        System.out.println("...................title from services onOptionsItemSelected j....................");
        return super.onOptionsItemSelected(item);

    }
}




