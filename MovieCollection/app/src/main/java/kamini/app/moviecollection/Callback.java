package kamini.app.moviecollection;

import android.net.Uri;

/**
 * Created by Kamini on 4/8/2016.
 */
public interface Callback {

         //for fragment click event
        public void onItemSelected(Uri MovieUri,Long mMovieId, MovieAdapter.ViewHolder vh);


}
