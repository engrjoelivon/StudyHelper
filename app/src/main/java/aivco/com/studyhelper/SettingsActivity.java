package aivco.com.studyhelper;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.List;
import java.util.Map;

import server_commmunication.HTTPServer;
import server_commmunication.HandleFirebase;
import server_commmunication.HandleFirebaseInterface;
import server_commmunication.LogingInRegisteringResult;
import server_commmunication.Logout;
import server_commmunication.ServerWithIon;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity implements AdapterView.OnItemSelectedListener, HandleFirebaseInterface, Logout, LogingInRegisteringResult {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */

    static SettingsActivity settingsActivity;
    static HandleFirebase handleFirebase;
    private static String email;
    private static String userpassword;
    static ChangePassword changePasswordfragment;
    static ChangeUsername changeUsernamefragment;
    static private boolean usernamefragment;
    static String myNewPassword,myNewEmail;//they represent values for email and password,when its changed by uses
    static HandleProgressDialog hpd;
    static ProgressDialog pg;


    public SettingsActivity() {
        settingsActivity=this;


    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                                                System.out.println(".................list preference............................");

                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            }
            else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
                            if(preference.getKey().equals("key_logout")) {


                                System.out.println(".................hello world............................");
                                       logOut();
                            }

                System.out.println(".................preference............................");


            }
            return true;
        }
    };



    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }
    public static void logOut()
    {
        System.out.println(".................close...........................");
         MainActivity.sharedPreferenceHelper.setData(MainActivity.USERNAME_KEY, null);
        MainActivity.sharedPreferenceHelper.setData(MainActivity.MYEMAIL, null);
        ContentTab.logout();
        ContentTab.loggoutIn=false;

              TitleContentActivity.logout();
              TitleFragment.logout();

              settingsActivity.finish();

       MainActivity.setBackpressed(true);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        handleFirebase=new HandleFirebase(this);
        email=SharedPreferenceHelper.getString(MainActivity.MYEMAIL);
        userpassword=SharedPreferenceHelper.getString(MainActivity.MYPASSWORD);
                System.out.println("................my email is..after is...MAIN ACTIVITY................" + SharedPreferenceHelper.getString(MainActivity.MYEMAIL));
        System.out.println("................password after is.....MAIN ACTIVITY................" + SharedPreferenceHelper.getString(MainActivity.MYPASSWORD));


    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || LogoutPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName)
                       || ChangePassword.class.getName().equals(fragmentName)
        || ChangeUsername.class.getName().equals(fragmentName)
                || Comment.class.getName().equals(fragmentName)


                ;




    }

    @Override
    public  void login(int code) {
        switch(code){
            case HandleFirebase.PASSWORDCHANGED:{
                System.out.println("................firbasehandlerInterface.......PASSWORDCHANGED.....................");

                SharedPreferenceHelper.setData(MainActivity.MYPASSWORD,myNewPassword);
                finish();
                break;


            }
            case HandleFirebase.USERNAMECHANGED:{
                System.out.println("................firbasehandlerInterface.......USERNAMECHANGED.....................");

                SharedPreferenceHelper.setData(MainActivity.MYEMAIL, myNewEmail);

                finish();
                break;
            }


        }


    }


    @Override
    public void reg(int code,Map<String, Object> res) {
        System.out.println("....................firbasehandlerInterface...SUCCESS.....................");

         }










    @Override
    public void errorCode(FirebaseError firebaseError) {


        switch (firebaseError.getCode()) {
            case FirebaseError.USER_DOES_NOT_EXIST: {

                System.out.println(".............firbasehandlerInterface.....USER_DOES_NOT_EXIST_firebaseError..........................");

                break;
            }

            case FirebaseError.INVALID_PASSWORD: {

                System.out.println(".........firbasehandlerInterface.........INVALID PASSWORD_firebaseError..........................");
                if(usernamefragment){
                    changeUsernamefragment.setResult(getString(R.string.username_changed_error));
                }
                else{
                    changePasswordfragment.setResult(getString(R.string.username_changed_error));



                }

                break;
            }

            case FirebaseError.NETWORK_ERROR: {
                System.out.println(".........firbasehandlerInterface.........NETWORK_ERROR_firebaseError..........................");

                if(usernamefragment){

                    changeUsernamefragment.setResult(getString(R.string.connection_error));

                }
                else{
                    changePasswordfragment.setResult(getString(R.string.connection_error));
                }
                break;
            }
            case FirebaseError.EMAIL_TAKEN: {
                System.out.println("........firbasehandlerInterface..........EMAIL taken..........................");
                changeUsernamefragment.setResult(getString(R.string.email_taken));
                break;
            }


        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void endApp() {
        System.out.println(".................close...........................");
        MainActivity.sharedPreferenceHelper.setData(MainActivity.USERNAME_KEY, null);
        MainActivity.sharedPreferenceHelper.setData(MainActivity.MYEMAIL,null);
        settingsActivity.finish();

        MainActivity.setBackpressed(true);

    }

    @Override
    public void successOrFailed(int code) {
        switch(code){
            case PASSWORDCHANGEDSUCCESFULLY:
            {
                pg.cancel();
                finish();
                break;
            }
            case USERNAMECHANGEDSUSSESFULLY:
            {
                pg.cancel();
                finish();
                break;
            }
            case COMMENTPOSTEDSUCCESFULLY:
            {
                break;
            }
            case EMAILVERIFIED:
            {
                break;
            }


        }

    }

    @Override
    public void resetOrVerify(String urlToResetOrVerifyUser) {

    }


    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.list_utitlity_layout,container,false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
           ListView listview= (ListView) getView().findViewById(R.id.list_utility);
            ImageButton backbutton= (ImageButton) getView().findViewById(R.id.backbutton);
            backbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            ArrayAdapter<String> ar=new ArrayAdapter<>(getActivity(),R.layout.textviewlayout);

           // Customized_Utility_List  ar=new Customized_Utility_List(getActivity(),R.layout.customized_utility_list_layout);

            ar.add(getResources().getString(R.string.about));
            ar.add(getResources().getString(R.string.app_settings));
            //checks if user is verified.If verified does not give the option to verify user,otherwise it gives the option to verify user
            if(!new SharedPreferenceHelper(settingsActivity).getBool(SharedPreferenceHelper.getString(MainActivity.MYEMAIL)))
            {
                ar.add(getResources().getString(R.string.verify));
            }



            listview.setAdapter(ar);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     switch(position){
                         case 0:{break;}
                         case 1:{break;}
                         case 2:{ new ServerWithIon(settingsActivity).validate();        break;}
                     }}
            });
        }

        public boolean validatedOrNot(){

           return new SharedPreferenceHelper(settingsActivity).getBool(HTTPServer.VALIDATION_KEY);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class LogoutPreferenceFragment extends PreferenceFragment {
        public LogoutPreferenceFragment() {

        }

        @Override

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
           addPreferencesFromResource(R.xml.logout);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            //
            bindPreferenceSummaryToValue(findPreference("key_logout"));
        }

}




    public static class ChangeUsername extends PreferenceFragment{
        AutoCompleteTextView password;
        Button changeusername;
        private AutoCompleteTextView newEmail;
        private Firebase myFirebaseRef;
        TextView result;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //myFirebaseRef = new Firebase("https://amber-heat-4308.firebaseio.com/");
            pg=new ProgressDialog(settingsActivity);
            changeUsernamefragment=this;
            usernamefragment=true;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View view=inflater.inflate(R.layout.changeusername, container, false);
            password    =  (AutoCompleteTextView)view.findViewById(R.id.password);
            newEmail   =  (AutoCompleteTextView)view.findViewById(R.id.newEmail);
            result=  (TextView)view.findViewById(R.id.result);
            changeusername = (Button) view.findViewById(R.id.changeusername);
            changeusername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    setProgress();
                    if(!new ServerWithIon(getActivity()).changeUsername(newEmail.getText().toString(), password.getText().toString()))
                    {
                        pg.cancel();
                        result.setVisibility(View.VISIBLE);
                        result.setText(getResources().getString(R.string.error_incorrect_password));
                    }


                }


            });
            return view;
        };


        @Override
        public void onStop() {
            super.onPause();
            usernamefragment=false;
        }


        public  void setResult(String errorText){
            result.setVisibility(View.VISIBLE);
            result.setText(errorText);


        }


    }


    public static class ChangePassword extends PreferenceFragment{
        AutoCompleteTextView newPassword1,newPassword2;
        Button changepass;
        TextView result;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            pg=new ProgressDialog(settingsActivity);
            changePasswordfragment=this;
        }
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


            View view=inflater.inflate(R.layout.changepassword, container, false);
            newPassword1    =  (AutoCompleteTextView)view.findViewById(R.id.newpassword1);
            newPassword2=  (AutoCompleteTextView)view.findViewById(R.id.newpassword2);
            final AutoCompleteTextView currentPassword = (AutoCompleteTextView) view.findViewById(R.id.current_password);
            result=  (TextView)view.findViewById(R.id.result);

            changepass = (Button) view.findViewById(R.id.chagepass);
            changepass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myNewPassword=newPassword1.getText().toString();


                    //handleFirebase.changePassword(email, userpassword, myNewPassword);
                    setProgress();
                    if(!new ServerWithIon(getActivity()).changePassword(myNewPassword,currentPassword.getText().toString())){

                        pg.cancel();
                        result.setVisibility(View.VISIBLE);
                        result.setText(getResources().getString(R.string.error_incorrect_password));
                    }





                }


            });
            return view;
        };



        public  void setResult(String errorText){
            result.setVisibility(View.VISIBLE);
            result.setText(errorText);


        }


    }

    public static class Comment extends PreferenceFragment{

        Button sendcomment;
       EditText comment;



        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            pg=new ProgressDialog(settingsActivity);

            hpd=new HandleProgressDialog() {
                @Override
                public void success(boolean succ) {
                    FragmentManager fragmentManager=getFragmentManager();
                    Bundle bundle=new Bundle();


                    pg.cancel();
                    if(succ)
                    {

                        UpdateFragment updateFragment=new UpdateFragment();
                        bundle.putString("message","Message have been succesfully posted,would you like to quit");
                        updateFragment.setArguments(bundle);
                        updateFragment.show(fragmentManager, "tag");




                    }
                    else
                    {
                        UpdateFragment updateFragment=new UpdateFragment();
                        bundle.putString("message","Could not send comment due to internet connection error,would you like to quit");
                        updateFragment.setArguments(bundle);
                        updateFragment.show(fragmentManager,"tag");
                    }


                }
            };


        }
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


            View view=inflater.inflate(R.layout.comment, container, false);
            sendcomment    =  (Button)view.findViewById(R.id.sendtext);
            comment=  (EditText)view.findViewById(R.id.entercomment);




            sendcomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setProgress();

                    new ServerWithIon(MainActivity.mycontext).postComment(comment.getText().toString(), hpd);


                }
            });



            return view;
        };


    }

    public static class UpdateFragment extends DialogFragment {
    String message;
        public UpdateFragment() {

        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
          Bundle b=  getArguments();
            message= b.getString("message");
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())// Set Dialog Icon
                    .setIcon(android.R.drawable.dialog_frame)
                    .setTitle("choose an option")

                    .setMessage(message)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            getActivity().finish();


                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
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



    ///handles interaction between Comment class and the postComment method of the serverwithion class/////
   public interface HandleProgressDialog
   {

       public void success(boolean succ);

   }

    public static void setProgress()
    {

        System.out.println("starting progress");
        pg.setMessage("connecting....");
        pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pg.setCanceledOnTouchOutside(false);
        pg.show();


    }

}
