package aivco.com.studyhelper;

import android.os.Build;

/**
 * Created by johnanderson1 on 11/7/15.
 */
public class StringUtility {
  //////will always return the first letter caps and other letter as small letters
    public static String firstlettercaps(String text)
    {
        if(text.length()>0)
        {
            text=text.toLowerCase();

            return	text.substring(0, 1).toUpperCase() + text.substring(1);


        }
        else{

            return "";
        }

    }
    /////will return a capiterlized first letter and every other letter after a fullstop///////
    public static String onlyFirstlettercaps(String text)
    {
        if(text.length()>0)
        {


            return	text.substring(0, 1).toUpperCase() + text.substring(1);


        }
        else{

            return "";
        }

    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

}
