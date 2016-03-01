package aivco.com.studyhelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.firebase.client.FirebaseError;

import java.util.Map;

import server_commmunication.HandleFirebase;
import server_commmunication.HandleFirebaseInterface;
import server_commmunication.UpdateServer;


/*
* Recieves execution order from titlefragment activity.Starts its TitleContentActivityFragment.
*
*
*
*
* */



public class TitleContentActivity extends AppCompatActivity implements View.OnClickListener, HandleFirebaseInterface {
    public static String tag="TitleContentActivity";
    public static Handler h;
    public static final String ACTION_BAR_KEY="actionbarkey";
    static TitleContentActivityFragment.Toolbarinterface myToolbarinterface;
    private ImageButton toolbardelete,toolbaredit,toolbarsave;
    public static HandleFirebase handleFirebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_content);

        h = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                int a = message.what;
                Bundle bundle = message.getData();
                String data = bundle.getString(ACTION_BAR_KEY);

                getSupportActionBar().setTitle("");


                return true;
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbardelete =(ImageButton)findViewById(R.id.toolbardelete);
        toolbaredit=(ImageButton)findViewById(R.id.toolbaredit);
        toolbarsave=(ImageButton)findViewById(R.id.toolbarsave);
        toolbardelete.setOnClickListener(this);
        toolbaredit.setOnClickListener(this);
        toolbarsave.setOnClickListener(this);
        handleFirebase=new HandleFirebase(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TitleContentActivity.this, MainActivity.class));
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView tv = (TextView) findViewById(R.id.tvcontenttoolbar);
        tv.setText(ContentTab.sp.getString(ContentTab.TITLECONTENTSELECTIONKEY, ""));
        Log.d(tag, "Oncreate of title content activity");

    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.titlecontentmenu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);




    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.toolbardelete)
        {




            Log.d(tag, "i am deleteing");
            myToolbarinterface.upDatedelete();
        }
        else if(view.getId() == R.id.toolbaredit)
        {


            Log.d(tag, "i am editing");
            toolbarsave.setVisibility(View.VISIBLE);
            myToolbarinterface.upDateedit();
        }
        else if(view.getId() == R.id.toolbarsave)
        {



            Log.d(tag, "i am saving it");


            myToolbarinterface.savebutton();
        }



    }


    public static  void toolbarInterfaceListener(TitleContentActivityFragment.Toolbarinterface toolbarinterface)
    {
        myToolbarinterface=toolbarinterface;

    }


    //////////////handles radio click events////////////////
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.low_p:{
                if (checked)
                    myToolbarinterface.sendpriorityValue(1);




                    break;}
            case R.id.med_p:{
                if (checked)
                    myToolbarinterface.sendpriorityValue(2);
                // Pirates are the best



                    break;}


            case R.id.high_p:
                if (checked){
                    myToolbarinterface.sendpriorityValue(3);
                    // Pirates are the best



                    break;}
            case R.id.diff_low:
                if (checked){
                    myToolbarinterface.senddiffValue(1);

                // Pirates are the best



                    break;}
            case R.id.diff_med:
                if (checked)
                {
                    myToolbarinterface.senddiffValue(2);

                // Pirates are the best



                    break;}
            case R.id.diff_high:
                if (checked)
                {
                    myToolbarinterface.senddiffValue(3);

                // Pirates are the best



                    break;}
        }
    }


    @Override
    public void login(int code) {

    }

    @Override
    public void reg(int code, Map<String, Object> res) {

    }

    @Override
    public void errorCode(FirebaseError fe) {

    }
}
