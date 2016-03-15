package in.mayanknagwanshi.popularmovies.lib;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MayankN on 03-02-2016.
 */
public class MovieData implements Parcelable{
    String PosterPath;
    String MetaData;
    int MovieId;

    @Override
    public int describeContents() {
        return 0;
    }
    public MovieData(){

    }

    private MovieData(Parcel in){
        PosterPath = in.readString();
        MetaData = in.readString();
        MovieId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(PosterPath);
        dest.writeString(MetaData);
        dest.writeInt(MovieId);
    }

    public final Parcelable.Creator<MovieData> CREATOR = new Parcelable.Creator<MovieData>(){
        @Override
        public MovieData createFromParcel(Parcel source) {
            return new MovieData(source);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };

    public String getMetaData() {
        return MetaData;
    }

    public void setMetaData(String metaData) {
        MetaData = metaData;
    }


    public int getMovieId() {
        return MovieId;
    }

    public void setMovieId(int movieId) {
        MovieId = movieId;
    }

    public String getPosterPath() {
        return PosterPath;
    }

    public void setPosterPath(String posterPath) {
        PosterPath = posterPath;
    }
}
