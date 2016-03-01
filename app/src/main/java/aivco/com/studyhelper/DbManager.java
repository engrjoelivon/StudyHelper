package aivco.com.studyhelper;

import android.content.Context;
import android.database.Cursor;

import backend.TitleInfo;

/**
 * Class sole function is to extract rows that are clicked on a list when list is switching from one groups to titles and titles to
 * content
 */
public class DbManager {
    TitleInfo titleInfo;
    public DbManager(Context context) {
         titleInfo=new TitleInfo(context);
    }
    public String getSelectedRowString(Cursor s,int i){


        s.moveToFirst();
        s.move(i);

        return s.getString(s.getColumnIndex(titleInfo.groupname_col) );
    }
    public String getSelectedTitleRow(Cursor s,int i)
    {


        s.moveToFirst();
        s.move(i);

        return s.getString(s.getColumnIndex(titleInfo.title_name_col) );
    }
    public String getSelectedRowOnSpinner(Cursor s,int i)
    {


        s.moveToFirst();
        s.move(i);

        return s.getString(s.getColumnIndex(titleInfo.title_name_col) );
    }
    public String getSelectedRowOnSpinnerGps(Cursor s,int i)
    {


        s.moveToFirst();
        s.move(i);

        return s.getString(s.getColumnIndex(titleInfo.title_name_col) );
    }

}
