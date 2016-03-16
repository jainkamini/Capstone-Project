package kamini.app.moviecollection;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import kamini.app.moviecollection.data.FetchService;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieListActivityFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>  {

    public static final String LOG_TAG = MovieListActivityFragment.class.getSimpleName();



    private MovieAdapter movieAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mEmptyView;
    private RecyclerView mRecyclerView;
    private boolean mIsRefreshing = false;
    public MovieListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);



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



   private void refresh() {
      getActivity(). startService(new Intent(getActivity(), FetchService.class));
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
                //updateRefreshingUI();
            }
            else if (FetchService.BROADCAST_ACTION_NO_CONNECTIVITY.equals(intent.getAction())) {
                mIsRefreshing=false;
               // updateRefreshingUI();
               Toast.makeText(getActivity(), getString(R.string.empty_recycler_view_no_network), Toast.LENGTH_LONG).show();
            }
        }
    };
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return MovieLoader.newMovieInstance(this.getActivity());
    }




    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        movieAdapter=new MovieAdapter(getActivity(),cursor);

        if(movieAdapter.getItemCount()==0){
            mEmptyView.setVisibility(View.VISIBLE);
        } else{
            mEmptyView.setVisibility(View.GONE);
        }
        mRecyclerView.setAdapter(movieAdapter);

    }

}
