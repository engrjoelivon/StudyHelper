package aivco.com.studyhelper;




import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.database.Cursor;

import android.os.AsyncTask;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import backend.TitleInfo;
import server_commmunication.HandleFirebase;


/**
 *Prints the final view
 */
public class TitleContentActivityFragment extends Fragment  {
    TitleInfo ti;
    EditText tvforquestion,tvforanswer,diff_level_view,priority_level_view;
    Toolbarinterface toolbarinterface;
    private String this_id;
    static View view;
    LinearLayout radio4priority,textLayout;
    TextView priorlabel,diffLabel;
    public static  int diff_value=3;
    public static int priority_value=3;
    public String titleName,groupName,expiryDate,givenOrNot;
    public int numOfTimeShown;
    public TitleContentActivityFragment() {


    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ti=new TitleInfo(MainActivity.mycontext);


       //interface is initialized to respond when changes are made from the titlecontentactivity class
        TitleContentActivity.toolbarInterfaceListener(toolbarinterface = new Toolbarinterface()
        {
            @Override
            public void upDateedit() {
                editContent();


            }

            @Override
            public void upDatedelete() {

                ti.deleteRow(this_id);
                getActivity().finish();

            }

            @Override
            public void savebutton() {
            ti.setUnique_Key(generateUniqueId(this_id));
                ti.setdateCreated(getTime());
                ti.setAnswer(tvforanswer.getText().toString());
                ti.setQuestionname(tvforquestion.getText().toString()) ;
                ti.setDifficulty_level(diff_value);
                ti.setPriority_level(priority_value);
                ti.setGroupname(groupName);
                ti.setTitle_name(titleName);
                ti.setExpiry_date(expiryDate);
                ti.setGiven_or_not(givenOrNot);
                ti.setNumber_of_times_visited(numOfTimeShown);




                ti.upDateRow(this_id);

                Toast.makeText(getActivity(),getResources().getString(R.string.textUpdated),Toast.LENGTH_SHORT).show();
                SharedPreferenceHelper.setData(HandleFirebase.SELFCHANGED, "changed");
                diff_level_view.setText(String.valueOf(diff_value));
                priority_level_view.setText(String.valueOf(priority_value));






                tvforanswer.setFocusableInTouchMode(false);
                tvforanswer.setEnabled(true);
                tvforanswer.clearFocus();
                tvforquestion.setFocusableInTouchMode(false);

                tvforquestion.setEnabled(true);
                tvforanswer.clearFocus();
                textLayout.setVisibility(View.VISIBLE);
                radio4priority.setVisibility(View.GONE);
                //EditText getfocus=(EditText)getView().findViewById(R.id.getfocus);
               // getfocus.requestFocus();
               // setDefaultPriorDiff();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(tvforanswer.getWindowToken(), 0);



            }

            public String generateUniqueId(String cValue){





                long localMillis = new DateTime().toLocalDateTime()
                        .toDateTime(DateTimeZone.UTC).getMillis();


                //String ans=localMillis + " " + cValue.substring(cValue.indexOf(" "));

       return StringUtility.seperateString(cValue," ",String.valueOf(localMillis));
            }



            public String getTime()
            {
                LocalDateTime ldt=new LocalDateTime(DateTimeZone.UTC);


                return ldt.toString();
            }

            @Override
            public void senddiffValue(int diff)
            {
                diff_value=diff;
                //System.out.println("**************************************changed dif to"+ diff_value);

            }

            @Override
            public void sendpriorityValue(int prior)
            {

                priority_value=prior;
                //System.out.println("**************************************changed prior to " +priority_value);
            }

            @Override
            public void setmove() {
                System.out.println("printing move...........................");
                FragmentManager fm=getFragmentManager();
                List_Utility nu=new List_Utility();
                Bundle b=new Bundle();
                b.putString("key", this_id);
                nu.setArguments(b);
                nu.show(fm,"tag");




            }
        });




    }

    public void setDefaultPriorDiff(){
      int diff=Integer.parseInt(diff_level_view.getText().toString());
       int prior=Integer.parseInt(priority_level_view.getText().toString());
        System.out.println("....................................setDefaultPriorDiff()....................................." + diff + " " + prior) ;


        if(diff==1)
        {
            System.out.println("...................................diff_level.....................................");


            RadioButton rd=(RadioButton) getView().findViewById(R.id.diff_low);
            rd.setChecked(true);
        }
        else if(diff==2)
        {RadioButton rd=(RadioButton) getView().findViewById(R.id.diff_med);
            rd.setChecked(true);}

        else if(diff==3) {
            RadioButton rd=(RadioButton) getView().findViewById(R.id.diff_high);
            rd.setChecked(true);
            System.out.println("...................................diff_high.....................................");

        }
        else {

            RadioButton rd=(RadioButton) getView().findViewById(R.id.diff_zero);

            rd.setChecked(true);

        }



        if(prior==1)
        {
            RadioButton rd=(RadioButton) getView().findViewById(R.id.low_p);
            rd.setChecked(true);
        }
        else if(prior==2)
        {RadioButton rd=(RadioButton) getView().findViewById(R.id.med_p);
            rd.setChecked(true); }
        else if(prior==3)
        {RadioButton rd=(RadioButton) getView().findViewById(R.id.high_p);
            rd.setChecked(true);
            System.out.println("...................................high_p....................................");


        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null)
        return inflater.inflate(R.layout.wholecontent2, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //ls=(ListView)getView().findViewById(R.id.titlecontentlistView);





         tvforquestion=(EditText)getView().findViewById(R.id.show_question_whole);
        tvforanswer=(EditText)getView().findViewById(R.id.show_ans_whole);
        diff_level_view=(EditText)getView().findViewById(R.id.diff_level);
        priority_level_view=(EditText)getView().findViewById(R.id.priority_level);
        radio4priority=(LinearLayout)getView().findViewById(R.id.radio4diff);
        textLayout=(LinearLayout)getView().findViewById(R.id.textLayout);
        diffLabel=(TextView)getView().findViewById(R.id.difflabel);
        priorlabel=(TextView)getView().findViewById(R.id.priorLabel);






        new titleContentLoader().execute();

    }




    public  void editContent(){

        tvforanswer.setFocusableInTouchMode(true);
        tvforanswer.setEnabled(true);
        tvforquestion.setFocusableInTouchMode(true);
        tvforquestion.setEnabled(true);
        textLayout.setVisibility(View.GONE);
        radio4priority.setVisibility(View.VISIBLE);
        setDefaultPriorDiff();
    }



    class titleContentLoader extends AsyncTask
    {


        @Override
        protected Object doInBackground(Object[] objects)
        {



            //return ti.collectTable(ContentTab.sp.getString("titlecontent",""));
            //return ti.collectRow(ContentTab.sp.getString("titlecontent",""));

            return ti.collectRow(SharedPreferenceHelper.getString(ContentTab.TITLECONTENTSELECTIONKEY));


        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
                Cursor cursor=(Cursor)o;
                if(cursor.moveToFirst())

                {

                   Message m= TitleContentActivity.h.obtainMessage();
                    Bundle b=new Bundle();
                    b.putString(TitleContentActivity.ACTION_BAR_KEY, cursor.getString(cursor.getColumnIndex(TitleInfo.title_name_col)));
                    m.setData(b);
                    m.sendToTarget();
                    priority_value=Integer.parseInt(cursor.getString(cursor.getColumnIndex(TitleInfo.prior_col)));
                    diff_value=Integer.parseInt(cursor.getString(cursor.getColumnIndex(TitleInfo.diff_col)));

                    tvforquestion.setText(cursor.getString(cursor.getColumnIndex(TitleInfo.question_col)));
                    tvforanswer.setText(cursor.getString(cursor.getColumnIndex(TitleInfo.answer_col)));
                    priority_level_view.setText(cursor.getString(cursor.getColumnIndex(TitleInfo.prior_col)));
                    diff_level_view.setText(cursor.getString(cursor.getColumnIndex(TitleInfo.diff_col)));
                    this_id=cursor.getString(cursor.getColumnIndex(TitleInfo.uniquekey_col));
                    expiryDate=cursor.getString(cursor.getColumnIndex(TitleInfo.expiry_time_col));
                    groupName=cursor.getString(cursor.getColumnIndex(TitleInfo.groupname_col));
                    titleName=cursor.getString(cursor.getColumnIndex(TitleInfo.title_name_col));
                    givenOrNot=cursor.getString(cursor.getColumnIndex(TitleInfo.given_col));
                    numOfTimeShown=cursor.getInt(cursor.getColumnIndex(TitleInfo.number_visited_col));


                    System.out.println("........diff......" + cursor.getString(cursor.getColumnIndex(TitleInfo.diff_col)));
                    System.out.println("........priority......" + cursor.getString(cursor.getColumnIndex(TitleInfo.prior_col)));
                    System.out.println("........number of time......"+cursor.getInt(cursor.getColumnIndex(TitleInfo.number_visited_col)));
                    System.out.println("........expirydate......"+cursor.getString(cursor.getColumnIndex(TitleInfo.expiry_time_col)));
                    System.out.println("........given......"+cursor.getString(cursor.getColumnIndex(TitleInfo.given_col)));
                    System.out.println("........uniquerow......"+cursor.getString(cursor.getColumnIndex(TitleInfo.uniquekey_col)));
                    System.out.println("........this id......"+this_id);


                }
            System.out.println("....i am closing...");

            ti.close();
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }


    }

    public interface Toolbarinterface
    {

         void upDateedit();
         void upDatedelete();
         void savebutton();
         void senddiffValue(int diffvalue);
         void sendpriorityValue(int priorvalue);
         void setmove();
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferenceHelper.setData(HandleFirebase.SELFCHANGED, null);
    }




}
