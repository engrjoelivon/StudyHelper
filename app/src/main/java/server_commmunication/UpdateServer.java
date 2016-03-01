package server_commmunication;

/**
 * Created by joel on 11/11/15.
 * UpdaterServer class is created to work with firebase class,when its object is created
 */

import android.app.Activity;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import backend.TitleInfo;

public class UpdateServer implements Map<String,Object>
{

    String group,title,priority,difficulty,question,answer,uniquekey,timeCreated,num_of_time_give,given;


    public UpdateServer(String group,String title,String priority,
                        String difficulty,String question,String answer,String unique_key,String timeCreated,String num_of_time)

    {


        this.group=group;
        this.title=title;
        this.priority=priority;
        this.difficulty=difficulty;
        this.question=question;
        this.answer=answer;
        this.uniquekey=unique_key;
        this.timeCreated=timeCreated;
        this.num_of_time_give=num_of_time;







    }

    public UpdateServer()
    {
    }

    public Map<String,Object>  setGroup(){


        return  myMap(group);
    }
    public Map<String,Object>  setTitle(){


        return  myMap(title);
    }
    public Map<String,Object>  setquestion(){


        return  myMap(question);
    }
    public Map<String,Object>  setAnswer(){


        return  myMap(answer);
    }
    public Map<String,Object>  setpriority(){


        return  myMap(priority);
    }
    public Map<String,Object>  setDifficulty(){


        return  myMap(difficulty);
    }
    public Map<String,Object>  setUniqueKey(){


        return  myMap(uniquekey);
    }
    public Map<String,Object>  setNumberOfTimeGiven(){


        return  myMap(num_of_time_give);
    }
    public Map<String,Object>  setGiven(){


        return  myMap(given);
    }

    public Map<String,Object> myMap(String value){
        Map<String,Object> thismap=new HashMap<>();
        thismap.put(uniquekey,value);


        return thismap;
    }

    public String getGiven() {
        return given;
    }

    public void setGiven(String given) {
        this.given = given;
    }

    public String getNum_of_time_give() {
        return num_of_time_give;
    }

    public void setNum_of_time_give(String num_of_time_give) {
        this.num_of_time_give = num_of_time_give;
    }

    public  Map<String,Object>[] updateValue(String uniquekey,String diff,String priority,String question,String answer)
    {

        Map<String,Object> uniquekeymap=new HashMap<>();
        uniquekeymap.put(uniquekey,uniquekey);

        Map<String,Object> diffmap=new HashMap<>();
        diffmap.put(uniquekey,diff);

        Map<String,Object> priormap=new HashMap<>();
        priormap.put(uniquekey,priority);

        Map<String,Object> questionmap=new HashMap<>();
        questionmap.put(uniquekey,null);

        Map<String,Object> answermap=new HashMap<>();
        answermap.put(uniquekey,null);



        Map<String,Object> []mapArray=new HashMap[5];
        mapArray[0]=uniquekeymap;
        mapArray[1]=diffmap;
        mapArray[2]=priormap;
        mapArray[3]=questionmap;
        mapArray[4]=answermap;


        return mapArray;
    }



    @Override
    public void clear() {

    }

    @Override
    public boolean containsKey(Object o) {
        return false;
    }

    @Override
    public boolean containsValue(Object o) {
        return false;
    }

    @NonNull
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public Object get(Object o) {
        return null;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @NonNull
    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Object put(String s, Object o) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ?> map) {

    }

    @Override
    public Object remove(Object o) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @NonNull
    @Override
    public Collection<Object> values() {
        return null;
    }
}
