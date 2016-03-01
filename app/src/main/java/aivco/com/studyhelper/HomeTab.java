package aivco.com.studyhelper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsoluteLayout;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import android.os.Handler;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;

import backend.TitleInfo;


public class HomeTab extends AppCompatActivity implements View.OnClickListener {
public String Tag="HomeTab";
    TextView myText,answer,startLabel;
    ImageSwitcher imageSwitcher;
    TextSwitcher textSwitcher;
    boolean showanswer=false;/////will determine which text should be displayed by the textswitcher widjet
    ProgressDialog pg;
    TitleInfo titleInfo;
    ArrayList<Topics> myTopics=null;///represent the list of questions and answer returned from the cursor query
    Handler handleQuesandans;
    int mytopicsize=-1;//variable to determine the size of the list,so as to increment
    public final int QUERYCOMPLETED=1;//usee inside the handler,to respond when the query is from the string
    public int retrieveQuery;
    int quesandAnsCount=0;
    int priorityvalue;//variable that holds the priority level


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tab);
        pg=new ProgressDialog(this);
        titleInfo=new TitleInfo(this);
        answer=(TextView)findViewById(R.id.answer);
        startLabel=(TextView)findViewById(R.id.startLabel);
        imageSwitcher=(ImageSwitcher)findViewById(R.id.imageSwitcher);
        textSwitcher=(TextSwitcher)findViewById(R.id.textSwitcher);
        priorityvalue=3;

       createTextSwitcher();
        createImageSwitcher();

       disableButtons();
        handleQuesandans=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {

              switch(message.what)
              {
                  case QUERYCOMPLETED:
                  {
                      System.out.println("..................completed query.......................");

   ////////////////////will be true only if the query returned values/////////////////////
                      if(myTopics.size() >0)
                      {
                          System.out.println("..................my topic is no null.......................");

                          mytopicsize=myTopics.size();
                          priorityvalue--;//reduces priority level so that next time a query will be made on the database it will return different values
                      }
                      ////////////////will be executed when the query returned no value////////////////////////
                      // but because the application still needs to perform another query for priority levels 2 and 1////////



                      else if(priorityvalue > 1)
                           {
                               System.out.println("..................priority value is not 1.......................");


                               GetTopics getTopics=new GetTopics();
                               getTopics.execute(String.valueOf(--priorityvalue));
                               break;
                           }
                      /////////////will be executed when all the queries are completed and it returns no value//////////////
                      ////////returns program to default values///////////
                           else
                          {
                              System.out.println("..................does not contain any values at all needs to return values to default.......................");
                               displayText(getResources().getString(R.string.question_ans));

                          titleInfo.returnToDefault();
                          priorityvalue=3;
                          break;

                          }
                      if(!showanswer)
                      {
                          System.out.println("..................so answer is false so show question.......................");

                          quesandAnsCount=--mytopicsize;
                          System.out.println("..................quesandanscount is......................."+ quesandAnsCount);


                                  setQuestion(myTopics.get(quesandAnsCount).question);


                      }
                      else
                      {

                          setAnswer(myTopics.get(quesandAnsCount).getAnswer());
                      }
                      break;
                  }

              }
                return false;
            }
        });

    }


   public void disableButtons(){
 answer.setVisibility(View.INVISIBLE);
       textSwitcher.setVisibility(View.INVISIBLE);


   }
    public void enableButtons(){
        textSwitcher.setVisibility(View.VISIBLE);
        answer.setVisibility(View.VISIBLE);
        startLabel.setVisibility(View.INVISIBLE);



    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.imageSwitcher:{
                if(SharedPreferenceHelper.getString(MainActivity.DATASTORED_KEY)!=null)
                {



                enableButtons();
                if(showanswer)
                {   System.out.println("..................show answer.......................");
                    setAnswer(getAnswer());
                }
                else
                {
                    System.out.println("..................show question.......................");

                        getQuestions("3");

                }

                break;
            }
                else
                {
                    displayText(getResources().getString(R.string.new_application));

                }
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



        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);

        textSwitcher .setInAnimation(in);
        textSwitcher .setOutAnimation(out);


    }

    public void createImageSwitcher(){





        // Set the ViewFactory of the ImageSwitcher that will create ImageView object when asked
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                // TODO Auto-generated method stub

                // Create a new ImageView set it's properties
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
                return imageView;
            }
        });

        // Declare the animations and initialize them
        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);

        // set the animation type to imageSwitcher
        imageSwitcher.setInAnimation(in);
        imageSwitcher.setOutAnimation(out);


        imageSwitcher.setImageResource(R.drawable.starticon);
        imageSwitcher.setOnClickListener(this);

    }

    ////////////////performs a query on the db,using the priority levels passed in,or obtains the question from the array list
    private void getQuestions(String priority)
    {    //When application starts mytopicsize is less than 0,so a query will be made on the db.
        ///mytopicsize will then represent the size of the cursor returned from the query,everytime a question is obtained from the arraylist i.e the cursor
        //mytopicsize reduces,until it becomes less than 0,at this point a new query will be made on the db.
        if(mytopicsize < 0)
        {
            System.out.println("..................get question from db......................");
            ///from start of application priority level will be set at 3///after every successful query priority level reduces by one,this is set inside the handler
            if(priorityvalue <1)
            {
                //when priority level becomes less than 1//that means all questions have been obtained and need to restart
                priorityvalue=3;
                titleInfo.returnToDefault();
                displayText(getResources().getString(R.string.question_ans));
                return;
            }
            GetTopics getTopics=new GetTopics();
        getTopics.execute(String.valueOf(priorityvalue));}
        else
        {
            System.out.println("..................get question from array.........size of array is.............."+quesandAnsCount);
            setQuestion(myTopics.get(quesandAnsCount).getQuestion());

        }

    }


    @Override
    protected void onStop()
    {
        super.onStop();
        mytopicsize=-1;
    }

    private void setQuestion(String question)
    {
        showanswer=true;
        System.out.println("..................setting question.......................");
        textSwitcher.setText(question);

      updateGiven();

    }

    private String getAnswer()
    {
        System.out.println("..................get answer.......................");

        String ans=  myTopics.get(quesandAnsCount).getAnswer();
        quesandAnsCount=--mytopicsize;
        System.out.println("..................i set count to......................." + quesandAnsCount);


        return ans;
    }


    private void setAnswer(String answer)
    {

        showanswer=false;
        textSwitcher.setText(answer);
        System.out.println("..................settings answer.......................");

    }

    /////after setting the data,i called updategiven to set the number to false////until returned back to true,it will not be pulled next session
    /////except after the whole data has completed

    public void updateGiven(){

        titleInfo.updateGiven(myTopics.get(quesandAnsCount).getId(),futuredateTime(myTopics.get(quesandAnsCount).getDiff_level()),myTopics.get(quesandAnsCount).getNumoftimegiven()+1);



    }

    public  String futuredateTime(String  diff)
    {
        System.out.println("..................settings answer......................."+diff);

        LocalDateTime ldt=null;
        if(Integer.parseInt(diff)==3)
        {
            System.out.println("..................(diff)==3.......................");
            ldt=new LocalDateTime();
            return ldt.plusHours(24).toString();
        }
        else if(Integer.parseInt(diff)==2)
        {
            System.out.println("..................(diff)==2......................");

        ldt=new LocalDateTime();
        return ldt.plusHours(48).toString();}

        else if(Integer.parseInt(diff)==1)
        {
            System.out.println("..................(diff)==1......................");

            ldt=new LocalDateTime();
            return ldt.plusHours(72).toString();}


        return null;
    }


    public void displayText(String s)
    {
        startLabel.setVisibility(View.VISIBLE);
        startLabel.setText(s);

    }


    public class GetTopics extends AsyncTask<String,String,ArrayList<Topics>>{

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            System.out.println("..................GetTopics.......................");
            showDialog();

        }

        @Override
        protected ArrayList<Topics> doInBackground(String... strings)
        {
            String question=null;
            ArrayList<Topics> mytopic=new ArrayList<>();
           Cursor cursor= titleInfo.getTopics(strings[0]);
            System.out.println("..........................cursor has a size of........................" + cursor.getCount());

            while(cursor.moveToNext()){
                mytopic.add(new Topics(cursor.getInt(cursor.getColumnIndex(TitleInfo.id_col)),
                        cursor.getString(cursor.getColumnIndex(TitleInfo.question_col)),
                        cursor.getString(cursor.getColumnIndex(TitleInfo.answer_col)),
                        cursor.getString(cursor.getColumnIndex(TitleInfo.diff_col)),
                        cursor.getInt(cursor.getColumnIndex(TitleInfo.number_visited_col))
                        ));


            }
            return mytopic;
        }

        @Override
        protected void onPostExecute(ArrayList<Topics> s)
        {
            super.onPostExecute(s);
            pg.dismiss();
            myTopics=s;
            handleQuesandans.sendEmptyMessage(1);

        }
    }




public void showDialog(){
    pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    pg.setMessage("get ready....");
    pg.setIndeterminate(true);
    pg.show();



}
    private class Topics{
        public int id;
        public String question;
        public String answer;
        public String diff_level;
        public int numoftimegiven;


        public Topics(int id,String question,String answer,String difficulty,int nOTGiven) {

            this.question=question;
            this.id=id;
            this.answer=answer;
            this.diff_level=difficulty;
            this.numoftimegiven=nOTGiven;
        }

        public String getDiff_level()
        {
            return diff_level;
        }

        public void setDiff_level(String diff_level)
        {
            this.diff_level = diff_level;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }

        public int getId() {
            return id;
        }

        public int getNumoftimegiven() {
            return numoftimegiven;
        }

        public void setNumoftimegiven(int numoftimegiven) {
            this.numoftimegiven = numoftimegiven;
        }
    }





}
