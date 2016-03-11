package ivan.capstone.com.capstone.XML;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ivan.capstone.com.capstone.DataObjects.Serie;
import ivan.capstone.com.capstone.R;

/**
 * Created by Ivan on 25/02/2016.
 */
public class XMLManager {



    private static final String URL = "http://thetvdb.com//banners/";
    private static final String TAG_SERIES = "Series";
    private static final String TAG_SERIE_ID = "seriesid";
    private static final String TAG_SERIE_NAME = "SeriesName";
    private static final String TAG_SERIE_NETWORK = "Network";
    private static final String TAG_SERIE_BANNER_URL = "banner";
    private static final String TAG_SERIE_RELEASE_DATE = "FirstAired";
    private static final String TAG_SERIE_RATING = "Rating";
    private static final String TAG_SERIE_VOTES = "RatingCount";
    private static final String TAG_SERIE_POSTER_URL = "poster";
    private static final String TAG_SERIE_GENRE = "Genre";
    private static final String TAG_SERIE_OVERVIEW = "Overview";
    private static final String TAG_ID = "id";
    // This method manage the series in XML format that are brought with the query by name
    public static List<Serie> GetSeriesFromXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        ArrayList<Serie> series = null;
        int eventType = parser.getEventType();
        Serie currentSerie = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    series = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals(TAG_SERIES) ){
                        currentSerie = new Serie();
                    } else if (currentSerie != null){
                        if (name.equals(TAG_SERIE_ID)){
                            currentSerie.setId(parser.nextText());
                        } else if (name.equals(TAG_SERIE_NAME)){
                            currentSerie.setName(parser.nextText());
                        } else if (name.equals(TAG_SERIE_NETWORK)){
                            currentSerie.setNetwork(parser.nextText());
                        } else if (name.equals(TAG_SERIE_BANNER_URL)){
                            currentSerie.setImage_url(URL + parser.nextText());
                        } else if (name.equals(TAG_SERIE_RELEASE_DATE)){
                            currentSerie.setDateReleased(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase(TAG_SERIES) && currentSerie != null){
                        series.add(currentSerie);
                    }
                    break;
            }
            eventType = parser.next();
        }
        return series;
    }

    public static Serie GetSerieFromXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        int eventType = parser.getEventType();
        Serie serie = new Serie();
        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals(TAG_ID)){
                        serie.setId(parser.nextText());
                    } else if (name.equals(TAG_SERIE_NAME)){
                        serie.setName(parser.nextText());
                    } else if (name.equals(TAG_SERIE_NETWORK)){
                        serie.setNetwork(parser.nextText());
                    } else if (name.equals(TAG_SERIE_BANNER_URL)){
                        String banner = parser.nextText();
                        if (!banner.equals((""))) serie.setImage_url(URL + banner);
                    } else if (name.equals(TAG_SERIE_RELEASE_DATE)){
                        serie.setDateReleased(parser.nextText());
                    }
                    else if (name.equals(TAG_SERIE_OVERVIEW)){
                        serie.setOverView(parser.nextText());
                    }
                    else if (name.equals(TAG_SERIE_GENRE)){
                        serie.setGenre(parser.nextText());
                    }
                    else if (name.equals(TAG_SERIE_POSTER_URL)){
                        String poster = parser.nextText();
                        if (!poster.equals((""))) serie.setPoster_url(URL + poster);
                    }
                    else if (name.equals(TAG_SERIE_RATING)){
                        serie.setRating(parser.nextText());
                    }
                    else if (name.equals(TAG_SERIE_VOTES)){
                        serie.setVotes(parser.nextText());
                    }
                    break;
            }
            eventType = parser.next();
        }
        return serie;
    }

    private static final String TAG_BANNER = "Banner";
    private static final String TAG_BANNER_TYPE = "BannerType";
    private static final String TAG_BANNER_PATH = "BannerPath";
    private static final String BANNER_POSTER = "poster";

    // This method manage the series in XML format that are brought with the query by name
    public static String GetPosrterFromXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        int eventType = parser.getEventType();
        String path = "";
        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals(TAG_BANNER_PATH) ){
                        path = parser.nextText();
                    } else if (name.equals(TAG_BANNER_TYPE)){
                        if (parser.nextText().equals(BANNER_POSTER)) return path;
                    }
                    break;
            }
            eventType = parser.next();
        }
        return path;
    }

}
