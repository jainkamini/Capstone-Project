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
Context context;

    public MovieAdapter(Context mcontext , Cursor mCursor){
        this.mCursor = mCursor;
        this.context=mcontext;

    }
   /* public MovieAdapter(List<MovieItem> items){
        this.items = items;
    }*/

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
      // MovieItem item = items.get(position);
        mCursor.moveToPosition(position);
        holder.txttitle.setText(mCursor.getString(MovieLoader.Query.original_title));
        holder.txtname.setText(mCursor.getString(MovieLoader.Query.title));
        holder.txtvotecount.setText(mCursor.getString(MovieLoader.Query.vote_count));
        holder.txtdate.setText(mCursor.getString(MovieLoader.Query.release_date));
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

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txttitle;
        public TextView txtname;
        public TextView txtvotecount;
        public ImageView imgposter;
        public TextView  txtdate;
        public TextView txtvoteavg;
        public ViewHolder(View v){
            super(v);
            txttitle = (TextView) v.findViewById(R.id.list_movie_title_textview);
            txtname = (TextView) v.findViewById(R.id.list_movie_name_textview);
            txtvotecount = (TextView) v.findViewById(R.id.list_movie_votecount_textview);
            imgposter = (ImageView) v.findViewById(R.id.list_movie_imageview);
            txtdate = (TextView) v.findViewById(R.id.list_movie_releasedate_textview);
            txtvoteavg = (TextView) v.findViewById(R.id.list_movierating_textview);

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

