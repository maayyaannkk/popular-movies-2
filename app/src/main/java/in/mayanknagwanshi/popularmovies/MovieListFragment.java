package in.mayanknagwanshi.popularmovies;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.mayanknagwanshi.popularmovies.data.MovieContract;
import in.mayanknagwanshi.popularmovies.lib.MovieAdapterNew;
import in.mayanknagwanshi.popularmovies.sync.MovieSyncAdapter;

/**
 * Created by MayankN on 10-03-2016.
 */
public class MovieListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private MovieAdapterNew movieAdapter;
    private String sort;
    ProgressDialog pd;

    public interface Callback {
        public void onItemSelected(Uri dateUri);
    }

    public MovieListFragment() {
        setHasOptionsMenu(false);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_movie_list, container, false);

        sort = getArguments().getString("sort");

        movieAdapter = new MovieAdapterNew(getActivity());
        rv.setAdapter(movieAdapter);

        return rv;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri movieWithSortUri = MovieContract.MovieEntry.buildMovieUriWithSort(sort);
        final String[] cols = new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE_PATH};
        return new CursorLoader(getActivity(), movieWithSortUri, cols, null, null, null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        Cursor c = getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, new String[]{MovieContract.MovieEntry._ID}, null, null, null);
        if (c.getCount() > 0) {
            if (data.moveToFirst()) {
                if(pd!=null && pd.isShowing()){
                    pd.hide();
                }
                movieAdapter.swapCursor(data);
                movieAdapter.setOnItemClickListener(new MovieAdapterNew.OnItemClickListener() {
                    @Override
                    public void onItemClicked(Cursor cursor) {
                        ((Callback) getActivity())
                                .onItemSelected(MovieContract.MovieEntry.buildMovieUri(cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry._ID))
                                ));

                    }
                });
            }
        } else {
            MovieSyncAdapter.syncImmediately(getActivity());
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Please wait while movies are loaded...");
            pd.show();
        }
        c.close();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        movieAdapter.swapCursor(null);
    }
}
