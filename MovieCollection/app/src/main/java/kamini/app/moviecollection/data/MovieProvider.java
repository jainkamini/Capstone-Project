package kamini.app.moviecollection.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Kamini on 9/29/2015.
 */
public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private MovieDbHelper mOpenHelper;

    static final int MOVIE = 100;
    static final int MOVIE_ID = 101;
    static final int TRAILAR = 102;
    static final int REVIEW = 103;
    static final int MOVIE_WITH_STATUS = 104;
    static final int MOVIE_WITH_ID = 105;
    static final int MOVIETRAILER_WITH_ID = 106;
    static final int MOVIE_WITH_FAVORITESTATUS = 107;
    static final int GENRE = 108;

    private static final SQLiteQueryBuilder sMovieQueryBuilder =
            new SQLiteQueryBuilder();
    private static final SQLiteQueryBuilder sMovieTrailerQueryBuilder =
            new SQLiteQueryBuilder();
    private static final String MOVIE_BY_STATUS = MovieContract.MovieEntry.COLUMN_MOVIE_STATUS + " = ?";
    private static final String MOVIE_BY_FAVORITESTATUS = MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITESTATUS + " = ?";
    private static final String GENERE_ID = MovieContract.GenreEntry.COLUMN_GENRE_ID + " in  ?";
   // private static final String MOVIE_BY_ID = MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?";


    private static final String MOVIE_BY_ID =   MovieContract.MovieEntry.TABLE_NAME+
            "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?";

    static{
        sMovieQueryBuilder.setTables(

               /*MovieContract.ReviewEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieEntry.TABLE_NAME +
                        " ON " + MovieContract.ReviewEntry.TABLE_NAME +
                        "." + MovieContract.ReviewEntry.COLUMN_MOVIEID_KEY +
                        " = " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID);*/




        MovieContract.MovieEntry.TABLE_NAME + " LEFT OUTER JOIN " +
                MovieContract.ReviewEntry.TABLE_NAME +
                " ON " + MovieContract.ReviewEntry.TABLE_NAME +
                "." + MovieContract.ReviewEntry.COLUMN_MOVIEID_KEY +
                " = " + MovieContract.MovieEntry.TABLE_NAME +
                "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID);

               /* MovieContract.MovieEntry.TABLE_NAME +" m "+
                " INNER JOIN " + MovieContract.ReviewEntry.TABLE_NAME
                        +" r  "+        " ON " + " m "+
                "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID   +      " = " +
                  " r "  + "." +    MovieContract.ReviewEntry.COLUMN_MOVIEID_KEY + " INNER JOIN " + MovieContract.TrailerEntry.TABLE_NAME
                        +" t "+ " ON " + " m "+ "." +MovieContract.MovieEntry.COLUMN_MOVIE_ID   +      " = "
                           +" t "+ "."+MovieContract.ReviewEntry.COLUMN_MOVIEID_KEY ) ;*/



    }
    static{
        sMovieTrailerQueryBuilder.setTables(

               /* MovieContract.TrailerEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieEntry.TABLE_NAME +
                        " ON " + MovieContract.TrailerEntry.TABLE_NAME +
                        "." + MovieContract.TrailerEntry.COLUMN_MOVIEID_KEY +
                        " = " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID);*/

                MovieContract.MovieEntry.TABLE_NAME + " LEFT OUTER JOIN " +
                        MovieContract.TrailerEntry.TABLE_NAME +
                        " ON " + MovieContract.TrailerEntry.TABLE_NAME +
                        "." + MovieContract.TrailerEntry.COLUMN_MOVIEID_KEY +
                        " = " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID);

               /* MovieContract.MovieEntry.TABLE_NAME +" m "+
                " INNER JOIN " + MovieContract.ReviewEntry.TABLE_NAME
                        +" r  "+        " ON " + " m "+
                "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID   +      " = " +
                  " r "  + "." +    MovieContract.ReviewEntry.COLUMN_MOVIEID_KEY + " INNER JOIN " + MovieContract.TrailerEntry.TABLE_NAME
                        +" t "+ " ON " + " m "+ "." +MovieContract.MovieEntry.COLUMN_MOVIE_ID   +      " = "
                           +" t "+ "."+MovieContract.ReviewEntry.COLUMN_MOVIEID_KEY ) ;*/



    }



    private Cursor getMovieByReview(
            Uri uri, String[] projection, String sortOrder,String mMovieid) {
     //   String locationSetting = WeatherContract.WeatherEntry.getLocationSettingFromUri(uri);


        return sMovieQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                MOVIE_BY_ID,
                new String[]{mMovieid},
                null,
                null,
                sortOrder
        );
    }


    private Cursor getMovieByTrailer(
            Uri uri, String[] projection, String sortOrder,String mMovieid) {
        //   String locationSetting = WeatherContract.WeatherEntry.getLocationSettingFromUri(uri);


        return sMovieTrailerQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                MOVIE_BY_ID,
                new String[]{mMovieid},
                null,
                null,
                sortOrder
        );
    }


    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority,  MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority,  MovieContract.PATH_MOVIE, MOVIE_WITH_FAVORITESTATUS);

       // matcher.addURI(authority,  MovieContract.PATH_MOVIE + "/*/#", MOVIE_ID );
        matcher.addURI(authority,  MovieContract.PATH_MOVIE + "/#", MOVIE_ID );


        matcher.addURI(authority,  MovieContract.PATH_TRAILER, TRAILAR);
        matcher.addURI(authority,  MovieContract.PATH_REVIEW, REVIEW);
        matcher.addURI(authority,  MovieContract.PATH_GENRE, GENRE);
        matcher.addURI(authority,MovieContract.PATH_MOVIE , MOVIE_WITH_STATUS);
        matcher.addURI(authority,MovieContract.PATH_MOVIE + "/*" , MOVIE_WITH_ID);
        matcher.addURI(authority,MovieContract.PATH_MOVIE + "/*/*" , MOVIETRAILER_WITH_ID);
        matcher.addURI(authority,MovieContract.PATH_MOVIE + "/*/*/*" , MOVIE_WITH_FAVORITESTATUS);




        return matcher;
    }





    @Override
    public boolean onCreate() {
        Context context = getContext();
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {

            /*case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;*/
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_ID :
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case TRAILAR :
                return MovieContract.TrailerEntry.CONTENT_ITEM_TYPE;
            case REVIEW :
                return MovieContract.ReviewEntry.CONTENT_ITEM_TYPE;
            case GENRE :
                return MovieContract.GenreEntry.CONTENT_ITEM_TYPE;
            case MOVIE_WITH_STATUS:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_WITH_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case MOVIE_WITH_FAVORITESTATUS:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE: {

                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRAILAR: {
                long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.TrailerEntry.buildTrailerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case GENRE: {
                long _id = db.insert(MovieContract.GenreEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.GenreEntry.buildGenreUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEW: {
                long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.ReviewEntry.buildReviewUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;


            }
            case MOVIE_WITH_STATUS: {

                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes deletegg all rows return the number of rows deleted
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case MOVIE:
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_ID:
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case TRAILAR:
                rowsDeleted = db.delete(
                        MovieContract.TrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case GENRE:
                rowsDeleted = db.delete(
                        MovieContract.GenreEntry.TABLE_NAME, null, null);
                break;
            case REVIEW:
                rowsDeleted = db.delete(
                        MovieContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_WITH_STATUS:
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;


        switch (match) {
            case MOVIE:

                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case MOVIE_ID:
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
            case MOVIE_WITH_STATUS:

                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case TRAILAR:
                rowsUpdated = db.update(MovieContract.TrailerEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case REVIEW:
                rowsUpdated = db.update(MovieContract.ReviewEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.

        SQLiteQueryBuilder moviequeryBuilder = new SQLiteQueryBuilder();
      //  moviequeryBuilder.setTables(MovieContract.MovieEntry.TABLE_NAME);

        Cursor retCursor;
        final int match = sUriMatcher.match(uri);



        switch (sUriMatcher.match(uri)) {
            case MOVIE: {


                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            }


            case MOVIE_ID: {

                final String _id = uri.getPathSegments().get(1);
                //moviequeryBuilder.appendWhere(MovieContract.MovieEntry._ID + "=" + uri.getPathSegments().get(1));

                /*retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry._ID + " = ?",
                        new String[]{_id},
                        null,
                        null,
                        sortOrder);*/
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;


            }



            case MOVIE_WITH_STATUS: {

                String MovieStatus = MovieContract.MovieEntry.getMovieStatusUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MOVIE_BY_STATUS,
                        new String[]{MovieStatus},
                        null,
                        null,
                        sortOrder);


                break;


            }

            case MOVIE_WITH_FAVORITESTATUS: {

                String MovieStatus = "1";
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MOVIE_BY_FAVORITESTATUS,
                        new String[]{MovieStatus},
                        null,
                        null,
                        sortOrder);


                break;


            }

            case MOVIE_WITH_ID: {
                String mMovieid = MovieContract.MovieEntry.getMovieIdUri(uri);

                    retCursor = getMovieByReview(
                            uri, projection, sortOrder, mMovieid);



                break;


            }

            case MOVIETRAILER_WITH_ID: {
                String mMovieid = MovieContract.MovieEntry.getMovieIdUri(uri);
                String trailer = MovieContract.MovieEntry.getMovieTrailerFromUri(uri);

                retCursor = getMovieByTrailer(
                        uri, projection, sortOrder, mMovieid);



                break;


            }

            case TRAILAR: {
                // moviequeryBuilder.appendWhere(MovieContract.MovieEntry._ID + "=" + uri.getPathSegments().get(1));
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TrailerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;


            }


            case GENRE: {
                // moviequeryBuilder.appendWhere(MovieContract.MovieEntry._ID + "=" + uri.getPathSegments().get(1));
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.GenreEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;


            }
            case REVIEW: {
                // moviequeryBuilder.appendWhere(MovieContract.MovieEntry._ID + "=" + uri.getPathSegments().get(1));
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;


            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }




   /* @Override
    public int bulkInsert(Uri uri, ContentValues[] values)
    {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match)
        {
            case MOVIE:
                db.beginTransaction();
                int returncount = 0;
                try
                {
                    for(ContentValues value : values)
                    {
                        long _id = db.insertWithOnConflict(MovieContract.MovieEntry.TABLE_NAME, null, value,
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1)
                        {
                            returncount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return returncount;
            default:
                return super.bulkInsert(uri,values);
        }
    }*/

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values)
    {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match)
        {
            case MOVIE: {
                db.beginTransaction();
                int returncount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(MovieContract.MovieEntry.TABLE_NAME, null, value,
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1) {
                            returncount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returncount;
            }


            case REVIEW: {
                db.beginTransaction();
                int returncount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(MovieContract.ReviewEntry.TABLE_NAME, null, value,
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1) {
                            returncount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returncount;
            }

            case GENRE: {
                db.beginTransaction();
                int returncount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(MovieContract.GenreEntry.TABLE_NAME, null, value,
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1) {
                            returncount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returncount;
            }

            case TRAILAR: {
                db.beginTransaction();
                int returncount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(MovieContract.TrailerEntry.TABLE_NAME, null, value,
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1) {
                            returncount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returncount;
            }
            default:
                return super.bulkInsert(uri,values);
        }
    }



}
