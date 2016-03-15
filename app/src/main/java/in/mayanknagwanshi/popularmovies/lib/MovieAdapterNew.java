package in.mayanknagwanshi.popularmovies.lib;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import in.mayanknagwanshi.popularmovies.R;
import in.mayanknagwanshi.popularmovies.custom.RecyclerViewCursorAdapter;
import in.mayanknagwanshi.popularmovies.data.MovieContract;
import in.mayanknagwanshi.popularmovies.data.MovieProvider;

/**
 * Created by MayankN on 10-03-2016.
 */
public class MovieAdapterNew extends RecyclerViewCursorAdapter<MovieAdapterNew.MovieViewHolder> implements View.OnClickListener{
    private final LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    @Override
    public void onBindViewHolder(MovieViewHolder holder, Cursor cursor) {
        holder.bindData(cursor);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = this.layoutInflater.inflate(R.layout.movie_item, parent, false);
        view.setOnClickListener(this);
        return new MovieViewHolder(view);
    }
    public void setOnItemClickListener(final OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }
    public MovieAdapterNew(final Context context) {
        super();

        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public void onClick(final View view)
    {
        if (this.onItemClickListener != null)
        {
            final RecyclerView recyclerView = (RecyclerView) view.getParent();
            final int position = recyclerView.getChildLayoutPosition(view);
            if (position != RecyclerView.NO_POSITION)
            {
                final Cursor cursor = this.getItem(position);
                this.onItemClickListener.onItemClicked(cursor);
            }
        }
    }
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView posterView;
        Context context;

        public MovieViewHolder(final View itemView) {
            super(itemView);
            context = itemView.getContext();
            posterView = (ImageView) itemView.findViewById(R.id.movie_image);
        }

        public void bindData(final Cursor cursor) {
            if(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_FAVOURITE))== MovieProvider.IS_FAVOURITE_TRUE){
                Picasso.with(context).load(new File(Environment.getExternalStorageDirectory().getPath()+"/moviedb/"+cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE_PATH))))
                        .placeholder(R.drawable.loader).error(R.drawable.logo)
                        .into(this.posterView);
            }else{
                Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_IMAGE_PATH)))
                        .placeholder(R.drawable.loader).error(R.drawable.logo)
                        .into(this.posterView);
            }
        }
    }
    public interface OnItemClickListener
    {
        void onItemClicked(Cursor cursor);
    }
}
