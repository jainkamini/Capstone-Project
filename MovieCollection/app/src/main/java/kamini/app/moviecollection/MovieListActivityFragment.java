package kamini.app.moviecollection;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import kamini.app.moviecollection.api.TheMovieDBService;
import kamini.app.moviecollection.models.MovieItem;
import kamini.app.moviecollection.models.TheMovieDBResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieListActivityFragment extends Fragment {

    public static final String LOG_TAG = MovieListActivityFragment.class.getSimpleName();

    private static final String API_BASE_URL = "http://api.themoviedb.org/3/discover/";
    private Call<TheMovieDBResult> call;
    private TheMovieDBResult movieresult;
    private List<MovieItem> items;
    private MovieAdapter movieAdapter;


    public MovieListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)

                .addConverterFactory(GsonConverterFactory.create())
                .build();

        movieAdapter = new MovieAdapter(items);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.list_movie_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(movieAdapter);

        TheMovieDBService.TheMovieDBAPI theMovieDBAPI = retrofit.create(TheMovieDBService.TheMovieDBAPI.class);

        call =  theMovieDBAPI.getMovieResponse("popularity.desc", "b85cf4603ce5916a993dd400866808bc");
        call.enqueue(new Callback<TheMovieDBResult>() {
            @Override
            public void onResponse(Response<TheMovieDBResult> response) {
                try {
                    movieresult = response.body();
                    items = movieresult.getresults();
                    movieAdapter.swapList(items);
                    Log.e(LOG_TAG, "url:" + movieresult);
                    Log.e(LOG_TAG, "response = " + new Gson().toJson(movieresult));
                } catch (NullPointerException e) {
                    Toast toast = null;
                    if (response.code() == 401) {
                        toast = Toast.makeText(getActivity(), "Unauthenticated", Toast.LENGTH_SHORT);
                    } else if (response.code() >= 400) {
                        toast = Toast.makeText(getActivity(), "Client Error " + response.code()
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

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        call.cancel();
    }
}
