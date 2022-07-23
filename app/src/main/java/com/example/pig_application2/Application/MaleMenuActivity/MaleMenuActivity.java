package com.example.pig_application2.Application.MaleMenuActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.pig_application2.Application.MaleMenuActivity.AddSperm.MaleAddSpermActivity;
import com.example.pig_application2.Application.MaleMenuActivity.SpermDetail.MaleSpermDetailScanRfidActivity;
import com.example.pig_application2.Application.MenuActivity;
import com.example.pig_application2.Application.SettingActivity.SettingActivity;
import com.example.pig_application2.Application.UserActivity.UserActivity;
import com.example.pig_application2.R;

public class MaleMenuActivity extends AppCompatActivity {
    private final Integer backScreenButtonID = 4;
    private ImageView AddSperm_ImageView, Setting_ImageView, SpermDetail_ImageView, User_ImageView;
    private final MaleMenuActivity Activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_male_menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initViews();
        initListeners();
        setClickEnables();
    }

    private void initViews(){
        AddSperm_ImageView              = findViewById(R.id.AddSperm_ImageView_MaleMenuActivity);
        SpermDetail_ImageView           = findViewById(R.id.SpermDetail_ImageView_MaleMenuActivity);
        User_ImageView                  = findViewById(R.id.User_ImageView_MaleMenuActivity);
        Setting_ImageView               = findViewById(R.id.Setting_ImageView_MaleMenuActivity);
    }

    private void initListeners(){
        User_ImageView.setOnClickListener(Activity::toUserActivity);
        Setting_ImageView.setOnClickListener(Activity::toSettingActivity);
        AddSperm_ImageView.setOnClickListener(Activity::toMaleAddSpermActivity);
        SpermDetail_ImageView.setOnClickListener(Activity::toMaleSpermDetailActivity);
    }

    private void setClickEnables(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User_ImageView.setEnabled(true);
                Setting_ImageView.setEnabled(true);
                AddSperm_ImageView.setEnabled(true);
                SpermDetail_ImageView.setEnabled(true);
            }
        }, 1000);
    }

    private void setClickDisables(){
        User_ImageView.setEnabled(false);
        Setting_ImageView.setEnabled(false);
        AddSperm_ImageView.setEnabled(false);
        SpermDetail_ImageView.setEnabled(false);
    }

    private void toSettingActivity(View view) {
        setClickDisables();
        Intent SettingActivity = new Intent(Activity, SettingActivity.class);
        startActivity(SettingActivity);
        setClickEnables();
    }

    private void toUserActivity(View view) {
        setClickDisables();
        Intent UserActivityIntent = new Intent(Activity, UserActivity.class);
        startActivity(UserActivityIntent);
        setClickEnables();
    }

    private void toMaleAddSpermActivity(View view){
        setClickDisables();
        Intent MaleAddSpermActivityIntent = new Intent(Activity, MaleAddSpermActivity.class);
        startActivity(MaleAddSpermActivityIntent);
        setClickEnables();
    }

    private void toMaleSpermDetailActivity(View view){
        setClickDisables();
        Intent MaleSpermDetailActivityIntent = new Intent(Activity, MaleSpermDetailScanRfidActivity.class);
        startActivity(MaleSpermDetailActivityIntent);
        setClickEnables();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getRepeatCount() == 0) {
            setClickDisables();
            if (keyCode == backScreenButtonID) {
                Intent MenuActivityIntent = new Intent(Activity, MenuActivity.class);
                startActivity(MenuActivityIntent);
                setClickEnables();
            }
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }
}