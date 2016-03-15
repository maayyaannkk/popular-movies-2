package in.mayanknagwanshi.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import in.mayanknagwanshi.popularmovies.data.MovieContract;
import in.mayanknagwanshi.popularmovies.data.MovieProvider;
import in.mayanknagwanshi.popularmovies.lib.MyTarget;

/**
 * Created by MayankN on 11-03-2016.
 */
public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    Uri argsUri;
    public static final String ARGS_URI = "args_uri";
    private TextView year, rating, playtime, synopsis, review, movieName;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView imageView, videoView;
    private ShareActionProvider mShareActionProvider;
    private CardView reviewCard, trailerCard;
    private FloatingActionButton favouriteFab;
    private boolean isFavourite = false;
    private ArrayList<ArrayList<String>> reviewList;
    private ArrayList<String> trailerList;
    final Handler handler = new Handler();
    final Handler handlerTrailer = new Handler();
    int reviewNum = -1, maxReview = 0, trailerNum = -1, maxTrailer = 0;

    public MovieDetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if(trailerList!=null && trailerList.size()>0){
            mShareActionProvider.setShareIntent(createShareIntent(movieName.getText().toString(),"https://www.youtube.com/watch?v=" + trailerList.get(0)));
        }
    }

    private Intent createShareIntent(String movieName, String trailerUrl) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Watch the trailer of " + movieName + " on youtube " + trailerUrl);
        return shareIntent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        year = (TextView) rootView.findViewById(R.id.textViewYear);
        synopsis = (TextView) rootView.findViewById(R.id.textViewSynopsis);
        playtime = (TextView) rootView.findViewById(R.id.textViewPlaytime);
        rating = (TextView) rootView.findViewById(R.id.textViewRating);
        review = (TextView) rootView.findViewById(R.id.textViewReview);
        movieName = (TextView) rootView.findViewById(R.id.textViewMovieName);
        reviewCard = (CardView) rootView.findViewById(R.id.cardViewReview);
        trailerCard = (CardView) rootView.findViewById(R.id.cardViewTrailer);
        videoView = (ImageView) rootView.findViewById(R.id.iv_youtube_view);


        favouriteFab = (FloatingActionButton) rootView.findViewById(R.id.favouriteFAB);

        collapsingToolbar =
                (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);


        imageView = (ImageView) rootView.findViewById(R.id.backdrop);
        final Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(" ");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            argsUri = arguments.getParcelable(ARGS_URI);
        }
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (argsUri != null) {
            return new CursorLoader(getActivity(), argsUri, null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, final Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            collapsingToolbar.setTitle(" ");
            movieName.setText(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_NAME)));
            year.setText(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_YEAR)));
            synopsis.setText(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS)));
            rating.setText(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RATING)));
            playtime.setText(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RUNTIME)));


            if (cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_FAVOURITE)) == MovieProvider.IS_FAVOURITE_TRUE) {
                isFavourite = true;
                favouriteFab.setImageResource(R.drawable.ic_favorite_white_24dp);
                Picasso.with(getActivity()).load(new File(Environment.getExternalStorageDirectory().getPath() + "/moviedb/" + cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH))))
                        .placeholder(R.drawable.loader).error(R.drawable.logo)
                        .into(imageView);
            } else {
                favouriteFab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w342/" + cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH)))
                        .placeholder(R.drawable.loader).error(R.drawable.logo)
                        .into(imageView);
            }
            final String movServerId = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_SERVER_ID));

            favouriteFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isFavourite) {
                        ContentValues mov = new ContentValues();
                        mov.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVOURITE, MovieProvider.IS_FAVOURITE_FALSE);
                        if (getContext().getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI, mov, MovieContract.MovieEntry.COLUMN_SERVER_ID + "=?", new String[]{movServerId}) > 0) {
                            favouriteFab.setImageResource(R.drawable.ic_favorite_white_24dp);
                            isFavourite = false;
                            File f = new File(Environment.getExternalStorageDirectory().getPath() + "/moviedb/" + cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH)));
                            if (f.exists()) f.delete();
                            f = new File(Environment.getExternalStorageDirectory().getPath() + "/moviedb/" + cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE_PATH)));
                            if (f.exists()) f.delete();
                            Snackbar.make(view, "Removed from Favourites", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    } else {
                        ContentValues mov = new ContentValues();
                        mov.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVOURITE, MovieProvider.IS_FAVOURITE_TRUE);
                        if (getContext().getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI, mov, MovieContract.MovieEntry.COLUMN_SERVER_ID + "=?", new String[]{movServerId}) > 0) {
                            favouriteFab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                            isFavourite = true;
                            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w342/" + cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH)))
                                    .placeholder(R.drawable.loader).error(R.drawable.logo)
                                    .into(new MyTarget(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH))));
                            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE_PATH)))
                                    .placeholder(R.drawable.loader).error(R.drawable.logo)
                                    .into(new MyTarget(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE_PATH))));
                            Snackbar.make(view, "Added to Favourites", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                }
            });


            Uri reviewUri = MovieContract.ReviewEntry.buildReviewMovie(cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_SERVER_ID)));
            Cursor reviewCursor = getContext().getContentResolver().query(reviewUri, null, null, null, null);
            if (reviewCursor != null && reviewCursor.moveToFirst()) {

                reviewList = new ArrayList<ArrayList<String>>();
                reviewCursor.moveToFirst();

                while (reviewCursor.moveToNext()) {
                    ArrayList<String> toAdd = new ArrayList<String>();
                    toAdd.add(reviewCursor.getString(reviewCursor.getColumnIndex(MovieContract.ReviewEntry.COLUMN_REVIEW_AUTHOR)));
                    toAdd.add(reviewCursor.getString(reviewCursor.getColumnIndex(MovieContract.ReviewEntry.COLUMN_REVIEW_CONTENT)));
                    reviewList.add(toAdd);
                }
                maxReview = reviewList.size();
                if (maxReview > 0) {
                    handler.postDelayed(runnable, 500);
                    reviewCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ReviewListDialogFragment df = new ReviewListDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt(ReviewListDialogFragment.TOTAL_REVIEW, reviewList.size());
                            for (int i = 0; i < reviewList.size(); i++) {
                                bundle.putStringArrayList(ReviewListDialogFragment.ARRAY_DATA + i + "", reviewList.get(i));
                            }
                            df.setArguments(bundle);
                            df.show(getFragmentManager(), "dialog");
                        }
                    });
                } else {
                    review.setText("No Reviews Found");
                }
            } else {
                review.setText("No Reviews Found");
            }

            Uri trailerUri = MovieContract.TrailerEntry.buildTrailerMovie(cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_SERVER_ID)));
            Cursor trailerCursor = getContext().getContentResolver().query(trailerUri, null, null, null, null);
            if (trailerCursor != null && trailerCursor.moveToFirst()) {
                trailerList = new ArrayList<String>();
                trailerCursor.moveToFirst();
                do {
                    trailerList.add(trailerCursor.getString(trailerCursor.getColumnIndex(MovieContract.TrailerEntry.COLUMN_TRAILER_KEY)));
                } while (trailerCursor.moveToNext());
                maxTrailer = trailerList.size();
                if (maxTrailer > 0) {
                    handlerTrailer.postDelayed(runnableT, 500);
                    trailerCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TrailerListDialogFragment df = new TrailerListDialogFragment();
                            Bundle b = new Bundle();
                            b.putStringArrayList(TrailerListDialogFragment.ARRAY_DATA, trailerList);
                            df.setArguments(b);
                            df.show(getFragmentManager(), "dialog");
                        }
                    });
                } else {
                    trailerCard.setVisibility(View.GONE);
                }
            } else {
                trailerCard.setVisibility(View.GONE);
            }

            trailerCursor.close();
            reviewCursor.close();
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            changeReview();
            handler.postDelayed(this, 3500);
        }
    };
    Runnable runnableT = new Runnable() {
        @Override
        public void run() {
            changeTrailer();
            handlerTrailer.postDelayed(this, 4500);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    private void changeTrailer() {
        if (trailerNum >= (maxTrailer - 1))
            trailerNum = 0;
        else
            trailerNum += 1;
        Picasso.with(getActivity()).load("http://img.youtube.com/vi/" + trailerList.get(trailerNum) + "/hqdefault.jpg")
                .error(R.drawable.logo)
                .into(videoView);
    }

    private void changeReview() {
        if (reviewNum >= (maxReview - 1))
            reviewNum = 0;
        else
            reviewNum += 1;
        ArrayList<String> temp = reviewList.get(reviewNum);
        review.setText(Html.fromHtml("<font color=\"#1976D2\"><b>" + temp.get(0) + "</b></font> " + temp.get(1)));
    }
}
