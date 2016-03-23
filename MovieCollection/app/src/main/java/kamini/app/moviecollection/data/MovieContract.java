package kamini.app.moviecollection.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Kamini on 9/28/2015.
 */
public class MovieContract {




    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "kamini.app.moviecollection";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_TRAILER = "trailer";
    public static final String PATH_REVIEW = "review";

    /* Inner class that defines the table contents of the movie table */
    public static final class MovieEntry implements BaseColumns {

       /* public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.example.xyzreader.items";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.example.xyzreader.items";*/
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        // Table name
        public static final String TABLE_NAME = "movie_details";



        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_POSTER = "movie_poster";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_MOVIE_DATE = "movie_date";
        public static final String COLUMN_MOVIE_RATING = "movie_rating";
        public static final String COLUMN_MOVIE_OVERVIEW = "movie_overview";
        public static final String COLUMN_MOVIE_NAME = "movie_name";
        public static final String COLUMN_MOVIE_VOTECOUNT = "movie_votecount";
        public static final String COLUMN_MOVIE_GENREIDS = "movie_genreids";
        public static final String COLUMN_MOVIE_STATUS = "movie_status";




        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
        // content://..../MovieId
       /* public static Uri buildFavouriteMoviesUriWithMovieId(int MovieId) {
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(MovieId)).build();
        }*/

       /* public static Uri buildMovieFavoriteSatus(String favoriteStatus) {
            return CONTENT_URI.buildUpon().appendPath(favoriteStatus).build();
        }*/

        public static Uri buildMovieStatus(String movieStatus)
        {
            return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_MOVIE_STATUS, movieStatus).build();
        }


        public static Uri buildMovieId(Long movieid)
        {
            return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_MOVIE_ID,Long.toString(movieid) ).appendPath("movieid").build();

        }


        public static String getMovieStatusUri(Uri uri) {
            String movieStatus = uri.getQueryParameter(COLUMN_MOVIE_STATUS);
            return movieStatus;
        }


        public static String getMovieIdUri(Uri uri) {
            String movieid = uri.getQueryParameter(COLUMN_MOVIE_ID);
           return movieid;
        }
    }
    public static final class TrailerEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        // Table name
        public static final String TABLE_NAME = "trailer";



        public static final String COLUMN_MOVIEID_KEY = "movie_id";
        public static final String COLUMN_MOVIE_KEY = "trailer_key";
        public static final String COLUMN_TRAILER_NMAE = "trailer_name";

        public static Uri buildTrailerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }

    public static final class ReviewEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        // Table name
        public static final String TABLE_NAME = "review";
        // Column with the foreign key into the  table.
        public static final String COLUMN_MOVIEID_KEY = "movie_id";

        public static final String COLUMN_MOVIE_AUTHOR = "review_poster";
        public static final String COLUMN_MOVIE_CONTENT = "review_content";
        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
