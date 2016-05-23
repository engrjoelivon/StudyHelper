package backend;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import server_commmunication.ServerWithIon;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class DbService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS


    // TODO: Rename parameters
    private static final String EXTRA_UNIQUE_KEY = "backend.extra.UNIQUE_KEY";
    private static final String EXTRA_URL = "backend.extra.UNIQUE_KEY";
    private static final String EXTRA_COL_NAME = "backend.extra.COLNAME";
    private static final String EXTRA_COL_VALUE = "backend.extra.COLVALUE";
    private static final String EXTRA_COL_ArrayOfValues = "backend.extra.COLVALUE";
    private static final String FORUPDATE = "backend.ACTION.FORUPDATE";
    private static final String FORUPDATEALL = "backend.ACTION.FORUPDATEALL";
    private static final String FOREXPIRY = "backend.ACTION.forexpiry";
    private static final String GENERATEQUQ ="backend.ACTION.forqa";
    private static final String VALIDATEUSER = "backend.ACTION.tovalidateuser";;
    ;
    private static Map<String,Map<String,String>> mapObject;
    private static ArrayList<Title> titles;

    static Context context1;
    public DbService() {
        super("DbService");


    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startAction(Context context, String url) {
        Intent intent = new Intent(context, DbService.class);
        intent.putExtra(EXTRA_URL, url);

        context1=context;
        context.startService(intent);
    }

    public static void StartUpdateLocal(Context context,String [] val) {
        Intent intent = new Intent(context, DbService.class);
        intent.setAction(FORUPDATE);
        intent.putExtra(EXTRA_COL_ArrayOfValues, val);
        context1=context;
        context.startService(intent);
    }

    public static void StartUpdateAllLocal(Activity context) {
        Intent intent = new Intent(context, DbService.class);
        intent.setAction(FORUPDATEALL);
        context1=context.getApplicationContext();
        context.startService(intent);
    }
    public static void startActionForExpiry(Context context) {
        Intent intent = new Intent(context, DbService.class);
        intent.setAction(FOREXPIRY);
        context1=context;
        context.startService(intent);
    }
    //if server returns records for a local system
    public static void startActionForgenerateQA(Context context) {
        Intent intent = new Intent(context, DbService.class);
        intent.setAction(GENERATEQUQ);
        context1=context;
        context.startService(intent);
    }


    //if server returns records for a local system
    public static void startActionToValidateUser(Context context) {
        Intent intent = new Intent(context, DbService.class);
        intent.setAction(VALIDATEUSER);
        context1=context;
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if(intent.getAction().equals(FORUPDATE))
            {
                //updateLocal(intent.getStringArrayExtra(EXTRA_COL_ArrayOfValues));
            }


               /* final String uniquekey = intent.getStringExtra(EXTRA_UNIQUE_KEY);
                final String colName = intent.getStringExtra(EXTRA_COL_NAME);
                final String colValue = intent.getStringExtra(EXTRA_COL_VALUE);
                insertData(uniquekey,colName,colValue);*/
             else if(intent.getAction().equals(FORUPDATEALL))
            {
                System.out.println("....................for update all....................."+ Thread.currentThread());
                //TitleInfo titleinfo=new TitleInfo(context1);
                new ServerWithIon(context1).checkRecordAtStartUp();
                //titleinfo.upDateAll(titles);
            }

            else if(intent.getAction().equals(FOREXPIRY))
            {
                System.out.println("....................indb runnung expiry....................." + Thread.currentThread());

                TitleInfo titleInfo=new TitleInfo(context1);
                titleInfo. calculateIfExpired();
            }
            else if(intent.getAction().equals(GENERATEQUQ))
            {
                new TitleInfo(context1).generateQuestionAndAnswer();

            }

            else if(intent.getAction().equals(VALIDATEUSER)){

                new ServerWithIon(context1).checkUserValidation(context1);

            }


        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void insertData(String uniquekey, String colName,String colValue) {
        System.out.println("....................insertdata.....................");
    TitleInfo titleInfo=new TitleInfo(context1);




    }





    public void setRow(Map<String,String> mymap,String key4row)
    {




    }
    /*
    * @param
    *
    *
    * */





}
