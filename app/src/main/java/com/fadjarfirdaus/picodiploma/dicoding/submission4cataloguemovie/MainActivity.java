package com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.fragment.FavoriteFragment;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.fragment.NowPlayingFragment;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.fragment.SearchFragment;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.fragment.UpCommingFragment;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.receiver.AlarmReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.fragment.SearchFragment.EXTRA_QUERY;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.bottomNav_main)BottomNavigationView bottomNavigationView;
    private final String STATE_FRAG = "state_frag";
    private final int STATE_NOWPLAYING = 0;
    private final int STATE_SEARCH = 1;
    private final int STATE_UPCOMMING = 2;
    private final String QUERY = "query";
    String query;
    private final String STATE_CHANGE_SETTING = "state_change_setting";
    private final int STATE_FAV = 3;
    private int state_now;
    public boolean changeLang;
    public void setState_now(int state_now) {
        this.state_now = state_now;
    }
    public static final int ALARM_REMINDER = 1;
    public static final int ALARM_NOWPLAYING = 2;
    private static final int HOUD_REMINDER = 7;
    private static final int HOUR_NOWPLAYING = 8;
    public static final String ISFIRSTRUN = "isfirstrun";
    public static final String FIRSTRUN_PREF = "firstrun_pref";

    private AlarmReceiver alarmReceiver = new AlarmReceiver();
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_navigation,menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.menu_search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    Bundle bundle = new Bundle();
                    bundle.putString(EXTRA_QUERY,s);
                    query = s;
                    SearchFragment searchFragment = new SearchFragment();
                    searchFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_container, searchFragment)
                            .commit();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SharedPreferences firstRun = getSharedPreferences(FIRSTRUN_PREF,Context.MODE_PRIVATE);
        Boolean isFirstRun = firstRun.getBoolean(ISFIRSTRUN,true);
        if (isFirstRun) {
            alarmReceiver.setDailyReminder(getApplicationContext());
            alarmReceiver.setReleaseReminder(getApplicationContext());
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            if(getSupportActionBar()!=null){
                getSupportActionBar().setTitle(getResources().getString(R.string.action_nowplaying));
            }
            changeLang =false;
            loadFragment(new NowPlayingFragment());
        }else if (savedInstanceState.getBoolean(STATE_CHANGE_SETTING) == true){
            int checker = savedInstanceState.getInt(STATE_FRAG);
            Fragment fragment = null;
            String title = "";

            switch (checker){
                case 0:
                    title = getResources().getString(R.string.action_nowplaying);
                    setState_now(STATE_NOWPLAYING);
                    fragment = new NowPlayingFragment();
                    break;
                case 1:
                    Bundle bundle = new Bundle();
                    String s = savedInstanceState.getString(QUERY);
                    bundle.putString(EXTRA_QUERY,s);
                    SearchFragment searchFragment = new SearchFragment();
                    searchFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_container, searchFragment)
                            .commit();
                    break;
                case 2:
                    title = getResources().getString(R.string.action_upComming);
                    setState_now(STATE_UPCOMMING);
                    fragment = new UpCommingFragment();
                    break;
                case 3:
                    title = getResources().getString(R.string.action_search);
                    setState_now(STATE_FAV);
                    fragment = new FavoriteFragment();
                    break;
            }
            changeLang = false;
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(title);
            }
            loadFragment(fragment);
        }
    }

    private boolean loadFragment(Fragment fragment){
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_container, fragment)
                    .commit();
            return true;
        }

        return false;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_FRAG,state_now);
        outState.putBoolean(STATE_CHANGE_SETTING,changeLang);
        outState.putString(QUERY,query);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        String title = "";
        switch (menuItem.getItemId()){
            case R.id.menu_nowPlaying:
                title = getResources().getString(R.string.action_nowplaying);
                setState_now(STATE_NOWPLAYING);
                fragment = new NowPlayingFragment();
                break;
            case R.id.menu_favorite:
                title = getResources().getString(R.string.action_search);
                setState_now(STATE_FAV);
                fragment = new FavoriteFragment();
                break;
            case R.id.menu_upcomming:
                title = getResources().getString(R.string.action_upComming);
                setState_now(STATE_UPCOMMING);
                fragment = new UpCommingFragment();
                break;
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
        return loadFragment(fragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_changelang){
            Intent changeLangIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(changeLangIntent);
            changeLang = true;
        }
        if (item.getItemId() == R.id.menu_setNotification) {
            Intent setNotif = new Intent(MainActivity.this,SettingActivity.class);
            startActivity(setNotif);
        }
        return super.onOptionsItemSelected(item);
    }
}
