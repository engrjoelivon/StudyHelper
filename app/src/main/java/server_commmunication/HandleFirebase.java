package server_commmunication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public static final String col_expiry="expiry_col";




    int result=0;
    String username;
    String firebaseApp="https://amber-heat-4308.firebaseio.com/";
    ProgressDialog pg;
    ArrayList<String> snapshotLIST=new ArrayList<>();

    SharedPreferenceHelper sharedPreferenceHelper;

    HandleFirebaseInterface handleFirebaseinterface;
    private boolean selfChanged;
    Context context;
    private String col_num_of_time_given="rep_the_number_of_time_subject_has_been_given";
    private String col_given="rep_if_subject_has_been_shown";//this will either be true or false default is false,and represented with integer 0
    private TitleInfo ti;
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




    //calls to upload record to server,record can be new record or record already stored on device//////
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
        myfirebase.child(username).child(col_num_of_time_given).updateChildren(updateServer.setNumberOfTimeGiven());
        myfirebase.child(username).child(col_given).updateChildren(updateServer.setGiven());
        myfirebase.child(username).child(col_date).updateChildren(updateServer.setDate());
        myfirebase.child(username).child(col_expiry).updateChildren(updateServer.setExpiry());

    }

    //because ServerKeys cannot bechanged,so new record will be created during updateing while old record will be deleted////
    public void nullifyOldRecord(UpdateServer updateServer)
    {


        System.out.println(".......................upload server.............");
        username=SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY);
        myfirebase.child(username).child(col_uniquekey).updateChildren(updateServer.setUniqueKeyForDelete());
        myfirebase.child(username).child(col_groups).updateChildren(updateServer.setGroup());
        myfirebase.child(username).child(col_title).updateChildren(updateServer.setTitle());
        myfirebase.child(username).child(col_ques).updateChildren(updateServer.setquestion());
        myfirebase.child(username).child(col_ans).updateChildren(updateServer.setAnswer());
        myfirebase.child(username).child(col_diff).updateChildren(updateServer.setDifficulty());
        myfirebase.child(username).child(col_prior).updateChildren(updateServer.setpriority());
        myfirebase.child(username).child(col_num_of_time_given).updateChildren(updateServer.setNumberOfTimeGiven());
        myfirebase.child(username).child(col_given).updateChildren(updateServer.setGiven());
        myfirebase.child(username).child(col_date).updateChildren(updateServer.setDate());
        myfirebase.child(username).child(col_expiry).updateChildren(updateServer.setExpiry());

    }


    public void update(Map<String,Object> ...myMap){
        username=SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY);

        myfirebase.child(username).child(col_uniquekey).updateChildren(myMap[0]);
        myfirebase.child(username).child(col_diff).updateChildren(myMap[1]);
        myfirebase.child(username).child(col_prior).updateChildren(myMap[2]);
        myfirebase.child(username).child(col_ques).updateChildren(myMap[3]);
        myfirebase.child(username).child(col_ans).updateChildren(myMap[4]);
        myfirebase.child(username).child(col_given).setValue(myMap[5]);
        myfirebase.child(username).child(col_date).setValue(myMap[6]);
        myfirebase.child(username).child(col_num_of_time_given).setValue(myMap[7]);


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
        System.out.println("....................inside listen for node change..........username is..........." + username);

///////////////////////////////////listen for change on unique key column///////////////////////////
        myfirebase.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenerateTitle generateTitle=new GenerateTitle();

                if (SharedPreferenceHelper.getString(SELFCHANGED) == null) {
                    System.out.println(".....self changed is null.............");


                    if (dataSnapshot.child(col_uniquekey).getValue() instanceof Map) {
                        System.out.println(".......its instance of map...........");



                        ti = new TitleInfo(activity);


                        ///////a value of zero means its a new application///////
                        if (ti.isEmpty())
                        {
                            Map<String, Object> myMap = (Map<String, Object>) dataSnapshot.child(col_uniquekey).getValue();
                            List<CheckForUpdates> checkForUpdatesList = ti.checkUpdate(
                                    myMap.keySet());

                            for (CheckForUpdates checkForUpdates : checkForUpdatesList) {
                                ///////////true if local machines needs an update//////
                                if (checkForUpdates.getUpdateLocal()) {

                                    String keyforlocal = checkForUpdates.getUniquekeyForLocal();
                                    String keyforother = checkForUpdates.getUniquekeyForOthers();
                                    System.out.println(".......unique que for local.in updatelocal.........." + keyforlocal);
                                    System.out.println(".......unique que for oder..........." + keyforother);

                                    Map<String, String> mymap = new HashMap();
                                    String[] ar = new String[8];


                                    ar[0] = (keyforlocal);//key for local
                                    ar[1] = (keyforother);//key for other
                                    ar[2] = (getDataFromSnapshot((Map<String, String>) dataSnapshot.child(col_groups).getValue(), keyforother));//
                                    ar[3] = (getDataFromSnapshot((Map<String, String>) dataSnapshot.child(col_title).getValue(), keyforother));
                                    ar[4] = (getDataFromSnapshot((Map<String, String>) dataSnapshot.child(col_ques).getValue(), keyforother));
                                    ar[5] = (getDataFromSnapshot((Map<String, String>) dataSnapshot.child(col_ans).getValue(), keyforother));
                                    ar[6] = (getDataFromSnapshot((Map<String, String>) dataSnapshot.child(col_diff).getValue(), keyforother));
                                    ar[7] = (getDataFromSnapshot((Map<String, String>) dataSnapshot.child(col_prior).getValue(), keyforother));


                                    DbService.StartUpdateLocal(activity, ar);


                                }
                                //if this case is true,it will mean changes have been made to the code on the local system and will need to be updated//
                                if (checkForUpdates.getUpdateOther()) {
                                    SharedPreferenceHelper.setData(HandleFirebase.SELFCHANGED, "changed");  //returned it to null inside mains on pause
                                    System.out.println(".......unique key for oder..........." + checkForUpdates.getUniquekeyForLocal());
                                    System.out.println("key for others"+checkForUpdates.getUniquekeyForOthers());
                                   generateTitle.collateRecord(checkForUpdates.getUniquekeyForLocal(),checkForUpdates.getUniquekeyForOthers());



                                }

                                if(checkForUpdates.isAddtolocal())
                                {
                                    System.out.println(".......ServerKeys to add to local.........." + checkForUpdates.getKeysToAddToLocal());

                                    if (dataSnapshot.child(col_uniquekey).getValue() instanceof Map)
                                    {
                                      Map<String,String> keyMap=new HashMap<String, String>();
                                        keyMap.put(checkForUpdates.getKeysToAddToLocal(),checkForUpdates.getKeysToAddToLocal());

                                        generateTitle.setMyTitleList(
                                                keyMap,
                                                (Map<String,String>)dataSnapshot.child(col_uniquekey).getValue(),
                                                (Map<String,String>)dataSnapshot.child(col_groups).getValue(),
                                                (Map<String,String>)dataSnapshot.child(col_title).getValue(),
                                                (Map<String,String>)dataSnapshot.child(col_prior).getValue(),
                                                (Map<String,String>)dataSnapshot.child(col_diff).getValue(),
                                                (Map<String,String>)dataSnapshot.child(col_ques).getValue(),
                                                (Map<String,String>)dataSnapshot.child(col_ans).getValue(),
                                                (Map<String,String>)dataSnapshot.child(col_date).getValue(),
                                                (Map<String,String>)dataSnapshot.child(col_num_of_time_given).getValue(),
                                                (Map<String,String>)dataSnapshot.child(col_given).getValue(),
                                                (Map<String,String>)dataSnapshot.child(col_expiry).getValue()

                                        );


                                    }



                                }




                                if(checkForUpdates.isAddToOther())
                                {
                                    System.out.println(".......ServerKeys to add to server.........." + checkForUpdates.getKeyForNewRecordForOther());


                                  generateTitle.setKeyForServer(checkForUpdates.getKeyForNewRecordForOther());


                                }



                            }
                            generateTitle.addRecord2Server();
                            generateTitle.upDateRecords();

                        }
                        ///will respond when local device is new,that is empty//////

                        else {

                            if (dataSnapshot.child(col_uniquekey).getValue() instanceof Map)
                            {


                                generateTitle.setMyTitleList(
                                        (Map<String,String>)dataSnapshot.child(col_uniquekey).getValue(),
                                        (Map<String,String>)dataSnapshot.child(col_uniquekey).getValue(),
                                        (Map<String,String>)dataSnapshot.child(col_groups).getValue(),
                                        (Map<String,String>)dataSnapshot.child(col_title).getValue(),
                                        (Map<String,String>)dataSnapshot.child(col_prior).getValue(),
                                        (Map<String,String>)dataSnapshot.child(col_diff).getValue(),
                                        (Map<String,String>)dataSnapshot.child(col_ques).getValue(),
                                        (Map<String,String>)dataSnapshot.child(col_ans).getValue(),
                                        (Map<String,String>)dataSnapshot.child(col_date).getValue(),
                                        (Map<String,String>)dataSnapshot.child(col_num_of_time_given).getValue(),
                                        (Map<String,String>)dataSnapshot.child(col_given).getValue(),
                                        (Map<String,String>)dataSnapshot.child(col_expiry).getValue()

                                        );


                            }



                           // System.out.println("size of title is"+generateTitle.getTitleList().size());





                        }


                    }

                }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }









    private String getDataFromSnapshot(Map<String,String>  mymap,String key)
    {



       return mymap.get(key);


    }



    public void createDialog()
    {


        pg.setMessage("connecting");
        pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pg.setCanceledOnTouchOutside(false);
        pg.show();

    }

    public class GenerateTitle{
      Title title=null;
        Map<String,String> titleMaps =null;
        List<Title> myTitleList=new ArrayList<>();
        List<String>  keysForLocal;
        List<String> keysforRecordToUpdateInServer=new ArrayList<>();
        List<String> keystoDelete =new ArrayList<>();///after ServerKeys have been updated by uploading the newkeys from the local machine the old key and their records would need to be deleted



        public GenerateTitle( ) {

            keysForLocal=new ArrayList<>();

        }




        public List<Title> getMyTitleList() {
            return myTitleList;
        }


        /////called to create all the records present in the server in the event of a new  device//or when a new record has been added to the server///
        ///and needs to be added to the loca devise/////
        public void setMyTitleList(Map<String,String >  mapKey,Map<String,String >  ...myMaps)
        {
            for(String key:mapKey.keySet())
            {
                title=new Title();
                for(int a=0;a<myMaps.length;a++)
                {
                    switch(a)
                    {
                        case 0:
                        {titleMaps=myMaps[a];
                            System.out.println(titleMaps.get(key));
                            title.setUnique_Key(titleMaps.get(key));
                            break;
                        }
                        case 1:
                        {titleMaps=myMaps[a];title.setGroupname(titleMaps.get(key));
                            break;
                        }
                        case 2:
                        {titleMaps=myMaps[a];title.setTitle_name(titleMaps.get(key));
                            break;
                        }
                        case 3:
                        {titleMaps=myMaps[a];title.setPriority_level(Integer.parseInt(titleMaps.get(key)));
                            break;}
                        case 4:
                        {titleMaps=myMaps[a];title.setDifficulty_level(Integer.parseInt(titleMaps.get(key)));
                            break;}
                        case 5:
                        {titleMaps=myMaps[a];title.setQuestionname(titleMaps.get(key));
                            break;}
                        case 6:
                        {titleMaps=myMaps[a];title.setAnswer(titleMaps.get(key));
                            break;}
                        case 7:
                        {titleMaps=myMaps[a];title.setdateCreated(titleMaps.get(key));
                            if(titleMaps.get(key) != null)
                            {
                                System.out.println(titleMaps.get(key));
                            }
                            break;}
                        case 8:
                        {titleMaps=myMaps[a];
                            if(titleMaps.get(key)!= null)
                            {
                                title.setNumber_of_times_visited(Integer.parseInt(titleMaps.get(key)));
                            }


                            break;}
                        case 9:
                        {titleMaps=myMaps[a];
                            if(titleMaps.get(key)!= null)
                            {
                                title.setGiven_or_not((titleMaps.get(key)));
                            }

                            break;}

                        case 10:
                        {titleMaps=myMaps[a];
                            if(titleMaps.get(key)!= null)
                            {
                                title.setExpiry_date((titleMaps.get(key)));
                            }

                            break;}




                    }

                }
                myTitleList.add(title);


            }


            ti.insertAllRows(this.getMyTitleList());



        }

        ////called when new records need to be uploaded to the server//by default this should happen automartically//but in the event
        //of network failure and phone going off,during next start up of phone it check this and reloads the server/////
        public void setKeyForServer(String keyForLocal){
            keysForLocal.add(keyForLocal);

        }

        ///addRecord2Server() is finally called to push records to server//////
        public void addRecord2Server()
        {

            if(keysForLocal.size()>0)
            {
                SharedPreferenceHelper.setData(HandleFirebase.SELFCHANGED, "changed");  //returned it to null inside mains on pause
               ti.getRowTable(keysForLocal) ;



            }



        }


        public void collateRecord(String key,String keyToDelete)
        {
            keysforRecordToUpdateInServer.add(key);
            keystoDelete.add(keyToDelete);

        }

        /////////////////this method will be called to update already existing records on the server,changes would have been made on this
        ///records from the local devise////
        public void upDateRecords(){
            if(keysforRecordToUpdateInServer.size() > 0)
            {

                ti.getRowTable(keysforRecordToUpdateInServer,keystoDelete) ;
            }


        }

    }



}
