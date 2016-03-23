package kamini.app.moviecollection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import kamini.app.moviecollection.data.MovieContract;




/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    // Store instance variables
    private String title;
    private int page;
    static final String DETAIL_URI = "URI";
    private Uri mUri;
    private boolean mTransitionAnimation;

    private static final int DETAIL_LOADER = 0;

    private static final String[] DETAIL_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_NAME,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER,
            MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_MOVIE_DATE,
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
            MovieContract.MovieEntry.COLUMN_MOVIE_VOTECOUNT,
            MovieContract.MovieEntry.COLUMN_MOVIE_RATING,
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
    public TextView txttitle;
    public TextView txtname;
    public TextView txtvotecount;
    public ImageView imgposter;
    public TextView  txtdate;
    public TextView txtvoteavg;

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(MovieDetailFragment.DETAIL_URI);
           // mTransitionAnimation = arguments.getBoolean(DetailFragment.DETAIL_TRANSITION_ANIMATION, false);
            Log.e(LOG_TAG,"arguments:" + arguments);
            Log.e(LOG_TAG,"Movieuri:" + mUri);
        }


    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        txttitle= (TextView) view.findViewById(R.id.list_movie_title_textview);
        imgposter=(ImageView)view.findViewById(R.id.list_movie_imageview);
       // getLoaderManager().initLoader(DETAIL_LOADER, null, this);
      //  tvLabel.setText(page + " -- " + title);
        return view;
    }
//this function for start the loader
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
      //  if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );



    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {



        if (data != null && data.moveToFirst() )
        {
           // Log.e(LOG_TAG, "cursorcount:" + data.getCount());
              txttitle.setText(data.getString(COL_MOVIE_TITLE));
         //   Log.e(LOG_TAG,"MovieTitle:" + data.getString(COL_MOVIE_TITLE));

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}
