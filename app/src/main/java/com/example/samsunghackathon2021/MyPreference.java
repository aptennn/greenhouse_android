package com.example.samsunghackathon2021;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;
// класс для сохранения языка в Shared Preference
public class MyPreference {
    private final SharedPreferences preference;
    Context context;
    public MyPreference(Context context) {
        this.context = context;
        preference = context.getSharedPreferences("SharedPreferenceExample", Context.MODE_PRIVATE);
    }

    String SYSLANG = Locale.getDefault().getLanguage();
    public String getLoginCount(){
        return preference.getString("Language",SYSLANG);
    }
    public void setLoginCount(String Language){
        SharedPreferences.Editor editor = preference.edit();
        editor.putString("Language", Language);
        editor.apply();
    }


}
