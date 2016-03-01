package aivco.com.studyhelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.prefs.Preferences;
/*
recieves execution order from Content tab
* moves execution to its titlefragmentfragment where titles from the selected group is retrieved from the db and displayes as list
*Saves the Title name selected by the user from the list item as a sharepreference object then passes execution to TitleContentActivity
*
* */

public class TitleFragment extends AppCompatActivity implements TitleFragmentFragment.StartTitleContent {

    public static final String EXTRA_FOR_ACTIVITY_CONTENT = "aivco.com.studyhelper_TitleFragment";
    private String tag="TitleFragment";
    public static Handler h;
    public static final String ACTION_BAR_KEY_TitleFragment="actionbarkey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_fragment);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TitleFragment.this, MainActivity.class));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.d(tag, "onCreate TitleFragment");


    }
    @Override
    protected void onResume() {
        super.onResume();
        TextView tv=(TextView)findViewById(R.id.toolbartv);
        getSupportActionBar().setTitle("");
        tv.setText(ContentTab.sp.getString(ContentTab.KEY_CHOSEN_GROUP, ""));//sets the action bar title


        Log.d(tag, "onResume TitleFragment");
    }



    //////////////saves the string that was returned from the onclicked list item,so that it can be used by the next actitity///////
    @Override
    public void startSecActivity(String title)
    {

        Log.d(tag, "startSecActivity TitleFragment");
        SharedPreferences.Editor editer=ContentTab.sp.edit();
        editer.putString(ContentTab.TITLECONTENTSELECTIONKEY,title);
        editer.commit();
        Intent intent=new Intent();
        intent.setClass(this,TitleContentActivity.class);
        intent.putExtra(EXTRA_FOR_ACTIVITY_CONTENT, title);
        startActivity(new Intent(this, TitleContentActivity.class));
    }



    @Override
    protected void onStop() {
        super.onStop();
        Log.d(tag, "onStop TitleFragment");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(tag, "onPause TitleFragment");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(tag, "Destroy TitleFragment");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(tag, "onStart() TitleFragment");
    }

}
