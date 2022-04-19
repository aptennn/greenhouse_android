package com.example.samsunghackathon2021.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.samsunghackathon2021.MainActivity;
import com.example.samsunghackathon2021.MyPreference;
import com.example.samsunghackathon2021.R;
import com.example.samsunghackathon2021.login.Login_Activity;

import org.jetbrains.annotations.NotNull;

import static android.content.Context.MODE_PRIVATE;

import java.util.Objects;


public class Settings extends Fragment {
    MyPreference myPreference;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override

    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        Switch switchBlinkPin13 = (Switch) view.findViewById(R.id.action_switch);
        if (loadState()) {
            switchBlinkPin13.setChecked(true);
        }
        switchBlinkPin13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switchBlinkPin13.isChecked()){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    saveState(true);

                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveState(false);


                }
            }
        });
        // выбор языков
        myPreference = new MyPreference(requireActivity());
        view.findViewById(R.id.button_rus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myPreference.setLoginCount("ru");
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        view.findViewById(R.id.button_eng).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myPreference.setLoginCount("en");
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.button_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveStateLog("");
                Intent i = new Intent(getActivity().getApplicationContext(), Login_Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        return view;
    }
    public void saveState(Boolean state) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Theme", MODE_PRIVATE);
        SharedPreferences.Editor editor  = sharedPreferences.edit();
        editor.putBoolean("NightMode", state);
        editor.apply();
    }
    public Boolean loadState() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Theme", MODE_PRIVATE);
        return sharedPreferences.getBoolean("NightMode", true);

    }
    public void saveStateLog(String auth) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Account", MODE_PRIVATE);
        SharedPreferences.Editor editor  = sharedPreferences.edit();
        editor.putString("GreenHouse_code", auth);
        editor.apply();
    }


}

