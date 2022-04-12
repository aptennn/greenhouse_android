package com.example.samsunghackathon2021;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

import kotlin.Suppress;
// работа с контекстом для локализации
public class MyContextWrapper extends ContextWrapper {
    public MyContextWrapper(Context base) {
        super(base);
    }


    @Suppress(names = "DEPRECATION")
    public static MyContextWrapper wrap(Context ctx, String language){
        Context context = ctx;
        Configuration config = context.getResources().getConfiguration();
        Locale sysLocale;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            sysLocale = getSystemLocale(config);

        }else{
            sysLocale = getSystemLocaleLegacy(config); }
        if (language != "" && sysLocale.getLanguage() != language) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setSystemLocale(config, locale);
            } else {
                setSystemLocaleLegacy(config, locale);
            }

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context = context.createConfigurationContext(config);
        } else {
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
        return new MyContextWrapper(context);
    }
    @Suppress(names = "DEPRECATION")
    private static Locale getSystemLocaleLegacy(Configuration config){
        return config.locale;
    }
    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSystemLocale(Configuration config){
        return config.getLocales().get(0);
    }
    @Suppress(names = "DEPRECATION")
    private static void setSystemLocaleLegacy(Configuration config, Locale locale){
        config.locale = locale;
    }
    @TargetApi(Build.VERSION_CODES.N)
    public static void setSystemLocale(Configuration config, Locale locale){
        config.setLocale(locale);
    }


}
