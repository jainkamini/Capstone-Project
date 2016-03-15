package kamini.app.moviecollection.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import kamini.app.moviecollection.data.MovieContract.MovieEntry;
import kamini.app.moviecollection.data.MovieContract.TrailerEntry;
import kamini.app.moviecollection.data.MovieContract.ReviewEntry;


/**
 * Manages a local database for movie data.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "movie.db";
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieEntry.COLUMN_MOVIE_ID +" INTEGER NOT NULL , " +
               MovieEntry.COLUMN_MOVIE_DATE +" TEXT   , " +
                MovieEntry.COLUMN_MOVIE_POSTER +" TEXT NOT NULL , " +
               MovieEntry.COLUMN_MOVIE_OVERVIEW +" TEXT NOT NULL , " +
               MovieEntry.COLUMN_MOVIE_RATING +" REAL NOT NULL , " +
                MovieEntry.COLUMN_MOVIE_TITLE +" TEXT NOT NULL ,  " +
                MovieEntry.COLUMN_MOVIE_NAME +" TEXT NOT NULL  , " +
                MovieEntry.COLUMN_MOVIE_GENREIDS +" TEXT NOT NULL , " +
                MovieEntry.COLUMN_MOVIE_VOTECOUNT +" INTEGER NOT NULL ,  " +
                MovieEntry.COLUMN_MOVIE_FAVORITESTATUS +" TEXT NOT NULL  " +


        " );";

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + MovieContract.TrailerEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                TrailerEntry.COLUMN_MOVIEID_KEY + " INTEGER NOT NULL, " +
                TrailerEntry.COLUMN_MOVIE_KEY + " TEXT NOT NULL, " +
               TrailerEntry.COLUMN_TRAILER_NMAE + " TEXT NOT NULL, " +



                " FOREIGN KEY (" + TrailerEntry.COLUMN_MOVIEID_KEY+ ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + ") " +

               ");";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                ReviewEntry.COLUMN_MOVIEID_KEY + " INTEGER NOT NULL, " +
                ReviewEntry.COLUMN_MOVIE_AUTHOR + " TEXT NOT NULL, " +
                ReviewEntry.COLUMN_MOVIE_CONTENT + " TEXT NOT NULL, " +
                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + TrailerEntry.COLUMN_MOVIEID_KEY+ ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + ") " +

                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
