package in.mayanknagwanshi.popularmovies.lib;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.mayanknagwanshi.popularmovies.R;

/**
 * Created by MayankN on 14-03-2016.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>{
    ArrayList<String> trailerList;
    Context context;
    public TrailerAdapter(ArrayList<String> trailerList,Context context){
        this.trailerList = trailerList;
        this.context = context;
    }

    public static class TrailerViewHolder extends RecyclerView.ViewHolder{
        ImageView trailerImage,playImage;
        public TrailerViewHolder(View itemView){
            super(itemView);
            trailerImage = (ImageView)itemView.findViewById(R.id.iv_youtube_view);
            playImage = (ImageView)itemView.findViewById(R.id.iv_button);
        }
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trailer_item, viewGroup, false);
        TrailerViewHolder rvh = new TrailerViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Picasso.with(context).load("http://img.youtube.com/vi/" + trailerList.get(position) + "/hqdefault.jpg")
                .error(R.drawable.logo)
                .into(holder.trailerImage);
        final int pos = position;
        holder.playImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/watch?v=" + trailerList.get(pos)));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }


}
