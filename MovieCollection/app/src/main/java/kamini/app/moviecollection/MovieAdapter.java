package kamini.app.moviecollection;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kamini.app.moviecollection.models.MovieItem;

/**
 * Created by Kamini on 3/12/2016.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<MovieItem> items;

    public MovieAdapter(List<MovieItem> items){
        this.items = items;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
       MovieItem item = items.get(position);

        holder.title.setText(item.getTitle());
    }

    @Override public int getItemCount() {
        if (items == null){
            return -1;
        }
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ViewHolder(View v){
            super(v);
            title = (TextView) v.findViewById(R.id.title);
        }
    }

    public void swapList(List<MovieItem> items){
        this.items = items;
        notifyDataSetChanged();
    }

}

