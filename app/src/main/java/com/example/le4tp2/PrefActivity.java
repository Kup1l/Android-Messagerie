package com.example.le4tp2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceActivity;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;


@EActivity(R.layout.activity_pref)
public class PrefActivity extends FragmentActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    SharedPreferences sp;

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("theme")) {
            boolean dark = sharedPreferences.getBoolean("theme", false);
            if (dark)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

            SharedPreferences.Editor edt = sp.edit();
            edt.putBoolean("theme", dark);
            edt.apply();
        }
    }

    @AfterViews
    public void create() {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);
    }
}
