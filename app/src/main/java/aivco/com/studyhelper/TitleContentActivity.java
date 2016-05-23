package aivco.com.studyhelper;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
    private ImageButton toolbarsave;
    public static HandleFirebase handleFirebase;
    public static String UNIQUE_KEY="uniquekeytosearchtable";
    FloatingActionButton fab;
    TextView tv;
    public static AppCompatActivity finishAcitivty;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        ImageButton toolbardelete,toolbaredit, toolbarmove;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_content);
        finishAcitivty=this;
        h = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                int a = message.what;
                if(a == 2)
                {
                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                    );
                    return true;
                }
                Bundle bundle = message.getData();
                    String data = bundle.getString(ACTION_BAR_KEY);
                    tv.setText(data);




                return true;
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbardelete =(ImageButton)findViewById(R.id.toolbardelete);
        toolbaredit=(ImageButton)findViewById(R.id.toolbaredit);
        toolbarsave=(ImageButton)findViewById(R.id.toolbarsave);
        toolbarmove=(ImageButton)findViewById(R.id.toolbarmove);
        toolbardelete.setOnClickListener(this);
        toolbaredit.setOnClickListener(this);
        toolbarsave.setOnClickListener(this);
        toolbarmove.setOnClickListener(this);
        handleFirebase=new HandleFirebase(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(TitleContentActivity.this, MainActivity.class));
                TitleFragment.logout();
                finish();

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tv = (TextView) findViewById(R.id.tvcontenttoolbar);
        tv.setSelected(true);

    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.titlecontentmenu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        System.out.println("********************home button pressed************************");

        return super.onOptionsItemSelected(item);




    }*/

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.toolbardelete)
        {


            new DeleteFragment().show(getFragmentManager(), "");



        }
        else if(view.getId() == R.id.toolbaredit)
        {
            toolbarsave.setVisibility(View.VISIBLE);
            myToolbarinterface.upDateedit();
            fab.setVisibility(View.INVISIBLE);
        }
        else if(view.getId() == R.id.toolbarsave)
        {
            toolbarsave.setVisibility(View.INVISIBLE);
            fab.setVisibility(View.VISIBLE);
            myToolbarinterface.savebutton();
            h.sendEmptyMessage(2);
        }

        else if(view.getId() == R.id.toolbarmove)
        {
            myToolbarinterface.setmove();
            System.out.println("printing move...........................");
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
            case R.id.diff_zero:
                if (checked){
                    myToolbarinterface.senddiffValue(0);

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
        Toast.makeText(this, getResources().getString(R.string.textUpdated), Toast.LENGTH_SHORT).show();


    }

    @Override
    public void reg(int code, Map<String, Object> res) {

    }

    @Override
    public void errorCode(FirebaseError fe) {

    }

    public static class DeleteFragment extends DialogFragment{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())// Set Dialog Icon
                    .setIcon(android.R.drawable.dialog_frame)
                    .setTitle("choose an option")

                    .setMessage("Are you sure you want to Delete")
                    .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(tag, "i am deleteing");
                            myToolbarinterface.upDatedelete();

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,   int which) {
                            // Do something else
                        }
                    }).create();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);

        }


    }

    public static void logout(){

        if(finishAcitivty != null)
        finishAcitivty.finish();
    }

}
