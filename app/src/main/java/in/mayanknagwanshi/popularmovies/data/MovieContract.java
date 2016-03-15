package in.mayanknagwanshi.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by MayankN on 02-03-2016.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "in.mayanknagwanshi.popularmovies.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_REVIEW = "review";
    public static final String PATH_TRAILER = "trailer";
    public static final String PATH_FAVOURITE = "favourite";

    public static final String SORT_POPULAR = "popularity";
    public static final String SORT_USER_RATED = "user_rating";
    public static final String SORT_FAVOURITE = "favourite";


    public static final class MovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_SERVER_ID = "movie_server_id";
        public static final String COLUMN_MOVIE_NAME = "movie_name";
        public static final String COLUMN_MOVIE_SYNOPSIS = "movie_synopsis";
        public static final String COLUMN_MOVIE_IMAGE_PATH = "movie_image_path";
        public static final String COLUMN_MOVIE_POSTER_PATH = "movie_poster_path";
        public static final String COLUMN_MOVIE_YEAR = "movie_year";
        public static final String COLUMN_MOVIE_RUNTIME = "movie_runtime";
        public static final String COLUMN_MOVIE_RATING = "movie_rating";
        public static final String COLUMN_MOVIE_FAVOURITE = "movie_is_favourite";
        public static final String COLUMN_MOVIE_POPULAR = "movie_is_popular";
        public static final String COLUMN_MOVIE_USER_RATED = "movie_is_user_rated";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static Uri buildMovieUriWithSort(String Sort){
            switch (Sort){
                case SORT_POPULAR:
                    return CONTENT_URI.buildUpon().appendPath(SORT_POPULAR).build();
                case SORT_USER_RATED:
                    return CONTENT_URI.buildUpon().appendPath(SORT_USER_RATED).build();
                case SORT_FAVOURITE:
                    return CONTENT_URI.buildUpon().appendPath(SORT_FAVOURITE).build();
            }
            return null;
        }
        public static String getSortCriteriaFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
        public static long getMovieIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }
        public static String getForeignMovieIdFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }

    public static final class ReviewEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        public static final String TABLE_NAME = "review";
        public static final String COLUMN_REVIEW_AUTHOR = "review_author";
        public static final String COLUMN_REVIEW_CONTENT = "review_content";
        public static final String COLUMN_REVIEW_MOV_KEY = "movie_id";

        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static Uri buildReviewMovie(long movieId) {
            return CONTENT_URI.buildUpon().appendPath(movieId+"").build();
        }
    }

    public static final class TrailerEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

        public static final String TABLE_NAME = "trailer";
        public static final String COLUMN_TRAILER_KEY = "trailer_key";
        public static final String COLUMN_TRAILER_MOV_KEY = "movie_id";

        public static Uri buildTrailerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static Uri buildTrailerMovie(long movieId) {
            return CONTENT_URI.buildUpon().appendPath(movieId+"").build();
        }
    }
}
