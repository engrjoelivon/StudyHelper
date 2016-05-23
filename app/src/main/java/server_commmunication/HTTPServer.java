package server_commmunication;

import android.content.Context;

import java.net.InetAddress;
import java.net.UnknownHostException;

import aivco.com.studyhelper.MainActivity;
import aivco.com.studyhelper.SharedPreferenceHelper;

/**
 * class server is a base class that can be extended,by other more specific classes.It holds members that
 * are constant for CRUD operations between server and client.
 */
public abstract class HTTPServer {
    //the fields defined below are the same on the server
    public static final String INSERTRECORDKEY="newrecord";  //key is the same as in server
    public static final String UPDATERECORDKEY="updaterecord";  //key is the same as in server
    public static final String DELETERECORDKEY="deleterecord";  //key is the same as in server
    public static final String USERNAMEKEY="username";  //key is the same as in server
    protected static final String PASSWORDKEY="password";/** @param key should be the same on the server*/
    public static final String DEVISENAMEKEY="devicename";  /** @param key key is the same as in server*/
    public static final String RETURNEDKEYFROMSERVER="key";  /**@param key will represent jsonObject key for a list of key for the record sent to the server*/
    public static final String SERVERRECORDKEYS="serverrecord";
    public static final String VALIDATION_KEY ="keytosetifuserisvalidated" ;
    protected static final int VALIDATED =1 ;

    /**@param serverrecord will be key for jsonObject for all the Record in the server but not in the client,these record could represent updated inserted or deleted*/

    protected final int REGISTRATIONOK=1;//server must ensure to return 1 for a registration that was completed successfully
    protected final int SIGNUPOK=2;//server must ensure to return 2 for login that was completed successfully
    private String recordToDeleteFromServer="deleteRecord/";
    private String urlForRecordToUpDate="updaterecords/";
    public static final String ATFIRSTSTARTING="key";  //key is the same as in server
    public static final String NEWDEVICE="1";  //key is the same as in server
    public  String serverURL;   //every connection between server and client requires a url,so the base url is defined here
    //to initiate a request to server add the appropriate path
    protected String signUp="signup/";//represents the path to the signup view at the server
    protected String login="login/";
    protected User thisUser;
    protected static final String NEWUSERNAMEKEY="newusername";//should be the same as in server
    protected static final String OLDPASSWORDKEY="oldpassword";//should be the same as in server
    protected static final String NEWPASSWORDKEY="newpassword";//should be the same as in server
    protected static final String RESETPASSWORDKEY="resetpassword";//should be the same as in server
    protected static final String RESETPASSWORDURL="resetpassword/";//should be the same as in server
    protected static final String CHANGEUSERNAMEURL="changeusername/";//should be the same as in server
    protected static final String CHANGEPASSWORDURL="changepassword/";//should be the same as in server
    protected static final String CHECKVALIDATEUSERURL="checkvalidate/";//should be the same as in server
    protected static final String VERIFYUSERKEY="verifyuser";//should be the same as in server
    protected static final String VALIDATEUSERURL="validateemail/";//should be the same as in server


    public HTTPServer(String serverurl) {

        this.serverURL=serverurl;

    }
    public HTTPServer(String serverurl,User thisuser) {

        this.serverURL=serverurl;
        this.thisUser=thisuser;
    }

    /**
     *<h3>
     *  method is called when application succesfully logs in,it checks the db to see if there is any record,if no record it will assume this user is new on the device,but could proberbly have records on the server
     * so it notifies.The server confirms if it has any record for the user,if yes,the server returns the record.
     *</h3>
     *
     */
    protected abstract void atFirstStarting();

    /**
     * <h1>
     *  abstract method called when application starts the first time,to add the device name to the server.
     * </h1>
     * @param  path pass in the path for the view in the server,should end with a forward slash,in case of django.Except if server specifies different format for url.
     *
     * */
    public abstract void addDevice(String path);

    public  String generateUrl(String path){

    return serverURL+path;
    }
    /**@return  returns a boolean that is true if the user succesfully logged in.Server must send same code and REGISTRATIONOK constant*/
    protected boolean successfulOrFailed(int result){

        return result==REGISTRATIONOK || result==SIGNUPOK ;
    }

     abstract void signUp();
     abstract void logIn();


    /**@return returns username from the userObject*/
    protected String getUserName(){
        if(thisUser != null)
        {
            return thisUser.getUsername();
        }
        return null;
    }
    /**@return returns password from the userObject*/
    protected String getPassword(){

        if(thisUser != null)
        {
            return thisUser.getPassword();
        }
       return null;
    }
    /**overide method to customize device communication with server,call super to confirm if password matches */
    protected boolean changeUsername(String newUsermail,String password)
    {
     return password .equals(SharedPreferenceHelper.getString(PASSWORDKEY));
    }
    /**overide method to customize device communication with server,call super to confirm if password matches */
    protected  boolean changePassword(String newPassword,String oldPassword)
    {

        return oldPassword .equals(SharedPreferenceHelper.getString(PASSWORDKEY));
    }

    protected  abstract void resetPassword(String username);


    /**<h1>Method is specified as concrete,because a call to super is necessary to confirm if the value for the validation
     * key stored in the sharedpreference is true or false</h1>
     * @param  context:-context where the sharedpreference will run
     * @return :-returns a boolean that confirms if the user have been validated*/
    protected boolean checkUserValidation(Context context){
        return new SharedPreferenceHelper(context).getBool(SharedPreferenceHelper.getString(MainActivity.MYEMAIL));
    }

    protected static void setValidation(Context context,int validationcode){
  if( validationcode == VALIDATED){

      new SharedPreferenceHelper(context).setBool(SharedPreferenceHelper.getString(MainActivity.MYEMAIL), true);
  }

    }
    /**<h1>calls method if user is not validated</h1>*/
    protected abstract void validate();
}
