package kamini.app.moviecollection.api;

import kamini.app.moviecollection.models.GenreResult;
import kamini.app.moviecollection.models.MovieReviewResult;
import kamini.app.moviecollection.models.MovieTrailerResult;
import kamini.app.moviecollection.models.TheMovieDBResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Kamini on 3/12/2016.
 */
public final class TheMovieDBService {





        public interface TheMovieDBAPI {


           /* @GET("movie")
            Call<TheMovieDBResult> getMovieResponse(@Query("sort_by") String sortKey,
                                                    @Query("api_key") String apiKey);*/

         //   @GET("upcoming")
            @GET("upcoming")
            Call<TheMovieDBResult> getMovieUpcomingResponse(
                                                    @Query("api_key") String apiKey);
            @GET("top_rated")
            Call<TheMovieDBResult> getMovieTopratedResponse(
                    @Query("api_key") String apiKey);
            @GET("popular")
            Call<TheMovieDBResult> getMoviePopularResponse(
                    @Query("api_key") String apiKey);

            @GET("now_playing")
            Call<TheMovieDBResult> getMovieNowplayingResponse(
                    @Query("api_key") String apiKey);
            @GET("similar")
            Call<TheMovieDBResult> getSimilarMovieResponse(
                                                    @Query("api_key") String apiKey);

            @GET("genre/movie/list")
            Call<GenreResult> getMovieGenreResponse(
                    @Query("api_key") String apiKey);

            @GET("reviews")
            Call<MovieReviewResult> getMovieReviewResponse(
                    @Query("api_key") String apiKey);

            @GET("videos")
            Call<MovieTrailerResult> getMovieVideoResponse(
                    @Query("api_key") String apiKey);


        }

}
