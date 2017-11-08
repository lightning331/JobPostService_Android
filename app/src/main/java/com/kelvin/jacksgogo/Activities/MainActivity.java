package com.kelvin.jacksgogo.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.kelvin.jacksgogo.Activities.BottomNavigation.BottomNavigationViewBehavior;
import com.kelvin.jacksgogo.Activities.BottomNavigation.BottomNavigationViewHelper;
import com.kelvin.jacksgogo.CustomView.AppMainActionbarView;
import com.kelvin.jacksgogo.Fragments.Favourite.FavouriteFragment;
import com.kelvin.jacksgogo.Fragments.Home.HomeFragment;
import com.kelvin.jacksgogo.Fragments.Profile.ProfileFragment;
import com.kelvin.jacksgogo.Fragments.Search.SearchFragment;
import com.kelvin.jacksgogo.R;
import com.kelvin.jacksgogo.Fragments.Appointments.AppHomeRecyclerViewFragment;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements AppHomeRecyclerViewFragment.OnFragmentInteractionListener {

    private Toolbar mToolbar;
    private AppMainActionbarView appMainActionbarView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            selectFragment(item);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_search:
                    return true;
                case R.id.navigation_appointments:
                    return true;
                case R.id.navigation_favourite:
                    return true;
                case R.id.navigation_profile:
                    return true;
            }
            return false;
        }
    };

    private void selectFragment(MenuItem item) {
        Fragment frag = null;
        mToolbar = (Toolbar) findViewById(R.id.myToolbar);
        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.navigation_home:
                frag = HomeFragment.newInstance(null,
                        null);
                mToolbar.setTitle(R.string.title_home);
                break;
            case R.id.navigation_search:
                frag = SearchFragment.newInstance(null,
                        null);
                mToolbar.setTitle(R.string.title_search);
                break;
            case R.id.navigation_appointments:
                frag = AppHomeRecyclerViewFragment.newInstance();
                break;
            case R.id.navigation_favourite:
                frag = FavouriteFragment.newInstance(null,
                        null);
                mToolbar.setTitle(R.string.title_favourite);
                break;
            case R.id.navigation_profile:
                frag = ProfileFragment.newInstance(null,
                        null);
                mToolbar.setTitle(R.string.title_profile);
                break;
        }

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, frag, frag.getTag());
            ft.commit();
            if (frag instanceof AppHomeRecyclerViewFragment) {
                this.addTopActionBarForAppointment(frag);
            } else {
                this.removeTopActionBarForAppointment();
            }
        }
    }

    private void addTopActionBarForAppointment(final Fragment frag) {
        mToolbar.removeView(appMainActionbarView);
        if (appMainActionbarView == null) {
            appMainActionbarView = new AppMainActionbarView(this);
        }
        mToolbar.addView(appMainActionbarView);

        appMainActionbarView.setTabbarItemClickListener(new AppMainActionbarView.OnTabbarItemClickListener() {
            @Override
            public void onTabbarItemClick(TextView item) {
                if (frag instanceof AppHomeRecyclerViewFragment) {
                    ((AppHomeRecyclerViewFragment)frag).refreshFragment(item.getTag());
                }
            }
        });
    }

    private void removeTopActionBarForAppointment() {
        mToolbar.removeView(appMainActionbarView);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        BottomNavigationView mbtmView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(mbtmView);
        mbtmView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mbtmView.setSelectedItemId(R.id.navigation_home);

        // Hide Bottom NavigationView and ToolBar
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mbtmView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

        appMainActionbarView = new AppMainActionbarView(this);
        mToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(mToolbar);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
