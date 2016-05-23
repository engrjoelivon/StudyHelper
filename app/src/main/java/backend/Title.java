package backend;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import aivco.com.studyhelper.MainActivity;
import aivco.com.studyhelper.SharedPreferenceHelper;

/**
 * Class sole purpose is a representation of the title object
 */
public class Title implements DbColumns {
    public Title() {
    }

    public String title_name;
    public String answer;
    public String group_name;
    public String question_name;
    public String unique_Key;
    public String dateCreated;
    public String expiry_date;
    public String given_or_not;
    public String InsertedrecordKey;
    public String deletedRecordKey;
    public String upDatedRecordKey;
    public int difficulty_level,priority_level,number_of_times_visited;

    public String getGiven_or_not() {
        return given_or_not;
    }

    public void setGiven_or_not(String given_or_not) {
        this.given_or_not = given_or_not;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }



    public String getUnique_Key() {

        return unique_Key;
    }

    public void setUnique_Key(String unique_Key) {
        this.unique_Key = unique_Key;
    }



    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }



    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getDifficulty_level() {
        return difficulty_level;
    }

    public void setDifficulty_level(int difficulty_level) {
        this.difficulty_level = difficulty_level;
    }

    public int getPriority_level() {

        return priority_level;
    }

    public void setPriority_level(int priority_level) {

        this.priority_level = priority_level;
    }

    public int getNumber_of_times_visited() {

        return number_of_times_visited;
    }

    public void setNumber_of_times_visited(int number_of_times_visited) {
        this.number_of_times_visited = number_of_times_visited;
    }


    public String getGroupname() {
        return group_name;
    }

    public void setGroupname(String groupname) {
        this.group_name = groupname;
    }

    public String getdateCreated() {
        return dateCreated;
    }

    public void setdateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getQuestionname() {
        return question_name;
    }

    public void setQuestionname(String questionname) {
        this.question_name = questionname;
    }



    @Override
    public String getObjectAsJsonArrayString() {

        List<String> thisObject=new ArrayList<>();
        thisObject.add(SharedPreferenceHelper.getString(MainActivity.MYEMAIL));
        thisObject.add(this.getUnique_Key());
        thisObject.add(SharedPreferenceHelper.getString(MainActivity.DEVICENAME));
        thisObject.add(this.getGroupname());
        thisObject.add(this.getTitle_name());
        thisObject.add(this.getQuestionname());
        thisObject.add(this.getAnswer());
        thisObject.add(this.difficulty_level+"");
        thisObject.add(this.getPriority_level()+"");
        thisObject.add(this.dateCreated);
        thisObject.add(this.expiry_date);
        thisObject.add(this.getNumber_of_times_visited()+"");
        thisObject.add(this.given_or_not);
        thisObject.add("");
        JSONArray jArray = new JSONArray(thisObject);





        return jArray.toString();
    }

    @Override
    public String acceptRecordsFromServer(JsonArray recordArray) {

        System.out.println("....................inserted records does exist....");
        return null;
    }

    @Override
    public List<String> deletedRecordsKeys(JsonArray deleteArray) {
        return null;
    }

    @Override
    public List<String> acceptupdatedRecordsFromServer(JsonArray recordArray) {
        return null;
    }

    @Override
    public boolean isApplicationNew() {
        return false;
    }
}
