package kamini.app.moviecollection.models;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
