package com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.R;
import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.receiver.AlarmReceiver;

import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.receiver.AlarmReceiver.DAILYREMINDER;
import static com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.receiver.AlarmReceiver.RELEASEREMINDER;

public class MyPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private String daily,relese;
    private SwitchPreference dailySwitch,releaseSwitch;
    private AlarmReceiver alarmReceiver;

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(daily)){
            Boolean cekdaily = sharedPreferences.getBoolean(daily,true);
            dailySwitch.setChecked(cekdaily);
            if (cekdaily == true) {
                alarmReceiver.setDailyReminder(getContext());
            }else{
                alarmReceiver.cancelAlarm(getContext(),DAILYREMINDER);
            }
        }
        if(key.equals(relese)){
            Boolean cekrelease = sharedPreferences.getBoolean(relese,true);
            releaseSwitch.setChecked(cekrelease);
            if (cekrelease == true) {
                alarmReceiver.setReleaseReminder(getContext());
            }else {
                alarmReceiver.cancelAlarm(getContext(),RELEASEREMINDER);
            }
        }
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preference);
        alarmReceiver = new AlarmReceiver();
        init();
        setSummaries();
    }
    private void init() {
        daily = getResources().getString(R.string.dailyreminder);
        relese = getResources().getString(R.string.releasereminder);

        dailySwitch = (SwitchPreference) findPreference(daily);
        releaseSwitch = (SwitchPreference) findPreference(relese);
    }
    private void setSummaries() {
        SharedPreferences sh = getPreferenceManager().getSharedPreferences();
        dailySwitch.setChecked(sh.getBoolean(daily,true));
        releaseSwitch.setChecked(sh.getBoolean(relese,true));
    }
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
