package aivco.com.studyhelper;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;




/**
 * LoginToServer class extends HttpConverter so as to define its custom implementation  for joinKeystoValues method
 * any class that initilizes this class must provide an implementation for it to run in another thread-
 */
public class LoginToServer extends HttpConnecter {

    private  String username,password;
    User user;
    public LoginToServer(User user) {
        this.username=user.username;
        this.password=user.password;


    }
    public LoginToServer(String username,String password) {
        this.username=username;
        this.password=password;
    }

    @Override
    public String joinKeysToValues(String html) {

        System.out.println("Extracting form's data...");
        ///1 parse html in jsoup.parse method
        Document doc= Jsoup.parse(html);
        ///2 obtain the form element from the html page returned by the get request using its id.
        Element formid=doc.getElementById("login_form");  /////project name is form_tutorials
        ///3 obtain all elements under the form id with element tag input
        Elements formtags=formid.getElementsByTag("input");//returns all the element with the tagname input under register form
        ///create a list to hold all ServerKeys and value contained in the form with the id above
        List<String> formelements=new ArrayList();
        for(Element element:formtags)
        {
            ///this loop will merge the ServerKeys to thier values.For ServerKeys where a value is coming from the user,partition an
            //condition as done below to add a value,otherwise its default value will be used.

            String key = element.attr("name");
            String value = element.attr("value");
            if(key.equals("username"))
                value=username;
            else if(key.equals("password"))
                value=password;


            try {
                formelements.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, ex);
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

        System.out.println(result + "result data is ....................................");
        return result.toString();





    }
}
