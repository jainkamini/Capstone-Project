package kamini.app.moviecollection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

;


public class MovieDetailActivity extends AppCompatActivity implements SimilarMovieFragment.Callback{
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

    @Override
    public void onItemSelected(Uri contentUri,Long mMovieId, MovieAdapter.ViewHolder vh) {
        Intent intent = new Intent(this, MovieDetailActivity.class)
                .setData(contentUri);
        intent.putExtra("MovieId", mMovieId);
        startActivity(intent);
        //  Log.e(LOG_TAG, "Mainactivity MovieId......" + mMovieId);
       /* if (mTwoPane) {
            Bundle args = new Bundle();
            if (value != null) {
                args.putParcelable("Serie", value);
            }
            DetailSerieFragment fragment = new DetailSerieFragment();
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .addSharedElement(imageView, getResources().getString(R.string.transition_photo))
                    .replace(R.id.fragment_detail_serie, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailSerieSearchedActivity.class);
            intent.putExtra("Serie", value);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, imageView, getResources().getString(R.string.transition_photo));
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }

        }*/
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

}