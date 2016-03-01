package backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by johnanderson1 on 10/31/15.
 */
public class Question {
    public String tag="GroupInfo_backend";
    public final String table_name="question_table";
    public String first_col="col_name";

    public String _id ="id";
    public String query="Create table "+table_name+"("+_id+" INTEGER PRIMARY KEY,"+first_col+" TEXT"+")";
    private int version=1;
    private String db_name="Db_4_question";
    private final String first_col_key="backend_first_col";
    Context context;

    public Question(Context context) {
        this.context=context;
        Log.d(tag,"GroupInfo constructor");
        question_db gdb=new question_db(context,db_name,null,version);
        //gdb.inSertGroup("How does ____ works");
        gdb.extractGroup("How does ____ works");
        // gdb.deleteGroup("Android");
        //gdb.updateGroups("Hardware");
    }
    private class question_db extends SQLiteOpenHelper {






        public question_db(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            Log.d("", "question_db constructor");

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            Log.d(tag, "onCreate ");
            sqLiteDatabase.execSQL(query);

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
        public void inSertGroup(String group_name)
        {
            Log.d(tag, "inSertquestion ");
            SQLiteDatabase writeSql=getWritableDatabase();
            ContentValues contentValues=new ContentValues();


            contentValues.put(first_col, group_name);
            long ans=  writeSql.insert(table_name,null,contentValues);
            Log.d(tag,ans+"");

        }

        public void extractGroup()
        {   SQLiteDatabase readsql= getReadableDatabase();

            Cursor cursor=readsql.query(table_name, new String[]{_id, first_col}, null, null, null, null, null);
            cursor.moveToFirst();

            cursor.moveToLast();
            String a=cursor.getString(cursor.getColumnIndex(first_col));

            Log.d(tag,a+"");

        }
        public void extractGroup(String where)
        {   SQLiteDatabase readsql= getReadableDatabase();
            Cursor cursor=readsql.query(table_name, new String[]{_id, first_col}, first_col +" = ?", new String[]{where}, null, null, null);
            cursor.moveToFirst();

            cursor.moveToLast();
            String a=cursor.getString(cursor.getColumnIndex(first_col));

            Log.d(tag,a+"");

        }
        public void deleteGroup(String row2del)
        {
            SQLiteDatabase readsql= getReadableDatabase();


            int a=  readsql.delete(table_name, first_col +" = ?", new String[]{"Hardware"});
            Log.d(tag,a+"");

        }
        public void updateGroups(String val)
        {

            SQLiteDatabase writeSql=getWritableDatabase();
            ContentValues contentValues=new ContentValues();


            contentValues.put(first_col, val);
            long ans=  writeSql.update(table_name, contentValues, first_col +" = ?", new String[]{"Java"});

            Log.d(tag,ans+"");
        }

    }








}
