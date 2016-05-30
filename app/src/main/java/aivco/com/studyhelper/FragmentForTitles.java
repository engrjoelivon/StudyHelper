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
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import backend.Title;
import backend.TitleInfo;


/**
 *Fragment defined to populate a listview with held in the group chosen from the group list presented by the content tab
 *The group name is stored as a sharedpreference object,with key KEY_CHOSEN_GROUP
 * The inner async class title loader performs queries the db to the a list of all the titles in the chosen group
 * The HandleSearching Inner class calls the Searching utility method to perform a search based on the entered string,in the SearchView View
 */
public class FragmentForTitles extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener {
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
    boolean showToolbar=false;//If the listview is scrolled while showtoolbar is false,which is the default state.It will not show the toolbar
    private AppBarLayout appBarLayout;
    private SearchView searchView;;


    public FragmentForTitles() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);







        //just commented
        //titleFragment.setSearchInterface(handleSearch); //passes handlesearch interface to titlefragment class



    }

    @Override
    public void onPause() {
        super.onPause();
        appBarLayout.setVisibility(View.GONE);

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null)
            return inflater.inflate(R.layout.fragment_for_titles, container, false);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState)
    {

        super.onViewCreated(view, savedInstanceState);
        searchView=(SearchView)getView().findViewById(R.id.search_view);
        dbManager=new DbManager(getActivity());


        appBarLayout=(AppBarLayout)getView().findViewById(R.id.toolbarlayout);
        final Button sbutton=(Button)getView().findViewById(R.id.search_button);
        final Button backButton=(Button)getView().findViewById(R.id.back_button);
        backButton.setOnClickListener(this);
        sbutton.setOnClickListener(this);
        ti=new TitleInfo(getActivity());
        //calls asynchronous class to get data from db//
        new TitleLoader().execute();
        //initialize list view
        ls=(ListView)getView().findViewById(R.id.titlelistView);
        ls.setOnItemClickListener(this);
        //listen when the listview scrolls,so as to hide and show the search,back and toolbar view
        ls.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {

                switch (scrollState)
                {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    {   if (showToolbar)
                        {
                            appBarLayout.setVisibility(View.VISIBLE);

                        }
                        sbutton.setVisibility(View.VISIBLE);
                        backButton.setVisibility(View.VISIBLE);

                        break;
                    }
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    {
                        appBarLayout.setVisibility(View.GONE);
                        sbutton.setVisibility(View.GONE);
                        backButton.setVisibility(View.GONE);
                        break;
                    }

                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    /**************HANDLE SEARCH ON TOOLBAR******************/

        searchView.setFocusable(false);
        searchView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchView.setFocusableInTouchMode(true);
                searchView.requestFocus();

                return false;
            }
        });


        searchView.setOnCloseListener(new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {
                /*System.out.println("..........the search bar has been closed.............");

                handleSearch.close();*/
                appBarLayout.setVisibility(View.GONE);
                repopulateAdaptor(arr,new String());
                startSearch=false;
                showToolbar=false;
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String search)
            {

               // handleSearch.searchText(newText);   //passes the text from the search bar to titlefragmentfragment handlesearch interface
                if(search.isEmpty())
                {
                    repopulateAdaptor(arr,new String());
                }
                else
                {
                    startSearch=true;
                    repopulateAdaptor(arr,search);

                }
                return false;
            }
        });


    }


    /////////////responds when an item is clicked,does not perform any function,first get the string that was clicked fron dbmanager///////////
    //////////////after which passes it to the activity that it is attached to //////////////////
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        String ans=list.get(i);

        if(startTitleContent != null && !startSearch )
            startTitleContent.startSecActivity(ans);
        else
        {


            startTitleContent.startSecActivity(handleSearching.getKey(i));

        }
    }

    @Override
    public void onClick(View v)
    {
         switch (v.getId())
        {
            case R.id.search_button:
            {
                showToolbar=true;
                appBarLayout.setVisibility(View.VISIBLE);
                searchView.setFocusableInTouchMode(true);
                searchView.requestFocus();
                break;
            }
            case R.id.back_button:
            {
                startTitleContent.closeFragment();
                break;
            }
        }
    }

    private  class TitleLoader extends AsyncTask
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
        //titleFragment=(TitleFragment)activity;
        contentTab=(ContentTab)activity;
        startTitleContent=(StartTitleContent)activity;
    }

     interface StartTitleContent
    {

        void startSecActivity(String title);
        void closeFragment();

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

    ///sets the list returned by the titleloader async method/////
      private void setListFromTitle(Object o)
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

    ////repopulate the adapter after a search,is performed./////
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

/**class to handle the search functionality of this fragment.*/
    private class HandleSearching
    {

        /*method to handle searching function for the class*/
        private List<Form> search(String searchText)
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




}
