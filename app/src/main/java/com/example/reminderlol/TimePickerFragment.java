package com.example.reminderlol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {

    Calendar calendar = Calendar.getInstance();

    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            return new TimePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, (TimePickerDialog.OnTimeSetListener) getActivity(), hour, minute, true);
        }else{
            return new TimePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, (TimePickerDialog.OnTimeSetListener) getActivity(), hour, minute, true);
        }
    }
}
