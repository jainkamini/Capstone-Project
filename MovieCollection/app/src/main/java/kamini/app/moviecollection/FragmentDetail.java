package kamini.app.moviecollection;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import kamini.app.moviecollection.data.FetchService;
import kamini.app.moviecollection.data.MovieContract;


public class FragmentDetail extends Fragment implements  android.support.v4.app. LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = FragmentDetail.class.getSimpleName();
    FragmentPagerAdapter adapterViewPager;
    public static final Bundle arguments = new Bundle();

    ViewPager mviewPager;
    private boolean mIsRefreshing = false;
    static final String DETAIL_URI = "URI";
    private Uri mUri;
    static final Long MOVIE_ID=12345678910L;
    private Long mMovieId;
    private static final int DETAIL_LOADER = 0;
    private String movieSelection="Detail";
    public static String mShareTrailerKey;
    private static int mFavoriteStatus;
    private static final String MOVIE_TRAILER_SHARE = "http://www.youtube.com/watch?v=";
    private static FloatingActionButton fab;
    private String mMovieName;

    private static final String[] DETAIL_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." +  MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.TABLE_NAME + "." +    MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_NAME,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER,
            MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_MOVIE_DATE,
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
            MovieContract.MovieEntry.COLUMN_MOVIE_VOTECOUNT,
            MovieContract.MovieEntry.COLUMN_MOVIE_RATING,
            MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH,

            MovieContract.TrailerEntry.COLUMN_TRAILER_KEY,
            MovieContract.TrailerEntry.COLUMN_TRAILER_NMAE,


    };

    public static final int _ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_MOVIE_NAME = 2;
    public static final int COL_MOVIE_POSTER = 3;
    public static final int COL_MOVIE_OVERVIEW = 4;
    public static final int COL_MOVIE_DATE = 5;
    public static final int COL_MOVIE_TITLE = 6;
    public static final int COL_MOVIE_VOTECOUNT = 7;
    public static final int COL_MOVIE_RATING = 8;
    public static final int COL_MOVIE_BACKDROP = 9;
    public static final int COL_MOVIE_TRAILERKEY = 10;
    public static final int COL_MOVIE_TRAILERNAME = 11;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     /*   mUri=getIntent().getData();
        mMovieId=getIntent().getExtras().getLong("MovieId");*/

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(MovieDetailFragment.DETAIL_URI);
            mMovieId=arguments.getLong(String.valueOf(MovieDetailFragment.MOVIE_ID));

            // mTransitionAnimation = arguments.getBoolean(DetailFragment.DETAIL_TRANSITION_ANIMATION, false);
            Log.e(LOG_TAG, "arguments:" + arguments);
            Log.e(LOG_TAG,"Movieid..........:" + mMovieId);
        }
    }


    public FragmentDetail() {
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        
        /**
         * Instantiate a ViewPager and a PagerAdapter
         */
        mviewPager = (ViewPager) rootView. findViewById(R.id.view_pager);

        mviewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        // adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        // vpPager.setAdapter(adapterViewPager);
        mviewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
               /* Toast.makeText(HomeActivity.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();*/
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });


        ((AppCompatActivity) getActivity()).setTitle(mMovieName);
        Log.e(LOG_TAG, "Movie name for title" + mMovieName);
     //   ((AppCompatActivity) getActivity()).setDisplayShowHomeEnabled(true);*/

        TabLayout tabLayout = (TabLayout)rootView. findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mviewPager);
        FloatingActionButton fab = (FloatingActionButton)rootView. findViewById(R.id.fab);
if (mMovieId != null) {
    mFavoriteStatus = checkFavorite();
}
        if (mFavoriteStatus==1)
        {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete_black));
        }

        else
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the tracker
               Tracker tracker = ((MyApplication)getActivity().getApplication()).getTracker();

// Send the hit
                tracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Favorite Movie")
                        .setAction("Did thing")
                        .setLabel(mMovieName)
                        .build());
                saveFavorite();






            }
        });
        /*mFavoriteStatus=checkFavorite();
        if (mFavoriteStatus==1)
        {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete_black));
        }

        else
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black));*/


        // getLoaderManager().initLoader(DETAIL_LOADER, null, (android.app.LoaderManager.LoaderCallbacks<Cursor>) this);
        getLoaderManager().initLoader(0, null, this);
        IntentFilter filter = new IntentFilter(FetchService.BROADCAST_ACTION_STATE_CHANGE);
        filter.addAction(FetchService.BROADCAST_ACTION_NO_CONNECTIVITY);

        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(mRefreshingReceiver,
                filter);
        if (savedInstanceState == null) {
            refresh();
        }
        return rootView;
    }



    private int saveFavorite() {
        ContentValues movieValues = new ContentValues();
        if (mFavoriteStatus == 0)
        {
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITESTATUS,
                    1);
            int mUpdateStatus =getContext(). getContentResolver().update(
                    MovieContract.MovieEntry.CONTENT_URI, movieValues, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=" + mMovieId, null);
            Toast.makeText(getContext(), "Movie Added", Toast.LENGTH_SHORT).show();
            //  fab.setImageResource(R.drawable.ic_delete_black);
            //  fab.setBackground(getResources().getDrawable(R.drawable.ic_delete_black));

            return 1;

        }
        else
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITESTATUS,
                    0);
        int mUpdateStatus = getContext().getContentResolver().update(
                MovieContract.MovieEntry.CONTENT_URI, movieValues, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=" + mMovieId, null);
        Toast.makeText(getContext(),"Movie Deleted",Toast.LENGTH_SHORT).show();
        //  fab.setBackground(getResources().getDrawable(R.drawable.ic_star_black));

        //   fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black));
        //   fab.setImageResource(R.drawable.ic_star_black);
        return 0;



    }

    private int checkFavorite() {

        Cursor cursor =  getContext().getContentResolver().query(MovieContract.MovieEntry.buildMovieUri(mMovieId), null,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " + mMovieId +
                        " AND " +
                        MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITESTATUS + " = " + 1,
                null, null);
        if (cursor.getCount() == 0) {
            return 0;


        }
        else
            return 1;
    }
    private void refresh() {
        Intent inputIntent=(new Intent( getContext(), FetchService.class));
        inputIntent.putExtra(FetchService.EXTRA_MOVIESELECTION, movieSelection);
        inputIntent.putExtra(FetchService.EXTRA_MOVIE_ID, String.valueOf(mMovieId));
        getActivity().  startService(inputIntent);
        //  getActivity(). startService(new Intent(getActivity(), FetchService.class));
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().    registerReceiver(mRefreshingReceiver,
                new IntentFilter(FetchService.BROADCAST_ACTION_STATE_CHANGE));
    }
    @Override
    public void onStop() {
        super.onStop();
        getActivity().  unregisterReceiver(mRefreshingReceiver);
    }



    private BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (FetchService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {
                mIsRefreshing = intent.getBooleanExtra(FetchService.EXTRA_REFRESHING, false);
                //updateRefreshingUI();
            }
            else if (FetchService.BROADCAST_ACTION_NO_CONNECTIVITY.equals(intent.getAction())) {
                mIsRefreshing=false;
                // updateRefreshingUI();
                Toast.makeText(context, getString(R.string.empty_recycler_view_no_network), Toast.LENGTH_LONG).show();
            }
        }
    };


   /* private void finishCreatingMenu(Menu menu) {
        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareForecastIntent());
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        *//*if (getActivity() instanceof MovieDetailActivity) {
            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.menu_detail, menu);
            finishCreatingMenu(menu);
        }*//*
       menu.clear();
        inflater.inflate(R.menu.menu_detail, menu);
        finishCreatingMenu(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }*/
    private void finishCreatingMenu(Menu menu) {
        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareForecastIntent());
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if ( getActivity() instanceof MovieDetailActivity ){
            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.menu_detail, menu);
            finishCreatingMenu(menu);
        }


    }




    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");

        shareIntent.putExtra(Intent.EXTRA_TEXT,
                MOVIE_TRAILER_SHARE + mShareTrailerKey);

        return shareIntent;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            return true;
        }
        if (id == R.id.action_play)
        {

            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" +mShareTrailerKey)));
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getContext(),
                    MovieContract.MovieEntry.buildMovieTrailerId(mMovieId,"T"),
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        AppCompatActivity activity = (AppCompatActivity)getActivity();
        Toolbar toolbarView = (Toolbar) getView().findViewById(R.id.toolbar);

        if (data != null && data.moveToFirst() )
        {

            mShareTrailerKey=data.getString(COL_MOVIE_TRAILERKEY);
            mMovieName=data.getString(COL_MOVIE_NAME);
            ((AppCompatActivity) getActivity()).   getSupportActionBar().setTitle(mMovieName);
            Menu menu = toolbarView.getMenu();
            if (null != menu ) menu.clear();
            toolbarView.inflateMenu(R.menu.menu_detail);
            finishCreatingMenu(toolbarView.getMenu());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private  int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }


        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment

if (mUri !=null && mMovieId !=null) {
    arguments.putParcelable(MovieDetailFragment.DETAIL_URI, mUri);
    arguments.putLong(String.valueOf(MovieDetailFragment.MOVIE_ID), mMovieId);
}
                 /* MovieDetailFragment fragmentFirst = new MovieDetailFragment();
                    fragmentFirst.setArguments(arguments);
                    return fragmentFirst;*/

               /* MovieDetailFragment fragment = new MovieDetailFragment();
                FragmentTransaction frTransaction = getChildFragmentManager().beginTransaction();

                frTransaction.add(R.id.movie_container, fragment);
                    frTransaction.addToBackStack(null);
                frTransaction.commit();*/
                   return MovieDetailFragment.newInstance(arguments);

                case 1: // Fragment # 0 - This will show FirstFragment different title
                   /*SimilarMovieFragment similarMovieFragment = new SimilarMovieFragment();
                    similarMovieFragment.setArguments(arguments);
                    return similarMovieFragment;*/
                    /*SimilarMovieFragment fragmentsimilar = new SimilarMovieFragment();
                    FragmentTransaction frTransactionsimilar = getChildFragmentManager().beginTransaction();


                    frTransactionsimilar.add(R.id.similarmovie_container, fragmentsimilar);
                    frTransactionsimilar.addToBackStack(null);
                    frTransactionsimilar.commit();*/
                    if (mUri !=null && mMovieId !=null) {
                        arguments.putParcelable(SimilarMovieFragment.DETAIL_URI, mUri);
                        arguments.putLong(String.valueOf(SimilarMovieFragment.MOVIE_ID), mMovieId);
                    }

                    return SimilarMovieFragment.newInstance(arguments);

                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Movie";
            } else {
                return "SIMILAR MOVIE";
            }
        }
    }



}
