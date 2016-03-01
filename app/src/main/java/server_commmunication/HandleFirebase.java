package server_commmunication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import aivco.com.studyhelper.LoginActivity;
import aivco.com.studyhelper.MainActivity;
import aivco.com.studyhelper.SharedPreferenceHelper;
import aivco.com.studyhelper.StringUtility;
import aivco.com.studyhelper.UtilityDistinctNumber;
import backend.DbService;
import backend.Title;
import backend.TitleInfo;

/**
 * Created by joel on 2/20/16.
 */
public class HandleFirebase {
    private static final String GROUPLEVEL = "name_of_group";
    private static final String TITLELEVEL = "name_of_title";
    private static final String NODESNAMES ="name_of_nodes_to_retrieve" ;
    private static final String DEVICES = "devices_using_the_app";
    private static final String KEY_FOR_THIS_DEVICE ="represents_key_for_his_device" ;
    private static final String NAME_FOR_THIS_DEVICE ="devicename" ;
    public static final String SELFCHANGED ="change_made_to_snapshot_by_this_device" ;

    private String devicename;
    Activity activity;
    Firebase myfirebase;
    public static final int SUCCESS=5;
    public static final int RESETSUCCESS=6;
    public static final int PASSWORDCHANGED=7;
    public static final int USERNAMECHANGED=8;
    public static final int INCORRECT_PASSWORD=2;
    public static final int USER_DOES_NOT_EXIST=4;
    public static final int NETWORK_ISSUES=3;
    public static final String col_groups="groups";
    public static final String col_title="title";
    public static final String col_diff="diff";
    public static final String col_prior="prior";
    public static final String col_ques="ques";
    public static final String col_ans="answer";
    public static final String col_uniquekey="unique_key";
    public static final String col_date="date_col";




    int result=0;
    String username;
    String [] devices;
    String firebaseApp="https://amber-heat-4308.firebaseio.com/";
    ProgressDialog pg;
    ArrayList<String> snapshotLIST=new ArrayList<>();
    private boolean nodevice,alreadychecked;
    ArrayList<String> devicelist;
    SharedPreferenceHelper sharedPreferenceHelper;
    ArrayList<String> totalDevice;
    LoginActivity loginActivity;
    HandleFirebaseInterface handleFirebaseinterface;
    private boolean selfChanged;
    Context context;
    private String col_num_of_time_given="rep_the_number_of_time_subject_has_been_given";
    private String col_given="rep_if_subject_has_been_shown";//this will either be true or false default is false,and represented with integer 0

    public HandleFirebase(Context context) {
        this.context=context;

        System.out.println(".......................HandleFirebase(Activity activity,String username) constructor...................");
        //this.username=username.replace(".","");
      //  Firebase.setAndroidContext(context);
        myfirebase=new Firebase(firebaseApp);
        sharedPreferenceHelper=new SharedPreferenceHelper(context);
        pg=new ProgressDialog(context);
        if(sharedPreferenceHelper.getString(NAME_FOR_THIS_DEVICE) == null){
            sharedPreferenceHelper.setData(NAME_FOR_THIS_DEVICE, StringUtility.getDeviceName());


        }
        devicename=sharedPreferenceHelper.getString("devicename");
        //addDevise(devicename);
        //listenForChangeOnNodes();

    }


    public HandleFirebase(Activity activity,String username)
    {
        this.activity=activity;

        System.out.println(".......................HandleFirebase(Activity activity,String username)...................");
        //this.username=username.replace(".","");
       // Firebase.setAndroidContext(activity);
        myfirebase=new Firebase(firebaseApp);
        sharedPreferenceHelper=new SharedPreferenceHelper(activity);
        pg=new ProgressDialog(activity);
       if(sharedPreferenceHelper.getString(NAME_FOR_THIS_DEVICE) == null){
           sharedPreferenceHelper.setData(NAME_FOR_THIS_DEVICE, StringUtility.getDeviceName());


       }
        devicename=sharedPreferenceHelper.getString("devicename");
        //addDevise(devicename);

        listenForChangeOnNodes();

    }

    public HandleFirebase(Activity activity) {
        this.activity=activity;
        System.out.println(".......................HandleFirebase(Activity activity) constructor......HandleFirebase(Activity activity).............");
        //Firebase.setAndroidContext(activity);
        myfirebase=new Firebase(firebaseApp);
        pg=new ProgressDialog(activity);
        handleFirebaseinterface=(HandleFirebaseInterface)activity;
        //retrieveObject();
       // listenForChangeOnNodes();

    }


    /////returns an integer representing the
    public int logInToFirebase(String username,String password){
        System.out.println(".......................logInToFirebase login.....................");


      createDialog();
      myfirebase.authWithPassword(username, password, new Firebase.AuthResultHandler() {
          @Override
          public void onAuthenticated(AuthData authData) {
              pg.dismiss();

              // LoginActivity.loginreg.login(SUCCESS);

              SharedPreferenceHelper.setData(MainActivity.USERNAME_KEY, authData.getUid());
              System.out.println(".......................succesfully logged in username is...................." + authData.getUid());
              handleFirebaseinterface.login(SUCCESS);


          }

          @Override
          public void onAuthenticationError(FirebaseError firebaseError) {
              pg.dismiss();
              // LoginActivity.loginreg.errorCode(firebaseError);
              handleFirebaseinterface.errorCode(firebaseError);

          }
      });

              return result;
          }

    public int registerToFirebase(String username,String password)
    {
        createDialog();
        myfirebase.createUser(username, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> res) {
                pg.dismiss();
                System.out.println("....................registerToFirebase...onsucces.....................");

                String username = res.get("uid") + "";
                SharedPreferenceHelper.setData(MainActivity.USERNAME_KEY, username);
                result = SUCCESS;
                myfirebase.child(username).child(col_groups).setValue("test ");
                myfirebase.child(username).child(col_title).setValue("test");
                myfirebase.child(username).child(col_ques).setValue("test");
                myfirebase.child(username).child(col_ans).setValue("test");
                myfirebase.child(username).child(col_diff).setValue("test");
                myfirebase.child(username).child(col_prior).setValue("test");
                myfirebase.child(username).child(col_uniquekey).setValue("test");
                myfirebase.child(username).child(col_num_of_time_given).setValue("test");
                myfirebase.child(username).child(col_given).setValue("test");
                myfirebase.child(username).child(col_date).setValue("test");

                // LoginActivity.loginreg.reg(result, res);
                handleFirebaseinterface.reg(result, res);

            }

            @Override
            public void onError(FirebaseError firebaseError) {
                pg.dismiss();
                // LoginActivity.loginreg.errorCode(firebaseError);
                handleFirebaseinterface.errorCode(firebaseError);

            }
        });

return result;
    }



    public void resetPassword(String username)
    {
        createDialog();
        myfirebase.resetPassword(username, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                pg.dismiss();
                handleFirebaseinterface.login(RESETSUCCESS);
                System.out.println("....................reset susccesfully...onsucces.....................");


            }

            @Override
            public void onError(FirebaseError firebaseError) {
                pg.dismiss();
                handleFirebaseinterface.errorCode(firebaseError);

            }
        });

    }

    public void changePassword(String email,String oldPassword,String newPassword)
    {
        createDialog();
        myfirebase.changePassword(email, oldPassword, newPassword, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                pg.dismiss();
                handleFirebaseinterface.login(PASSWORDCHANGED);
                System.out.println("....................changePassword...onsucces.....................");


            }

            @Override
            public void onError(FirebaseError firebaseError) {
                pg.dismiss();
                handleFirebaseinterface.errorCode(firebaseError);

            }
        });

    }


    public void changeUsername(String oldemail,String password,String  newemail)
    {
        createDialog();
        myfirebase.changeEmail(oldemail, password, newemail, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                pg.dismiss();
                handleFirebaseinterface.login(USERNAMECHANGED);
                System.out.println("....................password change...onsucces.....................");


            }

            @Override
            public void onError(FirebaseError firebaseError) {
                pg.dismiss();
                handleFirebaseinterface.errorCode(firebaseError);

            }
        });

    }




    public void upLoadObject(UpdateServer updateServer)
    {


        System.out.println(".......................upload server.............");
         username=SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY);
        myfirebase.child(username).child(col_uniquekey).updateChildren(updateServer.setUniqueKey());
        myfirebase.child(username).child(col_groups).updateChildren(updateServer.setGroup());
        myfirebase.child(username).child(col_title).updateChildren(updateServer.setTitle());
        myfirebase.child(username).child(col_ques).updateChildren(updateServer.setquestion());
        myfirebase.child(username).child(col_ans).updateChildren(updateServer.setAnswer());
        myfirebase.child(username).child(col_diff).updateChildren(updateServer.setDifficulty());
        myfirebase.child(username).child(col_prior).updateChildren(updateServer.setpriority());
        myfirebase.child(username).child(col_num_of_time_given).setValue(updateServer.setNumberOfTimeGiven());
        myfirebase.child(username).child(col_given).setValue(updateServer.setGiven());





    }

    public void update(Map<String,Object> ...myMap){
        username=SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY);

        System.out.println(".......................upload question.............");
        myfirebase.child(username).child(col_uniquekey).updateChildren(myMap[0]);
        myfirebase.child(username).child(col_diff).updateChildren(myMap[1]);
        myfirebase.child(username).child(col_prior).updateChildren(myMap[2]);
        myfirebase.child(username).child(col_ques).updateChildren(myMap[3]);
        myfirebase.child(username).child(col_ans).updateChildren(myMap[4]);


    }




    public Map<String,Object> myMap(String value,String uKey) {
        System.out.println(".......................mymap.............");

        Map<String,Object> thismap=new HashMap<>();
        thismap.put(uKey, value);


        return thismap;
    }

    public void listenForChangeOnNodes()
    {
        String username=SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY);
        System.out.println("....................inside listen for node change..........username is..........."+username);

///////////////////////////////////listen for change on unique key column///////////////////////////
        myfirebase.child(username).child(col_uniquekey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                System.out.println("....................new App Created.....................");
               getDataFromSnapshot(dataSnapshot,TitleInfo.uniquekey_col);



            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //////////////////////////listen for change on groups column//////////////////////////////
        myfirebase.child(username).child(col_groups).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {


                getDataFromSnapshot(dataSnapshot,TitleInfo.groupname_col);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //////////////////////////listen for change on titlecolumns//////////////////////////////
        myfirebase.child(username).child(col_title).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                System.out.println("....................col_title.....................");
                getDataFromSnapshot(dataSnapshot,TitleInfo.title_name_col);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //////////////////////////listen for change on groups column//////////////////////////////

        myfirebase.child(username).child(col_ques).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println("....................question changed.....................");
                getDataFromSnapshot(dataSnapshot,TitleInfo.question_col);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        //////////////////////////listen for change on groups column//////////////////////////////

        myfirebase.child(username).child(col_ans).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("....................col_ans.....................");


                getDataFromSnapshot(dataSnapshot,TitleInfo.answer_col);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        myfirebase.child(username).child(col_prior).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("....................col_prior.....................");
                getDataFromSnapshot(dataSnapshot,TitleInfo.prior_col);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        myfirebase.child(username).child(col_diff).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("....................col_diff.....................");
                getDataFromSnapshot(dataSnapshot,TitleInfo.diff_col);
            }

            @Override

            public void onCancelled(FirebaseError firebaseError) {

            }
        });








    }









    private void getDataFromSnapshot(DataSnapshot dataSnapshot,String col_name){


        if(SharedPreferenceHelper.getString(SELFCHANGED)== null){
            if(dataSnapshot.getValue() instanceof  String) {return;}
        Map<String,String> map=(Map)dataSnapshot.getValue();
        for(String s:map.keySet())
        { System.out.println("....................getting key.....................");
            DbService.startAction(activity,s, col_name,map.get(s));
        }
        }
        else{

            System.out.println("....................it is null.....................");

        }

    }











    /////////////the method below checks the server to see if the devise has been registered//////////////
    ////////if it is the first time the application is being used on anydevise the application///////
    ////////will create a device node////that will hold all devices used by the user/////////////
    ////////it will also immediately create add the device name to the devices node/////////////
    /////if the application gets deleted on a system a new devise name will be generated and readded to the///
    /////devices node on the server////////////////////////////

    public void addDevise(String name)
    {

        final String devicename=name;
        System.out.println("...................addDevise....................");
        snapshotLIST.add(devicename);

        myfirebase.child(username).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map myList=(Map)dataSnapshot.getValue();
               // System.out.println("size is"+myList.size());

                devicelist = new ArrayList<>();
                ////////////will be true if device node has not been created/////////////////////
                if (!dataSnapshot.hasChild(DEVICES)) {
                    System.out.println("...................child does not exist....................");
                    myfirebase.child(username).child(DEVICES).setValue(snapshotLIST);
                    totalDevice = snapshotLIST;
                    ///////////will only be true if this device has not been added to device list/////////
                } else if (sharedPreferenceHelper.getString(KEY_FOR_THIS_DEVICE) == null) {
                    System.out.println("...................device node exist but this device has not been added...................");
                    devicelist = (ArrayList<String>) dataSnapshot.child(DEVICES).getValue();
                    ArrayList<String> myarray = new ArrayList<>();
                    myarray.addAll(devicelist);
                    myarray.add(devicename);
                    myfirebase.child(username).child(DEVICES).setValue(myarray);
                    totalDevice = myarray;
                    totalDevice = devicelist;

                            /*for (String dev : devicelist) {
                                nodevice = true;
                                if (dev.equals(devicename)) {
                                    System.out.println("...................device node exist but this device have not been added................... " );
                                    nodevice = false;
                                    break;
                                }

                            }*/
                    sharedPreferenceHelper.setData(KEY_FOR_THIS_DEVICE, devicename);
                }
                ///////selfchange is true only if the data was added from the same device///////
                if(!dataSnapshot.hasChild(NODESNAMES))
                {
                    Map<String,String> map=new HashMap<>();
                    map.put("0ne","one");

                    System.out.println("...................node has been created....................");
                    myfirebase.child(username).child(NODESNAMES).setValue(map);

                }










                System.out.println("...................end of method................... " + nodevice);

            }





            @Override
            public void onCancelled(FirebaseError firebaseError) {

                System.out.println("...................there is error................... " + nodevice);
            }
        });








        System.out.println("...................end of add device method................... " + nodevice);
    }








    public void createDialog()
    {


        pg.setMessage("connecting");
        pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pg.setCanceledOnTouchOutside(false);
        pg.show();

    }


}
