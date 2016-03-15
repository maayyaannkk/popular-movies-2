package in.mayanknagwanshi.popularmovies.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

import in.mayanknagwanshi.popularmovies.R;
import in.mayanknagwanshi.popularmovies.data.MovieContract;
import in.mayanknagwanshi.popularmovies.data.MovieProvider;
import in.mayanknagwanshi.popularmovies.lib.JSONParser;

/**
 * Created by MayankN on 06-03-2016.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = MovieSyncAdapter.class.getSimpleName();
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "onPerformSync Called.");
        try {
            int[] PopularMovieIds = JSONParser.getMovieIds(JSONParser.SORT_POPULAR);
            int[] UserRatedMovieIds = JSONParser.getMovieIds(JSONParser.SORT_USER_RATED);
            Vector<ContentValues> cVVectorMovie = new Vector<ContentValues>(PopularMovieIds.length + UserRatedMovieIds.length);
            Vector<ContentValues> cVVectorReview = new Vector<ContentValues>();
            Vector<ContentValues> cVVectorTrailer = new Vector<ContentValues>();

            for (int i = 0; i < PopularMovieIds.length; i++) {
                JSONObject movieDetail = JSONParser.getMovieObject(PopularMovieIds[i]);
                ContentValues movieValues = new ContentValues();
                movieValues.put(MovieContract.MovieEntry.COLUMN_SERVER_ID, movieDetail.getInt("id"));
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_NAME, movieDetail.getString("original_title"));
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE_PATH, movieDetail.getString("poster_path"));
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movieDetail.getString("backdrop_path"));
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, movieDetail.getDouble("vote_average") + "/10");
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RUNTIME, movieDetail.getString("runtime") + " mins");
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS, movieDetail.getString("overview"));
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_YEAR, !movieDetail.getString("release_date").equals("") ? movieDetail.getString("release_date").substring(0, 4) : "");
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POPULAR, MovieProvider.IS_POPULAR_TRUE);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_USER_RATED, MovieProvider.IS_USER_RATED_FALSE);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVOURITE, MovieProvider.IS_FAVOURITE_FALSE);
                cVVectorMovie.add(movieValues);

                JSONArray reviewValues = JSONParser.getReviewObject(PopularMovieIds[i]).getJSONArray("results");

                for (int j = 0; j < reviewValues.length(); j++) {
                    ContentValues reviewValue = new ContentValues();
                    JSONObject review = reviewValues.getJSONObject(j);
                    reviewValue.put(MovieContract.ReviewEntry.COLUMN_REVIEW_MOV_KEY, PopularMovieIds[i]);
                    reviewValue.put(MovieContract.ReviewEntry.COLUMN_REVIEW_AUTHOR, review.getString("author"));
                    reviewValue.put(MovieContract.ReviewEntry.COLUMN_REVIEW_CONTENT, review.getString("content"));
                    cVVectorReview.add(reviewValue);
                }

                JSONArray trailerValues = JSONParser.getTrailerObject(PopularMovieIds[i]).getJSONArray("results");

                for (int k = 0; k < trailerValues.length(); k++) {
                    ContentValues trailerValue = new ContentValues();
                    JSONObject trailer = trailerValues.getJSONObject(k);
                    trailerValue.put(MovieContract.TrailerEntry.COLUMN_TRAILER_MOV_KEY, PopularMovieIds[i]);
                    trailerValue.put(MovieContract.TrailerEntry.COLUMN_TRAILER_KEY, trailer.getString("key"));
                    cVVectorTrailer.add(trailerValue);
                }
            }

            for (int i = 0; i < UserRatedMovieIds.length; i++) {
                JSONObject movieDetail = JSONParser.getMovieObject(UserRatedMovieIds[i]);
                ContentValues movieValues = new ContentValues();
                movieValues.put(MovieContract.MovieEntry.COLUMN_SERVER_ID, movieDetail.getInt("id"));
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_NAME, movieDetail.getString("original_title"));
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE_PATH, movieDetail.getString("poster_path"));
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movieDetail.getString("backdrop_path"));
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, movieDetail.getDouble("vote_average") + "/10");
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RUNTIME, movieDetail.getString("runtime") + " mins");
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS, movieDetail.getString("overview"));
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_YEAR, !movieDetail.getString("release_date").equals("") ? movieDetail.getString("release_date").substring(0, 4) : "");
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POPULAR, MovieProvider.IS_POPULAR_FALSE);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_USER_RATED, MovieProvider.IS_USER_RATED_TRUE);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVOURITE, MovieProvider.IS_FAVOURITE_FALSE);
                cVVectorMovie.add(movieValues);

                JSONArray reviewValues = JSONParser.getReviewObject(UserRatedMovieIds[i]).getJSONArray("results");

                for (int j = 0; j < reviewValues.length(); j++) {
                    ContentValues reviewValue = new ContentValues();
                    JSONObject review = reviewValues.getJSONObject(j);
                    reviewValue.put(MovieContract.ReviewEntry.COLUMN_REVIEW_MOV_KEY, UserRatedMovieIds[i]);
                    reviewValue.put(MovieContract.ReviewEntry.COLUMN_REVIEW_AUTHOR, review.getString("author"));
                    reviewValue.put(MovieContract.ReviewEntry.COLUMN_REVIEW_CONTENT, review.getString("content"));
                    cVVectorReview.add(reviewValue);
                }

                JSONArray trailerValues = JSONParser.getTrailerObject(UserRatedMovieIds[i]).getJSONArray("results");

                for (int k = 0; k < trailerValues.length(); k++) {
                    ContentValues trailerValue = new ContentValues();
                    JSONObject trailer = trailerValues.getJSONObject(k);
                    trailerValue.put(MovieContract.TrailerEntry.COLUMN_TRAILER_MOV_KEY, UserRatedMovieIds[i]);
                    trailerValue.put(MovieContract.TrailerEntry.COLUMN_TRAILER_KEY, trailer.getString("key"));
                    cVVectorTrailer.add(trailerValue);
                }

            }

            if (cVVectorTrailer.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVectorTrailer.size()];
                cVVectorTrailer.toArray(cvArray);
                getContext().getContentResolver().delete(MovieContract.TrailerEntry.CONTENT_URI, null, null);
                getContext().getContentResolver().bulkInsert(MovieContract.TrailerEntry.CONTENT_URI, cvArray);
            }
            if (cVVectorReview.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVectorReview.size()];
                cVVectorReview.toArray(cvArray);
                getContext().getContentResolver().delete(MovieContract.ReviewEntry.CONTENT_URI, null, null);
                getContext().getContentResolver().bulkInsert(MovieContract.ReviewEntry.CONTENT_URI, cvArray);
            }
            if (cVVectorMovie.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVectorMovie.size()];
                cVVectorMovie.toArray(cvArray);
                getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, null, null);
                getContext().getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);

    }

    public static Account getSyncAccount(Context context) {
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));
        if (null == accountManager.getPassword(newAccount)) {

            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            onAccountCreated(newAccount, context);

        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {

        MovieSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        syncImmediately(context);
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }
    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
