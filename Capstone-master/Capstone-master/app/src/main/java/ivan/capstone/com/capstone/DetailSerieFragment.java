package ivan.capstone.com.capstone;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bluejamesbond.text.DocumentView;
import com.bluejamesbond.text.style.TextAlignment;


import com.bumptech.glide.Glide;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ivan.capstone.com.capstone.DataObjects.Serie;
import ivan.capstone.com.capstone.XML.XMLManager;


public class DetailSerieFragment extends Fragment implements View.OnClickListener {

    boolean mTwoPane;
    Serie serie;
    TextView genre_text;
    TextView rating_text;
    TextView network_text;
    TextView releasedDate_text;
    ImageView poster;
    ImageView banner;
    ImageButton share_button;
    ImageButton save_button;
    TextView save_text;
    ImageButton unsave_button;
    TextView unsave_text;
    LinearLayout layout_detail;


    public DetailSerieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null && serie != null && !serie.getName().equals("")) actionBar.setTitle(serie.getName());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) getActivity().postponeEnterTransition();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_detail_serie, container, false);
        if (savedInstanceState != null && savedInstanceState.getParcelable("Serie")!= null) {
            serie = savedInstanceState.getParcelable("Serie");
            LoadData();
        }
        else {
            Bundle arguments = getArguments();
            if (arguments != null) {
                serie = arguments.getParcelable("Serie");
                mTwoPane = true;
            }else {
                Intent intent = getActivity().getIntent();
                serie = intent.getParcelableExtra("Serie");
                mTwoPane = false;
            }
            FetchSerieByIDTask getSerie = new FetchSerieByIDTask();
            getSerie.execute();
        }

        genre_text = (TextView)rootView.findViewById(R.id.genre_serie);
        rating_text = (TextView)rootView.findViewById(R.id.rating_serie);
        network_text = (TextView)rootView.findViewById(R.id.network_serie);
        releasedDate_text = (TextView)rootView.findViewById(R.id.releasedDate_serie);
        poster = (ImageView) rootView.findViewById(R.id.poster);
        banner = (ImageView) rootView.findViewById(R.id.banner);
        layout_detail = (LinearLayout) rootView.findViewById(R.id.description_wrapper);
        share_button = (ImageButton)rootView.findViewById(R.id.share);
        share_button.setOnClickListener(this);
        save_button = (ImageButton)rootView.findViewById(R.id.iv_tick_off);
        save_button.setOnClickListener(this);
        save_text = (TextView)rootView.findViewById(R.id.save_serie);
        unsave_button = (ImageButton)rootView.findViewById(R.id.iv_tick_on);
        unsave_button.setOnClickListener(this);
        unsave_text = (TextView)rootView.findViewById(R.id.unsave_serie);
        return rootView;
    }

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                            getActivity().startPostponedEnterTransition();
                        }
                        return true;
                    }
                });
    }

    public void LoadData() {
        String date = serie.getDateReleased();
        if (date != null  && !date.equals("")) {
            String str[] = date.split("-");
            if (str != null && str.length > 0) releasedDate_text.setText(getResources().getString(R.string.year_serie)+ " " + str[0]);
        }
        addOverwiewView(new StringBuilder().append(serie.getOverView()));
        String genre = getResources().getString(R.string.genre_serie) + " " + serie.getGenre();
        genre_text.setText(genre);
        String rating = serie.getRating() + getResources().getString(R.string.rating_serie) + "  " + serie.getVotes()+ " "   + getResources().getString(R.string.votes_serie);
        rating_text.setText(rating);
        String network = getResources().getString(R.string.network_serie) + "  " + serie.getNetwork();
        network_text.setText(network);

        scheduleStartPostponedTransition(poster);

        if (serie.getPoster_url().equals("")) {
            Glide.with(getActivity())
                    .load(R.drawable.old_tv)
                    .thumbnail(0.1f)
                    .centerCrop()
                    .into(poster);
        }
        else {
            Glide.with(getActivity())
                    .load(serie.getPoster_url())
                    .thumbnail(0.1f)
                    .centerCrop()
                    .into(poster);
        }
        Glide.with(getActivity())
                .load(serie.getImage_url())
                .thumbnail(0.1f)
                .fitCenter()
                .into(banner);
        if (serie.IsSaved()) {
            save_button.setVisibility(View.GONE);
            save_text.setVisibility(View.GONE);
            unsave_button.setVisibility(View.VISIBLE);
            unsave_text.setVisibility(View.VISIBLE);
        }
    }

    // Custom view which is justified
    private void addOverwiewView(CharSequence article) {
        final DocumentView documentView = new DocumentView(getActivity(), DocumentView.PLAIN_TEXT);
        documentView.getDocumentLayoutParams().setTextColor(Color.BLACK);
        documentView.getDocumentLayoutParams().setTextTypeface(Typeface.DEFAULT);
        documentView.getDocumentLayoutParams().setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        documentView.getDocumentLayoutParams().setTextAlignment(TextAlignment.JUSTIFIED);
        float margin = getResources().getDimension(R.dimen.medium_margin);
        float margin_calculated = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin, getResources().getDisplayMetrics());
        documentView.getDocumentLayoutParams().setInsetPaddingLeft(margin_calculated);
        documentView.getDocumentLayoutParams().setInsetPaddingRight(margin_calculated);
        documentView.getDocumentLayoutParams().setInsetPaddingBottom(margin_calculated);
        documentView.getDocumentLayoutParams().setLineHeightMultiplier(2f);
        documentView.getDocumentLayoutParams().setReverse(true);
        documentView.setText(article);
        layout_detail.addView(documentView);
    }

    @Override
    public void onClick(View view) {
        ImageButton button = (ImageButton)view;
        //share button
        if (button.getId() == R.id.share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String title = getResources().getString(R.string.check_serie) + serie.getName();
            String shareBody = "https://trakt.tv/search/tvdb/" + serie.getId() + "?id_type=show";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, title + " " + shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        if (button.getId() == R.id.iv_tick_off) {
            save_button.setVisibility(View.GONE);
            save_text.setVisibility(View.GONE);
            unsave_button.setVisibility(View.VISIBLE);
            unsave_text.setVisibility(View.VISIBLE);
            if (serie != null && !serie.getId().equals("")) serie.Save();
        }
        if (button.getId() == R.id.iv_tick_on) {
            unsave_button.setVisibility(View.GONE);
            unsave_text.setVisibility(View.GONE);
            save_button.setVisibility(View.VISIBLE);
            save_text.setVisibility(View.VISIBLE);
            if (serie != null && !serie.getId().equals("")) serie.Delete();
        }
    }

    public class FetchSerieByIDTask extends AsyncTask<String, Void,  Serie> {

        private final String LOG_TAG = FetchSerieByIDTask.class.getSimpleName();

        @Override
        protected  Serie doInBackground(String... params) {
            try {
                Serie result = null;
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                try {
                    final String SERIES_BASE_URL = "http://thetvdb.com/api/31700C7EECC0878D/series/" + serie.getId();
                    Uri builtUri = Uri.parse(SERIES_BASE_URL).buildUpon()
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
                    result = XMLManager.GetSerieFromXML(parser);
                } catch ( Exception e){
                    Log.e(LOG_TAG, "Error:" + e.getMessage());
                    result = null;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e(LOG_TAG, "Error closing stream", e);
                            return null;
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
        protected void onPostExecute(Serie result) {
            if (result != null) {
                serie = result;
            }else {
                if (!MyApplication.isNetworkAvailable()) {
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(MyApplication.getContext(),MyApplication.getContext().getString(R.string.nonetwork_message) , duration).show();
                } else {
                    // the serie must exist so if we do not find it it is because the server is down or because our connection
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(MyApplication.getContext(),MyApplication.getContext().getString(R.string.noserver_message) , duration).show();
                }
            }
            LoadData();
        }
    }
}
