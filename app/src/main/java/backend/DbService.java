package backend;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

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
    private static final String EXTRA_COL_NAME = "backend.extra.COLNAME";
    private static final String EXTRA_COL_VALUE = "backend.extra.COLVALUE";
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
    public static void startAction(Context context, String uniquekey, String colName,String colValue) {
        Intent intent = new Intent(context, DbService.class);
        intent.putExtra(EXTRA_UNIQUE_KEY, uniquekey);
        intent.putExtra(EXTRA_COL_NAME, colName);
        intent.putExtra(EXTRA_COL_VALUE, colValue);
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


                final String uniquekey = intent.getStringExtra(EXTRA_UNIQUE_KEY);
                final String colName = intent.getStringExtra(EXTRA_COL_NAME);
                final String colValue = intent.getStringExtra(EXTRA_COL_VALUE);
                insertData(uniquekey,colName,colValue);




        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void insertData(String uniquekey, String colName,String colValue) {
        System.out.println("....................insertdata.....................");
    TitleInfo titleInfo=new TitleInfo(context1);
        titleInfo.updateorCreate(uniquekey,colName,colValue);



    }



    public class UnsupportedOperationException extends Exception{
        public UnsupportedOperationException(String detailMessage) {
            super(detailMessage);
        }
    }
}
