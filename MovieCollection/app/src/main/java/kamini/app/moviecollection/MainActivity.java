package kamini.app.moviecollection;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements MovieListActivityFragment.Callback{
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    //trackid  UA-76024303-1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Uri contentUri = getIntent() != null ? getIntent().getData() : null;
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing NavigationView

        ((MyApplication)getApplication()).startTracking();





        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.


            if (savedInstanceState == null) {

                FragmentDetail fragment = new FragmentDetail();
                if (contentUri != null) {
                    Bundle args = new Bundle();
                    args.putParcelable(FragmentDetail.DETAIL_URI, contentUri);
                    fragment.setArguments(args);
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                        .commit();

                getSupportFragmentManager().beginTransaction()
                       .replace(R.id.movie_detail_container, new MovieDetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();



            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);


        }


    }

    public  void setFragment( String movieType,String movieStatus)
    {
        MovieListActivityFragment fragment = new MovieListActivityFragment();
        FragmentTransaction frTransaction = getSupportFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("movieselection", movieType);
        bundle.putString("moviestatus", movieStatus );
        fragment.setArguments(bundle);
        frTransaction.replace(R.id.fragment_fetchmovie, fragment);
        frTransaction.commit();
    }


    @Override
    public void onItemSelected(Uri contentUri,Long mMovieId, MovieAdapter.ViewHolder vh) {

        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(MovieDetailFragment.DETAIL_URI, contentUri);

            args.putLong(String.valueOf(MovieDetailFragment.MOVIE_ID),mMovieId);
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class)
                    .setData(contentUri);
            intent.putExtra("MovieId", mMovieId);
            startActivity(intent);
            Log.e(LOG_TAG, "Mainactivity MovieId......" + mMovieId);

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

    /**
     * Created by Kamini on 4/5/2016.
     */

}
