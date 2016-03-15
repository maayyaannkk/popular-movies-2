package in.mayanknagwanshi.popularmovies;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import in.mayanknagwanshi.popularmovies.lib.ReviewAdapter;

/**
 * Created by MayankN on 12-03-2016.
 */
public class ReviewListDialogFragment extends DialogFragment {

    RecyclerView rv;
    ArrayList<ArrayList<String>> reviewList;
    public static final String TOTAL_REVIEW = "total_review";
    public static final String ARRAY_DATA = "array_data";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_review_list, new LinearLayout(getActivity()), false);
        rv = (RecyclerView)v.findViewById(R.id.reviewRecycler);

        Bundle b = getArguments();
        int totalReview = b.getInt(TOTAL_REVIEW);
        reviewList = new ArrayList<ArrayList<String>>();
        for(int i=0;i<totalReview;i++){
            reviewList.add(b.getStringArrayList(ARRAY_DATA+i+""));
        }

        ViewGroup.LayoutParams layoutParams = rv.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        if(reviewList.size()==1 || reviewList.size()==2)
            layoutParams.height = getListItemHeight() * 3;
        else
            layoutParams.height = getListItemHeight() * (reviewList.size()+1);
        v.setLayoutParams(layoutParams);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(new ReviewAdapter(reviewList));

        Dialog builder = new Dialog(getActivity());
        builder.setCanceledOnTouchOutside(true);
        builder.setTitle("User Reviews");
        builder.setContentView(v);

        return builder;
    }
    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        super.onResume();
    }
    private int getListItemHeight() {
        TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.listPreferredItemHeightLarge, typedValue, true);
        DisplayMetrics metrics = new android.util.DisplayMetrics(); getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return (int) typedValue.getDimension(metrics);
    }
}
