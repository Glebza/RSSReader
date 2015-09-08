package com.example.student.myrss;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by student on 23.07.2015.
 */
public class HeadlinesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final int POSITION_OF_TITLES_IN_FEED = 0;
    public static final int POSITION_OF_LINKS_IN_FEED = 1;
    public static final int POSITION_OF_HEADLINES_IMAGES_IN_FEED = 2;
    public static final String TAG = HeadlinesFragment.class.getSimpleName();
    OnHeadlineSelectedListener mCallback;

    private ListView list;
    private List<String> headlinesTitles = new ArrayList<String>();
    private List<String> headlinesReferences = new ArrayList<String>();
    private List<Drawable> headlinesImages = new ArrayList<Drawable>();
    private List<String> headlinesImagesUrls = new ArrayList<String>();
    private ArticleViewAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView mRssFeed;
    private List<String[]> rssFeed = new ArrayList<String[]>();
    private AndroidPitRssFeedTask androidPitRssFeedTask;

    public List<String> getArticlesUrl() {
        return headlinesReferences;
    }

    public interface OnHeadlineSelectedListener {


        /** Called by HeadlinesFragment when a list item is selected */
         void onArticleSelected(int position);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate");
        setRetainInstance(true);
        androidPitRssFeedTask = new AndroidPitRssFeedTask(this);
        androidPitRssFeedTask.execute();



    }


    public HeadlinesFragment getFragment(){
        return  this;
    }
    public void createAdapterFeed(){
        Log.d("createAdapter", "create adapter");
        if (mRssFeed!= null){
            for(String[] s: rssFeed){
                headlinesTitles.add(s[POSITION_OF_TITLES_IN_FEED]);
                headlinesReferences.add(s[POSITION_OF_LINKS_IN_FEED]);
               headlinesImagesUrls.add(s[POSITION_OF_HEADLINES_IMAGES_IN_FEED]);
                //Log.d("rssFeed",s[0] + " " + s[1] + " " + s[2] + " ");
            }

            createAdapter();

        }

    }
    public void createAdapter(){
        adapter = new ArticleViewAdapter(headlinesTitles,headlinesReferences,headlinesImagesUrls,this);
        mRssFeed.setAdapter(adapter);
    }
    public void setImagesFromUrl(List<Drawable> images) {

        headlinesImages = images;

    }

    public void setFeed(List<String[]> list){
        rssFeed = list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
     //   mRssFeed = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRssFeed = (ListView) rootView.findViewById(R.id.headlinesListView);


             swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
            swipeRefreshLayout.setOnRefreshListener(this);


        createAdapter();

        return rootView;
    }


    @Override
    public void onRefresh() {
        Toast.makeText(getActivity(),"Refresh",Toast.LENGTH_LONG).show();
        androidPitRssFeedTask = new AndroidPitRssFeedTask(getFragment());
        androidPitRssFeedTask.execute();
    }
    @Override
    public void onStart() {
        super.onStart();
        list = (ListView) getActivity().findViewById(R.id.headlinesListView);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               mCallback.onArticleSelected(position);
            }
        }) ;


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG,"onAttach");
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"onActivityCreated");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        androidPitRssFeedTask.stopProgressDialog();
    }
}
