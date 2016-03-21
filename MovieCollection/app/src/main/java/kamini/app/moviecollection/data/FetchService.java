package kamini.app.moviecollection.data;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;
import java.util.Vector;

import kamini.app.moviecollection.MovieAdapter;
import kamini.app.moviecollection.R;
import kamini.app.moviecollection.api.TheMovieDBService;
import kamini.app.moviecollection.models.MovieItem;
import kamini.app.moviecollection.models.TheMovieDBResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/*
 * Created by Kamini on 3/13/2016.
 */
public class FetchService extends IntentService

    {
        private static final String TAG = "FetchService";
    public static final String LOG_TAG = FetchService.class.getSimpleName();

        public static final String BROADCAST_ACTION_STATE_CHANGE
                = "kamini.app.moviecollection.data.intent.action.STATE_CHANGE";
        public static final String EXTRA_MOVIESELECTION=
                 "kamini.app.moviecollection.data.intent.extra.MOVIE_SELECTION";
        public static final String EXTRA_REFRESHING
                = "kamini.app.moviecollection.data.intent.extra.REFRESHING";
        public static final String BROADCAST_ACTION_NO_CONNECTIVITY
                = "kamini.app.moviecollection.data.intent.action.NO_CONNECTIVITY";
public String movieSelection;
        public String movieStatus;

        Call<TheMovieDBResult> call;
    public FetchService() {
        super(TAG);
    }
    protected void onHandleIntent(Intent intent) {
      //  new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, true));
      //  new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, true);
        Bundle bundle =intent.getExtras();
        if (bundle !=null)
        {
            movieSelection=  bundle.getString(EXTRA_MOVIESELECTION);
            if (movieSelection.equals("Popular"))
            {
                movieStatus="P";
                getData(this.getResources().getString(R.string.popular));

            }
            else if (movieSelection.equals("Toprated"))
            {
                movieStatus="T";
                getData(this.getResources().getString(R.string.toprated));
            }
            else if (movieSelection.equals("Upcoming"))
            {
                movieStatus="U";
                getData(this.getResources().getString(R.string.upcoming));
            }
            else if (movieSelection.equals("Nowplaying"))
            {
                movieStatus="N";
                getData(this.getResources().getString(R.string.nowplaying));
            }
            Log.d(LOG_TAG, "Movie Selection. " +movieSelection);
        }




    }

    public void getData(String movieSelectiontype)
    {

          final String API_BASE_URL = "http://api.themoviedb.org/3/discover/";

             TheMovieDBResult movieresult;
         List<MovieItem> items;
         MovieAdapter movieAdapter;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)

                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TheMovieDBService.TheMovieDBAPI theMovieDBAPI = retrofit.create(TheMovieDBService.TheMovieDBAPI.class);

         //       call =  theMovieDBAPI.getMovieResponse( "vote_average.desc", "b85cf4603ce5916a993dd400866808bc");
        call =  theMovieDBAPI.getMovieResponse(movieSelectiontype, "b85cf4603ce5916a993dd400866808bc");
        call.enqueue(new Callback<TheMovieDBResult>() {
            @Override
            public void onResponse(Response<TheMovieDBResult> response) {
                try {
                    TheMovieDBResult movieresult;
                    movieresult = response.body();
                    List<MovieItem> items;
                    items = movieresult.getresults();
                    // movieAdapter.swapList(items);
                    InsertData(items);

                    Log.e(LOG_TAG, "url:" + movieresult);
                    Log.e(LOG_TAG, "response = " + new Gson().toJson(movieresult));

                } catch (NullPointerException e) {
                    Toast toast = null;
                    if (response.code() == 401) {
                        toast = Toast.makeText(getApplication(), "Unauthenticated", Toast.LENGTH_SHORT);
                    } else if (response.code() >= 400) {
                        toast = Toast.makeText(getApplication(), "Client Error " + response.code()
                                + " " + response.message(), Toast.LENGTH_SHORT);
                    }
                    toast.show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("getMovie threw: ", t.getMessage());
            }
        });


    }


        public void InsertData(List<MovieItem> items)
        {
            int i;
           // Context mContext;
            Vector<ContentValues> values = new Vector <ContentValues> (items.size());
// delete old movie type which is not favorite so we don't build up an endless history
            getApplicationContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                    MovieContract.MovieEntry.COLUMN_MOVIE_STATUS + " = ?",
                    new String[]{ movieStatus});
            for (i=0 ;i<items.size();i++)
            {
                ContentValues movie_value = new ContentValues();
                movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, items.get(i).getId());
                movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, items.get(i).getOriginal_title());
                movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_DATE,items.get(i).getRelease_date());

                movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_NAME, items.get(i).getTitle());
                movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_DATE,items.get(i).getRelease_date());
                movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,items.get(i).getOverview());
                if (items.get(i).getPoster_path()==null) {

                    movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, this.getResources().getString(R.string.iamgenotavaliable));
                }
                    else
                    movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, items.get(i).getPoster_path());

                movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING,items.get(i).getVote_average());
                movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTECOUNT,items.get(i).getVote_count());
                movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_GENREIDS,items.get(i).getGenreIds().toString());
                movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_STATUS, movieStatus);
                values.add(movie_value);

                Log.d(LOG_TAG, "Movie Name. " +items.get(i).getTitle());
            }

            if (values.size()>0)
            {

                ContentValues[] cvValue = new ContentValues[values.size()];
                values.toArray(cvValue);
                 getApplicationContext().getContentResolver().bulkInsert(
                        MovieContract.MovieEntry.CONTENT_URI,cvValue);
                Log.d(LOG_TAG, "Sync Complete. " + values.size() + " Inserted");
            }
        }
}
