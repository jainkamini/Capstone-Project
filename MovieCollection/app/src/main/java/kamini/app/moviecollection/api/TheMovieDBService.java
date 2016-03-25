package kamini.app.moviecollection.api;

import kamini.app.moviecollection.models.TheMovieDBResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Kamini on 3/12/2016.
 */
public final class TheMovieDBService {





        public interface TheMovieDBAPI {


            @GET("movie")
            Call<TheMovieDBResult> getMovieResponse(@Query("sort_by") String sortKey,
                                                    @Query("api_key") String apiKey);
            @GET("similar")
            Call<TheMovieDBResult> getSimilarMovieResponse(
                                                    @Query("api_key") String apiKey);


        }

}
