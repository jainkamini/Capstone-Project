package kamini.app.moviecollection.models;

import java.util.List;

/**
 * Created by Kamini on 3/12/2016.
 */
public class MovieItem {


    /*"poster_path",
            "adult",
            "overview",
            "release_date",
            "genre_ids",
            "id",
            "original_title",
            "original_language",
            "title",
            "backdrop_path",
            "popularity",
            "vote_count",
            "video",
            "vote_average"*/
    int id;
    String title;
    String poster_path;
    String overview;
    String release_date;
    String original_title;
    int vote_count;
   // String genre_ids;
    List<Integer> genre_ids;
Double vote_average;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

   /* public String getGenreIds() {
        return genre_ids;
    }

    public void setGenreIds(String genreIds) {
        this.genre_ids = genreIds;
    }*/

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getGenreIds() {
        return genre_ids;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genre_ids = genreIds;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }
}
