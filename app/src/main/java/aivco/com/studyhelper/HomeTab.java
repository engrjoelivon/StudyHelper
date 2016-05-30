package aivco.com.studyhelper;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import android.os.Handler;


import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import backend.DbService;
import backend.Title;
import backend.TitleInfo;


public class HomeTab extends AppCompatActivity implements View.OnClickListener {

    public static final int QAISREADY =10; //code for question and answer is ready
    private static final int QAREADY =11 ;

    public String tag="studyhelper.HomeTab3";
    TextView myText,answer,startLabel,label;
    static EditText  myanswer;
    TextSwitcher textSwitcher;
    boolean showanswer=false;/////will determine which text should be displayed by the textswitcher widjet
    ProgressDialog pg;
    TitleInfo titleInfo;
    public static Handler handleQuesandans;
    int mytopicsize=-1;//variable to determine the size of the list,so as to increment
    public final int QUERYCOMPLETED=1;//usee inside the handler,to respond when the query is from the string
    public int retrieveQuery;
    int quesandAnsCount=0;
    int priorityvalue;//variable that holds the priority level
    public List<Title>  list4QA;
    private ImageButton backButton,imageSwitcher,icMenuEdit,updateAnswer;
    private int count=0;//variable that handles values inserted into handleBack array
    private boolean firstime=false;
    private static String uniqueid;
    private int qaListCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tab);
        Log.d(tag,"oncreate");
        pg=new ProgressDialog(this);
        titleInfo=new TitleInfo(this);
        answer=(TextView)findViewById(R.id.answer);
        label=(TextView)findViewById(R.id.label);
        startLabel=(TextView)findViewById(R.id.startLabel);
        imageSwitcher=(ImageButton)findViewById(R.id.imageSwitcher);
        imageSwitcher.setOnClickListener(this);
        updateAnswer=(ImageButton)findViewById(R.id.update);
        updateAnswer.setOnClickListener(this);
        backButton=(ImageButton)findViewById(R.id.backbutton);
        backButton.setOnClickListener(this);
        icMenuEdit=(ImageButton)findViewById(R.id.edit);
        icMenuEdit.setOnClickListener(this);
        textSwitcher=(TextSwitcher)findViewById(R.id.textSwitcher);
        //myanswer=(EditText)findViewById(R.id.answer);

        priorityvalue=3;

        createTextSwitcher();
        answer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        //titleInfo.returnToDefault();
        //list4QA=titleInfo.generateQuestionAndAnswer(true);
        if(SharedPreferenceHelper.getString(   MainActivity.MYEMAIL) != null)
        new GetQA().execute();
        disableButtons();
        handleQuesandans=new Handler(new Handler.Callback()
        {
            @Override
            public boolean handleMessage(Message message) {
                switch(message.what)
                {
                    case QAREADY:{
                        Log.d(tag, "question and answer is very ready");
                        //list4QA=TitleInfo.listQA;
                        displayText(getResources().getString(R.string.new_application));
                        if(!(list4QA.size()==0))
                        enableButtons();

                        break;
                    }



                }
                return false;
            }

            });

    }

    @Override
    protected void onResume() {
        super.onResume();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(new View(this).getWindowToken(), 0);
    }

    public void disableButtons()
    {
        answer.setVisibility(View.INVISIBLE);
        textSwitcher.setVisibility(View.INVISIBLE);
        backButton.setEnabled(false);
        imageSwitcher.setEnabled(false);



    }
    public void enableButtons(){
        textSwitcher.setVisibility(View.VISIBLE);
        answer.setVisibility(View.VISIBLE);
        imageSwitcher.setEnabled(true);
        startLabel.setVisibility(View.GONE);




    }


    @Override
    public void onClick(View view) {
        answer.setFocusableInTouchMode(false);
        answer.clearFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        switch(view.getId()){


            case R.id.imageSwitcher:{
                if(list4QA .isEmpty())
                {
                    Log.d(tag, "its isEmpty");
                    titleInfo.returnToDefault();
                    //list4QA=titleInfo.generateQuestionAndAnswer(true);
                    new GetQA().execute();

                    displayText(getResources().getString(R.string.new_application));
                    textSwitcher.setText("");


                }
                else
                {
                    Log.d(tag,"it is not Empty");
                    if(qaListCount < list4QA.size())
                    { Log.d(tag,"qaListCount is less than total size");
                        backButton.setEnabled(true);
                        if (showanswer)
                        {
                            setAnswer(getAnswer());
                            qaListCount++;
                        } else
                        {
                            setQuestion(getQuestions());
                        }
                    }
                    else
                    {
                        Log.d(tag, "finised item in array need to reload");
                        //disableButtons();
                        titleInfo.returnToDefault();
                        qaListCount=0;

                        displayText(getResources().getString(R.string.question_ans));
                        //list4QA=titleInfo.generateQuestionAndAnswer(true);
                        new GetQA().execute();

                    }
                }

                break;
            }

            case R.id.backbutton:
            {
                System.out.println("..................backicon......................................................");
                //getBack();
                if(qaListCount > 0)
                {
                    qaListCount--;

                if (showanswer)
                {

                    System.out.println("..................its show answer for backbutton......................................................");
                    setAnswer(getAnswer());



                }
                else{
                    System.out.println("..................its show question for backbutton......................................................");
                    setQuestion(getQuestions());
                }

            }break;
            }
            case R.id.edit:
            {
                startSecActivity(uniqueid);
                break;
            }
            case R.id.update:
            {
                android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();
                updateFragment updateFragment=new updateFragment();
                updateFragment.show(fragmentManager,"tag");

            }
        }

    }

    public void createTextSwitcher(){
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {

                // TODO Auto-generated method stub
                // create new textView and set the properties like clolr, size etc
                myText = new TextView(HomeTab.this);
                myText.setGravity(Gravity.TOP | Gravity.LEFT);
                myText.setTextColor(getResources().getColor(R.color.homebackground));
                myText.setBackground(getResources().getDrawable(android.R.color.transparent));
                return myText;
            }
        });



        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);

        textSwitcher .setInAnimation(in);
        textSwitcher .setOutAnimation(out);


    }

    ////////////////performs a query on the db,using the priority levels passed in,or obtains the question from the array list
    private String getQuestions()
    {
        Title thisTitle=list4QA.get(qaListCount);
        return thisTitle.getQuestionname();
    }

    private void setQuestion(String question)
    {
        showanswer=true;
        System.out.println("..................setting question.......................");
        textSwitcher.setText(question);
        answer.setText("");
        label.setText(getResources().getString(R.string.labelQuestion));
        ((ScrollView)findViewById(R.id.myscroll)).scrollTo(0, 0);
        updateGiven();

    }

    private String getAnswer()
    {
        System.out.println("..................get answer.......................");
        Title thisTitle=list4QA.get(qaListCount);
        String ans=  thisTitle.getAnswer();
        uniqueid=thisTitle.getUnique_Key();
        System.out.println("..................uniqueid id......................."+ uniqueid);
        return ans;
    }


    private void setAnswer(String answer)
    {

        showanswer=false;
        textSwitcher.setText(answer);
        System.out.println("..................settings answer.......................");
        label.setText(getResources().getString(R.string.labelAnswer));
        ((ScrollView)findViewById(R.id.myscroll)).scrollTo(0, 0);
        firstime=true;
    }



    /////after setting the data,i called updategiven to set the number to false////until returned back to true,it will not be pulled next session
    /////except after the whole data has completed

    public void updateGiven(){
        Log.d(tag, "updateGiven()");
        Title qaTitle=list4QA.get(qaListCount);
        int nGiven=qaTitle.getNumber_of_times_visited()+1;
       // titleInfo.checkDiff(nGiven,qaTitle.getDifficulty_level(),qaTitle.getUnique_Key());
        titleInfo.updateGiven(qaTitle.getUnique_Key(),futuredateTime(qaTitle.getDifficulty_level()),nGiven,qaTitle.getDifficulty_level());
    }

    public  String futuredateTime(int  diff)
    {DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYY-MM-DD-HH-mm-ss");

        System.out.println("..................settings answer......................." + diff);

        LocalDateTime ldt=null;

        if(diff==3)
        {
            System.out.println("..................(diff)==3.......................");
            ldt=LocalDateTime.now(DateTimeZone.UTC);

            return ldt.plusHours(SharedPreferenceHelper.getInt(MainActivity.KEYFORDIFFICULTYHIGH)).toString();
        }
        else if(diff==2)
        {
            System.out.println("..................(diff)==2......................");

            ldt=new LocalDateTime(DateTimeZone.UTC);
            return ldt.plusHours(SharedPreferenceHelper.getInt(MainActivity.KEYFORDIFFICULTYMED)).toString();}

        else if(diff==1)
        {
            System.out.println("..................(diff)==1......................");

            ldt=new LocalDateTime(DateTimeZone.UTC);
            return ldt.plusHours(SharedPreferenceHelper.getInt(MainActivity.KEYFORDIFFICULTYLOW)).toString();}

        else if(diff==0)
        {
            System.out.println("..................(diff)==1......................");

            ldt=new LocalDateTime(DateTimeZone.UTC);
            return ldt.plusDays(SharedPreferenceHelper.getInt(MainActivity.KEYFORDIFFICULTYZERO)).toString();}


        return null;
    }


    public void displayText(String s)
    {
        startLabel.setVisibility(View.VISIBLE);
        startLabel.setText(s);

    }


    @Override
    protected void onStop()
    {
        super.onStop();
        mytopicsize=-1;
    }












    public void startSecActivity(String key)
    {


        if(key != null){
            SharedPreferenceHelper.setData(ContentTab.TITLECONTENTSELECTIONKEY,key);
            Intent intent=new Intent();
            intent.putExtra(TitleContentActivity.UNIQUE_KEY,key);
            intent.setClass(this, TitleContentActivity.class);
            startActivity(new Intent(this, TitleContentActivity.class));

        }

    }

    public void createDialog(String status,Context context)
    {

        pg=new ProgressDialog(context);
        pg.setMessage(status);
        pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pg.setCanceledOnTouchOutside(false);
        pg.show();

    }


    public static class updateFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())// Set Dialog Icon
                    .setIcon(android.R.drawable.dialog_frame)
                    .setTitle("choose an option")

                    .setMessage("Are you sure you want to Update,answer will be changed permanently")
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if(uniqueid != null)
                                new TitleInfo(getContext()).upDate(uniqueid,myanswer.getText().toString());

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,   int which) {
                            // Do something else
                        }
                    }).create();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);

        }


    }

    public class GetQA extends AsyncTask{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            createDialog("loading content.",HomeTab.this);


        }

        @Override
        protected Object doInBackground(Object[] params) {
            List<Title> res=titleInfo.generateQuestionAndAnswer();
            LocalTime furturetime= LocalTime.now().plusSeconds(1);
            while (LocalTime.now().isBefore(furturetime));

            return res;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            pg.cancel();
            list4QA=(List<Title>)o;
            handleQuesandans.sendEmptyMessage(QAREADY);
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);


        }
    }


}
