package ivan.capstone.com.capstone.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ivan.capstone.com.capstone.Data.SeriesContract.SeriesEntry;

/**
 * Created by Ivan on 07/03/2016.
 */
public class SeriesDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "series.db";

    public SeriesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_SERIES_TABLE = "CREATE TABLE " + SeriesEntry.TABLE_NAME + " (" +
                SeriesEntry._ID + " INTEGER PRIMARY KEY," +
                SeriesEntry.COLUMN_ID + " TEXT UNIQUE NOT NULL, " +
                SeriesEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                SeriesEntry.COLUMN_RATING + " TEXT NOT NULL, " +
                SeriesEntry.COLUMN_VOTES + " TEXT NOT NULL, " +
                SeriesEntry.COLUMN_BANNER_URL + " TEXT NOT NULL, " +
                SeriesEntry.COLUMN_POSTER_URL+ " TEXT NOT NULL, " +
                SeriesEntry.COLUMN_REALSED_DATE + " TEXT NOT NULL, " +
                SeriesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                SeriesEntry.COLUMN_GENRE + " TEXT NOT NULL, " +
                SeriesEntry.COLUMN_NETWORK + " TEXT NOT NULL " +

                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_SERIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SeriesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
