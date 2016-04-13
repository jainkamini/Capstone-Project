package kamini.app.moviecollection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import kamini.app.moviecollection.data.FetchService;
import kamini.app.moviecollection.data.MovieContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieListActivityFragment extends Fragment implements
        android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>  {

    public static final String LOG_TAG = MovieListActivityFragment.class.getSimpleName();



    private MovieAdapter movieAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mEmptyView;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private boolean mIsRefreshing = false;
    private String movieSelection;
    private String movieStatus;
    public MovieListActivityFragment() {
    }


    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);


      // ((AppCompatActivity) getActivity()). getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //    ((AppCompatActivity) getActivity()).setDisplayShowTitleEnabled(true);

        if (savedInstanceState ==null) {
            movieSelection = this.getArguments().getString("movieselection");
            movieStatus = this.getArguments().getString("moviestatus");
        }
        else
        {
            movieSelection = savedInstanceState.getString("movieselection");
            movieStatus = savedInstanceState.getString("moviestatus");
        }
    //    ((AppCompatActivity) getActivity()).setTitle(movieSelection);
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView. findViewById(R.id.swipe_refresh_layout);

        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.primary,
                R.color.primary_dark,
                R.color.primary_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Set refresh to false so the refresh icon doesn't just spin indefinitely
                // This is just a placeholder.
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        //Add Banner Ad
        AdView mAdView = (AdView)rootView. findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //   movieAdapter = new MovieAdapter(items);
        mEmptyView = (TextView) rootView.findViewById(R.id.recycler_view_empty);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_movie_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //movieAdapter.setHasStableIds(true);
        //  movieAdapter.setHasStableIds(true);
        // movieAdapter=new MovieAdapter(getActivity(),null);
        mRecyclerView.setHasFixedSize(true);

        // mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // recyclerView.setAdapter(movieAdapter);


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


    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putString("movieselection", movieSelection);
        outState.putString("moviestatus",movieStatus);
        super.onSaveInstanceState(outState);
    }



    private void refresh() {
        Intent inputIntent=(new Intent(getActivity(), FetchService.class));
        inputIntent.putExtra(FetchService.EXTRA_MOVIESELECTION, movieSelection);
        getActivity(). startService(inputIntent);
        //  getActivity(). startService(new Intent(getActivity(), FetchService.class));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecyclerView.setAdapter(null);
    }
    @Override
    public void onStart() {
        super.onStart();

        getActivity().registerReceiver(mRefreshingReceiver,
                new IntentFilter(FetchService.BROADCAST_ACTION_STATE_CHANGE));
    }
    @Override
    public void onStop() {
        super.onStop();
        getActivity(). unregisterReceiver(mRefreshingReceiver);
    }

    private BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (FetchService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {
                mIsRefreshing = intent.getBooleanExtra(FetchService.EXTRA_REFRESHING, false);
                updateRefreshingUI();
            }
            else if (FetchService.BROADCAST_ACTION_NO_CONNECTIVITY.equals(intent.getAction())) {
                mIsRefreshing=false;
                 updateRefreshingUI();
                Toast.makeText(getActivity(), getString(R.string.empty_recycler_view_no_network), Toast.LENGTH_LONG).show();
            }
        }
    };

    private void updateRefreshingUI() {
        mSwipeRefreshLayout.setRefreshing(mIsRefreshing);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        if (movieStatus == "F") {
            return MovieLoader.newMovieFaboriteInstance(this.getActivity(), "1");
        }

        else
            return MovieLoader.newMovieInstance(this.getActivity(), movieStatus);
    }


   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


            // Inflate the menu; this adds items to the action bar if it is present.

            inflater.inflate(R.menu.menu_detail, menu);
            MenuItem menuItem = menu.findItem(R.id.action_share);
            menuItem.setVisible(false);



    }*/


    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        //  movieAdapter=new MovieAdapter(getActivity(),MovieAdapter.MovieAdapterOnClickHandler()

        movieAdapter = new MovieAdapter(getActivity(), new MovieAdapter.MovieAdapterOnClickHandler() {

            @Override
            public void onClick(Long movieId, MovieAdapter.ViewHolder vh) {
                ((Callback) getActivity())
                        .onItemSelected(MovieContract.MovieEntry.buildMovieId(movieId),movieId
                                ,
                                vh
                        );
            }


        }, cursor   );
       /*mToolbar = (Toolbar)getView(). findViewById(R.id.toolbar);

        ((AppCompatActivity) getActivity()).  setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).setTitle(movieSelection);
        Menu menu = mToolbar.getMenu();
        if (null != menu) menu.clear();*/
        if(movieAdapter.getItemCount()==0){
            mEmptyView.setVisibility(View.VISIBLE);
        } else{
            mEmptyView.setVisibility(View.GONE);
        }


        mRecyclerView.setAdapter(movieAdapter);

    }

}
