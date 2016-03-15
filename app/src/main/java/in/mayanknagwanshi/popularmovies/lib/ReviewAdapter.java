package in.mayanknagwanshi.popularmovies.lib;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.mayanknagwanshi.popularmovies.R;

/**
 * Created by MayankN on 13-03-2016.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    ArrayList<ArrayList<String>> reviewList;

    public ReviewAdapter(ArrayList<ArrayList<String>> reviewList){
        this.reviewList = reviewList;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder{
        TextView reviewText;
        public ReviewViewHolder(View itemView){
            super(itemView);
            reviewText = (TextView)itemView.findViewById(R.id.textReviewItem);
        }
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_item, viewGroup, false);
        ReviewViewHolder rvh = new ReviewViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        ArrayList<String> temp = reviewList.get(position);
        holder.reviewText.setText(Html.fromHtml("<font color=\"#1976D2\"><b>" + temp.get(0) + "</b></font> " + temp.get(1)));
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

}
