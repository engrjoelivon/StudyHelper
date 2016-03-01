package aivco.com.studyhelper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import server_commmunication.HandleFirebase;
import server_commmunication.HandleFirebaseInterface;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, HandleFirebaseInterface{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private static final int LOGIN=1;
    private static final int REGISTER=2;
    private static final int REQUESTPASSWORDRESET=3;
    private int loginOrRegister;
    private HandleFirebase handleFirebase;
    static int outCome=0;
    String email,password;

    private TextView result;
    private Button changePassword,requestpasswordchange,mEmailRegisterButton,mEmailSignInButton,returntologin;
    private EditText oldpassword,newpassword,repeatpassword;
    private TextInputLayout emailLayout,passwordLayout,oldpasswordlayout,newpasswordlayout,repeatpasswordlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ///the line below hides the battery,the network and the time////
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        result=(TextView)findViewById(R.id.result);
        oldpassword=(AutoCompleteTextView)findViewById(R.id.oldpassword);
        oldpasswordlayout=(TextInputLayout)findViewById(R.id.oldpasswordTextInputLayout);
        newpasswordlayout=(TextInputLayout)findViewById(R.id.newpasswordTextInputLayout);
        newpassword=(AutoCompleteTextView)findViewById(R.id.newpassword);
        repeatpassword=(AutoCompleteTextView)findViewById(R.id.repeatpassword);
        repeatpasswordlayout=(TextInputLayout)findViewById(R.id.repeatpasswordTextInputLayout);
        emailLayout=(TextInputLayout)findViewById(R.id.emailTextInputLayout);
        passwordLayout=(TextInputLayout)findViewById(R.id.passwordTextInputLayout);
        returntologin=(Button)findViewById(R.id.returntologin);
        returntologin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                enableDisableLoginRegisters();

            }
        });
        requestpasswordchange=(Button)findViewById(R.id.access_to_change_password);
        requestpasswordchange.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

              enableDisablePasswordChange();


            }
        });
        changePassword=(Button)findViewById(R.id.change_password);
        changePassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                View focusView=null;
                // Check for a valid email address.
                if (TextUtils.isEmpty(email)) {
                    mEmailView.setError(getString(R.string.error_field_required));
                    mEmailView.requestFocus();
                    return;



                } else if (!isEmailValid(email)) {
                    mEmailView.setError(getString(R.string.error_invalid_email));
                    mEmailView.requestFocus();

                }
                handleFirebase.resetPassword(email);
            }
        });



        // Set up the login form.

        handleFirebase=new HandleFirebase(this);


        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                System.out.println("......................Oneditor.......................................");
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
         mEmailRegisterButton = (Button) findViewById(R.id.register);
         mEmailRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                loginOrRegister = REGISTER;
                attemptLogin();
            }
        });

         mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                loginOrRegister = LOGIN;
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    public void enableDisablePasswordChange(){

        changePassword. setVisibility(View.VISIBLE);
        mEmailSignInButton.setVisibility(View.GONE);
        mEmailRegisterButton.setVisibility(View.GONE);
        requestpasswordchange.setVisibility(View.GONE);
       // emailLayout.setVisibility(View.GONE);
        passwordLayout.setVisibility(View.GONE);
        mPasswordView.setVisibility(View.GONE);
        result.setVisibility(View.GONE);
       mEmailView.setEnabled(false);

        returntologin.setVisibility(View.VISIBLE);

    }
    public void enableDisableLoginRegisters()
    {
        result.setText("");
        changePassword. setVisibility(View.GONE);
        mEmailSignInButton.setVisibility(View.VISIBLE);
        mEmailRegisterButton.setVisibility(View.VISIBLE);
        requestpasswordchange.setVisibility(View.GONE);
        emailLayout.setVisibility(View.VISIBLE);
        passwordLayout.setVisibility(View.VISIBLE);
        mPasswordView.setVisibility(View.VISIBLE);
        result.setVisibility(View.VISIBLE);
        mEmailView.setVisibility(View.VISIBLE);
        oldpassword.setVisibility(View.GONE);
        newpassword.setVisibility(View.GONE);
        repeatpassword.setVisibility(View.GONE);
        returntologin.setVisibility(View.GONE);
        mEmailView.setEnabled(true);

    }


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            //mAuthTask = new UserLoginTask(email, password);
           // mAuthTask.execute((Void) null);
            switch (loginOrRegister){
                case LOGIN:{
                    System.out.println(".......................Will be loogging on.....................");
                    handleFirebase.logInToFirebase(email,password);

                    break;}
                case REGISTER:{
                    System.out.println(".......................doInBackground register.....................");
                    handleFirebase.registerToFirebase(email,password);

                    break;}


            }







        }
    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public  void login(int code) {
        switch(code){
            case HandleFirebase.SUCCESS:{
                System.out.println("................firbasehandlerInterface.......SUCCESS.....................");
                System.out.println("................my email is....................."+email);
                System.out.println("................password is....................."+password);
                SharedPreferenceHelper.removeData(MainActivity.MYEMAIL);
                SharedPreferenceHelper.removeData(MainActivity.MYPASSWORD);

                SharedPreferenceHelper.setData(MainActivity.MYEMAIL, email);
                SharedPreferenceHelper.setData(MainActivity.MYPASSWORD, password);


                System.out.println("................my email is..after is..................." + SharedPreferenceHelper.getString(MainActivity.MYEMAIL));
                System.out.println("................password after is....................." + SharedPreferenceHelper.getString(MainActivity.MYPASSWORD));

                finish();
                break;


            }
            case HandleFirebase.RESETSUCCESS:{
                System.out.println("................firbasehandlerInterface.......RESETSUCCESS.....................");
                enableDisableLoginRegisters();
                result.setText(getResources().getString(R.string.password_reset_response));
                mPasswordView.setText("");
                mPasswordView.requestFocus();

                break;

            }


        }


    }


    @Override
    public void reg(int code,Map<String, Object> res) {
        System.out.println("....................firbasehandlerInterface...SUCCESS.....................");


        SharedPreferenceHelper.removeData(MainActivity.MYEMAIL);
        SharedPreferenceHelper.removeData(MainActivity.MYPASSWORD);

        SharedPreferenceHelper.setData(MainActivity.MYEMAIL, email);
        SharedPreferenceHelper.setData(MainActivity.MYPASSWORD, password);

        MainActivity.USERNAME=email;
        finish(); }










    @Override
    public void errorCode(FirebaseError firebaseError) {


        switch (firebaseError.getCode()) {
            case FirebaseError.USER_DOES_NOT_EXIST: {

                System.out.println(".............firbasehandlerInterface.....USER_DOES_NOT_EXIST_firebaseError..........................");
                result.setVisibility(View.VISIBLE);
                result.setText(getResources().getString(R.string.user_does_not_exist));
                break;
            }

            case FirebaseError.INVALID_PASSWORD: {

                System.out.println(".........firbasehandlerInterface.........INVALID PASSWORD_firebaseError..........................");
                result.setVisibility(View.VISIBLE);
                result.setText(getResources().getString(R.string.invalid_password));
                requestpasswordchange.setVisibility(View.VISIBLE);

                break;
            }

            case FirebaseError.NETWORK_ERROR: {
                System.out.println(".........firbasehandlerInterface.........NETWORK_ERROR_firebaseError..........................");
                result.setVisibility(View.VISIBLE);
                result.setText(getResources().getString(R.string.connection_error));
                break;
            }
            case FirebaseError.EMAIL_TAKEN: {
                System.out.println("........firbasehandlerInterface..........EMAIL taken..........................");
                requestpasswordchange.setVisibility(View.VISIBLE);
                result.setVisibility(View.VISIBLE);
                result.setText(getResources().getString(R.string.email_taken));
                break;
            }


        }

    }


    @Override
    public void onBackPressed() {
        MainActivity.setBackpressed(true);
        finish();
       // super.onBackPressed();
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }




}




