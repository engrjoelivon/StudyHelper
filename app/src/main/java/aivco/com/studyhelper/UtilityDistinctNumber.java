package aivco.com.studyhelper;

import android.util.Log;

import java.util.Calendar;

/**
 *
 */
public class UtilityDistinctNumber {

    public static synchronized Long  myNumber()
    {
        Log.v("log_tag", " generating path..............................");
        String uniqueId = getTodaysDate()  + getCurrentTime() ;
        //+ (Math.random()+Math.random());

        Log.v("log_tag", "......................................."+uniqueId );


      return Long.valueOf(uniqueId);
    }

    private static String getTodaysDate() {


        final Calendar c = Calendar.getInstance();
        int todaysDate = (c.get(Calendar.YEAR) * 10000) +
                ((c.get(Calendar.MONTH) + 1) * 100) +
                (c.get(Calendar.DAY_OF_MONTH));
        Log.w("DATE:", String.valueOf(todaysDate));
        return (String.valueOf(todaysDate));

    }

    private static String getCurrentTime() {

        final Calendar c = Calendar.getInstance();
        int currentTime = (c.get(Calendar.HOUR_OF_DAY) * 10000) +
                (c.get(Calendar.MINUTE) * 100) +
                (c.get(Calendar.SECOND));
        Log.w("TIME:", String.valueOf(currentTime));
        return (String.valueOf(currentTime));
    }
}
