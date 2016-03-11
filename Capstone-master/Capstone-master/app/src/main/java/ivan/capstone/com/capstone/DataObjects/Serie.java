package ivan.capstone.com.capstone.DataObjects;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import ivan.capstone.com.capstone.Data.SeriesContract;
import ivan.capstone.com.capstone.MyApplication;

/**
 * Created by Ivan on 19/02/2016.
 */
public class Serie implements Parcelable {

    private long _id; // databaseID

    private String id;  // server series id
    private String name;
    private String image_url;
    private String overView;
    private String dateReleased;
    private String network;
    private String rating;
    private String votes;
    private String genre;
    private String poster_url;

    public Serie(){
        _id = 0;
        id = "";
        name= "";
        image_url= "";
        overView= "";
        dateReleased= "";
        network= "";
        rating= "";
        votes= "";
        genre= "";
        poster_url= "";
    }

    protected Serie(Parcel in) {
        _id = in.readLong();
        id = in.readString();
        name = in.readString();
        image_url = in.readString();
        overView = in.readString();
        dateReleased = in.readString();
        network = in.readString();
        rating = in.readString();
        votes = in.readString();
        genre = in.readString();
        poster_url = in.readString();
    }

    public Serie(int _id, String id, String name, String image_url, String overView, String dateReleased, String network, String rating, String votes, String genre, String poster_url) {
        this._id = _id;
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.overView = overView;
        this.dateReleased = dateReleased;
        this.network = network;
        this.rating = rating;
        this.votes = votes;
        this.genre = genre;
        this.poster_url = poster_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(image_url);
        dest.writeString(overView);
        dest.writeString(dateReleased);
        dest.writeString(network);
        dest.writeString(rating);
        dest.writeString(votes);
        dest.writeString(genre);
        dest.writeString(poster_url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Serie> CREATOR = new Parcelable.Creator<Serie>() {
        @Override
        public Serie createFromParcel(Parcel in) {
            return new Serie(in);
        }

        @Override
        public Serie[] newArray(int size) {
            return new Serie[size];
        }
    };

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getDateReleased() {
        return dateReleased;
    }

    public void setDateReleased(String dateReleased) {
        this.dateReleased = dateReleased;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getRating() {
        return rating;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public boolean IsSaved() {
        if (id.equals("")) return false;
        Cursor seriesCursor = MyApplication.getContext().getContentResolver().query(
                SeriesContract.SeriesEntry.CONTENT_URI,
                new String[]{SeriesContract.SeriesEntry._ID},
                SeriesContract.SeriesEntry.COLUMN_ID + " = ?",
                new String[]{id},
                null);
        if (seriesCursor.moveToFirst()) return true;
        else return false;
    }

    public void Save() {
        if (!IsSaved()) {
            ContentValues locationValues = new ContentValues();
            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            locationValues.put(SeriesContract.SeriesEntry.COLUMN_ID, id);
            locationValues.put(SeriesContract.SeriesEntry.COLUMN_NAME, name);
            locationValues.put(SeriesContract.SeriesEntry.COLUMN_NETWORK, network);
            locationValues.put(SeriesContract.SeriesEntry.COLUMN_POSTER_URL, poster_url);
            locationValues.put(SeriesContract.SeriesEntry.COLUMN_BANNER_URL, image_url);
            locationValues.put(SeriesContract.SeriesEntry.COLUMN_OVERVIEW, overView);
            locationValues.put(SeriesContract.SeriesEntry.COLUMN_RATING, rating);
            locationValues.put(SeriesContract.SeriesEntry.COLUMN_VOTES, votes);
            locationValues.put(SeriesContract.SeriesEntry.COLUMN_REALSED_DATE, dateReleased);
            locationValues.put(SeriesContract.SeriesEntry.COLUMN_GENRE, genre);
            // Finally, insert serie data into the database.
            Uri insertedUri = MyApplication.getContext().getContentResolver().insert(
                    SeriesContract.SeriesEntry.CONTENT_URI,
                    locationValues
            );
            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            _id = ContentUris.parseId(insertedUri);
        }
    }

    public void Delete() {
        if (IsSaved()) {
            MyApplication.getContext().getContentResolver().delete(SeriesContract.SeriesEntry.CONTENT_URI, SeriesContract.SeriesEntry.COLUMN_ID +"=?", new String[]{id});
            _id = 0;
        }
    }
}
