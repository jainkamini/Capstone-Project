package kamini.app.moviecollection;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import kamini.app.moviecollection.data.FetchService;
import kamini.app.moviecollection.data.MovieContract;

;


public class MovieDetailActivity extends AppCompatActivity implements SimilarMovieFragment.Callback, LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
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
    private static FloatingActionButton   fab;
    private String mMovieName;
// java.lang.NullPointerException: Attempt to invoke virtual method 'void android.support.design.widget.FloatingActionButton.setImageDrawable(android.graphics.drawable.Drawable)' on a null object reference
    //java.lang.NullPointerException: Attempt to invoke virtual method 'void android.support.design.widget.FloatingActionButton.setImageResource(int)' on a null object reference
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
    private static final SQLiteQueryBuilder sMovieQueryBuilder =
            new SQLiteQueryBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

       // Log.e(LOG_TAG, "MovieDetailacivity id...:" + getIntent().getExtras().getLong("MovieId"));
        /*final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_drawer);
        ab.setDisplayHomeAsUpEnabled(true);*/
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            // arguments = new Bundle();

            arguments.putParcelable(MovieDetailFragment.DETAIL_URI, getIntent().getData());

            mUri=getIntent().getData();
            mMovieId=getIntent().getExtras().getLong("MovieId");
            arguments.putLong(String.valueOf(MovieDetailFragment.MOVIE_ID), getIntent().getExtras().getLong("MovieId"));

           /* arguments.putParcelable(SimilarMovieFragment.DETAIL_URI, getIntent().getData());
            arguments.putString(SimilarMovieFragment.MOVIE_ID, getIntent().getExtras().toString());*/

            Log.e(LOG_TAG, "MovieDetailacivity id...:" + getIntent().getExtras().getLong("MovieId"));
            //  arguments.putBoolean(MovieDetailActivity.DETAIL_TRANSITION_ANIMATION, true);

          /*  MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_detail_movie, fragment)
                    .commit();*/

            // Being here means we are in animation mode
            //  supportPostponeEnterTransition();
        }

        /**
         * Instantiate a ViewPager and a PagerAdapter
         */
        mviewPager = (ViewPager) findViewById(R.id.view_pager);
        // if (viewPager != null) {
        // setupViewPager(mviewPager);
        // }
     //  getLoaderManager().initLoader(0, null, getBaseContext());
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        mviewPager.setAdapter(adapterViewPager);
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(mMovieName);
        Log.e(LOG_TAG, "Movie name for title" + mMovieName);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mviewPager);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

            mFavoriteStatus = checkFavorite();

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
                Tracker tracker = ((MyApplication) getApplication()).getTracker();

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
        getSupportLoaderManager().initLoader(0, null, this);
        IntentFilter filter = new IntentFilter(FetchService.BROADCAST_ACTION_STATE_CHANGE);
        filter.addAction(FetchService.BROADCAST_ACTION_NO_CONNECTIVITY);

        LocalBroadcastManager.getInstance(this).registerReceiver(mRefreshingReceiver,
                filter);
        if (savedInstanceState == null) {
            refresh();
        }

    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // outState.putParcelable("MovieKey", extras);
        outState.putParcelable(MovieDetailFragment.DETAIL_URI, mUri);
        outState.putLong(String.valueOf(MovieDetailFragment.MOVIE_ID), mMovieId);

        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //  extras=  savedInstanceState.getParcelable("MovieKey");
        mUri=savedInstanceState.getParcelable(MovieDetailFragment.DETAIL_URI);
        mMovieId=savedInstanceState.getLong(String.valueOf(MovieDetailFragment.MOVIE_ID));

        super.onRestoreInstanceState(savedInstanceState);
    }
    private int saveFavorite() {
        ContentValues movieValues = new ContentValues();
        if (mFavoriteStatus == 0)
        {
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITESTATUS,
                1);
            int mUpdateStatus = getContentResolver().update(
                    MovieContract.MovieEntry.CONTENT_URI, movieValues, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=" + mMovieId, null);
            Toast.makeText(this,"Movie Added",Toast.LENGTH_SHORT).show();
          //  fab.setImageResource(R.drawable.ic_delete_black);
          //  fab.setBackground(getResources().getDrawable(R.drawable.ic_delete_black));

            return 1;

        }
        else
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITESTATUS,
                    0);
        int mUpdateStatus = getContentResolver().update(
                MovieContract.MovieEntry.CONTENT_URI, movieValues, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=" + mMovieId, null);
        Toast.makeText(this,"Movie Deleted",Toast.LENGTH_SHORT).show();
      //  fab.setBackground(getResources().getDrawable(R.drawable.ic_star_black));

        //   fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black));
     //   fab.setImageResource(R.drawable.ic_star_black);
        return 0;



    }

    private int checkFavorite() {

        Cursor cursor = getApplicationContext().getContentResolver().query(MovieContract.MovieEntry.buildMovieUri(mMovieId), null,
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
        Intent inputIntent=(new Intent(this, FetchService.class));
        inputIntent.putExtra(FetchService.EXTRA_MOVIESELECTION, movieSelection);
        inputIntent.putExtra(FetchService.EXTRA_MOVIE_ID, String.valueOf(mMovieId));
        startService(inputIntent);
        //  getActivity(). startService(new Intent(getActivity(), FetchService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (mShareActionProvider != null ) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
        return true;
    }


    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");


        //String title = getResources().getString(R.string.check_serie) + serie.getName();
       // String shareBody = "https://trakt.tv/search/tvdb/" + serie.getId() + "?id_type=show";
      //  sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
       // sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, title + " " + shareBody);
       // startActivity(Intent.createChooser(sharingIntent, "Share via"));
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                MOVIE_TRAILER_SHARE + mShareTrailerKey);
       // shareIntent.putExtra(Intent.EXTRA_TEXT,"hiiiiiiiiiiiiiiiiiiiiii"+"hello");
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
   // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + movieTrailer.getmTrailerKey())));
    //N7gDl9HRsY4
    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" +mShareTrailerKey)));
}
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onItemSelected(Uri contentUri,Long mMovieId, MovieAdapter.ViewHolder vh) {
        Intent intent = new Intent(this, MovieDetailActivity.class)
                .setData(contentUri);
        intent.putExtra("MovieId", mMovieId);
        startActivity(intent);
        Log.e(LOG_TAG, "Mainactivity MovieId......" + mMovieId);
       /* if (mTwoPane) {
            Bundle args = new Bundle();
            if (value != null) {
                args.putParcelable("Serie", value);
            }
            DetailSerieFragment fragment = new DetailSerieFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .addSharedElement(imageView, getResources().getString(R.string.transition_photo))
                    .replace(R.id.fragment_detail_serie, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailSerieSearchedActivity.class);
            intent.putExtra("Serie", value);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, imageView, getResources().getString(R.string.transition_photo));
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }

        }*/
    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_movie_detail, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);


    }


    @Override
    public void onStart() {
        super.onStart();

        registerReceiver(mRefreshingReceiver,
                new IntentFilter(FetchService.BROADCAST_ACTION_STATE_CHANGE));
    }
    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(mRefreshingReceiver);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getApplicationContext(),
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



        if (data != null && data.moveToFirst() )
        {

mShareTrailerKey=data.getString(COL_MOVIE_TRAILERKEY);
            mMovieName=data.getString(COL_MOVIE_NAME);
            getSupportActionBar().setTitle(mMovieName);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

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
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment

                    MovieDetailFragment fragmentFirst = new MovieDetailFragment();

                    fragmentFirst.setArguments(arguments);
                    return fragmentFirst;



                case 1: // Fragment # 0 - This will show FirstFragment different title
                    SimilarMovieFragment similarMovieFragment = new SimilarMovieFragment();
                    similarMovieFragment.setArguments(arguments);
                    return similarMovieFragment;

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
