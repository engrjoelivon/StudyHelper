package backend;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import aivco.com.studyhelper.Create_Group;
import aivco.com.studyhelper.MainActivity;
import aivco.com.studyhelper.SharedPreferenceHelper;
import server_commmunication.HandleFirebase;
import server_commmunication.UpdateServer;

/**
 * Created by joel on 10/31/15.
 */
public class TitleInfo extends Title  {
    Date_Format date_format;
    public String tag="GroupInfo_backend ";
    Context context;
    TitleinfoDb titleinfoDb;
    private final String table_name="title_table";
    String db_name="db_4_titles";
    public String username="";
    private int version=1;
    public static final String id_col="_id";
    public static final String title_name_col="title_entered_by_user";
    public static final String groupname_col="groupname_title_is_located";
    public static final String given_col="if_question_is_given";////this col will hold a value that will tell if this question has been served.
    public static final String username_col="represents_username";
    public static final String diff_col="defines_its_difficultylevel";
    public static final String prior_col="defines_its_priority";
    public static final String question_col="defines_questions";
    public static final String answer_col="answers_entered_by_user";
    public static final String uniquekey_col="unique_key_for_all_devices";
    public static final String expiry_time_col="time_for_row_to_be_reshown";//represent the time row should be reshown
    public static final String timecreated_col="reps_the_time_row_was_shown ";//holds the exact time the row was created
    public  static final String number_visited_col="number_of_time_visited";
    public static final String date_col="date_title_created";
    private  String query="create table " +table_name+
            "("
            +id_col+" INTEGER PRIMARY KEY,"
            +title_name_col+" TEXT,"
            +groupname_col+" TEXT,"
            +diff_col+" INTEGER,"
            +prior_col +" INTEGER,"
            +question_col+" STRING,"
            +answer_col+" TEXT,"
            +given_col+" STRING,"
            +uniquekey_col+" STRING,"
            +expiry_time_col+" STRING,"
            +timecreated_col+" STRING,"
            +date_col+" STRING,"
            +username_col+" STRING,"
            +number_visited_col+" INTEGER"

            +")" ;

    private Context appCompatActivity;
    private HandleFirebase handleFirebase;


    public TitleInfo(Context context)
    {

       titleinfoDb=new TitleinfoDb(context,db_name,null,version);
        handleFirebase=new HandleFirebase(context);

        //this.appCompatActivity=(AppCompatActivity)context;

        ///this.insertTitles_db();
       // Cursor cursor= this.collectTable("Fragment");
       // cursor.moveToFirst();
       // Log.d(tag, cursor.getString(cursor.getColumnIndex(question_col)));
        //Log.d(tag, cursor.getString(cursor.getColumnIndex(answer_col)));

        }
    public TitleInfo(Context context,String groupName,String titlename,int diff,
                     int prior,String questions,String ans){
        titleinfoDb=new TitleinfoDb(context,db_name,null,version);
        this.group_name=groupName;
        this.title_name=titlename;
        this.question_name=questions;
        this.answer=ans;
        this.difficulty_level=diff;
        this.priority_level=prior;


    }





    public void insertRows_db(){
        Log.d(tag, "insert rows to db ");


        insertRowsToServer();
        titleinfoDb.inSertTitle(this.getGroupname(), this.getTitle_name(), this.getDifficulty_level(), this.getPriority_level()
                , this.getQuestionname(), this.getAnswer(), this.getUnique_Key(), this.getTimeCreated());

        SharedPreferenceHelper.setData(MainActivity.DATASTORED_KEY,MainActivity.DATASTORED_KEY_VALUE);
    }

    public boolean insertRowsToServer()
    {

        handleFirebase.upLoadObject(new UpdateServer(this.getGroupname(), this.getTitle_name(), String.valueOf(this.getPriority_level()), String.valueOf(this.getDifficulty_level()),
                this.getQuestionname(), this.getAnswer(), this.getUnique_Key(), this.getTimeCreated(),this.getNumber_of_times_visited()+""));
        //handleFirebase.upLoadObject(map);

        return false;
    }


    //////method called to return all the given records back to false////////////
    public void returnToDefault()
    {
        Cursor cursor=titleinfoDb.extractAllid();
        while(cursor.moveToNext())
        {
            int id=cursor.getInt(cursor.getColumnIndex(id_col));
            titleinfoDb.returnGiventoDefault(id, "0", null);

        }


    }
    //////the value it accepts will determine if it will be returned to its default value or if it will be recorded as given.//given
    //here means the data has been given to the user/////a value of 1//represents true//0 repsents false,which is the default value//
    ////an expiry time has been introduced,a program will always run when application starts to determine if the row should be returned//
    //to zero.This will be based on the expiry date time object,and also by the number of times this row has been given////////
    /////////////////update the titles that have been showed by the home page////1 reps true,meaning the row with this id has been shown///////////0 rep false/////

    public void  updateGiven(int idcol,String expirytime,int numoftimegiven){
        System.out.println(".................................................");

         titleinfoDb.updateGiven(idcol,"1",expirytime,numoftimegiven);
    }






    /////////////////delete a record using its id///////////////
    public void  deleteRow(String  uniqueid){
        System.out.println(".................................................");

        titleinfoDb.deleteRow(uniqueid);
    }
    /////////////////method will return topics as cursor,this will be used by the home activity to simulate the questions////////
    ////////////////the string that it accepts represents,the priority levels to returns///////////////////////
    public Cursor getTopics(String priority){

        return  titleinfoDb.extractTopic(priority);
    }


    /**id in thi case represents uniquekey,this is used because it needs to be unique accross all devices*/
    public void upDateRow(String id,int diff,int priority,String question,String answer){


        titleinfoDb.updateAll(id,diff,priority,question,answer);
    }

    /////////////will return unique groups names,no repetitions////////////////////////////
    public List<String> collectUniqueGn()
    {

        String gname=null;
        Cursor c=titleinfoDb.extractGroup();
        Set<String> set =new TreeSet<>();
        while(c.moveToNext())
        {
            gname = c.getString(c.getColumnIndex(groupname_col));
            if(gname != null)
            set.add(gname);


        }
        List<String > l=new ArrayList<>(set);
        c.close();
        return l;
    }

    /////////////return all the groups in db////////////////////
    public Cursor collectGroups()
    {
        Log.d(tag, "collectGroups() ");
        return titleinfoDb.extractGroup();

    }
    /////////////return  groups as a list////////////////////
    public List<String>  collectGroupsAsList()
    {   List<String > l=new ArrayList<>();
        Log.d(tag, "collectGroupsAsList ");

        Cursor c=titleinfoDb.extractGroup();

        while(c.moveToNext())
        {
           String gname= c.getString(c.getColumnIndex(groupname_col));
            Log.d(tag, gname);
            l.add(gname);


        }
        return  l;

    }

    /////////////return questions as a list////////////////////
    public List<String>  questionsAsList()
    {   List<String > l=new ArrayList<>();
        Log.d(tag, "collectGroups() ");

        Cursor c=titleinfoDb.extractQuestion();
        c.moveToFirst();
        while(c.moveToNext())
        {
            l.add(c.getString(c.getColumnIndex(question_col)));

        }
        return  l;

    }
    //////////////returns all the ordered titles names in db with the enterered group names///////////////////
    public List<String> collectUniqueTn(String groupname)
    {
        Log.d(tag, "collectUniqueTn");

        String gname=null;
        Cursor c=titleinfoDb.extractTitles(groupname);
        Set<String> set =new TreeSet<>();
        while(c.moveToNext())
        {
            gname = c.getString(c.getColumnIndex(title_name_col));
            Log.d(tag, gname);
            set.add(gname);


        }
        List<String > l=new ArrayList<>(set);
        return l;
    }


    //////////////returns all the titles in db///////////////////
    public Cursor collectTitles(String groupname)
    {

        return titleinfoDb.extractTitles(groupname);
    }



    ///////////////return a row from the table/////////////////////
    public Cursor collectTable(String row)
    {
        Log.d(tag, "collectTable ");
        return titleinfoDb.selectTable(row);
    }


    /**the method below will be called to update or create a new row depending on if the row already exist.the data to be updated will be
     * coming from the server.
     * key represents the uniquekey,the colname
     * */
    public void updateorCreate(String uniquekey,String colName,String value)
    {
        System.out.println("....................updateorcreate.....................");
       if(titleinfoDb.isAvailable(uniquekey))
       {
           titleinfoDb.update(colName,value,uniquekey);

       }
        else{
           titleinfoDb.newRow(uniquekey,value,colName);

       }


    }





    ///////////////////////innerclass that creates table/////////////////////////////////////
    public class TitleinfoDb extends SQLiteOpenHelper{

    public TitleinfoDb(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

     /////////////////////////innerclass constructor////////////////////////////////
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(query);
        Log.d(tag, "Table created succesfully ");
        System.out.println("....................Table created succesfully.......................");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


        public void inSertTitle(String group_name,String title,int diff,
                                int prior,String ques,String ansc,String uniquekey,String time_created)
        {

            System.out.println("difficulty is "+ diff);
            System.out.println("priority is "+ prior);


            SQLiteDatabase writeSql=getWritableDatabase();
            ContentValues contentValues=new ContentValues();

            //contentValues.put(id_col, 0);
            contentValues.put(title_name_col, title);
            contentValues.put(groupname_col, group_name);
            contentValues.put(diff_col, diff);
            contentValues.put(prior_col, prior);
            contentValues.put(question_col, ques);
            contentValues.put(answer_col, ansc);
            contentValues.put(uniquekey_col, uniquekey);
            contentValues.put(timecreated_col, time_created);
            contentValues.put(given_col, "0");
            contentValues.put(number_visited_col, "0");
            contentValues.put(date_col, TitleInfo.this.getDate_col());
            contentValues.put(username_col, SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY));
            System.out.println("the date is" + TitleInfo.this.getDate_col());





            long ans=  writeSql.insert(table_name, null, contentValues);
            Log.d(tag,ans+" row  inserted");
            Log.d(tag, "title successfully inSerted ");




        }
        //////////////////return a row from the table///////////////////////////
        public Cursor selectTable(String row){
            Log.d(tag, "selectTable ");

            SQLiteDatabase readsql= getReadableDatabase();
            Log.d(tag, "after sql ");

            return readsql.query(table_name, new String[]{}
                    , title_name_col +"=?"+ " AND " + username_col + "=?", new String[]{row,SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY)}, null, null, null);

        }



        //////////returns all the groupname in the database///////////
        public Cursor extractGroup()
        {
            SQLiteDatabase readsql= getReadableDatabase();


            return readsql.query(true,table_name, new String[]{id_col,groupname_col,title_name_col,timecreated_col}, username_col + "=?", new String[]{SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY)}, null, null,groupname_col,null);
        }

        public void extractGroup(String where)

        {   SQLiteDatabase readsql= getReadableDatabase();

            Cursor cursor=readsql.query(table_name,null, title_name +" = ?", new String[]{where}, null, null, null);
            //cursor.moveToFirst();


            String a=cursor.getString(cursor.getColumnIndex(title_name));

            Log.d(tag, a + "");

        }
        /////////returns all the titles in a particular group//////////
        public Cursor extractTitles(String groupname)
        {   SQLiteDatabase readsql= getReadableDatabase();

            Cursor cursor = readsql.query
                    (table_name, new String[]{id_col, title_name_col}, groupname_col + " = ?"+ " AND " + username_col + "=?",
                            new String[]{groupname,SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY)}, null, null, null);
            //cursor.moveToFirst();

            //cursor.moveToLast();
            //String a=cursor.getString(cursor.getColumnIndex(title_name_col));

           // Log.d(tag, a + "");
            return cursor;
        }

        /////////returns all the titles in a particular group//////////
        public Cursor extractAllid()
        {   SQLiteDatabase readsql= getReadableDatabase();

            Cursor cursor = readsql.query
                    (table_name, new String[]{id_col}, username_col + "=?",
                            new String[]{SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY)}, null, null, null);
            //cursor.moveToFirst();

            //cursor.moveToLast();
            //String a=cursor.getString(cursor.getColumnIndex(title_name_col));

            // Log.d(tag, a + "");
            return cursor;
        }


        ///////////////extract question////////////////////////
        public Cursor extractQuestion()
        {   SQLiteDatabase readsql= getReadableDatabase();

            Cursor cursor = readsql.query
                    (table_name, new String[]{question_col}, null,
                            null, null, null, null);
            //cursor.moveToFirst();

            //cursor.moveToLast();
            //String a=cursor.getString(cursor.getColumnIndex(title_name_col));

            // Log.d(tag, a + "");
            return cursor;
        }


        ///////////////extract topic for the home activity////////////////////////
        public Cursor extractTopic(String priority)
        {   SQLiteDatabase readsql= getReadableDatabase();

            Cursor cursor = readsql.query
                    (table_name, new String[]{id_col, question_col,answer_col,diff_col,number_visited_col}, prior_col + " = ?" + " AND " + given_col + "=?"+ " AND " + username_col + "=?",
                            new String[]{priority,"0",SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY)}, null, null, null);

            //cursor.moveToFirst();

            //cursor.moveToLast();
            //String a=cursor.getString(cursor.getColumnIndex(title_name_col));

            // Log.d(tag, a + "");
            return cursor;
        }
        ///////////////delete a row from a the record//////////////////
        //////since id number are unique,it will be sensible to obtain the id number for the title//////
        public void deleteRow(String uniqueid)
        {
            SQLiteDatabase readsql= getReadableDatabase();


            int a=  readsql.delete(table_name, uniquekey_col + " = ?", new String[]{uniqueid});
           System.out.println("............................delete row" + a);

        }


        public void updateAll(String  id,int diff,int prior,String ques,String ansc)
        {


            SQLiteDatabase writeSql=getWritableDatabase();
            ContentValues contentValues=new ContentValues();

            //contentValues.put(id_col, 0);
            contentValues.put(diff_col, diff);
            contentValues.put(prior_col, prior);
            contentValues.put(question_col, ques);
            contentValues.put(answer_col, ansc);

            System.out.println("difficulty is " + diff);
            System.out.println("priority is " + prior);



            long ans=  writeSql.update(table_name, contentValues, uniquekey_col + " =?" + " AND " + username_col + "=?", new String[]{id, SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY)});
            Log.d(tag,ans+" row  inserted");
            Log.d(tag, "title successfully inSerted ");

        }

        public void updateGroups(String newval,String preval)
        {

            SQLiteDatabase writeSql=getWritableDatabase();
            ContentValues contentValues=new ContentValues();


            contentValues.put(groupname_col, newval);
            long ans=  writeSql.update(table_name, contentValues, groupname_col + " = ?", new String[]{preval});

            Log.d(tag, ans+"");
        }

        public long update(String colname,String value,String key)
        {

            SQLiteDatabase writeSql=getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(colname, value);
            long ans=  writeSql.update(table_name, contentValues, uniquekey_col +" = ?", new String[]{key});
            System.out.println("....................number of row affected....................."+ans);

            return ans;
        }

        public boolean isAvailable(String key){
            System.out.println("....................isavailable.....................");
            SQLiteDatabase readsql= getReadableDatabase();
            Cursor cursor = readsql.query
                    (table_name, new String[]{uniquekey_col}, uniquekey_col + " = ?",
                            new String[]{key}, null, null, null);

            return cursor.moveToFirst();
        }
       public void newRow(String uniquekey,String value,String colvalue){
           SQLiteDatabase writeSql=getWritableDatabase();
           ContentValues contentValues=new ContentValues();
           contentValues.put(uniquekey_col, uniquekey);
           contentValues.put(username_col, SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY));
           contentValues.put(colvalue, value);
           long ans = writeSql.insert(table_name, null, contentValues);
           System.out.println("....................row created....................."+ans);

       }


        public void updateGiven(int colnumber,String value,String expirytime,int num_of_time)
        {

            SQLiteDatabase writeSql=getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(given_col, value);
            contentValues.put(expiry_time_col, expirytime);
            contentValues.put(number_visited_col, num_of_time);
            long ans=  writeSql.update(table_name, contentValues, id_col +" = ?", new String[]{colnumber+""});

            Log.d(tag,ans+"");
        }

        public void returnGiventoDefault(int colnumber,String value,String expirytime)
        {

            SQLiteDatabase writeSql=getWritableDatabase();
            ContentValues contentValues=new ContentValues();

            contentValues.put(given_col, value);
            contentValues.put(expiry_time_col, expirytime);
            long ans=  writeSql.update(table_name, contentValues, id_col +" = ?", new String[]{colnumber+""});

            Log.d(tag,ans+"");
        }




    }

    }


