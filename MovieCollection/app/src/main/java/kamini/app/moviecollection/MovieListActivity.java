package kamini.app.moviecollection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import kamini.app.moviecollection.models.MovieItem;
import kamini.app.moviecollection.models.TheMovieDBResult;
import retrofit2.Call;


public class MovieListActivity extends AppCompatActivity {
    public static final String LOG_TAG = MovieListActivity.class.getSimpleName();

    private static final String API_BASE_URL = "http://api.themoviedb.org/3/discover/";
    private Call<TheMovieDBResult> call;
    private TheMovieDBResult movieresult;
    private List<MovieItem> items;
    private MovieAdapter movieAdapter;
    private NevigationDrawerActivity navigationDrawerActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);


    }
    @Override protected void onStop() {
        super.onStop();
        // Unsubscribe
     //   call.cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
