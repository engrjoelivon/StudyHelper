package aivco.com.studyhelper;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import backend.TitleInfo;
import server_commmunication.HandleFirebase;
import server_commmunication.ServerWithIon;

/*
* Implements onclick listener to handle clicks from save,text and image buttons
* implements CreateGroups and Create questions,from thier parent fragment,so as to accept the click events that they generate
* and perform the necessaray actions
*
*
* */

public class AddTab extends AppCompatActivity implements AdapterView.OnItemSelectedListener,Create_Group.Groupname
        ,View.OnClickListener,AddTab_Text_Answer_Dialog.SendAnswerText
{
    private static final int IDENTIFICATION_ACTIVITY_KEY = 1;
    private final String tag=("AddTab");
    private Create_Group cg;
    TitleInfo titleInfo,titleInfodb;
    private Button bsave;
    private ImageButton bText,bImage;
    TextView tvgrp,questgrp;
    EditText editText_4_title;
    private  Spinner spinner_Ques,spinner_Grps;
    List<String> l_ques,l_gps;
    private String spinnerQues;
    private String spinnerGps;
    RadioButton prioritybuttonlow,diff_buttonlow;
    ErrorHolder holder;
    private AddtabValidator adv;
    ArrayAdapter<String> adapterGrps, adapterSpiner;
    private SpinnerHelper spinnerHelper;
    AddTab_Text_Answer_Dialog atq;
    ImageView  myImage;
    String imagepath;
    HandleFirebase handleFirebase;
    private ConnectivityManager cm;
    private String urlInsertRecord="insertrecords/";
    SharedPreferenceHelper sharedPreferenceHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tab);
        Log.d(tag, "onCreate");
        sharedPreferenceHelper=new SharedPreferenceHelper(this);
        //handleFirebase=new HandleFirebase(this,"");///only added the empty string so it does not call the constructor meant for login activity
        cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        titleInfo=new TitleInfo(this);
        adv=new AddtabValidator();



        tvgrp=(TextView)findViewById(R.id.grouptxt);
        questgrp=(EditText)findViewById(R.id.questiontxt);
        editText_4_title=(EditText)findViewById(R.id.editText_4_title);
        bsave=(Button)findViewById(R.id.saveTitles); bsave.setOnClickListener(this);
        bText=(ImageButton)findViewById(R.id.button_text);bText.setOnClickListener(this);
        //bImage=(ImageButton)findViewById(R.id.button_image); bImage.setOnClickListener(this);
        prioritybuttonlow=(RadioButton)findViewById(R.id.low_p); prioritybuttonlow.performClick();
        diff_buttonlow=(RadioButton)findViewById(R.id.diff_low);diff_buttonlow.performClick();






        spinnerHelper= new SpinnerHelper() {
            @Override
            public void setSpinnerChange(int cases) {
                switch (cases){
                    case 1:{


                        adapterGrps=new MyCustomAdaptor(AddTab.this,R.layout.custom_layout_for_view,l_gps);

                        spinner_Grps.setAdapter(adapterGrps);
                        adapterGrps.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_Grps.setSelection(l_gps.size() - 1);
                        break;

                    }

                    case 2:
                    {
                        Log.d(tag, "case2");
                        adapterGrps=new MyCustomAdaptor(AddTab.this,R.layout.custom_layout_for_view,l_ques);
                        spinner_Ques.setAdapter(adapterSpiner);
                        spinner_Ques.setSelection(l_ques.size() - 1);
                        break;



                    }



                }



            }










        };






        registerForContextMenu(tvgrp);
        //registerForContextMenu(questgrp);

    }



    @Override
    protected void onResume() {
        super.onResume();

        ///////////instance of error holder interface its purpose is to return error to this activity as they occur in any of its
        //////subactivity.
        holder=new ErrorHolder() {
            @Override
            public void error(String error) {
                Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();

            }
        };
        ////////////////////////////populate the spinner for Groups/////////////////////////////
        spinner_Grps = (Spinner) findViewById(R.id.grpspinner);

        // l_gps=new ArrayList<String>( Arrays.asList(getResources().getStringArray(R.array.spinner_values)));
        l_gps=titleInfo.collectUniqueGn();
       // adapterGrps = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,l_gps);
        adapterGrps=new MyCustomAdaptor(this,R.layout.custom_layout_for_view,l_gps);

       // adapterGrps.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_Grps.setAdapter(adapterGrps);
        spinner_Grps.setOnItemSelectedListener(this);






//////////////////////populate the spinner for questions/////////////////////////////


        //
        spinner_Ques = (Spinner) findViewById(R.id.spinner_question);
        l_ques=new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.spinner_questions)));
        //adapterSpiner = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,l_ques);
        adapterSpiner=new MyCustomAdaptor(this,R.layout.custom_layout_for_view,l_ques);
        //adapterSpiner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Ques.setAdapter(adapterSpiner);
        spinner_Ques.setOnItemSelectedListener(this);



    }




    ////// /////////////////////////////return page views to their initial default state ////////////////////////


    public void returnPageTotDefault()
    {
        Log.d(tag, "returnPageTotDefault");
      prioritybuttonlow.performClick();
        diff_buttonlow.performClick();
        editText_4_title.setText("");
        adv.setAnswer_As_text("");
        questgrp.setText("");







    }




    public void saveInfos2db()
    {




        titleInfo.setAnswer(adv.getAnswer_As_text());
        titleInfo.setDifficulty_level(adv.getDiff_val());
        titleInfo.setPriority_level(adv.getPrior_val());
        titleInfo.setTitle_name(adv.getTitleName());
        //titleInfo.setQuestionname(adv.getQuesName());   questiontxt
        titleInfo.setQuestionname(questgrp.getText().toString());
        titleInfo.setGroupname(adv.getGroupName());
        titleInfo.setUnique_Key(generateUniqueKey());
        titleInfo.setdateCreated(getTime());
        titleInfo.setNumber_of_times_visited(0);
        titleInfo.setGiven_or_not("0");
        titleInfo.setExpiry_date("0");

        titleInfo.insertRows_db();
        saveInfosToServer();

        sharedPreferenceHelper.setData(HandleFirebase.SELFCHANGED, "changed");



    }

    public void saveInfosToServer(){
        if(checkConnection())
        {
            System.out.println("there is connection");
          //postJson();
            new ServerWithIon(this).postNewRecord(titleInfo,urlInsertRecord);


        }
        else{
            System.out.println("there is no connection");

        }

    }


    public boolean checkConnection()
    {


        NetworkInfo activeNetwork=cm.getActiveNetworkInfo();
        ///active network will represent the first network that is connected or null if no network is connected.



        return(activeNetwork != null && activeNetwork.isConnected() );
    }
    ////////returns a date and a time object using a universal zone,UTC so as to synchronise accross all devices and platforms////////
   public String getTime()
   {
       LocalDateTime ldt=new LocalDateTime(DateTimeZone.UTC);


   return ldt.toString();
   }

    public String generateUniqueKey(){


        long localMillis = new DateTime().toLocalDateTime()
                .toDateTime(DateTimeZone.UTC).getMillis();
        String ans=localMillis+" "+new Random().nextLong();
                System.out.println("..........................uniquekey for this item is............."+ans);

    return ans;
    }


    /////////////////////////responds to clicks on spinner///////////////////
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


        if(adapterView==spinner_Ques)
        {

            spinnerQues=l_ques.get(i);
            adv.setQuestionName(StringUtility.firstlettercaps(spinnerQues));
            Log.d(tag,spinnerQues);


        }
        else if(adapterView==spinner_Grps)
        {

            spinnerGps=l_gps.get(i);
            adv.setGroupName(StringUtility.firstlettercaps(spinnerGps));
            Log.d(tag,spinnerGps);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        Log.d(tag, "onCreate");

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        FragmentManager fm=getFragmentManager();
        if(v instanceof TextView)
        {

            TextView txt=(TextView)v;

        }
        if(v==tvgrp)
        {cg=new Create_Group();
            cg.show(fm, "group");}


       // getMenuInflater().inflate(R.menu.grpcontextmenu,menu);


    }

    //////////////////////////////responds when the save button is clicked for create group dialog////////////////////
    @Override
    public void name(String name) {
        Log.d(tag, name);
        adv.setGroupName(StringUtility.firstlettercaps(name));
        cg.dismiss();
    }




    ////////////////////////////////responds when the Enter button is clicked for AddTab_Text_Answer_Dialog////////////
    @Override
    public  void sendAnswer(String text) {
        adv.setAnswer_As_text(text);
        Log.d(tag, text);
        atq.dismiss();

    }

    //////////////handles radio click events////////////////
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.low_p:
                if (checked)
                    // Pirates are the best

                adv.setPrior_val(AddtabValidator.PRIOR_lOW);
                 Log.d(tag,"low_p");

                    break;
            case R.id.med_p:
                if (checked)
                    adv.setPrior_val(AddtabValidator.PRIOR_MED);

                    // Ninjas rule

                    break;
            case R.id.high_p:
                if (checked)
                    // Ninjas rule
                    adv.setPrior_val(AddtabValidator.PRIOR_HIGH);
                break;
            case R.id.diff_low:
                if (checked)
                    Log.d(tag,"diff_low");
                adv.setDiff_val(AddtabValidator.DIFF_LOW);
                    break;
            case R.id.diff_med:
                if (checked)
                    adv.setDiff_val(AddtabValidator.DIFF_MED);
                    Log.d(tag,"diff_med");

                    break;
            case R.id.diff_high:
                if (checked)
                    // Ninjas rule
                    adv.setDiff_val(AddtabValidator.DIFF_HIGH);
                Log.d(tag,"diff_HIGH");
                    break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        sharedPreferenceHelper.setData(HandleFirebase.SELFCHANGED, null);
        Log.d(tag, "onpause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(tag, "onStop");
    }


    ///////////////////////////////////responds to button clicks in the addtab.java class////////////////////////////////////

    @Override
    public void onClick(View view) {




       int  id=view.getId();

       if (view == bsave )
       {
           //TitleInfo titleInfo=new TitleInfo(this);
           if(editText_4_title.getText().toString().equals(""))
           {
               Toast.makeText(getApplicationContext(),"You must enter a text for Title",Toast.LENGTH_SHORT).show();
           Log.d(tag, " save pressed,info entered in title" +editText_4_title.getText().toString());
               return;
           }


           adv.setTitleName(editText_4_title.getText().toString());
           saveInfos2db();

           returnPageTotDefault();



           Log.d(tag, " end save");


       }

       ///////starts the addtabquestions dialog///////////
        else if(id == bText.getId())
       {
           FragmentManager fm=getFragmentManager();
          atq=new AddTab_Text_Answer_Dialog();
           atq.show(fm,"Answer");


           Log.d(tag, "bText");

       }
        else if(id == bImage.getId())
       {

           Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
           startActivityForResult(intent, IDENTIFICATION_ACTIVITY_KEY);
           Log.d(tag, "bImage");



       }


    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
        {
          myImage=(ImageView)findViewById(R.id.myimage);
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            imagepath= handlePicture(UtilityDistinctNumber.myNumber(),bp);

            myImage.setImageBitmap(BitmapFactory.decodeFile(imagepath));


        }


    }*/

    public String handlePicture(long reportId, Bitmap picture) {
        // Saves the new picture to the internal storage with the unique identifier of the report as
        // the name. That way, there will never be two report pictures with the same name.
        String picturePath = "";
        File internalStorage = this.getDir("ReportPictures", Context.MODE_PRIVATE);
        File reportFilePath = new File(internalStorage, reportId + ".png");
        picturePath = reportFilePath.toString();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(reportFilePath);
            picture.compress(Bitmap.CompressFormat.PNG, 100 /*quality*/, fos);  //picture //bitmap
            fos.close();
        }
        catch (Exception ex) {
            Log.i("DATABASE", "Problem updating picture", ex);
            picturePath = "";
        }

    return picturePath;
    }


    public  class AddtabValidator
    {
        private String groupName,quesName,titleName,answer_As_text;

        private byte diff_val=1;
        private byte prior_val=1;
        private static final byte DIFF_LOW=1;
        private static final byte DIFF_MED=2;
        private static final byte DIFF_HIGH=3;
        private static final byte PRIOR_HIGH=3;
        private static final byte PRIOR_MED=2;
        private static final byte PRIOR_lOW=1;

        ///////////////method to check if newly created group already exists/////////////////////
     public void checkGroup(String title)
     {


     }

        public String getTitleName()
        {
            return titleName;
        }

        public void setTitleName(String titleName)
        {
            this.titleName = titleName;
        }


        public String getGroupName()
        {
            return groupName;
        }

        public void setGroupName(String groupName)
        {
            for(String li:l_gps)
            {
                if(li.equals(groupName) || groupName.equals("") )
                {

                    this.groupName = li;

                    return;

                }

            }
            this.groupName= groupName;

            l_gps.add(groupName);
            spinnerHelper.setSpinnerChange(1);/////calls the spinner change method from the spinnerhelper class





        }



        public String getQuesName()
        {
            return quesName;
        }

        public void setQuestionName(String quesName)
        {
            for(String li:l_ques )
            {
                if(li.equals(quesName) || quesName.equals(""))///should in case the user does not enter any value for group name it will maintain the first string on the spinner
                {
                    this.quesName = li;


                    return;

                }

            }
            this.quesName= quesName;
            l_ques.add(quesName);

            spinnerHelper.setSpinnerChange(2);/////calls the spinner change method from the spinnerhelper class



        }

        public byte getDiff_val() {
            return diff_val;
        }

        public void setDiff_val(byte diff_val) {
            this.diff_val = diff_val;
        }

        public byte getPrior_val() {
            return prior_val;
        }

        public void setPrior_val(byte prior_val) {
            this.prior_val = prior_val;
        }

        public String getAnswer_As_text() {
            return answer_As_text;
        }

        public void setAnswer_As_text(String answer_As_text) {
            this.answer_As_text = answer_As_text;
        }
    }


}
