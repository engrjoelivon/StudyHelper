package aivco.com.studyhelper;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joel
 */
public class SearchingUtility {
    static List<Integer> positions;
    public static final String isEmpty="EMPTY";
    private SearchingUtility()
    {

    }

    /**Utility method to search through a list for strings that starts with a char or text.method is case insensitive.
     * method also sets another list holding all the positions of the strings that match.To get the list call getPosition of result
     * @param listOfString  list holding the string to search through
     * @param stringToSearch string to search for within the list
     * @return A list holding the string if found
     * */
    public static List<Form> startWithSearch(List<String > listOfString ,String stringToSearch)
    {
        positions=new ArrayList<>();
      List<Form> result=new ArrayList<>() ;
        Form form=null;
        if(!stringToSearch.equals(isEmpty))
        {
            for(int count=0; count<listOfString.size();count++)
            {
                if((listOfString.get(count).toLowerCase()).startsWith(stringToSearch.toLowerCase()))
                {

                    form=new Form();
                    form.setGroup(listOfString.get(count));
                    result.add(form);
                    setPositions(count);

                }
            }

        }




        return result;

    }
    public static void setPositions(int position){
        positions.add(position);

    }

  /**getter method that returns an array list holding all the position of strings that matched.since arrays positions are guaranteed
   * the position will match the position of the array that was passed in for the same string*/
    public static List<Integer> getPositionsOfResult(){
        return positions;
    }

}
