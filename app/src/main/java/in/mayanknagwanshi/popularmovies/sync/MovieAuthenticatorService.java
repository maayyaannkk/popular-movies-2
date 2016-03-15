package in.mayanknagwanshi.popularmovies.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by MayankN on 06-03-2016.
 */
public class MovieAuthenticatorService extends Service {

    private MovieAuthenticator mMovieAuthenticator;

    @Override
    public void onCreate() {
        mMovieAuthenticator = new MovieAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMovieAuthenticator.getIBinder();
    }
}
