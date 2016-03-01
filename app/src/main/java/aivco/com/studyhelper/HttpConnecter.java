package aivco.com.studyhelper;





import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;

import java.util.List;



import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract Class defined to perform Http get and Post Request.It is defined as abstract because the
 * Joinkeystovalue method needs values that are dependent on the server page,so as to correctly define its names and value pairs.
 * Class that extends this class must advise on declaring a new thread for it to run.Because this class cannot run in the main thread.
 *
 */
public abstract class HttpConnecter {
    List<String> cookies=new ArrayList();
    int responseCode;
    InputStream is;
    HttpURLConnection httpURLConnection;
    final String USER_AGENT = "Mozilla/5.0";



    public String doGet(String urlString)
    {
        {   HttpURLConnection conn=null;

            StringBuilder builder=new StringBuilder();
            try {
                //connect
                URL url=new URL(urlString);
                conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                //set properties to perform request like a browser
                conn.setRequestProperty("Host", "http://192.168.100.13:8000/");
                conn.setRequestProperty("User-Agent", USER_AGENT);
                conn.setRequestProperty("Accept",
                        "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                /////if cookies is defined add cookies properties/////////
                if (cookies != null) {
                    for (String cookie : this.cookies) {
                        conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
                    }
                }
                ////get response code to verify if connection was succesfull
                responseCode=conn.getResponseCode();
                System.out.println(responseCode+" response code.......................................");

                ////get html document
                BufferedReader bReader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String lineText=null;


                while((lineText=bReader.readLine())!=null)
                {
                    builder.append(lineText);


                }



            } catch (MalformedURLException e) {
                e.printStackTrace();
                System.out.println("An error occured.......................................");

            }
            catch(IOException e)
            {



            }
            /////set cookies using the cookies datas from the response header//////
            setCookies(conn.getHeaderFields().get("Set-Cookie"));
            //joinKeysToValues(builder.toString());

            return joinKeysToValues(builder.toString());
    }}
    public abstract String joinKeysToValues(String html);




    protected void doPost(String url,String postData)
    {
        System.out.println("doPost in HttpConnector" );
        try {

            URL urlobj=new URL(url);
            HttpURLConnection conn=(HttpURLConnection)urlobj.openConnection();
            System.out.println("connection succesfull");
            // 2 post request like a browser
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Host", "http://192.168.100.8:8000/");
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            for (String cookie : this.cookies) {
                System.out.println("cookies values is ....................... "+cookie.split(";", 1)[0]);
                conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]+";"+MainActivity.sharedPreferences.getString("sessionid",""));
            }
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Referer", "http://192.168.100.8:8000/");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", Integer.toString(postData.length()));

            ///3 set httpurlconnection object to pass datas to server
            conn.setDoOutput(true);
            conn.setDoInput(true);

            DataOutputStream dOS=new DataOutputStream(conn.getOutputStream());
            dOS.writeBytes(postData);
            System.out.println("data written");
            dOS.flush();
            dOS.close();
            System.out.println("dos close");

            ///4 must obtain response code
            int responseCode=conn.getResponseCode();
            setCookies(conn.getHeaderFields().get("Set-Cookie"));
            SharedPreferences.Editor  editer=     MainActivity.sharedPreferences.edit();

            if(conn.getHeaderFields().get("Set-Cookie")!=null)
            {
                editer.putString("sessionid", conn.getHeaderFields().get("Set-Cookie").get(0));
                editer.commit();
                System.out.println("cookies....." + conn.getHeaderFields().get("Set-Cookie").get(0));

            }

            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + postData);
            System.out.println("Response Code : " + responseCode);



        } catch (MalformedURLException ex) {
            Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, ex);
        }



    }
    public void setCookies(List<String> cookiesString){
        this.cookies=cookiesString;


    }



}
