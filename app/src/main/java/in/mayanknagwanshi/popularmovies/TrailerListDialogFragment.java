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

import in.mayanknagwanshi.popularmovies.lib.TrailerAdapter;

/**
 * Created by MayankN on 14-03-2016.
 */
public class TrailerListDialogFragment extends DialogFragment {
    RecyclerView rv;
    public static final String ARRAY_DATA = "array_data";
    ArrayList<String> trailerList;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_trailer_list, new LinearLayout(getActivity()), false);
        rv = (RecyclerView)v.findViewById(R.id.trailerRecycler);

        trailerList = new ArrayList<String>();
        trailerList = getArguments().getStringArrayList(ARRAY_DATA);

        ViewGroup.LayoutParams layoutParams = rv.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        if(trailerList.size()==1 || trailerList.size()==2)
            layoutParams.height = getListItemHeight() * 4;
        else
            layoutParams.height = getListItemHeight() * (trailerList.size()+2);
        v.setLayoutParams(layoutParams);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(new TrailerAdapter(trailerList,getActivity()));

        Dialog builder = new Dialog(getActivity());
        builder.setCanceledOnTouchOutside(true);
        builder.setTitle("Movie Trailers");
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
