package backend;

/**
 * Class sole purpose is a representation of the title object
 */
public class Title {
    public Title() {
    }

    public String title_name,answer,group_name,question_name,unique_Key,timeCreated,date_col;
    public int difficulty_level,priority_level,number_of_times_visited;
    public Date_Format dateFormat;



    public String getTimeCreated() {
        return timeCreated;
    }

    public String getUnique_Key() {

        return unique_Key;
    }

    public void setUnique_Key(String unique_Key) {
        this.unique_Key = unique_Key;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
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
    public Date_Format getDateFormat() {

        return dateFormat;
    }

    public void setDateFormat(Date_Format dateFormat) {

        this.dateFormat = dateFormat;
    }

    public String getGroupname() {
        return group_name;
    }

    public void setGroupname(String groupname) {
        this.group_name = groupname;
    }

    public String getDate_col() {
        return date_col;
    }

    public void setDate_col(String date_col) {
        this.date_col = date_col;
    }

    public String getQuestionname() {
        return question_name;
    }

    public void setQuestionname(String questionname) {
        this.question_name = questionname;
    }

}
