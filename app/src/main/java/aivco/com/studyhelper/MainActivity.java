package aivco.com.studyhelper;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import com.firebase.client.Firebase;
import android.os.Handler;
import backend.DbService;
import server_commmunication.HandleFirebase;

public class MainActivity extends TabActivity implements  Login_Dialog_Fragment.DialogControl{
    public static final String MYEMAIL ="useremail" ;
    public static final String MYPASSWORD ="userpassword" ;
    public static final String KEYFORNUMBEROFTIMEVISITED = "MainActivitynumberoftime";

    private Login_Dialog_Fragment ldg;
    private User user;
    static SharedPreferences sharedPreferences;
    public static  String USERNAME="studyhelper_username";
    public static final String USERNAME_KEY="username";
    public static final String PASSWORD="studyhelper_password";
    public static final String DEVICENAME="studyhelper_devicename";
    private String tag="studyhelper_main";
    public static boolean backpressed=false;
    public static SharedPreferenceHelper sharedPreferenceHelper;
    public static final String DATASTORED_KEY ="stored";//represent ServerKeys for data stored in the database.
    public static String DATASTORED_KEY_VALUE ="datastored";
    public static Context mycontext;
    public static final String KEYFORDIFFICULTYHIGH="MainActivity_high";
    public static final String KEYFORDIFFICULTYMED="MainActivity_med";
    public static final String KEYFORDIFFICULTYLOW="MainActivity_low";
    public static final String KEYFORDIFFICULTYZERO ="MainActivity_zero" ;
    public static Handler handler;
    public ProgressDialog progressDialog;
    public static Activity finishAcitivty;
    public static final String TITLEKEY="key_for_title";//title to use when calling confirmation dialog
    public static final String MESSAGEKEY="key_for_message";//key_to_use_to_obtain message from bundle in confirmation dialog


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        finishAcitivty=this;
        Firebase.setAndroidContext(this);
        sharedPreferenceHelper=new SharedPreferenceHelper(this);
        sharedPreferences = getSharedPreferences("NAME", MODE_PRIVATE);
        mycontext=getApplicationContext();
        handler
                =new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                int a=message.what;
                switch (a){

                }
                progressDialog.cancel();


                return false;
            }
        });


        ////set default values for app  //below default values are entered for the app,this values can be set by the user from the app
        //settings default value is 10.
        if(sharedPreferenceHelper.getInt(KEYFORDIFFICULTYHIGH)== 0)
        {
            sharedPreferenceHelper.setData(KEYFORDIFFICULTYZERO,7);//diffulty for zero is set at 7days
            sharedPreferenceHelper.setData(KEYFORDIFFICULTYHIGH,12);//difficulty for high is set at 12hrs
            sharedPreferenceHelper.setData(KEYFORDIFFICULTYMED,24);//24hrs
            sharedPreferenceHelper.setData(KEYFORDIFFICULTYLOW,36); //first time application runs default value for questions with difficulty low visited is set at 36
            sharedPreferenceHelper.setData(KEYFORNUMBEROFTIMEVISITED,10);  //first time application runs default value for number of time visited is set at 10

        }


        if(sharedPreferenceHelper.getString(USERNAME_KEY)==null)
        {
            System.out.println("................Main activty on create it is null..........................");

            startActivity(new Intent(this,LoginActivity.class));
        }


        //DbService.startActionForExpiry(this);
        ///the line below hides the battery,the network and the time////
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TabHost tabHost=getTabHost();
        Resources resource=getResources();

        TabSpec tabSpecHome=tabHost.newTabSpec("Home")
                .setIndicator("", resource.getDrawable(R.drawable.home_config))
                .setContent(new Intent(this, HomeTab.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));


        TabSpec tabSpecAdd=tabHost.newTabSpec("Add")
                .setIndicator("",resource.getDrawable(R.drawable.add_config))
                .setContent(new Intent(this, AddTab.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));


        TabSpec tabSpecContents=tabHost.newTabSpec("Content")
                .setIndicator("",resource.getDrawable(R.drawable.contents_config))
                .setContent(new Intent(this, ContentTab.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));


        TabSpec tabSpecSettings=tabHost.newTabSpec("Setting")
                .setIndicator("",resource.getDrawable(R.drawable.settings_config))
                .setContent(new Intent(this, SettingsActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));




        tabHost.addTab(tabSpecHome);
        tabHost.addTab(tabSpecAdd);
        tabHost.addTab(tabSpecContents);
        tabHost.addTab(tabSpecSettings);


        tabHost.setCurrentTab(0);




    }

    @Override
    protected void onResume() {

        super.onResume();
        System.out.println("................onResume.........................." + sharedPreferenceHelper.getString("username"));


if(backpressed)
{finish();}
        else{
    System.out.println("................backpressed is false.........................." );
    System.out.println("................my email is..after is...MAIN ACTIVITY................" + SharedPreferenceHelper.getString(MainActivity.MYEMAIL));
    System.out.println("................password after is.....MAIN ACTIVITY................" + SharedPreferenceHelper.getString(MainActivity.MYPASSWORD));


}
        user=new User();
        user.setUsername("engrjoelivon@engrjoeivon");
        user.setPassword("nawoitomo27");




        if(SharedPreferenceHelper.getString(MYEMAIL) != null)
        {

            //new ServerWithIon(this).checkRecordAtStartUp("checkinserts/");
            //new TitleInfo(this).calculateIfExpired();
           System.out.println(Thread.currentThread());
            //decided to make the checkupdate all method run at the background thread,another option was to make it run at the main thread
            //start a progress dialog then,then wait for a call via the handler to end the dialog.I put away that option
            //because since the checkupdate method runs three calls it will be hard to monitor when each of the calls returns from call to server.
            //ran expriy first so if any changes are made and passed to the table they can be moved to the server once
            DbService.startActionForExpiry(this);
            DbService.StartUpdateAllLocal(this);
            //DbService.startActionForgenerateQA(this);
            DbService.startActionToValidateUser(this);


        }




    }

    public static void setBackpressed(boolean value){

        System.out.println("................setting back pressed.........................");
backpressed=value;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void runProgress(){
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Connecting ..........");
        progressDialog.show();
    }

    ////interace removedialog method from the Login_Dialog_Fragment.It returns the user object,which could either represent login or signup depending on the entercode///
    @Override
    public void removeDialog(User user,int entercode) {
        Log.d(tag, "code value" + entercode);
        Log.d(tag, "email is " + user.getEmail());
        Log.d(tag, "password is " + user.getPassword());
        if(entercode == 0)
        login(user);

        else{
            signup(user);
        }
        ldg.dismiss();
    }

    @Override
    public void saveUser() {

    }

   /////////////login user to both the application ////////////////////////////////////
    public void login(User user)
    {

        storeNamePassword(user.getEmail(),user.getPassword());
        Log.d(tag,"loggedin");

    }
    ///////////signup user for both application  ////////////////////////////////////////
    public void signup(User user){
        storeNamePassword(user.getEmail(),user.getPassword());
        Log.d(tag, "signedup");

    }

    /////inflates the login and signup fragment/////
    public void login_signup()
    {

        FragmentManager fm=getFragmentManager();
        ldg=new Login_Dialog_Fragment();
        ldg.show(fm, "my fragement");

    }

    public void storeNamePassword(String name,String password)
    {
    SharedPreferences.Editor editer=sharedPreferences.edit();
        editer.putString(USERNAME,name);
        editer.putString(PASSWORD,password);
        editer.commit();
        Log.d(tag, "usernameandpasswordstored");

    }

    ///////////perform check to see if there is internet before making a connection////////////////
    public boolean internetchecker()

    {
        ConnectivityManager cm=(ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nI[]=cm.getAllNetworkInfo();
        for(NetworkInfo info:nI)
        {

            if(info.getState()== NetworkInfo.State.CONNECTED)
            {return true;}
        }
        return false;
    }





    public class LoginSignUpusertoserver extends AsyncTask<User,String,Boolean>
    {


        @Override
        protected Boolean doInBackground(User... users)
        {
            Log.d(tag,"do in background");
            LoginToServer loginToServer=new LoginToServer(user);
            String htmlString=loginToServer.doGet("http://192.168.100.13:8000/login/");
            loginToServer.doPost("http://192.168.100.13:8000/login/",htmlString);

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        sharedPreferenceHelper.setData(HandleFirebase.SELFCHANGED, null);
        backpressed=false;
    }


   public static void logout(){

       finishAcitivty.finish();
   }



}
