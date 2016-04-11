package kamini.app.moviecollection;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class NevigationDrawerFragment extends Fragment {
    public static final String LOG_TAG = NevigationDrawerFragment.class.getSimpleName();
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootView = inflater.inflate(R.layout.fragment_nevigation, container, false);
        toolbar = (Toolbar)rootView. findViewById(R.id.toolbar);
        ( (AppCompatActivity) getActivity()).   setSupportActionBar(toolbar);
      //  ( (AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Upcoming");
        // setSupportActionBar(toolbar);
        //Initializing NavigationView
        navigationView = (NavigationView)rootView. findViewById(R.id.navigation_view);
        setFragment("Upcoming","U");
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {




                    case R.id.poular:


                        setFragment("Popular","P");
                        return true;
                    case R.id.toprated:
                        setFragment("Toprated","T");
                        return true;
                    case R.id.upcoming:
                        setFragment("Upcoming","U");
                        return true;
                    case R.id.nowplaying:
                        setFragment("Nowplaying","N");
                        return true;
                    case R.id.favorite:
                        setFragment("Favorite","F");
                        return true;

                    default:
                        Toast.makeText(getContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });


        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) rootView.findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }
    public  void setFragment( String movieType,String movieStatus)
    {
        ( (AppCompatActivity) getActivity()).getSupportActionBar().setTitle(movieType);
        MovieListActivityFragment fragment = new MovieListActivityFragment();
        FragmentTransaction frTransaction = getChildFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("movieselection", movieType);
        bundle.putString("moviestatus", movieStatus );
        fragment.setArguments(bundle);
        frTransaction.replace(R.id.fragment_fetchmovie, fragment);
        frTransaction.commit();
    }


}