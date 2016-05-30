package server_commmunication;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ProgressBar;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import aivco.com.studyhelper.LoginActivity;
import aivco.com.studyhelper.MainActivity;
import aivco.com.studyhelper.SettingsActivity;
import aivco.com.studyhelper.SharedPreferenceHelper;
import backend.DbColumns;
import backend.HandleRecords;
import backend.Title;
import backend.TitleInfo;

/**
 * Created by joel on 3/11/16.
 */
public class ServerWithIon extends HTTPServer {
    private static final String EXTRA_URL = "backend.extra.UNIQUE_KEY";
    public Context context;
    private String recordToDeleteFromServer="deleteRecord/";
    private final String  UPDATERECORDSWHENGIVEN="updaterecordwhengiven/";
    //private static String SERVERURL="http://192.168.100.13:8000/";   http://engrjoelivon.pythonanywhere.com/
    private static String SERVERURL="http://engrjoelivon.pythonanywhere.com/";
    private String checkUpdates="checkinserts/";//path to check update on system and on server
    public String newDevice="newdevice/";//path to file in server to check if a user already have records on the server
    protected final String SUGGESTIONS="suggestions/";
    ProgressDialog pg=null;
    static LoginActivity loginActivity;
    static SettingsActivity settingsActivity;
    int res=0;
    String username="";
    public ServerWithIon(Context context) {
        // super("http://192.168.0.9:8000/");


        super("http://engrjoelivon.pythonanywhere.com/");
        this.context=context;
        //pg=new ProgressDialog(Mcheckinserts/ainActivity.mycontext);
        if(context instanceof SettingsActivity)
        {
            System.out.println("...................its instance of settings activity......................." );

            settingsActivity=(SettingsActivity)context;
        }
        if(context instanceof LoginActivity)
        {
            loginActivity=(LoginActivity)context;
        }
        HttpURLConnection conn=null;
        int responseCode=0;

        String html= null;
        try {
            //connect

            URL url=new URL(generateUrl(recordToDeleteFromServer));
            conn=(HttpURLConnection)url.openConnection();
            System.out.println("...................after....connection................");
            conn.setRequestMethod("GET");
            //set properties to perform request like a browser
            conn.setRequestProperty("Host", generateUrl(recordToDeleteFromServer));
            //conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");



    } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }}

    //constructor to be called during signup or login
    public ServerWithIon(Object activity,User user) {
        // super("http://192.168.0.9:8000/", user);
          super("http://engrjoelivon.pythonanywhere.com/", user);
        if(activity instanceof  LoginActivity)
        {
            loginActivity=(LoginActivity)activity;
            context=loginActivity.getApplicationContext();
        }
        else if(activity instanceof  SettingsActivity)
        {
            settingsActivity=(SettingsActivity)activity;
            context=settingsActivity.getApplicationContext();

        }

        //pg=new ProgressDialog(Mcheckinserts/ainActivity.mycontext);

        HttpURLConnection conn=null;
        int responseCode=0;

        String html= null;
        try {
            //connect

            URL url=new URL(generateUrl(recordToDeleteFromServer));
            conn=(HttpURLConnection)url.openConnection();
            System.out.println("...................after....connection................");
            conn.setRequestMethod("GET");
            //set properties to perform request like a browser
            conn.setRequestProperty("Host", generateUrl(recordToDeleteFromServer));
            //conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");



        }  catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }


    public void postNewRecord(TitleInfo titleInfo,String path)  {
        System.out.println("...................postNewRecord.......................");



        ////// adds an image below as ascii string encoded using base BASE64./////
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty(INSERTRECORDKEY, titleInfo.getObjectAsJsonArrayString());



        Ion.with(context)
                .load(generateUrl(path))
                .setJsonObjectBody(jsonObject)
                .asJsonObject()


                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        System.out.println("...............................oncallback method of ion");
                        //JsonElement jsonElement = result.get("email");
                        if (result != null) {

                            JsonElement jsonElement = result.get(RETURNEDKEYFROMSERVER);


                            String ans = jsonElement.getAsString();
                            System.out.println(ans);
                            System.out.println("...............................returned" + ans);

                            new TitleInfo(context).removeAddedKey(ans);
                        }


                    }
                });



    }

    public void postDeletedRecord(String uniqueKey,String path)
    {
        System.out.println("...................postDeletedRecord.......................");
        Ion.with(context)
                .load(generateUrl(path))
                .setBodyParameter(USERNAMEKEY, SharedPreferenceHelper.getString(MainActivity.MYEMAIL))
                .setBodyParameter(DEVISENAMEKEY, SharedPreferenceHelper.getString(MainActivity.DEVICENAME))
                .setBodyParameter(DELETERECORDKEY, uniqueKey)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        System.out.println("........................deleted.......................");
                        if (result != null) {
                            System.out.println("...................Successfully deleted......................." + result);
                            new TitleInfo(context).removeDeletedKey(result);

                        }

                    }

                });


    }


    public void postUpdatedRecord(TitleInfo titleInfo,String oldKey,String updatetype,String url)  {
        System.out.println("...................postUpdatedRecord.......................");



        ////// adds an image below as ascii string encoded using base BASE64./////
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("oldkey", oldKey);
        jsonObject.addProperty("updateType",updatetype );
        jsonObject.addProperty(UPDATERECORDKEY, titleInfo.getObjectAsJsonArrayString());



        Ion.with(context)
                .load(generateUrl(url))
                .setJsonObjectBody(jsonObject)
                .asJsonObject()


                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        System.out.println("...............................oncallback method of ion");
                        //JsonElement jsonElement = result.get("email");


                        if (result != null) {


                            JsonElement jsonElement = result.get(RETURNEDKEYFROMSERVER);


                            String ans = jsonElement.getAsString();

                            System.out.println("...............................returned" + ans);

                            new TitleInfo(context).removeUpdatedKey(ans);
                        }


                    }
                });



    }


    //method called when a record is given at the home page
    public void postUpdatedWhenGivenRecord(String key,String nOTAccesed,String Expire,String path)  {
        ////// adds an image below as ascii string encoded using base BASE64./////
        JsonObject jsonObject=new JsonObject();
        ArrayList<String> thisArray=new ArrayList<>();
        thisArray.add(SharedPreferenceHelper.getString(MainActivity.MYEMAIL));
        thisArray.add(key);
        thisArray.add(nOTAccesed);
        thisArray.add(Expire);
        thisArray.add(SharedPreferenceHelper.getString(MainActivity.DEVICENAME));
        jsonObject.addProperty(UPDATERECORDKEY, new JSONArray(thisArray).toString());



        Ion.with(context)
                .load(generateUrl(UPDATERECORDSWHENGIVEN))
                .setJsonObjectBody(jsonObject)
                .asJsonObject()


                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        System.out.println("...............................oncallback method of ion of update when given");
                        //would only be null if there is no internet connection
                        if (result != null) {

                            JsonElement jsonElement = result.get(RETURNEDKEYFROMSERVER);
                            //if server returned null

                            if (jsonElement instanceof JsonNull) {
                                System.out.println(".............instance of jsonnull");
                            } else {
                                String ans = jsonElement.getAsString();

                                System.out.println("...............................returned" + ans);

                                new TitleInfo(context).removeUpdatedKey(ans);
                            }

                        }


                    }
                });

    jsonObject=null;

    }




    public boolean checkRecordAtStartUp()
    {
        String inserturl=generateUrl(checkUpdates); //handles insert updates
        String username=SharedPreferenceHelper.getString(MainActivity.MYEMAIL);
        String devicename=SharedPreferenceHelper.getString(MainActivity.DEVICENAME);



        JSONArray jsonArray=null;
        List<String> insertedList=new ArrayList<>();
        List<String> updatedList=new ArrayList<>();
        List<String> deletedList=new ArrayList<>();
        deletedList.add(username);
        deletedList.add(devicename);




        JsonObject jsonObject=new JsonObject();


        List<HandleRecords> handleRecordsList=new TitleInfo(context).checkActions();
        for(HandleRecords handleRecords:handleRecordsList)
        {
            System.out.println("............HandleRecords..............");

            List<DbColumns> insertedObj= handleRecords.getInsertedColoumnObjects();
                  for(DbColumns myColumnObj:insertedObj )
                  {
                      System.out.println("............inserted..............");
                      Title thisTitle=(Title)myColumnObj;
                      insertedList.add(thisTitle.getObjectAsJsonArrayString());
                  }

            List<String> deletedObj= handleRecords.getDeletedColoumnObjects();
            for(String deletedkeys:deletedObj )
            {
                deletedList.add(deletedkeys);
            }


            List<DbColumns> updatedObj= handleRecords.getUpdatedColoumnObjects();
            for(DbColumns myColumnObj:updatedObj )
            {
                System.out.println("............updatedObj..............");
                Title thisTitle=(Title)myColumnObj;
                updatedList.add(thisTitle.getObjectAsJsonArrayString());

            }







        }

        ////////////////////////////////addDeviceToServer////////////////////////
        //addDevice("adddevice/");
        //signUp();
       // logIn();



        ///////////////////////////////insert///////////////////////////////////
        jsonArray=new JSONArray(insertedList);
        jsonObject.addProperty(INSERTRECORDKEY, jsonArray.toString());
        jsonObject.addProperty(USERNAMEKEY, SharedPreferenceHelper.getString(MainActivity.MYEMAIL));
        jsonObject.addProperty(DEVISENAMEKEY, SharedPreferenceHelper.getString(MainActivity.DEVICENAME));


        Ion.with(context)
                .load(inserturl)
                .setJsonObjectBody(jsonObject)
                .asJsonObject()


                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        System.out.println("...............................oncallback method of ion");
                        if (result != null) {
                            System.out.println("...............................result of onstart insert is not null....");

                            JsonArray myArray = result.getAsJsonArray(RETURNEDKEYFROMSERVER);
                            for (JsonElement jsonElement : myArray) {


                                new TitleInfo(context).removeAddedKey(jsonElement.getAsString());
                            }



                           removeInsertedkeysFromServer(new TitleInfo(context).
                                   acceptRecordsFromServer(result.getAsJsonArray(SERVERRECORDKEYS)));

















                        }
                    }
                });


     /////////////////////////////////////delete//////////////////////////////////////////////////

        JsonObject jsondelete=new JsonObject();
        jsonArray=new JSONArray(deletedList);
        jsondelete.addProperty(DELETERECORDKEY, jsonArray.toString());


        Ion.with(context)
                .load(generateUrl("deleteatstart/"))
                .setJsonObjectBody(jsondelete)
                .asJsonObject()


                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        System.out.println("...............................oncallback method of ion");
                        if (result != null) {
                            JsonArray myArray = result.getAsJsonArray(RETURNEDKEYFROMSERVER);
                            for (JsonElement jsonElement : myArray) {

                                if (!isJsonElementNull(jsonElement)) {

                                    System.out.println("...............................its not null ");

                                    new TitleInfo(context).removeDeletedKey(jsonElement.getAsString());
                                }

                            }

                            JsonArray deletedArray = result.getAsJsonArray(SERVERRECORDKEYS);
                            ArrayList<String> deletedKeys = (ArrayList<String>) new TitleInfo(context).deletedRecordsKeys(deletedArray);
                            if (deletedKeys.size() != 0) {

                                removeDeletedKeys(new JSONArray(deletedKeys).toString());


                            }
                            ;


                        }
                    }
                });


        ///////////////////////////////////updated///////////////////////////////////////////////////

        JsonObject jsonUpdated=new JsonObject();
        JSONArray jsonArrayUpdated=new JSONArray(updatedList);
        jsonUpdated.addProperty(UPDATERECORDKEY, jsonArrayUpdated.toString());
        jsonUpdated.addProperty(USERNAMEKEY, SharedPreferenceHelper.getString(MainActivity.MYEMAIL));
        jsonUpdated.addProperty(DEVISENAMEKEY, SharedPreferenceHelper.getString(MainActivity.DEVICENAME));


        Ion.with(context)
                .load(generateUrl("updaterecordsatstartup/"))
                .setJsonObjectBody(jsonUpdated)
                .asJsonObject()


                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        System.out.println("...............................oncallback method of ion");
                        if (result != null) {
                            JsonArray myArray = result.getAsJsonArray(RETURNEDKEYFROMSERVER);
                            for (JsonElement jsonElement : myArray) {


                                System.out.println("...............................oncallback method of ion " + jsonElement);
                                if (!isJsonElementNull(jsonElement)) {

                                    String key = jsonElement.getAsString();
                                    new TitleInfo(context).removeUpdatedKey(key);
                                }


                            }

                            JsonArray updateArray = result.getAsJsonArray(SERVERRECORDKEYS);
                            ArrayList<String> updatedKeys = (ArrayList<String>)
                                    new TitleInfo(context).acceptupdatedRecordsFromServer(updateArray);
                            if (updatedKeys.size() != 0) {

                                removeServerActions("removeupdatedkeys/", new JSONArray(updatedKeys).toString());
                            }

                        }


                    }
                });



     return false;
    }





    //**will remove keys from server**//
    public void removeInsertedkeysFromServer(String jsonDeletedKeys){
        System.out.println("removing inserted key from server");

        JsonObject jsonObject=new JsonObject();
        String username=SharedPreferenceHelper.getString(MainActivity.MYEMAIL);
        String devicename=SharedPreferenceHelper.getString(MainActivity.DEVICENAME);
        System.out.println("username is " + username + " devicename is " + devicename);
        jsonObject.addProperty(INSERTRECORDKEY, jsonDeletedKeys);
        jsonObject.addProperty(USERNAMEKEY, username);
        jsonObject.addProperty(DEVISENAMEKEY, devicename);


        Ion.with(context)
                .load(generateUrl("removeinsertedkey/"))
                .setJsonObjectBody(jsonObject)
                        .asJsonObject()


                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        System.out.println("...............................oncallback method of ion");

                    }
                });



    }

    //**will remove deleted keys from server**//
    public void removeDeletedKeys(String deletedkeys){

        System.out.println("removing deleted key from server");

        JsonObject jsonObject=new JsonObject();
        String username=SharedPreferenceHelper.getString(MainActivity.MYEMAIL);
        String devicename=SharedPreferenceHelper.getString(MainActivity.DEVICENAME);
        System.out.println("username is " + username + " devicename is " + devicename);
        jsonObject.addProperty(DELETERECORDKEY, deletedkeys);
        jsonObject.addProperty(USERNAMEKEY, username);
        jsonObject.addProperty(DEVISENAMEKEY, devicename);


        Ion.with(context)
                .load(generateUrl("removedeletedkeys/"))
                .setJsonObjectBody(jsonObject)
                .asJsonObject()


                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        System.out.println("...............................oncallback method of ion of remove deleted keys");

                    }
                });





    }

    /**
     * @param removeServerActions actions means delete,update and insert keys.After the server sends keys for records any of the three actions mentioned,
     * this method will be called so the server can remove the keys from its list,depening on the caller.
     * @param jsonActionString would represent a json array converted to a string
     * @param path This will represent the path for the action to perform in the server,the path should end in /
     * */
    public void removeServerActions(String path,String jsonActionString)
    {
        JsonObject jsonObject=new JsonObject();
        String username=SharedPreferenceHelper.getString(MainActivity.MYEMAIL);
        String devicename=SharedPreferenceHelper.getString(MainActivity.DEVICENAME);
        System.out.println("username is "+username +" devicename is "+devicename);
        jsonObject.addProperty(UPDATERECORDKEY, jsonActionString);
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("devicename", devicename);


        Ion.with(context)
                .load(generateUrl(path))
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        System.out.println("...............................oncallback method of ion of remove deleted keys");

                    }
                });


    }
    @Override
    public void addDevice(String path){

        ///////////////////////////////adddevice///////////////////////////////////


        System.out.println("++++++++++++...Add device.................++++++++++");
        Ion.with(context)
                .load(generateUrl(path))
                .setBodyParameter(USERNAMEKEY, SharedPreferenceHelper.getString(MainActivity.MYEMAIL))
                .setBodyParameter(DEVISENAMEKEY, SharedPreferenceHelper.getString(MainActivity.DEVICENAME))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        System.out.println("........................succesfully added device.......................");
                        if (result != null) {
                            System.out.println("...................value returned after adding device......................." + result);


                        }

                    }

                });


    }

    @Override
    public void signUp() {
        createDialog("connecting......",loginActivity);
        Ion.with(context)
                .load(generateUrl(signUp))
                .setBodyParameter(USERNAMEKEY, getUserName())
                .setBodyParameter(PASSWORDKEY, getPassword())
                .setBodyParameter(DEVISENAMEKEY, SharedPreferenceHelper.getString(MainActivity.DEVICENAME))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        pg.cancel();

                        System.out.println("........................succesfully signedup......................");
                        if (result != null) {

                            handlejsonObject(result);
                            System.out.println("...................value returned after signing up......................." + result);

                            if (successfulOrFailed(res)) {
                                SharedPreferenceHelper.setData(MainActivity.MYEMAIL, username);
                                SharedPreferenceHelper.setData(MainActivity.USERNAME_KEY, username);

                                loginActivity.successOrFailed(res);

                            } else {

                                loginActivity.successOrFailed(res);
                            }


                        } else {

                            loginActivity.successOrFailed(LoginActivity.NETWORKERROR);

                        }

                    }

                });

    }

    @Override
    public void logIn() {
        createDialog("connecting......",loginActivity);
        Ion.with(context)
                .load(generateUrl(login))
                .setBodyParameter(USERNAMEKEY, getUserName())
                .setBodyParameter(PASSWORDKEY, getPassword())
                        //SharedPreferenceHelper.getString(MainActivity.MYPASSWORD))
                .setBodyParameter(DEVISENAMEKEY, SharedPreferenceHelper.getString(MainActivity.DEVICENAME))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        pg.cancel();

                        System.out.println("........................LOGIN......................");
                        if (result != null) {

                            handlejsonObject(result);
                            System.out.println("...................value returned after LOGIN up......................." + result);

                            if (successfulOrFailed(res)) {

                                SharedPreferenceHelper.setData(MainActivity.MYEMAIL, getUserName());
                                SharedPreferenceHelper.setData(MainActivity.USERNAME_KEY, getUserName());
                                SharedPreferenceHelper.setData(PASSWORDKEY, getPassword());
                                loginActivity.successOrFailed(res);

                            }
                            //if code returned is not success code
                            else {

                                loginActivity.successOrFailed(res);
                            }


                        }
                        //if could not connect to server due to internet connection error
                        else {

                            loginActivity.successOrFailed(LoginActivity.NETWORKERROR);

                        }

                    }

                });

    }



    //handles resolving json for login and signup uses jsonObject of java class not Gson
    public void handlejsonObject(String result)
    {
        try {
            JSONObject jsonObject=new JSONObject(result);

            JSONArray jsonArray= jsonObject.getJSONArray("reg_key");
            res=Integer.parseInt(jsonArray.getString(0)) ;
            username=jsonArray.getString(1);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }

    //post records to server
    public void serverPost(){


    }



    @Override
    public void atFirstStarting() {

        System.out.println("inside atfirstStarting");
  if(new TitleInfo(MainActivity.mycontext).isApplicationNew())
  {
      JsonObject jsonObject=new JsonObject();
      String username=SharedPreferenceHelper.getString(MainActivity.MYEMAIL);
      String devicename=SharedPreferenceHelper.getString(MainActivity.DEVICENAME);
      System.out.println("##########################inside atfirstStarting. username is " + username + " devicename is " + devicename);
      jsonObject.addProperty(ATFIRSTSTARTING, NEWDEVICE);
      jsonObject.addProperty(USERNAMEKEY, username);
      jsonObject.addProperty(DEVISENAMEKEY, devicename);

      //createDialog("updating......",context);
      Ion.with(MainActivity.mycontext)
              .load(generateUrl(newDevice))
              .setJsonObjectBody(jsonObject)
              .asJsonObject()
              .setCallback(new FutureCallback<JsonObject>() {
                  @Override
                  public void onCompleted(Exception e, JsonObject result) {
                      //pg.cancel();
                      System.out.println("##########################oncallback method of ion of remove deleted keys########################");

                      //considering making this run at the background thread
                      if (result != null) {


                          System.out.println("##########################result is not null########################");

                          new TitleInfo(context).acceptRecordsFromServer(result.getAsJsonArray(RETURNEDKEYFROMSERVER));

                      }
                      loginActivity.successOrFailed(LoginActivity.UPDATE);

                  }
              });


  }
        loginActivity.successOrFailed(LoginActivity.UPDATE);
    }
    /**
     *@param jsonElement Accepts a jsonelement,checks if it is null,if null would return true otherwise false
     * */
    //method should be called to check if jsonElement is null.Json element cannot be checked direclty with null
    public boolean isJsonElementNull(JsonElement jsonElement)
    {
          return jsonElement instanceof  JsonNull;
    }


    public void postComment(String comment,final SettingsActivity.HandleProgressDialog hpd){



        //pg = new ProgressDialog(MainActivity.mycontext);
        System.out.println("++++++++++++...posting comment.................++++++++++");


        Ion.with(context)
                .load(generateUrl(SUGGESTIONS) )
                .setBodyParameter(USERNAMEKEY, SharedPreferenceHelper.getString(MainActivity.MYEMAIL))
                .setBodyParameter("comment", comment)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        if (result != null) {
                            System.out.println("...................sussecfully posted comment......................." + result);


                            hpd.success(true);
                            return;
                        } else {
                            hpd.success(false);
                        }


                    }

                });


    }
    public void createDialog(String status,Context context)
    {

        pg=new ProgressDialog(context);
        pg.setMessage(status);
        pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pg.setCanceledOnTouchOutside(false);
        pg.show();

    }

    @Override
    public void resetPassword(String username) {
        System.out.println("reset password");
        createDialog("connecting.....",loginActivity);
        Ion.with(context)
                .load(generateUrl(RESETPASSWORDURL))
                .setBodyParameter(USERNAMEKEY, username)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        pg.cancel();
                        if (result != null) {
                            try{
                                loginActivity.successOrFailed(Integer.valueOf(result));
                            }
                            catch(NumberFormatException nfe){
                                nfe.printStackTrace();

                            }



                        }


                    }

                });




    }

    @Override
    public boolean changeUsername(String newUsermail, String password) {

        if (!super.changeUsername(newUsermail, password)) {
            //System.out.println("...................password incorrect for changeusername......................." );
            return false;} else
        {            //System.out.println("...................password correct for changeusername......................." );

            Ion.with(context)
                                    .load(generateUrl(CHANGEUSERNAMEURL))
                                    .setBodyParameter(USERNAMEKEY, SharedPreferenceHelper.getString(MainActivity.MYEMAIL))
                                    .setBodyParameter(NEWUSERNAMEKEY, newUsermail)
                                    .asString()
                                    .setCallback(new FutureCallback<String>() {
                                        @Override
                                        public void onCompleted(Exception e, String result) {

                                            if (result != null) {
                                                System.out.println("...................sussecfully username changed......................." + result);
                                                 try{
                                                     settingsActivity.successOrFailed(Integer.parseInt(result));

                                                 }
                                                 catch(NumberFormatException nfe)
                                                 {
                                                   nfe.printStackTrace();
                                                 }

                                            }




                                        }

                                    });


                        }
                        return true;
                    }

    @Override
    public boolean changePassword(String newPassword, String oldPassword) {
        System.out.println("changePassword");
                        //compares the password entered as old password to the password stored on the system
                        if (!super.changePassword(newPassword, oldPassword))
                        { System.out.println("it returned false");     return false;}

                        else {
                            Ion.with(context)
                                    .load(generateUrl(CHANGEPASSWORDURL))
                                    .setBodyParameter(USERNAMEKEY, SharedPreferenceHelper.getString(MainActivity.MYEMAIL))
                                    .setBodyParameter(NEWPASSWORDKEY, newPassword)
                                    .asString()
                                    .setCallback(new FutureCallback<String>() {
                                        @Override
                                        public void onCompleted(Exception e, String result) {

                                            if (result != null)
                                            {
                                                settingsActivity.successOrFailed(SettingsActivity.PASSWORDCHANGEDSUCCESFULLY);
                                            }




                                        }

                                    });


                        }
                        return true;


                    }


    @Override
    public boolean checkUserValidation(final Context context) {

        if(super.checkUserValidation(context)) return false;
        Ion.with(context)
                .load(generateUrl(CHECKVALIDATEUSERURL))
                .setBodyParameter(USERNAMEKEY, SharedPreferenceHelper.getString(MainActivity.MYEMAIL))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        if (result != null) {
                            try{
                                setValidation(context,Integer.parseInt(result));
                                System.out.println("Result from validation..................."+result);
                            }


                                catch(NumberFormatException nfe)
                                {
                                  nfe.printStackTrace();

                            }


                        }



                    }

                });

     return false;
    }

    @Override
    public void validate() {

        Ion.with(context)
                .load(generateUrl(VALIDATEUSERURL)+"?"+USERNAMEKEY+"="+SharedPreferenceHelper.getString(MainActivity.MYEMAIL))
                //.setBodyParameter(USERNAMEKEY, SharedPreferenceHelper.getString(MainActivity.MYEMAIL))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        if (result != null) {
                            setValidation(context, VALIDATED);

                        }



                    }

                });

    }
}
