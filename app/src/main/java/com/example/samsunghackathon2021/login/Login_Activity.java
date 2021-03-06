package com.example.samsunghackathon2021.login;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.samsunghackathon2021.MainActivity;
import com.example.samsunghackathon2021.R;

public class Login_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        if (loadState().equals("")){
            setContentView(R.layout.layout_login);
            EditText et_log = findViewById(R.id.edit_text_login);
            Button log_btn = findViewById(R.id.btn_login);
            log_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (et_log.getText().toString().isEmpty()){
                        Toast.makeText(Login_Activity.this, "Empty error", Toast.LENGTH_SHORT).show();
                    }else{
                        saveState(et_log.getText().toString());
                        Intent i = new Intent(Login_Activity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(i);
                        finish();
                    }

                }
            });
            et_log.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){
                        log_btn.callOnClick();
                        return true;
                    }
                    return false;
                }
            });
        }else{
            Intent i = new Intent(Login_Activity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(i);
            finish();
        }



    }
    public void saveState(String auth) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Account", MODE_PRIVATE);
        SharedPreferences.Editor editor  = sharedPreferences.edit();
        editor.putString("GreenHouse_code", auth);
        editor.apply();
    }
    public String loadState() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Account", MODE_PRIVATE);
        return sharedPreferences.getString("GreenHouse_code", "");

    }
}
