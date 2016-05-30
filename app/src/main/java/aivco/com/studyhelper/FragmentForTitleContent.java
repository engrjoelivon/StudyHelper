package aivco.com.studyhelper;




import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
public class FragmentForTitleContent extends Fragment implements View.OnClickListener {
    private ImageButton toolbarsave,toolbardelete,toolbaredit, toolbarmove;
    private Button back_button;
    TitleInfo ti;
    private EditText tvforquestion,tvforanswer,diff_level_view,priority_level_view;
    private String this_id;
    static View view;
    private LinearLayout radio4priority,textLayout;
    private TextView priorlabel,diffLabel;
    private static  int diff_value=3;
    private static int priority_value=3;
    private String titleName,groupName,expiryDate,givenOrNot;
    private int numOfTimeShown;
    private ContentTab contentTab;
    private OnFragmentInteractionListener onFragmentInteractionListener;
    public FragmentForTitleContent() {


    }

    @Override
    public void onCreate( Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ti=new TitleInfo(MainActivity.mycontext);

    }

     void setDefaultPriorDiff(){
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
            return inflater.inflate(R.layout.fragment_for_title_content, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        //ls=(ListView)getView().findViewById(R.id.titlecontentlistView);



       //////////////////////initialize toolbar views/////////////////////////
        toolbardelete =(ImageButton)getView().findViewById(R.id.toolbardelete);
        toolbaredit=(ImageButton)getView().findViewById(R.id.toolbaredit);
        toolbarsave=(ImageButton)getView().findViewById(R.id.toolbarsave);
        toolbarmove=(ImageButton)getView().findViewById(R.id.toolbarmove);
        back_button=(Button)getView().findViewById(R.id.back_button);
        toolbardelete.setOnClickListener(this);
        back_button.setOnClickListener(this);
        toolbaredit.setOnClickListener(this);
        toolbarsave.setOnClickListener(this);
        toolbarmove.setOnClickListener(this);


        ///////////////////initialize content views////////////////////////
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




    public  void editContent()
    {

        tvforanswer.setFocusableInTouchMode(true);
        tvforanswer.setEnabled(true);
        tvforquestion.setFocusableInTouchMode(true);
        tvforquestion.setEnabled(true);
        textLayout.setVisibility(View.GONE);
        radio4priority.setVisibility(View.VISIBLE);
        setDefaultPriorDiff();
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.back_button:
            {
                onFragmentInteractionListener.closeContent();
                break;
            }
            case R.id.toolbaredit:
            {
                break;
            }
            case R.id.toolbarmove:
            {
                break;
            }
            case R.id.toolbarsave:
            {
                break;
            }

        }



    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        onFragmentInteractionListener=(OnFragmentInteractionListener)activity;

    }

    class titleContentLoader extends AsyncTask
    {


        @Override
        protected Object doInBackground(Object[] objects)
        {
            return ti.collectRow(SharedPreferenceHelper.getString(ContentTab.TITLECONTENTSELECTIONKEY));

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Cursor cursor=(Cursor)o;
            if(cursor.moveToFirst())

            {


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
        protected void onProgressUpdate(Object[] values)
        {
            super.onProgressUpdate(values);
        }


    }



    @Override
    public void onPause() {
        super.onPause();
        SharedPreferenceHelper.setData(HandleFirebase.SELFCHANGED, null);
    }

    public interface OnFragmentInteractionListener
    {

        void onFragmentInteraction(String message);
        void closeContent();

    }


}
