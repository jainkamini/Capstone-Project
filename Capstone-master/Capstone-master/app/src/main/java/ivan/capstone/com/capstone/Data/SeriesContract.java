package ivan.capstone.com.capstone.Data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by Ivan on 07/03/2016.
 */
public class SeriesContract {

    public static final String CONTENT_AUTHORITY = "ivan.capstone.com.capstone";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_SERIES = "series";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /* Inner class that defines the table contents of the location table */
    public static final class SeriesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SERIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SERIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SERIES;

        // Table name
        public static final String TABLE_NAME = "series";

        //fields
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_NETWORK = "network";
        public static final String COLUMN_REALSED_DATE = "date";
        public static final String COLUMN_VOTES = "votes";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_BANNER_URL = "banner";
        public static final String COLUMN_POSTER_URL = "poster";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_GENRE = "genre";

        public static Uri buildSeriesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getIDFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }





}
