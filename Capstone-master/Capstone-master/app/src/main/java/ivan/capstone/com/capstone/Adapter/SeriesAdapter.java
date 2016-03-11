package ivan.capstone.com.capstone.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ivan.capstone.com.capstone.DataObjects.Serie;
import ivan.capstone.com.capstone.R;

/**
 * Created by Ivan on 19/02/2016.
 */
public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.ViewHolder> {


    private List<Serie> series;
    private int layout;

    public SeriesAdapter(List<Serie> items,  OnItemClickListener listener, int layoutData)
    {
        series = items;
        this.externalListernetClick = listener;
        this.layout = layoutData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = series.get(position);
        holder.viewTitle.setText(series.get(position).getName());
        holder.viewReleaseDate.setText(series.get(position).getDateReleased());
        holder.viewNetwork.setText(series.get(position).getNetwork());

        if (layout == R.layout.item_list_myseries) {
            Glide.with(holder.itemView.getContext())
                    .load(holder.item.getPoster_url())
                    .thumbnail(0.1f)
                    .centerCrop()
                    .into(holder.viewMiniatura);

        } else {
            Glide.with(holder.itemView.getContext())
                    .load(holder.item.getImage_url())
                    .thumbnail(0.1f)
                    .centerCrop()
                    .into(holder.viewMiniatura);

        }


    }

    @Override
    public int getItemCount() {
        if (series != null) {
            return series.size() > 0 ? series.size() : 0;
        } else {
            return 0;
        }
    }


    private String getIdArticulo(int posicion) {
        if (posicion != RecyclerView.NO_POSITION) {
            return series.get(posicion).getId();
        } else {
            return null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
            public final TextView viewTitle;
            public final TextView viewReleaseDate;
            public final ImageView viewMiniatura;
            public final TextView viewNetwork;

        public Serie item;

        public ViewHolder(View view) {
            super(view);
            view.setClickable(true);
            viewTitle = (TextView) view.findViewById(R.id.name_serie);
            viewReleaseDate = (TextView) view.findViewById(R.id.date_serie);
            viewMiniatura = (ImageView) view.findViewById(R.id.image_serie);
            viewNetwork = (TextView) view.findViewById(R.id.network_serie);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            externalListernetClick.onClick(this, getAdapterPosition(), viewMiniatura);
        }
    }

    public interface OnItemClickListener {
        public void onClick(ViewHolder viewHolder, int position, ImageView imageView);
    }

    private OnItemClickListener externalListernetClick;

}
