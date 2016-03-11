package ivan.capstone.com.capstone;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import ivan.capstone.com.capstone.DataObjects.Serie;

public class MySeriesActivity extends AppCompatActivity implements MySeriesFragment.Callback{

    private Toolbar mToolbar;
    public boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "PFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myseries);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyApplication.getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        if (savedInstanceState == null) {
        }
        if (findViewById(R.id.fragment_detail_serie) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
            //this will get rid of an unnecessary shadow below the action bar for smaller screen devices like phones.
            // Then the action bar and Today item will appear to be on the same plane (as opposed to two different planes,
            // where one casts a shadow on the other).
            getSupportActionBar().setElevation(0f);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
    public void onItemSelected(Serie value, ImageView imageView) {
        if (mTwoPane) {
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

        }
    }
}
