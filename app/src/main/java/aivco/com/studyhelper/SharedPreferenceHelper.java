package aivco.com.studyhelper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by joel on 7/18/15.
 * This is a sharedPreference helper class,every necessary functions in the sharedPreference class has been defined here.
 * To use this class,instantiate it inside the Activity class and pass in the activity context.After this call one of its method
 * to save or getdatas.The setData method is overloaded,it can accept string,longs int etc.
 */
public class SharedPreferenceHelper {
private static final String APP_SHARED_PREF= SharedPreferenceHelper.class.getSimpleName();
private static SharedPreferences _sharedPreference;
private static SharedPreferences.Editor editor;
    private static final String DEFAULTSTRINGVALUE=null;//holds the default value for string this can be referenced inside the activity that defines the class,to know if the value has changed.
public SharedPreferenceHelper(Context context)
{
 this._sharedPreference=context.getSharedPreferences(APP_SHARED_PREF, Activity.MODE_PRIVATE);
    this.editor=_sharedPreference.edit();



}
    public static String getString(String key)
    {


        return _sharedPreference.getString(key, DEFAULTSTRINGVALUE);
    }
    public static int getInt(String key)
    {


        return _sharedPreference.getInt(key, 0);
    }

    public Long getLong(String key)
    {


        return _sharedPreference.getLong(key, 0);
    }
    public boolean getBool(String key)
    {


        return _sharedPreference.getBoolean(key, false);
    }

    public void setBool(String key,boolean data)
    {

        editor.putBoolean(key, data);
        editor.commit();


    }

    public static void setData(String key,String data)
    {

        editor.putString(key, data);
        editor.apply();

    }
    public void setData(String key,int data)
    {
        editor.putInt(key,data);
        editor.commit();


    }
    public  void setData(String key,long data)
    {
        editor.putLong(key,data);
        editor.commit();


    }

    public static void removeData(String key)
    {
        editor.remove(key);
        editor.apply();


    }



}
