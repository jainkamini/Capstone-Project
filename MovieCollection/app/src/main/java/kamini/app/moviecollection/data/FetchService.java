package kamini.app.moviecollection.data;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;
import java.util.Vector;

import kamini.app.moviecollection.MovieAdapter;
import kamini.app.moviecollection.R;
import kamini.app.moviecollection.api.TheMovieDBService;
import kamini.app.moviecollection.models.GenreItem;
import kamini.app.moviecollection.models.GenreResult;
import kamini.app.moviecollection.models.MovieItem;
import kamini.app.moviecollection.models.MovieReviewItem;
import kamini.app.moviecollection.models.MovieReviewResult;
import kamini.app.moviecollection.models.MovieTrailerItem;
import kamini.app.moviecollection.models.MovieTrailerResult;
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
        public static final String EXTRA_MOVIE_ID=
                "kamini.app.moviecollection.data.intent.extra.MOVIED_ID";
public String movieSelection;
        public String mMovieId;

        public String movieStatus;

        Call<TheMovieDBResult> call;
        Call<MovieReviewResult> callReview;
        Call<MovieTrailerResult> callTrailer;
        Call<GenreResult> callGenre;
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
            mMovieId=bundle.getString(EXTRA_MOVIE_ID);

            if (movieSelection.equals("Popular"))
            {
                movieStatus="P";
                getMovieGenreData();
                getMovieData(this.getResources().getString(R.string.popular));

            }
            else if (movieSelection.equals("Toprated"))
            {
                movieStatus="T";
                getMovieGenreData();
                getMovieData(this.getResources().getString(R.string.toprated));
            }
            else if (movieSelection.equals("Upcoming"))
            {
                movieStatus="U";
                getMovieGenreData();
                getMovieData(this.getResources().getString(R.string.upcoming));
            }
            else if (movieSelection.equals("Nowplaying"))
            {
                movieStatus="N";
                getMovieGenreData();
                getMovieData(this.getResources().getString(R.string.nowplaying));
            }
            else if (movieSelection.equals("Similar"))
            {
                movieStatus="S";

                getSimilarMovieData(this.getResources().getString(R.string.similar));
                Log.d(LOG_TAG, "Movie ID on Fetch . " + mMovieId);
            }

            else if (movieSelection.equals("Detail"))
            {

               getMovieReviewData();
                getMovieTrailerData();
                Log.d(LOG_TAG, "Movie ID on Fetch . " + mMovieId);
            }
            Log.d(LOG_TAG, "Movie Selection. " +movieSelection);
        }




    }

    public void getSimilarMovieData(String movieSelectiontype)
    {

          //final String API_BASE_URL = "http://api.themoviedb.org/3/movie/"+mMovieId+"/similar/";
        final String API_BASE_URL = "  http://api.themoviedb.org/3/movie/"+mMovieId+"/";

             TheMovieDBResult movieresult;
         List<MovieItem> items;
         MovieAdapter movieAdapter;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)

                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TheMovieDBService.TheMovieDBAPI theMovieDBAPI = retrofit.create(TheMovieDBService.TheMovieDBAPI.class);


        call =  theMovieDBAPI.getSimilarMovieResponse("b85cf4603ce5916a993dd400866808bc");
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




        public void getMovieReviewData()
        {

            //final String API_BASE_URL = "http://api.themoviedb.org/3/movie/"+mMovieId+"/similar/";
            final String API_BASE_URL = "  http://api.themoviedb.org/3/movie/"+mMovieId+"/";

            MovieReviewResult movieresult;
            List<MovieReviewItem> items;


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)

                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            TheMovieDBService.TheMovieDBAPI theMovieDBAPI = retrofit.create(TheMovieDBService.TheMovieDBAPI.class);
callReview =theMovieDBAPI.getMovieReviewResponse("b85cf4603ce5916a993dd400866808bc");

           // call =  theMovieDBAPI.getMovieReviewResponse("b85cf4603ce5916a993dd400866808bc");
            callReview.enqueue(new Callback<MovieReviewResult>() {
                @Override
                public void onResponse(Response<MovieReviewResult> response) {
                    try {
                        MovieReviewResult movieresult;
                        movieresult = response.body();
                        List<MovieReviewItem> items;
                        items = movieresult.getresults();
                        // movieAdapter.swapList(items);
                        InsertReviewData(items);

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
        public void getMovieTrailerData()
        {

            //final String API_BASE_URL = "http://api.themoviedb.org/3/movie/"+mMovieId+"/similar/";
            final String API_BASE_URL = "  http://api.themoviedb.org/3/movie/"+mMovieId+"/";

            MovieTrailerResult movieresult;
            List<MovieTrailerResult> items;


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)

                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            TheMovieDBService.TheMovieDBAPI theMovieDBAPI = retrofit.create(TheMovieDBService.TheMovieDBAPI.class);


            callTrailer =  theMovieDBAPI.getMovieVideoResponse("b85cf4603ce5916a993dd400866808bc");
            callTrailer.enqueue(new Callback<MovieTrailerResult>() {
                @Override
                public void onResponse(Response<MovieTrailerResult> response) {
                    try {
                        MovieTrailerResult movieresult;
                        movieresult = response.body();
                        List<MovieTrailerItem> items;
                        items = movieresult.getresults();
                        // movieAdapter.swapList(items);
                        InsertTrailerData(items);

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

        public void getMovieGenreData()
        {

            //final String API_BASE_URL = "http://api.themoviedb.org/3/movie/"+mMovieId+"/similar/";
            final String API_BASE_URL = "  http://api.themoviedb.org/3/";

            GenreResult movieresult;
            List<GenreResult> items;


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)

                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            TheMovieDBService.TheMovieDBAPI theMovieDBAPI = retrofit.create(TheMovieDBService.TheMovieDBAPI.class);


            callGenre =  theMovieDBAPI.getMovieGenreResponse("b85cf4603ce5916a993dd400866808bc");
            callGenre.enqueue(new Callback<GenreResult>() {
                @Override
                public void onResponse(Response<GenreResult> response) {
                    try {
                        GenreResult movieresult;
                        movieresult = response.body();
                        List<GenreItem> items;
                        items = movieresult.getresults();
                        // movieAdapter.swapList(items);
                        InsertGenreData(items);

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

        public String getGenreName(List<Integer> genreid) {
            String mGenreName = "";


            Cursor cursorgenre = getApplicationContext().getContentResolver().query(MovieContract.GenreEntry.CONTENT_URI, null,
                    null,
                    null, null);
            int listcount = genreid.size();
            if (cursorgenre.getCount() != 0) {
                for (int k = 0; k < cursorgenre.getCount(); k++) {
                    cursorgenre.moveToPosition(k);
                    for (int j = 0; j < genreid.size(); j++) {

                        if (genreid.get(j).equals(cursorgenre.getInt(1))) {
                            mGenreName = mGenreName + cursorgenre.getString(2);
                            mGenreName = mGenreName + "/";
                        }
                    }
                }
            }
            if (mGenreName.contains("/"))
            {
                return mGenreName;
               // return mGenreName.replace(mGenreName.substring(mGenreName.length() - 1), " ");
            }
            else
                return mGenreName;
        }
        public void getMovieData(String movieSelectiontype)
        {

           // final String API_BASE_URL = "http://api.themoviedb.org/3/movie/"+movieSelectiontype+"/";

            final String API_BASE_URL = "http://api.themoviedb.org/3/movie/";
            TheMovieDBResult movieresult;
            List<MovieItem> items;
            MovieAdapter movieAdapter;

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)

                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            TheMovieDBService.TheMovieDBAPI theMovieDBAPI = retrofit.create(TheMovieDBService.TheMovieDBAPI.class);


            if (movieSelectiontype.equals(this.getResources().getString(R.string.popular)) )

            {
                call =  theMovieDBAPI.getMoviePopularResponse("b85cf4603ce5916a993dd400866808bc");
            }
            else if (movieSelectiontype.equals(this.getResources().getString(R.string.toprated)) )
            {call =  theMovieDBAPI.getMovieTopratedResponse( "b85cf4603ce5916a993dd400866808bc");


            }
            else if (movieSelectiontype.equals(this.getResources().getString(R.string.nowplaying)) ) {
        call =  theMovieDBAPI.getMovieNowplayingResponse( "b85cf4603ce5916a993dd400866808bc");

            }
          else
                call =  theMovieDBAPI.getMovieUpcomingResponse( "b85cf4603ce5916a993dd400866808bc");

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
            /*getApplicationContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                    MovieContract.MovieEntry.COLUMN_MOVIE_STATUS + " = ?"  ,
                    new String[]{ movieStatus});*/
            getApplicationContext().getContentResolver().delete(MovieContract.MovieEntry.buildMovieUri(0),
                    MovieContract.MovieEntry.COLUMN_MOVIE_STATUS + " = "+"'" + movieStatus + "'"+" AND " +
                            MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITESTATUS + " = " + 0,null);
            for (i=0 ;i<items.size();i++) {



                Cursor cursor = getApplicationContext().getContentResolver().query(MovieContract.MovieEntry.buildMovieUri(items.get(i).getId()), null,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " + items.get(i).getId() +
                                " AND " +
                                MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITESTATUS + " = " + 0,
                        null, null);
                if (cursor.getCount() == 0) {
                   /* getApplicationContext().getContentResolver().delete(MovieContract.MovieEntry.buildMovieUri(items.get(i).getId()),
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " + mMovieId + " AND " +
                                    MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITESTATUS + " = " + 0,
                            null);*/

                    ContentValues movie_value = new ContentValues();
                    movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, items.get(i).getId());
                    movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, items.get(i).getOriginal_title());
                    movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_DATE, items.get(i).getRelease_date());

                    movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_NAME, items.get(i).getTitle());
                    movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_DATE, items.get(i).getRelease_date());

                    if (items.get(i).getOverview() == null) {
                        movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, "No Overview");
                    } else
                        movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, items.get(i).getOverview());
                    if (items.get(i).getBackdrop_path() == null) {
                        movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, this.getResources().getString(R.string.iamgenotavaliable));
                    } else {
                        movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, items.get(i).getBackdrop_path());
                    }
                    if (items.get(i).getPoster_path() == null) {

                        movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, this.getResources().getString(R.string.iamgenotavaliable));
                    } else
                        movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, items.get(i).getPoster_path());

                    movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, items.get(i).getVote_average());
                    movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTECOUNT, items.get(i).getVote_count());
                 String mGenreName=   getGenreName(items.get(i).getGenreIds());

                   // movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_GENREIDS, items.get(i).getGenreIds().toString());
                    movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_GENREIDS, mGenreName);
                    movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_STATUS, movieStatus);
                    movie_value.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITESTATUS, 0);
                    values.add(movie_value);

                    Log.d(LOG_TAG, "Movie Name. " + items.get(i).getTitle());
                    Log.d(LOG_TAG, "Genre List. " + items.get(i).getGenreIds().toString());
                }
            }
                if (values.size() > 0) {

                    ContentValues[] cvValue = new ContentValues[values.size()];
                    values.toArray(cvValue);
                    getApplicationContext().getContentResolver().bulkInsert(
                            MovieContract.MovieEntry.CONTENT_URI, cvValue);
                    Log.d(LOG_TAG, "Sync Complete. " + values.size() + " Inserted");

            }
        }
        public void InsertGenreData(List<GenreItem> items)
        {



            int i;
            // Context mContext;
            Vector<ContentValues> values = new Vector <ContentValues> (items.size());
// delete old movie type which is not favorite so we don't build up an endless history

            getApplicationContext().getContentResolver().delete(MovieContract.GenreEntry.CONTENT_URI,
                   null  ,
                    null);

            for (i=0 ;i<items.size(); i++) {
                ContentValues movie_value = new ContentValues();
                movie_value.put(MovieContract.GenreEntry.COLUMN_GENRE_ID, items.get(i).getId());
                movie_value.put(MovieContract.GenreEntry.COLUMN_GENRE_NAME, items.get(i).getName());


                values.add(movie_value);


            }

            if (values.size() > 0) {

                ContentValues[] cvValue = new ContentValues[values.size()];
                values.toArray(cvValue);
                getApplicationContext().getContentResolver().bulkInsert(
                        MovieContract.GenreEntry.CONTENT_URI,cvValue);
               // Log.d(LOG_TAG, "Sync Review Complete. " + values.size() + " Inserted");
            }
        }



        public void InsertReviewData(List<MovieReviewItem> items)
        {



            int i;
            // Context mContext;
            Vector<ContentValues> values = new Vector <ContentValues> (items.size());
// delete old movie type which is not favorite so we don't build up an endless history
            getApplicationContext().getContentResolver().delete(MovieContract.ReviewEntry.CONTENT_URI,
                    MovieContract.ReviewEntry.COLUMN_MOVIEID_KEY + " = ?",
                    new String[]{ mMovieId});
            for (i=0 ;i<items.size();i++)
            {
                ContentValues movie_value = new ContentValues();
                movie_value.put(MovieContract.ReviewEntry.COLUMN_MOVIEID_KEY, mMovieId);
                movie_value.put(MovieContract.ReviewEntry.COLUMN_REVIEW_AUTHOR, items.get(i).getAuthor());
                if(items.get(i).getAuthor()==null) {
                    movie_value.put(MovieContract.ReviewEntry.COLUMN_REVIEW_CONTENT, items.get(i).getContent().replace("\r\n","  "));
                }
                else

                    movie_value.put(MovieContract.ReviewEntry.COLUMN_REVIEW_CONTENT, items.get(i).getContent().replace("\r\n","  ")+" -------- "
                            +items.get(i).getAuthor());

                movie_value.put(MovieContract.ReviewEntry.COLUMN_REVIEW_ID, items.get(i).getId());
                movie_value.put(MovieContract.ReviewEntry.COLUMN_REVIEW_URL, items.get(i).getUrl());

                values.add(movie_value);

                Log.d(LOG_TAG, "review content . " + items.get(i).getContent());
            }

            if (values.size()>0)
            {

                ContentValues[] cvValue = new ContentValues[values.size()];
                values.toArray(cvValue);
                getApplicationContext().getContentResolver().bulkInsert(
                        MovieContract.ReviewEntry.CONTENT_URI,cvValue);
                Log.d(LOG_TAG, "Sync Review Complete. " + values.size() + " Inserted");
            }
        }
        public void InsertTrailerData(List<MovieTrailerItem> items)
        {
            int i;
            // Context mContext;
            Vector<ContentValues> values = new Vector <ContentValues> (items.size());
// delete old movie type which is not favorite so we don't build up an endless history
            getApplicationContext().getContentResolver().delete(MovieContract.TrailerEntry.CONTENT_URI,
                    MovieContract.TrailerEntry.COLUMN_MOVIEID_KEY + " = ?",
                    new String[]{ mMovieId});
            for (i=0 ;i<items.size();i++)
            {
                ContentValues movie_value = new ContentValues();
                movie_value.put(MovieContract.TrailerEntry.COLUMN_MOVIEID_KEY, mMovieId);
                movie_value.put(MovieContract.TrailerEntry.COLUMN_TRAILER_KEY, items.get(i).getKey());
                movie_value.put(MovieContract.TrailerEntry.COLUMN_TRAILER_ID, items.get(i).getId());
                movie_value.put(MovieContract.TrailerEntry.COLUMN_TRAILER_NMAE, items.get(i).getName());

                values.add(movie_value);

                Log.d(LOG_TAG, "Movie Name. " +items.get(i).getName());
            }

            if (values.size()>0)
            {

                ContentValues[] cvValue = new ContentValues[values.size()];
                values.toArray(cvValue);
                getApplicationContext().getContentResolver().bulkInsert(
                        MovieContract.TrailerEntry.CONTENT_URI,cvValue);
                Log.d(LOG_TAG, "Sync Trailer Complete. " + values.size() + " Inserted");
            }
        }
}
