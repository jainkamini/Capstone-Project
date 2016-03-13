package kamini.app.moviecollection.models;

import java.util.List;

/**
 * Created by Kamini on 3/12/2016.
 */
public class TheMovieDBResult {
    List<MovieItem> results;

    public List<MovieItem> getresults() {
        return results;
    }

    public void setresults(List<MovieItem> results) {
        this.results = results;
    }
}
