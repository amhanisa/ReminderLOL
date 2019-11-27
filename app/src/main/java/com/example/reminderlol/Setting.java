package com.example.reminderlol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class Setting extends AppCompatActivity {

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.setting_frag, new PreferenceFragment()).commit();
    }


//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
//        System.out.println("ada yang berubah");
//        if(s.equals("dark_preference")){
//            if(sharedPreferences.getBoolean(s, false)){
//                findViewById(R.id.setting_frag).setBackgroundColor(Color.parseColor("#333333"));
//                System.out.println("hey");
//            } else{
//                findViewById(R.id.setting_frag).setBackgroundColor(Color.parseColor("#FFFFFF"));
//                System.out.println("hay");
//            }
//        }
//    }

    public static class PreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            System.out.println("ada yang berubah");
            if (s.equals("dark_preference")) {
                if (sharedPreferences.getBoolean(s, false)) {
                    getActivity().findViewById(R.id.setting_frag).setBackgroundColor(Color.parseColor("#333333"));
                    System.out.println("hey");
                } else {
                    getActivity().findViewById(R.id.setting_frag).setBackgroundColor(Color.parseColor("#FFFFFF"));
                    System.out.println("hay");
                }
            }
        }
    }
}
