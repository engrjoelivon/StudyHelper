package aivco.com.studyhelper;


import android.database.Cursor;

import android.os.AsyncTask;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Random;

import backend.TitleInfo;
import server_commmunication.HandleFirebase;
import server_commmunication.UpdateServer;

/**
 *Prints the final view
 */
public class TitleContentActivityFragment extends Fragment  {
    TitleInfo ti;
    EditText tvforquestion,tvforanswer,diff_level,priority_level;
    Toolbarinterface toolbarinterface;
    private String this_id;
    LinearLayout radio4priority,textLayout;
    TextView priorlabel,diffLabel;
    public  int diff_value=3;
    public int priority_value=3;

    public TitleContentActivityFragment() {


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        TitleContentActivity.toolbarInterfaceListener(toolbarinterface = new Toolbarinterface() {
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

                ti.upDateRow
                        (this_id, diff_value,
                                priority_value, tvforquestion.getText().toString(), tvforanswer.getText().toString());
                SharedPreferenceHelper.setData(HandleFirebase.SELFCHANGED,"changed");
               TitleContentActivity. handleFirebase.update(new UpdateServer().updateValue(this_id,diff_value+"",priority_value+"",tvforquestion.getText().toString(),
                        tvforanswer.getText().toString()));


            }

            @Override
            public void senddiffValue(int diff)
            {
                diff_value=diff;

            }

            @Override
            public void sendpriorityValue(int prior)
            {

                priority_value=prior;
            }
        });




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TitleContentActivity.tag, "onCreateView of TitleContentActivityFragment");
        return inflater.inflate(R.layout.whole_content, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //ls=(ListView)getView().findViewById(R.id.titlecontentlistView);





         tvforquestion=(EditText)getView().findViewById(R.id.show_question_whole);
        //tvforquestion.setEnabled(false);
        tvforanswer=(EditText)getView().findViewById(R.id.show_ans_whole);
        diff_level=(EditText)getView().findViewById(R.id.diff_level);
        priority_level=(EditText)getView().findViewById(R.id.priority_level);
        radio4priority=(LinearLayout)getView().findViewById(R.id.radio4diff);
        textLayout=(LinearLayout)getView().findViewById(R.id.textLayout);

        diffLabel=(TextView)getView().findViewById(R.id.difflabel);
        priorlabel=(TextView)getView().findViewById(R.id.priorLabel);

        ti=new TitleInfo(getActivity());



        new titleContentLoader().execute();

    }




    public  void editContent(){

tvforanswer.setFocusableInTouchMode(true);
        tvforanswer.setEnabled(true);
tvforquestion.setFocusableInTouchMode(true);
        tvforquestion.setEnabled(true);

        //priority_level.setEnabled(true);
        textLayout.setVisibility(View.GONE);
        radio4priority.setVisibility(View.VISIBLE);


        System.out.println("....................................edit content.....................................");
    }


    class titleContentLoader extends AsyncTask
    {


        @Override
        protected Object doInBackground(Object[] objects)
        {
            //perfrom a query on the data base to obtain all the names of titles contents
            //add the cursor object to adaptor

            ContentTab.sp.getString("titlecontent","");
            return ti.collectTable(ContentTab.sp.getString("titlecontent",""));



        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
                Cursor cursor=(Cursor)o;
                if(cursor.moveToFirst())

                {

                   Message m= TitleContentActivity.h.obtainMessage();
                    Bundle b=new Bundle();
                    b.putString(TitleContentActivity.ACTION_BAR_KEY, cursor.getString(cursor.getColumnIndex(ti.title_name_col)));
                    m.setData(b);
                    m.sendToTarget();
                    Log.d(TitleContentActivity.tag, "onCreateView " + cursor.getString(cursor.getColumnIndex(ti.question_col)));

                    tvforquestion.setText(cursor.getString(cursor.getColumnIndex(ti.question_col)));
                    tvforanswer.setText(cursor.getString(cursor.getColumnIndex(ti.answer_col)));
                    priority_level.setText(cursor.getString(cursor.getColumnIndex(TitleInfo.prior_col)));
                    diff_level.setText(cursor.getString(cursor.getColumnIndex(TitleInfo.diff_col)));
                    this_id=cursor.getString(cursor.getColumnIndex(TitleInfo.uniquekey_col));
                    System.out.println("........diff......" + cursor.getString(cursor.getColumnIndex(TitleInfo.diff_col)));
                    System.out.println("........priority......" + cursor.getString(cursor.getColumnIndex(TitleInfo.prior_col)));
                    System.out.println("........number of time......"+cursor.getInt(cursor.getColumnIndex(TitleInfo.number_visited_col)));
                    System.out.println("........expirydate......"+cursor.getString(cursor.getColumnIndex(TitleInfo.expiry_time_col)));
                    System.out.println("........given......"+cursor.getString(cursor.getColumnIndex(TitleInfo.given_col)));
                    System.out.println("........uniquerow......"+cursor.getString(cursor.getColumnIndex(TitleInfo.uniquekey_col)));
                    System.out.println("........this id......"+this_id);
                    System.out.println("........time created......"+cursor.getString(cursor.getColumnIndex(TitleInfo.date_col)));

                }

        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }


    }

    public interface Toolbarinterface
    {

        public void upDateedit();
        public void upDatedelete();
        public void savebutton();
        public void senddiffValue(int diffvalue);
        public void sendpriorityValue(int priorvalue);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferenceHelper.setData(HandleFirebase.SELFCHANGED, null);
    }
}
