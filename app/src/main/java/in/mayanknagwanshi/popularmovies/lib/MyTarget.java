package in.mayanknagwanshi.popularmovies.lib;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by MayankN on 16-03-2016.
 */
public class MyTarget implements Target {

    private String fileName;
    public MyTarget(String fileName){
        this.fileName = fileName;
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/moviedb");
        if(!file.exists()) file.mkdir();
    }

    @Override
    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
        new Thread(new Runnable() {

            @Override
            public void run() {

                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/moviedb/"+fileName);
                try {
                    file.createNewFile();
                    FileOutputStream ostream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                    ostream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        if (placeHolderDrawable != null) {}
    }
}
