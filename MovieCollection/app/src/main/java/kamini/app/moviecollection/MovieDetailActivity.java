package kamini.app.moviecollection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

;


public class MovieDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = new Bundle();
            arguments.putParcelable(FragmentDetail.DETAIL_URI, getIntent().getData());

            arguments.putLong(String.valueOf(FragmentDetail.MOVIE_ID), getIntent().getExtras().getLong("MovieId"));

            FragmentDetail fragment = new FragmentDetail();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();

            // Being here means we are in animation mode
       //     supportPostponeEnterTransition();
        }
    }


}
