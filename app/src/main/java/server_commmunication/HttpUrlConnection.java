package server_commmunication;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class is defined to handle internet connection to and from a server.
 *
 */
public class HttpUrlConnection extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
     static final String ACTION_POST = "joe.aivco.com.http_exe.action.post";
    static final String ACTION_GET = "joe.aivco.com.http_exe.action.get";

    public static final int TEXT=1;
    public static final int IMAGE=2;
    public int textorimage;




    final String USER_AGENT = "Mozilla/5.0";
    List<String> cookies=new ArrayList<>();
    public static String log="HttpUrlConnection";


    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "joe.aivco.com.http_exe.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "joe.aivco.com.http_exe.extra.PARAM2";
    private static final String URL_PARAM ="url" ;
    private static final String ARRAY_KEYS = "joe.aivco.com.http_exe.action.keys";
    private static final String JSON_ARRAY = "jsonarraykeys";
    private ConnectivityManager cm;

    /**
     * Starts this service to perform action GET with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    ////will have to use this when performing a get request for Json request of type json///////////



    ////will have to use this when performing a get request for regular string///////////
    public static void startActionGET(Context context, String url) {
        Log.d("tag", "startActionGET");

        Intent intent = new Intent(context, HttpUrlConnection.class);
        intent.setAction(ACTION_GET);
        intent.putExtra(URL_PARAM, url);

        context.startService(intent);
    }

    /**
     * Starts this service to perform action POST with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionPOST(Context context, String param1, String param2,String url) {
        Log.d(log,"startActionPOST");
        Intent intent = new Intent(context, HttpUrlConnection.class);
        intent.setAction(ACTION_POST);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        intent.putExtra(URL_PARAM, url);
        context.startService(intent);
    }

    public  boolean networkConnectionCheck(Context context) {


        cm=(ConnectivityManager)getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork=cm.getActiveNetworkInfo();
            ///active network will represent the first network that is connected or null if no network is connected.
        return(activeNetwork != null && activeNetwork.isConnected() );



    }

    public HttpUrlConnection() {
        super("HttpUrlConnection");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_POST.equals(action)) {
                Log.d(log, "ACTION_POST");
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                final String urlParam = intent.getStringExtra(URL_PARAM);
                handleActionPOST(param1, param2, urlParam);

            } else if (ACTION_GET.equals(action)) {
                final String urlParam = intent.getStringExtra(URL_PARAM);
                handleActionGet(urlParam);
            }


        }
    }

    /**
     * Handle action POST calls the post methods to post data to a server,in a case where crsf token is disbled,one can
     * make the call directly,by calling perfrom post
     */



    private void handleActionPOST(String name, String email,String urlparam) {


        CookieHandler.setDefault(new CookieManager());
       String html= performGet(urlparam);
       String postParam= extractFormName(html, name, email);


        performPost(urlparam,email);






    }

    /**
     * Handle action GET in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGet(String urlParam)
    {

        String res=performGet(urlParam);


    }


    /**
     * Handle action GET JSON in the provided background thread with the provided
     * parameters.
     */



    protected String performGet(String urlstring)
    {   Log.d(log, "performGet");
        HttpURLConnection conn=null;
        int responseCode=0;

        String html= null;
        try {
            //connect

            URL url=new URL(urlstring);
            conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            //set properties to perform request like a browser
            conn.setRequestProperty("Host", urlstring);
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            /////if cookies is defined add cookies properties
            if (cookies != null) {
                for (String cookie : this.cookies) {
                    conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
                }
            }
            ////get response code to verify if connection was succesfull
            responseCode=conn.getResponseCode();
            Log.d(log, responseCode + " res");
            ////get html document
            html=readText(conn);


        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(log, "MalformedURLException there is error");

        }
          catch(IOException e)
          {

              Log.d(log, "IOException there is error");

          }
        /////set cookies using the cookies datas from the response header
        setCookies(conn.getHeaderFields().get("Set-Cookie"));

        return html;
    }

    private  String readText(HttpURLConnection conn) throws IOException {
        StringBuilder builder=new StringBuilder();
        BufferedReader bReader=new BufferedReader(new InputStreamReader( conn.getInputStream()));
        String lineText=null;


        while((lineText=bReader.readLine())!=null)
        {
            builder.append(lineText);


        }


        return builder.toString();
    }







    public String extractFormName(String html,String values1,String values2)
    {
        System.out.println("Extracting form's data...");
        ///1 parse html in jsoup.parse method
        Document doc= Jsoup.parse(html);
        ///2 obtain the form element from the html page returned by the get request using its id.
        Element formid=doc.getElementById("register_form");  /////project name is form_tutorials


        ///3 obtain all elements under the form id with element tag input
        Elements formtags=formid.getElementsByTag("input");//returns all the element with the tagname input under register form
        ///create a list to hold all keys and value contained in the form with the id above
        List<String> formelements=new ArrayList<>();
        for(Element element:formtags)
        {
            ///this loop will merge the keys to thier values.For keys where a value is coming from the user,partition an
            //condition as done below to add a value,otherwise its default value will be used.

            String key = element.attr("name");
            String value = element.attr("value");
            if(key.equals("title"))
                value=values1;
            else if(key.equals("picture"))
                value=values2;
            try {
                formelements.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(HttpUrlConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(value+" value data is ....................................");

        }

        StringBuilder result=new StringBuilder();
        for(String formele:formelements)
        {
            if(result.length()==0)
            {
                result.append(formele);

            }
            else
            {
                result.append("&"+formele);

            }

        }

        System.out.println(result+"result data is ....................................");
        return result.toString();
    }

    public void performPost(String url,String postData)
    {
        Log.d(log,"PERFORMING POST :"+postData);

        final  String MULTIPART_BOUNDARY = "------------------563i2ndDfv2rTHiSsdfsdbouNdArYfORhxcvxcvefj3q2f";
        try {
            URL urlobj=new URL(url);
            HttpURLConnection conn=(HttpURLConnection)urlobj.openConnection();

            // 2 post request like a browser
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Host", url);
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + MULTIPART_BOUNDARY);


            for (String cookie : this.cookies) {
                conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
            }
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Referer", url);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Type", "image/gif");
            //conn.setRequestProperty("Content-Length", Integer.toString(postData.length()));

            ///3 set httpurlconnection object to pass datas to server
            conn.setDoOutput(true);
            conn.setDoInput(true);


            DataOutputStream dOS=new DataOutputStream(conn.getOutputStream());
            dOS.writeBytes(postData);


            dOS.flush();
            dOS.close();

            ///4 must obtain response code
            int responseCode=conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + postData);
            System.out.println("Response Code : " + responseCode);



        } catch (MalformedURLException ex) {
            Logger.getLogger(HttpUrlConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HttpUrlConnection.class.getName()).log(Level.SEVERE, null, ex);
        }


    }










    public void setCookies(List<String> cookiesString){
        this.cookies=cookiesString;
    }


    /*
    *
    * Accepts a string array that represemt the keys for regular string,and accept another keys for arrays
    * will return a string,with only one word on a line
    * */




}
