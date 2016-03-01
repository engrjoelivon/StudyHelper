package aivco.com.studyhelper;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by johnanderson1 on 9/12/15.
 */
public class Login_Dialog_Fragment extends DialogFragment implements View.OnClickListener {

    private EditText password,confirm_password,emailEditText;
    private String tag="studyhelper_Login_Dialog";
    private DialogControl dialogControl;
    Button signupB, loginB,enterB;
    private RegistrationValidation rV;
    final String FIRST_NAME_KEY="userkey";
    protected final String PASSWORKD_KEY="pass";
    final String EMAIL_KEY="email";
    final String LAST_KEY="lastkey";
    int ENTERCODE=0;
    User user;

    public Login_Dialog_Fragment(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
         user=null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.login_dialog_fragment, container, false);

            getDialog().setTitle("SignUp/Login ");




        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        rV=new RegistrationValidation();

        confirm_password=(EditText)getView().findViewById(R.id.confirm_password);
        password=(EditText)getView().findViewById(R.id.password);
        emailEditText=(EditText)getView().findViewById(R.id.email);
         enterB =(Button)getView().findViewById(R.id.enter);enterB.setOnClickListener(this);
         loginB =(Button)getView().findViewById(R.id.login);loginB.setOnClickListener(this);
         signupB =(Button)getView().findViewById(R.id.signup);signupB.setOnClickListener(this);

}

    @Override
    public void onClick(View view) {
        ////when user is usingsignup////
        if(view==signupB)
        {
            confirm_password.setVisibility(View.VISIBLE);
            signupB.setTextColor(getResources().getColor(R.color.buttonclicked));
            loginB.setTextColor(getResources().getColor(R.color.onfocus));

            ENTERCODE=1;
        }
        ///when user is using login///
        else if(view==loginB)
        {
            confirm_password.setVisibility(View.INVISIBLE);
            loginB.setTextColor(getResources().getColor(R.color.buttonclicked));
            signupB.setTextColor(getResources().getColor(R.color.onfocus));

            ENTERCODE=0;

        }
        else if(view==enterB)
        {
            Log.d(tag,"enter button");
            //getDialog().cancel();

             if(ENTERCODE == 0)
             {
                 if(validatelogin())
                 {
                     Log.d(tag, "validate login is true");
                     this.dialogControl.removeDialog(user, ENTERCODE)

                 ;}

             }
            else
             {
                 Log.d(tag, "validate signup is true");
                 if(validatesignup())
                 { Log.d(tag, "validate login is true");
                     this.dialogControl.removeDialog(user, ENTERCODE);
                  }

             }

            Log.d(tag,"end enter button");
        }
    }
    public boolean validatelogin()
    {boolean res=false;
       if( rV.emailValidation(emailEditText.getText().toString()) )
           if(rV.passwordValidation(password.getText().toString()))
             {
                 user=new User(emailEditText.getText().toString(),password.getText().toString());
                 res=true;
             }
           else{ getDialog().setTitle("pass is invalid "); }
       else
       {
           getDialog().setTitle("Email is invalid ");
           res=false;
       }
        Log.d(tag, "validate login boolean " + res);
    return res;
    }

    public boolean validatesignup()
    {
      boolean res=false;
        if(rV.emailValidation(emailEditText.getText().toString()))
            if(rV.ConfirmPassword(password.getText().toString(), confirm_password.getText().toString()))
            {
                user=new User(emailEditText.getText().toString(),password.getText().toString());
                res=true;
            }
            else
            {

                getDialog().setTitle("unmatch password ");
            }

        else
        {

            getDialog().setTitle("Email is invalid ");

        }
        return res;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dialogControl=(DialogControl)activity;

    }
    public interface DialogControl{

      public void removeDialog(User user,int entercode);
        public void saveUser();

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(tag, "onpause");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(tag, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(tag, "onDestroyView()");
    }
}
