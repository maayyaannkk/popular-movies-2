package in.mayanknagwanshi.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import in.mayanknagwanshi.popularmovies.data.MovieContract;
import in.mayanknagwanshi.popularmovies.sync.MovieSyncAdapter;

public class MainActivity extends AppCompatActivity implements MovieListFragment.Callback {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_list_container) != null) {
            mTwoPane = true;
            Bundle bundle = new Bundle();
            bundle.putParcelable(MovieDetailFragment.ARGS_URI, MovieContract.MovieEntry.buildMovieUri(1));
            MovieDetailFragment mdf = new MovieDetailFragment();
            mdf.setArguments(bundle);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, mdf)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        MovieSyncAdapter.initializeSyncAdapter(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        String temp = "";
        String title = "";
        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 0:
                    temp = MovieContract.SORT_POPULAR;
                    title = "Most Popular";
                    break;
                case 1:
                    temp = MovieContract.SORT_USER_RATED;
                    title = "User Rated";
                    break;
                case 2:
                    temp = MovieContract.SORT_FAVOURITE;
                    title = "My Favourites";
                    break;
            }
            Bundle bundle = new Bundle();
            bundle.putString("sort", temp);
            Fragment f = new MovieListFragment();
            f.setArguments(bundle);
            adapter.addFragment(f, title);
        }
        viewPager.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(MovieDetailFragment.ARGS_URI, MovieContract.MovieEntry.buildMovieUri(1));
            MovieDetailFragment mdf = new MovieDetailFragment();
            mdf.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, mdf)
                    .commit();
        }
    }

    @Override
    public void onItemSelected(Uri movieUri) {
        if (mTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(MovieDetailFragment.ARGS_URI, movieUri);
            MovieDetailFragment mdf = new MovieDetailFragment();
            mdf.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, mdf)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.setData(movieUri);
            startActivity(intent);
        }
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
