package backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import aivco.com.studyhelper.HomeTab;
import aivco.com.studyhelper.MainActivity;
import aivco.com.studyhelper.SharedPreferenceHelper;
import server_commmunication.HandleFirebase;
import server_commmunication.CheckForUpdates;

import server_commmunication.UpdateServer;
import server_commmunication.ServerWithIon;

/**
 * Created by joel on 10/31/15.
 */
public  class TitleInfo extends Title  {


    //myactions////
    private static TitleinfoDb mInstance = null;
    private static final String actionTableName="Table_to_keys_for_actions";///actions are when crud operations are performed
    public static final String actionsIdCol="column_id_for_actions";
    public static final String insertedKeyCol="column_to_store_inserted_keys";
    public static final String deletedKeyCol="column_to_store_deleted_keys";
    public static final String updatedKeyCol="column_to_store_updated_keys";
    public static List<Title> listQA;



    //Titleinfodb///
     static final String GIVE ="0" ;
    static final String DNTGIVE ="1" ;
    public static String tag="GroupInfo_backend";
    Context context;
    TitleinfoDb titleinfoDb;
    MyActions myactionsDb;
    private static final String table_name="title_table";
    String db_name="db_4_titles";
    public String username="";
    private int version=1;
    private int otherVersion=1;
    public static final String id_col="_id";
    public static final String title_name_col="title_entered_by_user";
    public static final String groupname_col="groupname_title_is_located";
    public static final String given_col="if_question_is_given";////this col will hold a value that will tell if this question has been served.
    public static final String username_col="represents_username";
    public static final String email_col="represents_user_email";
    public static final String diff_col="defines_its_difficultylevel";
    public static final String prior_col="defines_its_priority";
    public static final String question_col="defines_questions";
    public static final String answer_col="answers_entered_by_user";
    public static final String uniquekey_col="unique_key_for_all_devices";
    public static final String expiry_time_col="time_for_row_to_be_reshown";//represent the time row should be reshown
    public  static final String number_visited_col="number_of_time_visited";
    public static final String date_col="date_title_created";
    private static  String query="create table " +table_name+
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
            +email_col+" STRING,"              //trying to use for email column
            +date_col+" STRING,"
            +username_col+" STRING,"
            //+email_col+" STRING,"        //just inserted
            +number_visited_col+" INTEGER"

            +")" ;

    private static String queryMyactions="create table "+actionTableName+"("+actionsIdCol+" integer primary key,"+username_col+" text,"+
    insertedKeyCol+" text,"+deletedKeyCol+" text,"+updatedKeyCol+" text)";

    private HandleFirebase handleFirebase;
    private String recordToDeleteFromServer="deleteRecord/";
    private String urlForRecordToUpDate="updaterecords/";


    public TitleInfo(Context context)
    {

       //titleinfoDb=new TitleinfoDb(context,db_name,null,version);
        titleinfoDb=TitleinfoDb.getInstance(context,db_name,null,version);
        myactionsDb=MyActions.getInstance(context,actionTableName,null,version);
        //handleFirebase=new HandleFirebase(context);
       this.context=context;

        }
    public TitleInfo(Context context,String groupName,String titlename,int diff,
                     int prior,String questions,String ans){
        //titleinfoDb=new TitleinfoDb(context,db_name,null,version);
        titleinfoDb=TitleinfoDb.getInstance(context,db_name,null,version);
        this.group_name=groupName;
        this.title_name=titlename;
        this.question_name=questions;
        this.answer=ans;
        this.difficulty_level=diff;
        this.priority_level=prior;
        this.context=context;


    }

/*
*
* @param
* check the Myactions table to return all the row present in the table
* */
    public List<HandleRecords> checkActions()
    {  System.out.println(".......................HandleRecords.................");
        List<HandleRecords> handleRecordsList=new ArrayList<>();
        Cursor cursorForRow=null;
        HandleRecords hr=new HandleRecords();
        Cursor c=myactionsDb.selectTable();
        System.out.println(".....................size of c is................." + c.getCount());
        while (c.moveToNext())
        {


                if(c.getString(c.getColumnIndex(insertedKeyCol)) != null)
            {
                System.out.println(".......................HandleRecords..there are inserted record...............");
                cursorForRow= titleinfoDb.selectRowWithUnique(c.getString(c.getColumnIndex(insertedKeyCol)));
                hr.setInsertedColoumnObject(generateTitle(cursorForRow));
            }

            if(c.getString(c.getColumnIndex(deletedKeyCol)) != null)
            {
                System.out.println(".......................Deleted record.................");
                //cursorForRow= titleinfoDb.selectRowWithUnique(c.getString(c.getColumnIndex(deletedKeyCol)));
                hr.setDeletedColoumnObject(c.getString(c.getColumnIndex(deletedKeyCol)));

            }
            if(c.getString(c.getColumnIndex(updatedKeyCol)) != null)
            {
                System.out.println("*****************************updated record********************************");
                cursorForRow= titleinfoDb.selectRowWithUnique(c.getString(c.getColumnIndex(updatedKeyCol)));

                hr.setUpdatedColoumnObject(generateTitle(cursorForRow));

            }

            handleRecordsList.add(hr);
            hr=new HandleRecords();

        }

        c.close();

        return handleRecordsList;
    }

    /** <h1>
     * method generate a new title object bases on cursor from db
     * </h1>
     * @param c represents a cursor object.
     * */
    public Title  generateTitle(Cursor c){
        Title thisTitle=new Title();
        if(c.moveToFirst()){
            System.out.println(".......................query has a size of................."+c.getCount());

            thisTitle.setUnique_Key(getCursorValue(c, uniquekey_col));
        thisTitle.setGroupname(getCursorValue(c, groupname_col));
        thisTitle.setTitle_name(getCursorValue(c, title_name_col));
        thisTitle.setQuestionname(getCursorValue(c, question_col));
        thisTitle.setAnswer(getCursorValue(c, answer_col));
        thisTitle.setdateCreated(getCursorValue(c, date_col));
        thisTitle.setExpiry_date(getCursorValue(c, expiry_time_col));
        thisTitle.setGiven_or_not(getCursorValue(c, given_col));
        thisTitle.setNumber_of_times_visited(getCursorValueInt(c, number_visited_col));
        thisTitle.setDifficulty_level(getCursorValueInt(c, diff_col));
        thisTitle.setPriority_level(getCursorValueInt(c, prior_col));}


        return thisTitle;
    }

    /**@param column to return the string present in a column*/
    private String getCursorValue(Cursor c,String column)
    {
      return  c.getString(c.getColumnIndex(column)) ;

    }
    /**@param column to return the int present in a column*/
    private int getCursorValueInt(Cursor c,String column)
    {
        return  c.getInt(c.getColumnIndex(column)) ;

    }

    public synchronized void insertRows_db(){
        Log.d(tag, "insert rows to db ");
        titleinfoDb.inSertAll(this);
        myactionsDb.addedKey(this.getUnique_Key());
        insertRowsToServer();
        SharedPreferenceHelper.setData(MainActivity.DATASTORED_KEY,MainActivity.DATASTORED_KEY_VALUE);
    }


    /**
     * @param key the realkey by accepting the key and returning only the random generated key,without the timestamps
     * <h3>
     *   key
     * </h3>
     *  @param key
     *  key for this record
     *
     * */
    public static String realKey(String key){


        System.out.println("**************************" + key);
        if(key != null && key.contains(" ") )
        {
        String ans=key.substring(key.indexOf(" "),key.length());
        return ans.trim();
        }

        return key;
    }

    public boolean insertRowsToServer()
    {




        return false;
    }


    public List [] searchResult(String text2Search,String gName)
    {

        List<String> keyRes=new ArrayList<>();
        List<String> titleRes=new ArrayList<>();
        Cursor c=titleinfoDb.searchTable(text2Search,gName);
        while(c.moveToNext())
        {

            keyRes.add(c.getString(c.getColumnIndex(uniquekey_col)));
            titleRes.add(c.getString(c.getColumnIndex(title_name_col)));

        }
       return new List[]{keyRes,titleRes};

    }




    public void updateGroupName(String unique_key,String newRow)
    {

        titleinfoDb.update(groupname_col,newRow,unique_key);
        updateActions(unique_key);
    }

    ///////////////return a row using its uniquekey///this method is called to update and to add an existing record to server//////////////////
    public void getRowTable(List<String> uniquekey)
    {


        Cursor c=null;
        for(String key:uniquekey)
        {
            c= titleinfoDb.selectRowWithUnique(key);
            if(c.moveToFirst())
            {

                handleFirebase.upLoadObject(
                        new UpdateServer(
                                c.getString(c.getColumnIndex(groupname_col)),
                        c.getString(c.getColumnIndex(title_name_col)),
                        String.valueOf(c.getInt(c.getColumnIndex(prior_col))),
                        String.valueOf(c.getInt(c.getColumnIndex(diff_col))),
                                c.getString(c.getColumnIndex(question_col)),
                                c.getString(c.getColumnIndex(answer_col)),
                                c.getString(c.getColumnIndex(uniquekey_col)),
                                c.getString(c.getColumnIndex(expiry_time_col)),
                                String.valueOf(c.getInt(c.getColumnIndex(number_visited_col))),
                                c.getString(c.getColumnIndex(given_col)),
                                c.getString(c.getColumnIndex(date_col))));




            }

        }



    }


    ///////////////this method is called to update an an existing record on the server////will delete the oldrecord//////////////
    public void getRowTable(List<String> uniquekey,List<String> delete)
    {
        System.out.println(".......................deleting records................."+delete);

        for(String key:delete)
        {

            handleFirebase.nullifyOldRecord(
                    new UpdateServer(key));}





    }







    //////method called to return all the given records back to false///////////
    public void returnToDefault()
    {
        Cursor cursor=titleinfoDb.extractAllid();
        while(cursor.moveToNext())
        {
            String uniquekey=cursor.getString(cursor.getColumnIndex(uniquekey_col));

            titleinfoDb.returnGiventoDefault(uniquekey);

        }


        cursor.close();
    }
    //////the value it accepts will determine if it will be returned to its default value or if it will be recorded as given.//given
    //here means the data has been given to the user/////a value of 1//represents true//0 repsents false,which is the default value//
    ////an expiry time has been introduced,a program will always run when application starts to determine if the row should be returned//
    //to zero.This will be based on the expiry date time object,and also by the number of times this row has been given////////
    /////////////////update the titles that have been showed by the home page////1 reps true,meaning the row with this id has been shown///////////0 rep false/////

    public void  updateGiven(String unique_key,String expirytime,int numoftimegiven){


         titleinfoDb.updateGiven(unique_key, "1", expirytime, numoftimegiven);
        myactionsDb.updatedKeys(unique_key);
        myactionsDb.removeAddedKey(unique_key);
        new ServerWithIon(context).postUpdatedWhenGivenRecord(unique_key, numoftimegiven + "", expirytime, "updaterecordwhengiven/");

        titleinfoDb.close();
    }
    public void updateActions(String unique_key){
        myactionsDb.updatedKeys(unique_key);
        myactionsDb.removeAddedKey(unique_key);

    }

    /**
     *checks if a record has expired by comparing its expiry date to todays today.
     * */

    public void calculateIfExpired()
    {
        //System.out.println(".......................calculating if expiry.................");

        Cursor c=titleinfoDb.selectforExpiryNumberOfTimeV();
        while(c .moveToNext())
        {
            String uniquekey=c.getString(c.getColumnIndex(uniquekey_col));
            String expiry=c.getString(c.getColumnIndex(expiry_time_col));
            System.out.println("............there are values to calculate................"+expiry);

            if(!(expiry == null) && (!expiry.equals("0")) && ifExpired(expiry))
            {
                System.out.println("............i have expired................"+expiry);

                titleinfoDb.upDateGivenForExpiry(c.getString(c.getColumnIndex(uniquekey_col)),GIVE);
                updateActions(uniquekey);


            }
            ///////////handle nvc///////////////
            int nvisited=c.getInt(c.getColumnIndex(number_visited_col));

            switch (c.getInt(c.getColumnIndex(diff_col)))
            {
                case 1:{
                    //System.out.println("............case 2...........number of time visited is................."+nvisited);
                    if (calculateNumberVisited(nvisited))
                    {
                        titleinfoDb.update(diff_col, "0", c.getString(c.getColumnIndex(uniquekey_col)));
                        titleinfoDb.update(number_visited_col,"0",c.getString(c.getColumnIndex(uniquekey_col)));
                        updateActions(uniquekey);

                    }
                    break;
                }

                case 2:{
                    //System.out.println("............case 2...........number of time visited is................."+nvisited);
                    if (calculateNumberVisited(nvisited))
                    {
                        titleinfoDb.update(diff_col, "1", c.getString(c.getColumnIndex(uniquekey_col)));
                        titleinfoDb.update(number_visited_col,"0",c.getString(c.getColumnIndex(uniquekey_col)));
                        updateActions(uniquekey);

                    }
                    break;
                }
                case 3: {
                    //System.out.println("............case 3...........number of time visited is................."+nvisited);
                    if (calculateNumberVisited(nvisited))
                    {

                        titleinfoDb.update(diff_col, "2", c.getString(c.getColumnIndex(uniquekey_col)));
                        titleinfoDb.update(number_visited_col,"0",c.getString(c.getColumnIndex(uniquekey_col)));
                        updateActions(uniquekey);

                    }

                    break;
                }


            }







        }


        c.close();


    }

    public boolean ifExpired(String expiry){
        //System.out.println(".......................calculate expiry method.................");
        DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY-MM-DD-HH:mm:ss");
        LocalDateTime expirydate=new LocalDateTime(expiry);

        LocalDateTime todaydatetime=new LocalDateTime();
        //if todays date is after expiry date,return true

        return todaydatetime.isAfter(expirydate);
    }

public boolean calculateNumberVisited(int nOTV){

    return nOTV > SharedPreferenceHelper.getInt(MainActivity.KEYFORNUMBEROFTIMEVISITED);
}



    ////////checkupdate will compare server data to client data anytime application starts/////////////////
     public List<CheckForUpdates> checkUpdate (Set<String> keys4ODERS)
     {
         CheckForUpdates checkthis=null;
         List<CheckForUpdates> checkForUpdatesList=new ArrayList<>();

         String uniquekeyforlocal=null;
         Long timeStamp4Local=0l;
         String cValue=null;
         String uniquekey4oder=null;
         Long timeStamp4Oder=0l;
         boolean out=false;






        Cursor c=titleinfoDb.extractUniqueid();

         ////////////check if the server has more ServerKeys than the local device//////
         //////////any key that is checked queried against all the key in the local system should return true,if false that/////
         //the ServerKeys does not exist on the local system//the querying is done using its key and not the entire uniquekey///////
         if(c.getCount() < keys4ODERS.size())
         {
             for(String key:keys4ODERS)
             {
                 String realKey=key.substring(key.indexOf(" "));

                 if(!titleinfoDb.isAvailable(realKey))
                 {
                     System.out.println("............not on local machine...... "+realKey);
                     checkthis=new CheckForUpdates();
                     checkthis.setAddtolocal(true);
                     checkthis.setKeystoAddlocal(key);
                     checkForUpdatesList.add(checkthis);


                 }
             }


         }

      while(c.moveToNext())
         {   out=false;
             int countOther=keys4ODERS.size();
             cValue=c.getString(c.getColumnIndex(uniquekey_col));
             uniquekeyforlocal=cValue.substring(cValue.indexOf(" "));
             timeStamp4Local=Long.valueOf(cValue.substring(0, cValue.indexOf(" ")));
             System.out.println(".............uniquekey "+uniquekeyforlocal);
             System.out.println(".............timeStamp "+timeStamp4Local);

             for(String key:keys4ODERS)
             {

                 System.out.println(".............count is ................"+--countOther);
                 //--countOther;//reduces the value count until it is zero
                 uniquekey4oder=key.substring(cValue.indexOf(" "));

                 timeStamp4Oder=Long.valueOf(key.substring(0, cValue.indexOf(" ")));
                 System.out.println(".............timestamp4oder "+timeStamp4Oder);
                System.out.println(".............uniquekey4oder "+uniquekey4oder);



                 if((uniquekey4oder.trim()) .equals(uniquekeyforlocal.trim()))
                 {
                     System.out.println("......it is equal .."+uniquekey4oder +"unique key for local "+ uniquekeyforlocal);

                     if(timeStamp4Oder<(timeStamp4Local))
                     {
                         System.out.println(".........local.timestamp is higher.................."+uniquekey4oder);
                         checkthis=new CheckForUpdates();
                         checkthis.setUpdateOther(true);
                         checkthis.setUniquekeyForOthers(key);
                         checkthis.setUniquekeyForLocal(cValue);
                         checkForUpdatesList.add(checkthis);

                         //upload the values to other


                     }

                     else if(timeStamp4Oder > timeStamp4Local)
                     {
                         System.out.println("............local is lesser  download the ServerKeys that have been.changed on the server................."+uniquekey4oder);
                         //upload the values for the local system
                         checkthis=new CheckForUpdates();
                         checkthis.setUpdateLocal(true);
                         checkthis.setUniquekeyForLocal(cValue);
                         checkthis.setUniquekeyForOthers(key);
                         checkForUpdatesList.add(checkthis);


                     }

                     else if(timeStamp4Oder.equals(timeStamp4Local))
                     {
                         System.out.println("..........timestamp..is equal.....................");
                         checkthis=new CheckForUpdates();
                         checkForUpdatesList.add(checkthis);

                     }




                  out=true;

                 }
                 ////the idea for the condition below is based on the premise that,countOther will be zero only when it goes through the whole iteration
                 ////and cvalue ie the value key in question from the db,does not match any key on the server,this will mean that record is not on the server
                 else if( countOther == 0)
                 {


                     System.out.println(".....................not on the server......................." +cValue);
                     checkthis=new CheckForUpdates();
                     checkthis.setAddToOther(true);
                     checkthis.setKeyForNewRecordForOther(cValue);
                     checkForUpdatesList.add(checkthis);
                     // the values is not yet present in the server so send it to server //

                 out=true;
                 }






                 System.out.println(".............breaking out of for ................");
                 if(out)

                 {break;}


         }}

         c.close();
         return checkForUpdatesList;
     }



    /////////////////delete a record using its id///////////////
    public void  deleteRow(String  uniqueid){
        System.out.println(".................................................");

        titleinfoDb.deleteRow(uniqueid);
        myactionsDb.deletedKeys(uniqueid);
        myactionsDb.removeAddedKey(uniqueid);
        myactionsDb.removeupdatedKeys(uniqueid);
        new ServerWithIon(context).postDeletedRecord(uniqueid, recordToDeleteFromServer);

    }
    /////////////////method will return topics as cursor,this will be used by the home activity to simulate the questions////////
    ////////////////the string that it accepts represents,the priority levels to returns///////////////////////
    public Cursor getTopics(String priority){



        return  titleinfoDb.extractTopic(priority,GIVE);
    }

    public List<Title> generateQuestionAndAnswer(){
       // System.out.println("generate qa called");

        listQA = new ArrayList<>();
        Cursor c=null;
        for(int prior_level=3;prior_level>0;--prior_level)
        {
           System.out.println("priority value for "+prior_level);

             c=titleinfoDb.extractTopic(String.valueOf(prior_level),GIVE);
            while (c.moveToNext())
            {
                if(Integer.parseInt(c.getString(c.getColumnIndex(diff_col)))!= 0) //perform check so as to return only priority levels between 1 and 3
                listQA.add(generateTitle4QA(c));
            }


        }
        if(c != null)
        c.close();
        System.out.println("size of total title QAgenerated IS " + listQA.size());
        HomeTab.handleQuesandans.sendEmptyMessage(HomeTab.QAISREADY);

        return listQA;
    }





    /**method to generate Title object from a cursor*/
    public Title generateTitle4QA(Cursor c){
        Title title4QA=new Title();

        title4QA.setUnique_Key(c.getString(c.getColumnIndex(uniquekey_col)));
        title4QA.setQuestionname(c.getString(c.getColumnIndex(question_col)));
        title4QA.setAnswer(c.getString(c.getColumnIndex(answer_col)));
        title4QA.setNumber_of_times_visited(c.getInt(c.getColumnIndex(number_visited_col)));
        title4QA.setDifficulty_level(Integer.parseInt(c.getString(c.getColumnIndex(diff_col))));

        return title4QA;
    }


    /**id in this case id represents uniquekey,this is used because it needs to be unique accross all devices*/
    public void upDateRow(String unique_key){
        System.out.println("....................update..row...................");

        titleinfoDb.updateRow(this, unique_key);
        myactionsDb.updatedKeys(unique_key);
        myactionsDb.removeAddedKey(unique_key);
        new ServerWithIon(context).postUpdatedRecord(this,unique_key,"contentupdate", urlForRecordToUpDate);

    }

    /**id in this case id represents uniquekey,this is used because it needs to be unique accross all devices*/
    public void upDate(String unique_key,String ans){
        System.out.println("....................update..row...................");

        titleinfoDb.update(answer_col, ans, unique_key);
        myactionsDb.updatedKeys(unique_key);
        myactionsDb.removeAddedKey(unique_key);
        //new ServerWithIon(context).postUpdatedRecord(this,unique_key,"contentupdate", urlForRecordToUpDate);

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
    /////////////returns a row from the table ////////////////////
    public Cursor  collectRow(String uniquekey)
    {

      return  titleinfoDb.selectRowWithUnique(uniquekey);
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
    public List<Title> collectTitles(String groupname)
    {
        List<Title> tilesList=new ArrayList<>();
       Cursor c= titleinfoDb.extractTitles(groupname);
        while(c.moveToNext())
        {
            Title title=new Title();
            title.setTitle_name(c.getString(c.getColumnIndex(title_name_col)));
            title.setUnique_Key(c.getString(c.getColumnIndex(uniquekey_col)));
            tilesList.add(title);



        }
        c.close();
        return tilesList;
    }



    ///////////////return a row from the table/////////////////////
    public Cursor collectTable(String row)
    {
        Log.d(tag, "collectTable ");
        return titleinfoDb.selectTable(row);
    }



    public Map<String,Object> myMap(String value,String uKey) {
        System.out.println(".......................mymap.............");

        Map<String,Object> thismap=new HashMap<>();
        thismap.put(uKey, value);


        return thismap;
    }






    /////////called for a new system,in the case where a records are present on the server//////////////
    public void insertAllRows(List<Title> mytitle)
    {

       for(Title title:mytitle)
       {
           titleinfoDb.inSertAll(title);
       }

    }

/**@param will check the table to see if there is any row present
 *
 */
    public boolean isEmpty()
    {


        System.out.println("....................is Empty.....................");


      return   titleinfoDb.selectAllRow().moveToFirst();
    }

    public void removeAddedKey(String key){
        myactionsDb.removeAddedKey(key);
    }

    public void removeUpdatedKey(String key){
        myactionsDb.removeupdatedKeys(key);
    }
    public void removeDeletedKey(String key){
        myactionsDb.removeDeletedKeys(key);
    }

    public void close(){


        titleinfoDb.close();
    }




    ////////////////////////////////////////////innerclass that creates table////////////////////////////////////////////////////////////////////////////////7
    public static class TitleinfoDb extends SQLiteOpenHelper{


    public TitleinfoDb(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }




        /////////////////////////innerclass constructor////////////////////////////////
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(query);
        System.out.println("....................Table created succesfully.......................");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
        public static TitleinfoDb getInstance(Context ctx,String name, SQLiteDatabase.CursorFactory factory, int version) {
            if (mInstance == null) {
                mInstance = new TitleinfoDb(ctx.getApplicationContext(),name,factory,version);
            }
            return mInstance;


        }




        public void inSertAll(Title titles)
        {
            System.out.println("....................insert all.......................");
            SQLiteDatabase writeSql=getWritableDatabase();
            ContentValues contentValues=new ContentValues();

            //contentValues.put(id_col, 0);
            contentValues.put(title_name_col, titles.getTitle_name());
            contentValues.put(groupname_col, titles.getGroupname());
            contentValues.put(diff_col, titles.getDifficulty_level());
            contentValues.put(prior_col, titles.getPriority_level());
            contentValues.put(question_col, titles.getQuestionname() );
            contentValues.put(answer_col,titles.getAnswer() );
            contentValues.put(uniquekey_col, titles.getUnique_Key());
            contentValues.put(date_col, titles.getdateCreated());
            contentValues.put(expiry_time_col, titles.getExpiry_date());
            contentValues.put(number_visited_col, titles.getNumber_of_times_visited());
            contentValues.put(given_col, titles.getGiven_or_not());
            contentValues.put(username_col, SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY));
            contentValues.put(email_col, SharedPreferenceHelper.getString(MainActivity.MYEMAIL));







            long ans=  writeSql.insert(table_name, null, contentValues);

            writeSql.close();



        }


        //////////////////return all row from the table///////////////////////////
        public Cursor selectAllRow(){
            SQLiteDatabase readsql= getReadableDatabase();
            return readsql.query(table_name, new String[]{}
                    ,  email_col + "=?", new String[]{SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY)}, null, null, null);

        }

        /**
         * returns true or false,depening on if the table is empty
         * */
        public boolean isTableNew(){
            SQLiteDatabase readsql= getReadableDatabase();
            Cursor c= readsql.query(table_name, new String[]{}
                    ,  email_col + "=?", new String[]{SharedPreferenceHelper.getString(MainActivity.MYEMAIL)}, null, null, null);
            boolean res= !c.moveToFirst();

            c.close();
            // System.out.println("table is new :"+ c.getCount());
            return res;
        }
        /**
         * @param columnName accepts a column name and returns a cursor holding all the record for that column*/
        public Cursor selectSingleColumn(String columnName){


            System.out.println("............selectDiff...... ");

            SQLiteDatabase readsql= getReadableDatabase();


            return readsql.query(table_name, new String[]{columnName}
                    , email_col + "=? ", new String[]{SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY)}, null, null, null);



        }





        //////////////////return a row from the table///////////////////////////
        public Cursor selectTable(String row){


            SQLiteDatabase readsql= getReadableDatabase();


            return readsql.query(table_name, new String[]{}
                    , title_name_col + "=?" + " AND " + email_col + "=?", new String[]{row, SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY)}, null, null, null);

        }


        //////////////////return a row using its uniquenumber///////////////////////////
        public Cursor selectRowWithUnique(String uniquekey){

            System.out.println("............selectRowWithUnique...... " + uniquekey);
            String realKey=realKey(uniquekey);

            System.out.println("............real key is...... " + realKey);
            SQLiteDatabase readsql= getReadableDatabase();
            String query = " select  * from "+table_name + " where "+uniquekey_col+" like '%"+realKey+"'";

            return readsql.rawQuery(query, null);
        }


        public Cursor searchTable(String string2search,String gname){


            SQLiteDatabase readsql= getReadableDatabase();
            String query = "select "+  title_name_col+","+uniquekey_col+" from "+table_name + " where " + email_col +"=? AND "+ groupname_col +"=?" +" AND upper("+title_name_col+") like '%"+string2search.toUpperCase()+"%'";  //the query is performed to be case insensitive,by converting all the records in the where column to uppercase and converting the value to uppercase also
            return readsql.rawQuery(query,new String[]{SharedPreferenceHelper.getString(MainActivity.MYEMAIL),gname});

        }

        //////////////////return all rows uniqueid and diff_col///////////////////////////
        public Cursor selectforExpiryNumberOfTimeV(){

            System.out.println("............selectforExpiryNumberOfTimeV...... ");
            SQLiteDatabase readsql= getReadableDatabase();
            return readsql.query(table_name, new String[]{uniquekey_col, diff_col, expiry_time_col,number_visited_col}
                    , email_col + "=? ", new String[]{SharedPreferenceHelper.getString(MainActivity.MYEMAIL)}, null, null, null);
        }

        //////////////////return a row using its uniquenumber///////////////////////////
        public Cursor selectDiff(){

            System.out.println("............selectDiff...... ");

            SQLiteDatabase readsql= getReadableDatabase();
            return readsql.query(table_name, new String[]{uniquekey_col, diff_col, expiry_time_col}
                    , email_col + "=? ", new String[]{SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY)}, null, null, null);

        }




        //////////returns all the groupname in the database///////////
        public Cursor extractGroup()
        {
            SQLiteDatabase readsql= getReadableDatabase();


            return readsql.query(true,table_name, new String[]{id_col,groupname_col,title_name_col,date_col},email_col + "=?", new String[]{SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY)}, null, null,groupname_col,null);
        }


        /////////returns all the titles in a particular group//////////
        public Cursor extractTitles(String groupname)
        {   SQLiteDatabase readsql= getReadableDatabase();

            return readsql.query
                    (table_name, new String[]{uniquekey_col, title_name_col}, groupname_col + " = ?"+ " AND " + email_col + "=?",
                            new String[]{groupname,SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY)}, null, null, null);


        }

        /////////returns all the titles in a particular group//////////
        public Cursor extractAllid()
        {   SQLiteDatabase readsql= getReadableDatabase();

            return readsql.query
                    (table_name, new String[]{uniquekey_col}, email_col + "=?",
                            new String[]{SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY)}, null, null, null);
        }

        /////////returns all unique id present in the table//////////
        public Cursor extractUniqueid()
        {   SQLiteDatabase readsql= getReadableDatabase();

            return readsql.query
                    (table_name, new String[]{uniquekey_col}, email_col + "=?",
                            new String[]{SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY)}, null, null, null);


        }

        /////////returns both the numberoftimecolumns and the expirydate column needed to calculate the value for given//////////
        public Cursor forGiven(String diff,int numOfTimeGive,String expiry)
        {   SQLiteDatabase readsql= getReadableDatabase();

            return readsql.query
                    (table_name, new String[]{uniquekey_col,expiry_time_col,number_visited_col}, email_col + "=?",
                            new String[]{SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY)}, null, null, null);


        }





        ///////////////extract topic for the home activity////////////////////////
        public Cursor extractTopic(String priority,String notGiven)
        {   SQLiteDatabase readsql= getReadableDatabase();
            return readsql.query
                    (table_name, new String[]{uniquekey_col, question_col, answer_col, diff_col, number_visited_col}, prior_col + " = ?" + " AND " + given_col + "=?" + " AND " + email_col + "=?",
                            new String[]{priority, notGiven, SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY)}, null, null, null);

        }
        ///////////////delete a row from a the record//////////////////
        //////since id number are unique,it will be sensible to obtain the id number for the title//////
        public void deleteRow(String uniqueid)
        {
            SQLiteDatabase readsql= getReadableDatabase();

            int a=  readsql.delete(table_name, uniquekey_col + " like '%" + TitleInfo.realKey(uniqueid) + "'", null);

            //int a=  readsql.delete(table_name, uniquekey_col + " = ?", new String[]{uniqueid});
           System.out.println("............................delete row in titleinfo " + a);
           readsql.close();
        }


        public void updateRow(Title title,String uniqueKey)
        {


            SQLiteDatabase writeSql=getWritableDatabase();
            ContentValues contentValues=new ContentValues();

            //contentValues.put(id_col, 0);
            contentValues.put(diff_col, title.getDifficulty_level());
            contentValues.put(prior_col, title.getPriority_level());
            contentValues.put(question_col, title.getQuestionname());
            contentValues.put(answer_col, title.getAnswer());
            contentValues.put(date_col, title.getdateCreated());
            contentValues.put(uniquekey_col, title.getUnique_Key());
            contentValues.put(number_visited_col, title.getNumber_of_times_visited());
            int ans=  writeSql.update(table_name, contentValues, uniquekey_col + " like '%" + TitleInfo.realKey(uniqueKey) + "'", null);


            //long ans=  writeSql.update(table_name, contentValues, uniquekey_col + " =?" + " AND " + username_col + "=?", new String[]{uniqueKey, SharedPreferenceHelper.getString(MainActivity.USERNAME_KEY)});
           System.out.println("Row Updated" + ans);
            writeSql.close();

        }

        public void updateGroups(String newval,String preval)
        {

            SQLiteDatabase writeSql=getWritableDatabase();
            ContentValues contentValues=new ContentValues();


            contentValues.put(groupname_col, newval);
            long ans=  writeSql.update(table_name, contentValues, groupname_col + " = ?", new String[]{preval});

            Log.d(tag, ans + "");
        }



        /**<h3>
         * Method should be called to update one column in a table
         * </h3>
         * @param colname accepts a column name that would be updated
           @param value the value for the column
         @param key the key that should be used to search the column
                    @return ans return the number of rows updated,returns 0 if no row was updated
        */
        public long update(String colname,String value,String key)
        {
            SQLiteDatabase writeSql=getWritableDatabase();

            ContentValues contentValues=new ContentValues();

            contentValues.put(colname, value);
            int ans=  writeSql.update(table_name, contentValues, uniquekey_col + " like '%" + TitleInfo.realKey(key) + "'", null);
            writeSql.close();

            return ans;
        }

        public boolean isAvailable(String key){

            SQLiteDatabase readsql= getReadableDatabase();
            System.out.println("....................isavailable.....................");
            String query = " select "+ uniquekey_col +" from "+table_name + " where "+uniquekey_col+" like '%"+key+"'";
            Cursor c=readsql.rawQuery(query, null);
            boolean res= c.moveToFirst();
            c.close();
            readsql.close();
            return res;
        }


        public void upDateGivenForExpiry(String uniquekey,String value)
        {
            System.out.println(".......................upDateGivenForExpiry.................");

            SQLiteDatabase writeSql=getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(given_col, value);
            contentValues.put(expiry_time_col, "0");
            long ans=  writeSql.update(table_name, contentValues, uniquekey_col +" = ?", new String[]{uniquekey});

            System.out.println(".......................upDateGivenForExpiry................." + ans);
        }

        public void updateGiven(String uniquekey,String value,String expirytime,int num_of_time)
        {

            SQLiteDatabase writeSql=getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(given_col, value);
            contentValues.put(expiry_time_col, expirytime);
            contentValues.put(number_visited_col, num_of_time);
            long ans=  writeSql.update(table_name, contentValues, uniquekey_col +" = ?", new String[]{uniquekey});

            System.out.println(".......................updateGiven................." + ans);
        }

        public void returnGiventoDefault(String uniqueid)
        {


            SQLiteDatabase writeSql=getWritableDatabase();
            ContentValues contentValues=new ContentValues();

            contentValues.put(given_col, GIVE);
            contentValues.put(expiry_time_col, "0");
            long ans=  writeSql.update(table_name, contentValues, uniquekey_col +" = ?", new String[]{uniqueid});


        }



    }
    public static class MyActions  extends SQLiteOpenHelper{
        private static MyActions myActionsInstance;

        public MyActions(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }


        public static MyActions getInstance(Context ctx,String name, SQLiteDatabase.CursorFactory factory, int version) {
            if (myActionsInstance == null) {
                myActionsInstance = new MyActions(ctx.getApplicationContext(),name,factory,version);
            }
            return myActionsInstance;


        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(queryMyactions);
            System.out.println("....................Table queryMyactions created succesfully.......................");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        public Cursor selectTable(){
            System.out.println("....................selectTable MyActions.......................");

            SQLiteDatabase sdb=getWritableDatabase();
            return sdb.query(actionTableName, new String[]{}
                    ,  username_col + "=?", new String[]{SharedPreferenceHelper.getString(MainActivity.MYEMAIL)}, null, null, null);

        }

        public void addedKey(String key)
        {
            SQLiteDatabase sdb=getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(insertedKeyCol,key);
            contentValues.put(username_col, SharedPreferenceHelper.getString(MainActivity.MYEMAIL));
            long ans=  sdb.insert(actionTableName, null, contentValues);
            sdb.close();
        }
        public void removeAddedKey(String key)
        {
            SQLiteDatabase readsql= getReadableDatabase();
            int a=  readsql.delete(actionTableName, insertedKeyCol + " like '%" + TitleInfo.realKey(key) + "'", null);

            readsql.close();
        }
//////ServerKeys for updated records///////////////
        public synchronized void updatedKeys(String key)
        {
            System.out.println("............real key in updatedKeys is...... " + TitleInfo.realKey(key));
            SQLiteDatabase readsql= getReadableDatabase();
            String query = " select "+ updatedKeyCol +" from "+actionTableName + " where "+updatedKeyCol+" like '%"+TitleInfo.realKey(key)+"'";
            Cursor c= readsql.rawQuery(query, null);
            System.out.println("....size is ...... "+c.getCount());
            if (c.moveToFirst())
            {

                readsql.close();
                return ;
            }

            ContentValues contentValues=new ContentValues();
            contentValues.put(updatedKeyCol,key);
            contentValues.put(username_col,SharedPreferenceHelper.getString(MainActivity.MYEMAIL));
            long ans=  readsql.insert(actionTableName, null, contentValues);
            System.out.println("added key is  " + key );
            readsql.close();
            c.close();
        }
        //ServerKeys for updated records///////////////
        public void removeupdatedKeys(String key)
        {



            /*String query = " delete  from "+actionTableName + " where "+updatedKeyCol+" like '%"+realKey+"'"; */

            SQLiteDatabase readsql= getReadableDatabase();

            int a=  readsql.delete(actionTableName, updatedKeyCol + " like '%" + TitleInfo.realKey(key) + "'", null);
            readsql.rawQuery(query, null);
            readsql.close();
        }

 ///////////ServerKeys for deleted records //////////////
        public synchronized void deletedKeys(String key)
        {

            SQLiteDatabase sdb=getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(deletedKeyCol,key);
            contentValues.put(username_col, SharedPreferenceHelper.getString(MainActivity.MYEMAIL));
            long ans=  sdb.insert(actionTableName, null, contentValues);
            sdb.close();

        }

        ///////////ServerKeys for deleted records //////////////
        public void removeDeletedKeys(String key)
        {
            System.out.println("removeDeletedKeys : " + key);
            SQLiteDatabase readsql= getReadableDatabase();
            int a=  readsql.delete(actionTableName, deletedKeyCol + " like '%" + TitleInfo.realKey(key) + "'", null);
            readsql.close();

        }


        /**
         * returns true or false,depening on if the table is empty
         * */
        public boolean isTableNew(){
            SQLiteDatabase readsql= getReadableDatabase();
            Cursor c= readsql.query(actionTableName, new String[]{}
                    ,  username_col + "=?", new String[]{SharedPreferenceHelper.getString(MainActivity.MYEMAIL)}, null, null, null);
            boolean res= !c.moveToFirst();

            c.close();
            // System.out.println("table is new :"+ c.getCount());
            return res;
        }



    }

    //****************************************overriden methods***************************************//


    @Override
    public String acceptRecordsFromServer(JsonArray recordArray) {
        System.out.println("+++++++++accept records from server+++++++++++++");
        List<String> keys=new ArrayList<>();
        for(JsonElement jo:recordArray)
        {  System.out.println("+++++++++accept records from server has keys+++++++++++++");
            String thisKey=jo.getAsJsonArray().get(11).getAsString();
            System.out.println("real key is "+TitleInfo.realKey(thisKey));
            if (!titleinfoDb.isAvailable(TitleInfo.realKey(thisKey))) //make checks to ensure no two records are inserted
            {
                this.setAnswer(jo.getAsJsonArray().get(0).getAsString());
                this.setdateCreated(jo.getAsJsonArray().get(1).getAsString());
                this.setDifficulty_level(jo.getAsJsonArray().get(2).getAsInt());
                this.setExpiry_date(jo.getAsJsonArray().get(3).getAsString());
                this.setGiven_or_not(jo.getAsJsonArray().get(4).getAsString());
                this.setGroupname(jo.getAsJsonArray().get(5).getAsString());
                this.setNumber_of_times_visited(jo.getAsJsonArray().get(6).getAsInt());
                this.setPriority_level(jo.getAsJsonArray().get(7).getAsInt());
                //this.set(jo.getAsJsonArray().get(8).getAsString());
                this.setQuestionname(jo.getAsJsonArray().get(9).getAsString());
                this.setTitle_name(jo.getAsJsonArray().get(10).getAsString());
                this.setUnique_Key(thisKey);
                System.out.println(".................inside title overriden method...." + this.getUnique_Key());
                titleinfoDb.inSertAll(this);;

            }



            keys.add(thisKey);





            //titleinfoDb.inSertAll(this);
        }

        return new JSONArray(keys).toString();
    }

    @Override
    public List<String> acceptupdatedRecordsFromServer(JsonArray recordArray)
    {
        List<String> keys=new ArrayList<>();
        for(JsonElement jo:recordArray)
        {   String thisKey=jo.getAsJsonArray().get(11).getAsString();
            System.out.println("real key is "+TitleInfo.realKey(thisKey));
            if (titleinfoDb.isAvailable(TitleInfo.realKey(thisKey)))
            {
                this.setAnswer(jo.getAsJsonArray().get(0).getAsString());
                this.setdateCreated(jo.getAsJsonArray().get(1).getAsString());
                this.setDifficulty_level(jo.getAsJsonArray().get(2).getAsInt());
                this.setExpiry_date(jo.getAsJsonArray().get(3).getAsString());
                this.setGiven_or_not(jo.getAsJsonArray().get(4).getAsString());
                this.setGroupname(jo.getAsJsonArray().get(5).getAsString());
                this.setNumber_of_times_visited(jo.getAsJsonArray().get(6).getAsInt());
                System.out.println("..........accept updated record .number of time visited.." + jo.getAsJsonArray().get(6).getAsInt());
                this.setPriority_level(jo.getAsJsonArray().get(7).getAsInt());
                //this.set(jo.getAsJsonArray().get(8).getAsString());
                this.setQuestionname(jo.getAsJsonArray().get(9).getAsString());
                this.setTitle_name(jo.getAsJsonArray().get(10).getAsString());
                this.setUnique_Key(thisKey);
                System.out.println("..........inside accept update record..." + this.getUnique_Key());

                titleinfoDb.updateRow(this, thisKey);
            }





            keys.add(thisKey);





            //titleinfoDb.inSertAll(this);
        }


        return keys;
    }

    @Override
    public List<String> deletedRecordsKeys(JsonArray deleteArray) {
        System.out.println("inside delete record keys");
        List<String> keys=new ArrayList<>();

        String thiskey=" ";
        for(JsonElement jsonElement:deleteArray)
        {
            System.out.println("..........................there are keys to delete................................................");
            thiskey=jsonElement.getAsString();
            if (thiskey != "") {
                titleinfoDb.deleteRow(thiskey);
                myactionsDb.removeupdatedKeys(thiskey);
                myactionsDb.removeAddedKey(thiskey);
                keys.add(thiskey);


            }



        }


        return keys;
    }

    @Override
    public boolean isApplicationNew() {
        System.out.println("isApplicationNew()");
        if(titleinfoDb.isTableNew() && myactionsDb.isTableNew())
           return  true ;

        return false;
    }
}


