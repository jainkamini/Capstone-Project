package ivan.capstone.com.capstone;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ivan.capstone.com.capstone.Adapter.SeriesAdapter;
import ivan.capstone.com.capstone.DataObjects.Serie;
import ivan.capstone.com.capstone.XML.XMLManager;


public class SearchFragment extends Fragment implements SeriesAdapter.OnItemClickListener{

    EditText inputSearch;
    RecyclerView recyclerView;
    FetchSerieByNameTask serieByNameTask;
    SeriesAdapter seriesAdapter;
    List<Serie> series;

    public SearchFragment() {
        // Required empty public constructor
    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        void onItemSelected(Serie value, ImageView imageView);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.search_recycler);
        recyclerView.setNestedScrollingEnabled(false);

        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("ListSeries") != null) {
            series = savedInstanceState.getParcelableArrayList("ListSeries");
            seriesAdapter = new SeriesAdapter(series, this, R.layout.item_list_search);
            recyclerView.setAdapter(seriesAdapter);
            seriesAdapter.notifyDataSetChanged();
        } else {
            series= new ArrayList<Serie>();
            seriesAdapter = new SeriesAdapter(series, this, R.layout.item_list_search);
            recyclerView.setAdapter(seriesAdapter);
        }
        try {
            doSearch(rootView);
        }catch (Exception e)
        {
            Log.e("onCreateView", e.toString() + " " + e.getMessage());
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putParcelableArrayList("ListSeries", new ArrayList<Serie>(series));
        super.onSaveInstanceState(outState);
    }


    private void doSearch(View rootView) {
        inputSearch = (EditText)rootView.findViewById(R.id.searchListSeries);
        inputSearch.setOnEditorActionListener(
            new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        if (serieByNameTask != null) serieByNameTask.cancel(true);
                        if (!inputSearch.getText().toString().equals("")) {
                            serieByNameTask = new FetchSerieByNameTask();
                            serieByNameTask.execute(inputSearch.getText().toString());
                        } else {
                            series.clear();
                            seriesAdapter.notifyDataSetChanged();
                        }
                        return true; // consume.
                    }
                    return false;
                }
            });
    }

    @Override
    public void onClick(SeriesAdapter.ViewHolder viewHolder, int position, ImageView imageView ) {
        ((Callback) getActivity()).onItemSelected(series.get(position), imageView);
    }


    public class FetchSerieByNameTask extends AsyncTask<String, Void,  List<Serie>> {

        private final String LOG_TAG = FetchSerieByNameTask.class.getSimpleName();

        private String getDataFromArtist(Serie serie){
            return serie.getName();
        }

        @Override
        protected  List<Serie> doInBackground(String... params) {
            try {
                List<Serie> result= new ArrayList<Serie>();
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                try {
                    final String SERIES_BASE_URL = "http://thetvdb.com/api/GetSeries.php?";
                    final String QUERY_PARAM = "seriesname";
                    Uri builtUri = Uri.parse(SERIES_BASE_URL).buildUpon()
                            .appendQueryParameter(QUERY_PARAM,  params[0] )
                            .build();

                    URL url = new URL(builtUri.toString());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    InputStream inputStream = urlConnection.getInputStream();
                    if (inputStream == null) {
                        return null;
                    }
                    XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = pullParserFactory.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(inputStream, null);
                    result = XMLManager.GetSeriesFromXML(parser);
                } catch ( Exception e){
                    result = null;
                    Log.e(LOG_TAG, "Error:" + e.getMessage());
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e(LOG_TAG, "Error closing stream", e);
                        }
                    }
                }
                return result;
            }
            catch (Exception ex)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Serie> result) {
            if (result != null) {
                if (result.size() == 0) {
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(getActivity(),getString(R.string.noseries_message) , duration).show();
                } else {
                    series.clear();
                    series.addAll(result);
                    seriesAdapter.notifyDataSetChanged();
                }
            } else {
                series.clear();
                seriesAdapter.notifyDataSetChanged();
                if (!MyApplication.isNetworkAvailable()) {
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(MyApplication.getContext(),MyApplication.getContext().getString(R.string.nonetwork_message) , duration).show();
                } else {
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(MyApplication.getContext(),MyApplication.getContext().getString(R.string.noserver_message) , duration).show();
                }
            }
        }
    }

}
