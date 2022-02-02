package com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fadjarfirdaus.picodiploma.dicoding.submission4cataloguemovie.fragment.MyPreferenceFragment;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportFragmentManager().beginTransaction().add(R.id.setting_holder, new MyPreferenceFragment()).commit();
    }
}
