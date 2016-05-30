package aivco.com.studyhelper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import backend.Title;
import backend.TitleInfo;


/**
 * Recieves execution order from Titlefragment loads the title from the titleinfo db
 */
public class TitleFragmentFragment extends Fragment implements AdapterView.OnItemClickListener {
    public static String log="TitleFragmentFragment";
    private ListView ls;
    private StartTitleContent startTitleContent;
    TitleInfo ti;
    List<String> list;
    DbManager dbManager;
    static View view;
    private List<Title>   titleList;
    List<Title>   finalTitleList;
    private List<Form>   myAdaptorList;
    private List<String>  itemList;
    private TitleFragment titleFragment;
    private CustomAdaptorForListView arr;
    private boolean startSearch=false;
    private HandleSearching handleSearching;//utility class to handle searching functions
    private ContentTab contentTab;

    public TitleFragmentFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HandleSearch handleSearch=new HandleSearch() {
            @Override
            public void close() {

                repopulateAdaptor(arr,new String());
                startSearch=false;
            }

            @Override
            public void searchText(String search)
            {
                Log.d(log,"handleSearch recieved text "+search);
                if(search.isEmpty())
                {
                    repopulateAdaptor(arr,SearchingUtility.isEmpty);
                }
                else{
                    startSearch=true;
                    repopulateAdaptor(arr,search);

                }

            }
        };

        //just commented
        titleFragment.setSearchInterface(handleSearch); //passes handlesearch interface to titlefragment class



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null)
        return inflater.inflate(R.layout.fragment_title_fragment, container, false);
    return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbManager=new DbManager(getActivity());
        ti=new TitleInfo(getActivity());
        new TitleLoader().execute();
        ls=(ListView)getView().findViewById(R.id.titlelistView);
        ls.setOnItemClickListener(this);

    }


    /////////////responds when an item is clicked,does not perform any function,first get the string that was clicked fron dbmanager///////////
   //////////////after which passes it to the activity that it is attached to //////////////////
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        Log.d(log,"onitemclicked");
        String ans=list.get(i);

      if(startTitleContent != null && !startSearch )
        startTitleContent.startSecActivity(ans);
        else
      {
          Log.d(log,"position clicked is"+i);

          startTitleContent.startSecActivity(handleSearching.getKey(i));

      }
       }
    class TitleLoader extends AsyncTask
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Object[] objects)
        {


         ///calls collectUnique from titleInfo class,this will return only unique rows from the table
            //return ti.collectUniqueTn(ContentTab.sp.getString(ContentTab.KEY_CHOSEN_GROUP, ""));
            return ti.collectTitles(ContentTab.sp.getString(ContentTab.KEY_CHOSEN_GROUP, ""));
        }
        //add the cursor object to adaptor
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        ///set the adaptor here
            //setList(o);
            setListFromTitle(o);


        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        titleFragment=(TitleFragment)activity;
        //contentTab=(ContentTab)activity;
        startTitleContent=(StartTitleContent)activity;
    }

    public interface StartTitleContent
    {

      void startSecActivity(String title);

    }

    public void setList(Object o)
    {
        list=(List)o;
        List<Form> adaptorlist=new ArrayList<>();




        //generate Listform to be added to custom adaptor
        for(String formElement:list)
        {
            Form form=new Form();
            form.setGroup(formElement);
            adaptorlist.add(form);


        }
        myAdaptorList=new ArrayList<>(adaptorlist);

        CustomAdaptorForListView  arr=new CustomAdaptorForListView(getActivity(),
                R.layout.background_for_group_content,adaptorlist);

        ls.setAdapter(arr);



    }
    public void setListFromTitle(Object o)
    {   itemList=new ArrayList<>();
        list=new ArrayList<>();
        titleList=(List)o;
        List<Form> adaptorlist=new ArrayList<>();
        for(Title title:titleList)
        {
            Form form=new Form();
            form.setGroup(title.getTitle_name());//even though i called setGroup but adding titlename
            list.add(title.getUnique_Key());
            itemList.add(title.getTitle_name());
            adaptorlist.add(form);

        }
          arr=new CustomAdaptorForListView(getActivity(),
                R.layout.background_for_group_content,adaptorlist);
        ls.setAdapter(arr);


    }

    private void repopulateAdaptor(CustomAdaptorForListView customAdaptorForListView,String searchText)
    {
      customAdaptorForListView.clear();
        handleSearching=new HandleSearching();

        for(Form val:handleSearching.search(searchText))
        {
            customAdaptorForListView.add(val);
            Log.d(log,"adding form to adaptor "+val.getGroup());
        }
    }
     private class HandleSearching
     {

      public List<Form> search(String searchText)
      {
          return SearchingUtility.startWithSearch(itemList,searchText);
      }

      /**
       * calls the utility class to get the array holding the positions.After the searched items have been populated if a position is
       * clicked to get the key for this position,use the position clicked number to get the value at the same position in
       * the array returned by getposition.That value will be the index for the key in the list array.
       * */
       List<Integer> getPositions()
      {
          return SearchingUtility.getPositionsOfResult();
      }


       String getKey(int index)
      {
          startSearch=true;

          return list.get(getPositions().get(index));
      }

     }

    public interface HandleSearch{

        void close();
        void searchText(String search);
    }


}
