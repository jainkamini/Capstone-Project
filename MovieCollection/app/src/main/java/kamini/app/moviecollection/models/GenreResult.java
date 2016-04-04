package kamini.app.moviecollection.models;

import java.util.List;

/**
 * Created by Kamini on 4/4/2016.
 */
public class GenreResult {
    List<GenreItem> genres;

    public List<GenreItem> getresults() {
        return genres;
    }

    public void setresults(List<GenreItem> genres) {
        this.genres = genres;
    }
}
