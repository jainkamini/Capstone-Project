package kamini.app.moviecollection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;


public class MovieDetailActivity extends AppCompatActivity implements SimilarMovieFragment.Callback{
    public static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    FragmentPagerAdapter adapterViewPager;
    public static final Bundle arguments = new Bundle();
    ViewPager mviewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * Instantiate a ViewPager and a PagerAdapter
         */
        mviewPager = (ViewPager) findViewById(R.id.view_pager);
        // if (viewPager != null) {
        // setupViewPager(mviewPager);
        // }
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        mviewPager.setAdapter(adapterViewPager);
        // adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        // vpPager.setAdapter(adapterViewPager);
        mviewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
               /* Toast.makeText(HomeActivity.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();*/
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mviewPager);


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            // arguments = new Bundle();

            arguments.putParcelable(MovieDetailFragment.DETAIL_URI, getIntent().getData());


            arguments.putLong(String.valueOf(MovieDetailFragment.MOVIE_ID), getIntent().getExtras().getLong("MovieId"));

           /* arguments.putParcelable(SimilarMovieFragment.DETAIL_URI, getIntent().getData());
            arguments.putString(SimilarMovieFragment.MOVIE_ID, getIntent().getExtras().toString());*/

            Log.e(LOG_TAG, "MovieDetailacivity id...:" + getIntent().getExtras().getLong("MovieId"));
            //  arguments.putBoolean(MovieDetailActivity.DETAIL_TRANSITION_ANIMATION, true);

          /*  MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_detail_movie, fragment)
                    .commit();*/

            // Being here means we are in animation mode
            //  supportPostponeEnterTransition();
        }


    }

    @Override
    public void onItemSelected(Uri contentUri,Long mMovieId, MovieAdapter.ViewHolder vh) {
        Intent intent = new Intent(this, MovieDetailActivity.class)
                .setData(contentUri);
        intent.putExtra("MovieId", mMovieId);
        startActivity(intent);
        Log.e(LOG_TAG, "Mainactivity MovieId......" + mMovieId);
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


   /* private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new MovieDetailFragment(), "MOVIE");
        adapter.addFragment(new MovieDetailFragment(), "SIMILAR MOVIES");


        viewPager.setAdapter(adapter);
    }*/
   /* static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {

            mFragments.add(fragment);
            mFragments.get(0).setArguments(arguments);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }*/

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }


        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment

                    MovieDetailFragment fragmentFirst = new MovieDetailFragment();

                    fragmentFirst.setArguments(arguments);
                    return fragmentFirst;



                case 1: // Fragment # 0 - This will show FirstFragment different title
                    SimilarMovieFragment similarMovieFragment = new SimilarMovieFragment();
                    similarMovieFragment.setArguments(arguments);
                    return similarMovieFragment;

                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Movie";
            } else {
                return "SIMILAR MOVIE";
            }
        }
    }
}
