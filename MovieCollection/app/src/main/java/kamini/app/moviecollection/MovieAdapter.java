package kamini.app.moviecollection;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Kamini on 3/12/2016.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

  //  private List<MovieItem> items;

    private Cursor mCursor;
    final private Context context;
    final private MovieAdapterOnClickHandler mClickHandler;
    public MovieAdapter(Context mContext ,MovieAdapterOnClickHandler dh, Cursor mCursor){
        this.mCursor = mCursor;
        this.context=mContext;
        this.mClickHandler=dh;

    }
   /* public MovieAdapter(List<MovieItem> items){
        this.items = items;
    }*/

    public static interface MovieAdapterOnClickHandler {
        void onClick(Long movieId, ViewHolder vh);
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
      // MovieItem item = items.get(position);
        mCursor.moveToPosition(position);
       // holder.txttitle.setText("Original Title : " + mCursor.getString(MovieLoader.Query.original_title). + mCursor.getString(MovieLoader.Query.release_date));
        holder.txttitle.setText("Original Title : " + mCursor.getString(MovieLoader.Query.original_title));
        holder.txtname.setText(mCursor.getString(MovieLoader.Query.title));
        holder.txtvotecount.setText("Vote Count : "+mCursor.getString(MovieLoader.Query.vote_count));
       holder.txtdate.setText("Release Date : "+mCursor.getString(MovieLoader.Query.release_date));
        holder.txtvoteavg.setText(mCursor.getString(MovieLoader.Query.vote_average));

        Log.e(MovieListActivityFragment.LOG_TAG, "Position Item :" + mCursor.getString(MovieLoader.Query.original_title));

        Picasso.with(context).load("http://image.tmdb.org/t/p/w185" + mCursor.getString(MovieLoader.Query.poster_path)).into(holder.imgposter);

    }

    @Override
    public long getItemId(int position) {
        mCursor.moveToPosition(position);
        Log.e(MovieListActivityFragment.LOG_TAG, "Position Item id :" + (MovieLoader.Query._id));
        return mCursor.getLong(MovieLoader.Query._id);
    }
    @Override public int getItemCount() {
       /* if (mCursor == null){
            return -1;
        }*/
        return mCursor.getCount();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txttitle;
        public TextView txtname;
        public TextView txtvotecount;
        public ImageView imgposter;
        public TextView  txtdate;
        public TextView txtvoteavg;
        public ViewHolder(View view){
            super(view);
            txttitle = (TextView) view.findViewById(R.id.list_movie_title_textview);
            txtname = (TextView) view.findViewById(R.id.list_movie_name_textview);
            txtvotecount = (TextView) view.findViewById(R.id.list_movie_votecount_textview);
            imgposter = (ImageView) view.findViewById(R.id.list_movie_imageview);
            txtdate = (TextView) view.findViewById(R.id.list_movie_releasedate_textview);
            txtvoteavg = (TextView) view.findViewById(R.id.list_movierating_textview);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
           // int dateMovieIdIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            int dateMovieIdIndex = mCursor.getInt(MovieLoader.Query.movieid);
            mClickHandler.onClick(new Long(dateMovieIdIndex),this);
            Log.e(MovieListActivityFragment.LOG_TAG,"Movieuriadapter"+dateMovieIdIndex);
           // mICM.onClick(this);
        }
    }


    public Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        this.mCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

   /* public void swapList(Cursor mCursor){
        this.mCursor = mCursor;
        notifyDataSetChanged();
    }*/

}

