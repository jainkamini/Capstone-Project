package kamini.app.moviecollection.models;

import java.util.List;

/**
 * Created by Kamini on 3/24/2016.
 */
public class MovieReviewResult {
    List<MovieReviewItem> results;

    public List<MovieReviewItem> getresults() {
        return results;
    }

    public void setresults(List<MovieReviewItem> results) {
        this.results = results;
    }
}
