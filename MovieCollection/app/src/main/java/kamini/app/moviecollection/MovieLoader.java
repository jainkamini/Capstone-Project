package kamini.app.moviecollection;

import android.content.Context;

import android.support.v4.content.CursorLoader;
import android.net.Uri;

import kamini.app.moviecollection.data.MovieContract;

/**
 * Created by Kamini on 3/13/2016.
 */
public class MovieLoader extends CursorLoader {


    public static MovieLoader newMovieInstance(Context context,String movieSelection) {
      //  return new MovieLoader(context, MovieContract.Items.buildDirUri());
       // return new MovieLoader(context, MovieContract.MovieEntry.CONTENT_URI);
       return new MovieLoader(context, MovieContract.MovieEntry.buildMovieStatus(movieSelection));
       // CONTENT_URI
    }
    public static MovieLoader newMovieInstance(Context context,String movieSelection,Long movieID) {
        //  return new MovieLoader(context, MovieContract.Items.buildDirUri());
        // return new MovieLoader(context, MovieContract.MovieEntry.CONTENT_URI);
        return new MovieLoader(context, MovieContract.MovieEntry.buildMovieStatus(movieSelection));
        // CONTENT_URI
    }

    public static MovieLoader newMovieFaboriteInstance(Context context,String movieSelection) {
        //  return new MovieLoader(context, MovieContract.Items.buildDirUri());
        // return new MovieLoader(context, MovieContract.MovieEntry.CONTENT_URI);
        return new MovieLoader(context, MovieContract.MovieEntry.buildMovieFavoriteStatus(movieSelection,"1","1"));
        // CONTENT_URI
    }

   /* public static MovieLoader newInstanceForItemId(Context context, long itemId) {
        return new MovieLoader(context, MovieContract.MovieEntry.buildMovieUri(itemId));
    }*/

    private MovieLoader(Context context, Uri uri) {
        super(context, uri, Query.PROJECTION, null, null, null);
    }

    public interface Query {
        String[] PROJECTION = {
                 MovieContract.MovieEntry._ID,
                  MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                MovieContract.MovieEntry.COLUMN_MOVIE_NAME,
                MovieContract.MovieEntry.COLUMN_MOVIE_POSTER,
                MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
                MovieContract.MovieEntry.COLUMN_MOVIE_DATE,
                MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
                MovieContract.MovieEntry.COLUMN_MOVIE_VOTECOUNT,
                MovieContract.MovieEntry.COLUMN_MOVIE_RATING,
                MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH,

        };
int _id=0;
        int movieid=1;
        int title=2;
        int poster_path=3;
        int overview=4;
        int release_date=5;
        int original_title=6;
        int vote_count=7;
        int vote_average=8;
        int back_drop=9;


    }
}
