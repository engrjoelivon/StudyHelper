package aivco.com.studyhelper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import backend.TitleInfo;

public class ServerService extends Service {
    static Context mycontext;
    private static TitleInfo TitleI;
    private static ErrorHolder errorNotifier;
    UpdateServer updateServer;

    int responseCode;
    String log="ServerService";
    OutputStream os;
    List<String> cookies=new ArrayList();

    InputStream is;
    HttpURLConnection httpURLConnection;
    final String USER_AGENT = "Mozilla/5.0";

    public ServerService() {

        Log.d(log,"ServerService constructor");
    }

    //////BEFORE THE SERVER SERVICE STARTS settitleinfo is called to set the titleinfo
    public static void setTitleInfo (TitleInfo titleInfo)
    {

        TitleI=titleInfo;

    }

    public static void setErrorNotifier(ErrorHolder notifier) {
        errorNotifier = notifier;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
    public static void service_context(Context context)
    {
        mycontext=context;


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      updateServer=new UpdateServer();
       System.out.println("......................"+internetchecker());
        pushDatas(internetchecker());
        stopSelf(startId);

        return super.onStartCommand(intent, flags, startId);

    }
    public boolean internetchecker()

    {
        ConnectivityManager cm=(ConnectivityManager)getSystemService(mycontext.CONNECTIVITY_SERVICE);
        NetworkInfo nI[]=cm.getAllNetworkInfo();
        for(NetworkInfo info:nI)
        {

            if(info.getState()== NetworkInfo.State.CONNECTED)
            {return true;}
        }
        return false;
    }

    public void pushDatas(boolean yes)
    {
        if(true)
        {
            Log.d(log,"there is internet connection");
            Thread thread=new Thread(updateServer);
            thread.start();
        }
        else{

            errorNotifier.error("You do not have internet connection");
        }


    }

    ///////////////class that manages connection to server//////////////////

    public class UpdateServer implements Runnable
    {
        List<String> cookies=new ArrayList();


        @Override
        public void run()
        {
            String url ="http://192.168.100.8:8000/addMobileTitle/";
           /* Log.d(log, "in run method for server");
            //pushDatasForUpdate(TitleI);
            String url ="http://192.168.100.8:8000/addMobileTitle/";
            String html=performGet(url);
            if(responseCode == 200)
            {
            String data=    extractFormName(html,TitleI)     ;
            performPost(url,data);}*/
            DatasToServer dts=new DatasToServer(TitleI);
            String html=dts.doGet(url);
            dts.doPost(url,html);


        }






    }






}
