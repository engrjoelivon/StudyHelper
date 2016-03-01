package aivco.com.studyhelper;

import android.app.FragmentManager;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.firebase.client.Firebase;

import java.net.CookieHandler;
import java.net.CookieManager;

import server_commmunication.HandleFirebase;

public class MainActivity extends TabActivity implements  Login_Dialog_Fragment.DialogControl{
    public static final String MYEMAIL ="useremail" ;
    public static final String MYPASSWORD ="userpassword" ;
    private Login_Dialog_Fragment ldg;
    private User user;
    static SharedPreferences sharedPreferences;
    public static  String USERNAME="studyhelper_username";
    public static final String USERNAME_KEY="username";
    public static final String PASSWORD="studyhelper_password";
    private String tag="studyhelper_main";
    public static boolean backpressed=false;
    public static SharedPreferenceHelper sharedPreferenceHelper;
    public static final String DATASTORED_KEY ="stored";//represent keys for data stored in the database.
    public static String DATASTORED_KEY_VALUE ="datastored";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        sharedPreferenceHelper=new SharedPreferenceHelper(this);
        sharedPreferences=getSharedPreferences("NAME", MODE_PRIVATE);


        if(sharedPreferenceHelper.getString(USERNAME_KEY)==null)
        {
            System.out.println("................Main activty on create it is null..........................");

            startActivity(new Intent(this,LoginActivity.class));
        }


        ///the line below hides the battery,the network and the time////
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TabHost tabHost=getTabHost();
        Resources resource=getResources();

        TabSpec tabSpecHome=tabHost.newTabSpec("Home")
                .setIndicator("", resource.getDrawable(R.drawable.home_config))
                .setContent(new Intent(this, HomeTab.class));


        TabSpec tabSpecAdd=tabHost.newTabSpec("Add")
                .setIndicator("",resource.getDrawable(R.drawable.add_config))
                .setContent(new Intent(this, AddTab.class));


        TabSpec tabSpecContents=tabHost.newTabSpec("Content")
                .setIndicator("",resource.getDrawable(R.drawable.contents_config))
                .setContent(new Intent(this, ContentTab.class));


        TabSpec tabSpecSettings=tabHost.newTabSpec("Setting")
                .setIndicator("",resource.getDrawable(R.drawable.settings_config))
                .setContent(new Intent(this, SettingsActivity.class));




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







    }

    public static void setBackpressed(boolean value){

        System.out.println("................setting back pressed........................." );
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

    ////interace removedialog method from the Login_Dialog_Fragment.It returns the user object,which could either represent login or signup depending on the entercode///
    @Override
    public void removeDialog(User user,int entercode) {
        Log.d(tag,"code value" +entercode);
        Log.d(tag,"email is " +user.getEmail());
        Log.d(tag,"password is " +user.getPassword());
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
        Log.d(tag,"signedup");

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
    protected void onPause() {
        super.onPause();
        backpressed=false;
    }
}
