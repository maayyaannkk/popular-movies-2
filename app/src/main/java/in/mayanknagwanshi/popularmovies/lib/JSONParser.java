package in.mayanknagwanshi.popularmovies.lib;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MayankN on 03-02-2016.
 */
public class JSONParser {

    public static final String LOG_TAG = "error";
    public static final String API_KEY = ""; //Input api key in this string
    public static final String BASE_POPULAR_URL = "http://api.themoviedb.org/3/movie/popular";
    public static final String BASE_TOP_RATED_URL = "http://api.themoviedb.org/3/movie/top_rated";
    public static final String BASE_MOVIE_URL = "http://api.themoviedb.org/3/movie/";//add id
    public static final String BASE_REVIEW_URL = "http://api.themoviedb.org/3/movie/";//add id/reviews
    public static final String BASE_TRAILER_URL = "http://api.themoviedb.org/3/movie/";//add id/videos
    public static final int SORT_POPULAR = 100;
    public static final int SORT_USER_RATED = 200;

    public static JSONObject getMovieObject(int id) {
        Uri builtUri = null;
        builtUri = Uri.parse(BASE_MOVIE_URL+id).buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .build();
        try{
            JSONObject movieDetail = new JSONObject(NetworkCall(builtUri));
            return movieDetail;
        }catch (JSONException e){
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getReviewObject(int movieId){
        Uri builtUri = null;
        builtUri = Uri.parse(BASE_REVIEW_URL+movieId+"/reviews").buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .build();
        try{
            JSONObject reviewDetail = new JSONObject(NetworkCall(builtUri));
            return reviewDetail;
        }catch (JSONException e){
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getTrailerObject(int movieId){
        Uri builtUri = null;
        builtUri = Uri.parse(BASE_TRAILER_URL+movieId+"/videos").buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .build();
        try{
            JSONObject trailerDetail = new JSONObject(NetworkCall(builtUri));
            return trailerDetail;
        }catch (JSONException e){
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    public static int[] getMovieIds(int sort) {
        Uri builtUri = null;

        switch (sort) {
            case SORT_POPULAR: {
                builtUri = Uri.parse(BASE_POPULAR_URL).buildUpon()
                        .appendQueryParameter("api_key", API_KEY)
                        .build();
                break;
            }
            case SORT_USER_RATED: {
                builtUri = Uri.parse(BASE_TOP_RATED_URL).buildUpon()
                        .appendQueryParameter("api_key", API_KEY)
                        .build();
                break;
            }
            default:
                throw new UnsupportedOperationException("Sort unclear");
        }
        String resultJsonString = NetworkCall(builtUri);
        try {
            int[] toReturn = new int[20];
            JSONObject obj = new JSONObject(resultJsonString);
            JSONArray arr = obj.getJSONArray("results");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject mov = arr.getJSONObject(i);
                toReturn[i] = mov.getInt("id");
            }
            return toReturn;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }



    public static String NetworkCall(Uri builtUri) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        final String LOG_TAG = "error";
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String resultJsonString = null;

        try {

            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            resultJsonString = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return resultJsonString;
    }
}
