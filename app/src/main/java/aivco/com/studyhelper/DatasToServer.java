package aivco.com.studyhelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import backend.TitleInfo;

/**
 * Pushes all the entered datas to server
 */
public class DatasToServer extends HttpConnecter{
    TitleInfo title;
    public DatasToServer(TitleInfo titleInfo) {

        this.title=titleInfo;

    }

    @Override

    public String joinKeysToValues(String html) {

        System.out.println("Extracting form's data...");
        ///1 parse html in jsoup.parse method
        Document doc= Jsoup.parse(html);
        ///2 obtain the form element from the html page returned by the get request using its id.
        Element formid=doc.getElementById("register_form");  /////project name is form_tutorials
        ///3 obtain all elements under the form id with element tag input
        Elements formtags=formid.getElementsByTag("input");//returns all the element with the tagname input under register form
        ///create a list to hold all keys and value contained in the form with the id above
        List<String> formelements=new ArrayList();
        for(Element element:formtags)
        {
            ///this loop will merge the keys to thier values.For keys where a value is coming from the user,partition an
            //condition as done below to add a value,otherwise its default value will be used.

            String key = element.attr("name");
            String value = element.attr("value");
            if(key.equals("title"))
                value=title.getTitle_name();
            else if(key.equals("groupname"))
                value=title.getGroupname();
            else if(key.equals("questions"))
                value=title.getQuestionname();
            else if(key.equals("difficulty"))
                value=title.getDifficulty_level()+"";
            else if(key.equals("priority"))
                value=title.getPriority_level()+"";
            else if(key.equals("answer_text"))
                ////////test if an answer is entered if yes it adds it to the value other no
                if(!(title.getAnswer()==null))
                    value=title.getAnswer();

            try {
                formelements.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(value+" value data is ....................................");

        }

        StringBuilder result=new StringBuilder();
        for(String formele:formelements)
        {
            if(result.length()==0)
            {
                result.append(formele);

            }
            else
            {
                result.append("&"+formele);

            }

        }

        System.out.println(result + "result data is ....................................");
        return result.toString();
    }
}
