package kamini.app.moviecollection.data;

/**
 * Created by Kamini on 3/13/2016.
 */
public class FetchService{ /*extends IntentService  {
    public static final String LOG_TAG = FetchService.class.getSimpleName();
    public FetchService() {
        super(LOG_TAG);
    }
    protected void onHandleIntent(Intent intent) {

    }

    public void getData()
    {

          final String API_BASE_URL = "http://api.themoviedb.org/3/discover/";
         Call<TheMovieDBResult> call;
             TheMovieDBResult movieresult;
         List<MovieItem> items;
         MovieAdapter movieAdapter;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)

                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TheMovieDBService.TheMovieDBAPI theMovieDBAPI = retrofit.create(TheMovieDBService.TheMovieDBAPI.class);
        call =  theMovieDBAPI.getMovieResponse("popularity.desc", "b85cf4603ce5916a993dd400866808bc");
        call.enqueue(new Callback<TheMovieDBResult>() {
            @Override
            public void onResponse(Response<TheMovieDBResult> response) {
                try {
                    TheMovieDBResult movieresult;
                    movieresult = response.body();
                    List<MovieItem> items;
                    items = movieresult.getresults();
                    movieAdapter.swapList(items);
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
                Log.e("getQuestions threw: ", t.getMessage());
            }
        });

    }*/
}
