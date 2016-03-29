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

import com.squareup.picasso.Picasso;

import kamini.app.moviecollection.data.MovieContract;




/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>   {
    public static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    // Store instance variables
    private String title;
    private int page;
    static final String DETAIL_URI = "URI";
    private Uri mUri;
    static final Long MOVIE_ID=12345678910L;
    private Long mMovieId;
    private boolean mTransitionAnimation;
private  String mReviewContent="Review";
    private boolean mIsRefreshing = false;
    private static final int DETAIL_LOADER = 0;

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
            MovieContract.ReviewEntry.COLUMN_REVIEW_CONTENT,
            MovieContract.ReviewEntry.COLUMN_REVIEW_AUTHOR,

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
    public static final int COL_MOVIE_REVIEWCONTENT = 10;
    public static final int COL_MOVIE_REVIEWAUTHOR = 11;
    public TextView txttitle;
    public TextView txtname;
    public TextView txtvotecount;
    public ImageView imgposter;
    public ImageView imgbackdrop;
    public TextView  txtdate;
    public TextView txtvoteavg;
    public TextView txtoverview;
    public TextView txtreview;


    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(MovieDetailFragment.DETAIL_URI);
            mMovieId=arguments.getLong(String.valueOf(MovieDetailFragment.MOVIE_ID));
           // mTransitionAnimation = arguments.getBoolean(DetailFragment.DETAIL_TRANSITION_ANIMATION, false);
            Log.e(LOG_TAG,"arguments:" + arguments);
            Log.e(LOG_TAG,"Movieid..........:" + mMovieId);
        }

        /*page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");*/
    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        txttitle= (TextView) view.findViewById(R.id.list_movie_title_textview);
        imgbackdrop=(ImageView)view.findViewById(R.id.list_backdrop_imageview);
        imgposter=(ImageView)view.findViewById(R.id.list_movieposter_imageview);
        txtname=(TextView)view.findViewById(R.id.list_movie_name_textview);
        txtdate=(TextView)view.findViewById(R.id.list_movie_releasedate_textview);
        txtvoteavg=(TextView)view.findViewById(R.id.list_movierating_textview);
        txtvotecount=(TextView)view.findViewById(R.id.list_movie_votecount_textview);
        txtoverview=(TextView)view.findViewById(R.id.list_overview_textview);
        txtreview=(TextView)view.findViewById(R.id.list_review_textview);

        getLoaderManager().initLoader(DETAIL_LOADER, null, this);


       // getLoaderManager().initLoader(DETAIL_LOADER, null, this);
      //  tvLabel.setText(page + " -- " + title);
        return view;
    }







    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
       if ( null != mUri ) {
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
return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {



        if (data != null && data.moveToFirst() )
        {
           // Log.e(LOG_TAG, "cursorcount:" + data.getCount());
             txttitle.setText(data.getString(COL_MOVIE_TITLE));
            txtname.setText(data.getString(COL_MOVIE_NAME));
            txtvotecount.setText(data.getString(COL_MOVIE_VOTECOUNT));
            txtvoteavg.setText(data.getString(COL_MOVIE_RATING));
            txtdate.setText(data.getString(COL_MOVIE_DATE));
            txtoverview.setText(data.getString(COL_MOVIE_OVERVIEW));

            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185" + data.getString(COL_MOVIE_BACKDROP)).into(imgbackdrop);
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185" + data.getString(COL_MOVIE_POSTER)).into(imgposter);




           mReviewContent="";
            for (int i=0;i<data.getCount();i++) {
                data.moveToPosition(i);
                mReviewContent = mReviewContent +  data.getString(COL_MOVIE_REVIEWCONTENT)+ System.getProperty ("line.separator")+ System.getProperty ("line.separator");
                //txtreview.setText(txtreview.getText()+ data.getString(COL_MOVIE_REVIEWCONTENT));
                Log.e(LOG_TAG,"review content:" +mReviewContent);
            }

         //   txtreview.setText(mReviewContent);
          //  txtreview.setText("");
           /* mReviewContent="";
            for (int i=0;i<data.getCount();i++) {
                data.moveToPosition(i);
                if (i==0) {
                    mReviewContent=data.getString(COL_MOVIE_REVIEWCONTENT)+System.getProperty("line.separator");
                  //  txtreview.setText(  data.getString(COL_MOVIE_REVIEWCONTENT) + System.getProperty("line.separator") + System.getProperty("line.separator"));
                   // Log.e(LOG_TAG, "review content:" + txtreview.getText());
                }
                else
                    mReviewContent=mReviewContent+data.getString(COL_MOVIE_REVIEWCONTENT)+System.getProperty("line.separator");
                  // txtreview.setText( txtreview.getText() + data.getString(COL_MOVIE_REVIEWCONTENT) + System.getProperty("line.separator") + System.getProperty("line.separator"));

             //   mReviewContent = mReviewContent +  data.getString(COL_MOVIE_REVIEWCONTENT)+ System.getProperty ("line.separator")+ System.getProperty ("line.separator");
                //txtreview.setText(txtreview.getText()+ data.getString(COL_MOVIE_REVIEWCONTENT));
              //  Log.e(LOG_TAG, "review content:" + txtreview.getText());
            }*/
           // txtreview.setText(mReviewContent);

            Log.e(LOG_TAG,"datacount:" + data.getCount());

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}
