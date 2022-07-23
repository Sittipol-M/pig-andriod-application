package com.example.pig_application2.Application.FemaleMenuActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.pig_application2.Application.FemaleMenuActivity.AddBreed.FemaleAddBreedActivity;
import com.example.pig_application2.Application.FemaleMenuActivity.BreedDetail.FemaleBreedDetailScanRfidActivity;
import com.example.pig_application2.Application.MenuActivity;
import com.example.pig_application2.Application.SettingActivity.SettingActivity;
import com.example.pig_application2.Application.UserActivity.UserActivity;
import com.example.pig_application2.R;

public class FemaleMenuActivity extends AppCompatActivity {
    private final Integer backScreenButtonID = 4;
    private Intent intent;
    private ImageView Breed_ImageView, Setting_ImageView, AddBreed_ImageView, User_ImageView;
    private final FemaleMenuActivity Activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initViews();
        initListeners();
    }

    private void initViews(){
        Breed_ImageView     = findViewById(R.id.Breed_ImageView_FemaleMenuActivity);
        AddBreed_ImageView  = findViewById(R.id.AddBreed_ImageView_FemaleMenuActivity);
        User_ImageView      = findViewById(R.id.User_ImageView_FemaleMenuActivity);
        Setting_ImageView   = findViewById(R.id.Setting_ImageView_FemaleMenuActivity);
    }

    private void initListeners(){
        Breed_ImageView.setOnClickListener(Activity::onclick);
        AddBreed_ImageView.setOnClickListener(Activity::onclick);
        User_ImageView.setOnClickListener(Activity::toUserActivity);
        Setting_ImageView.setOnClickListener(Activity::toSettingActivity);
    }

    private void setClickEnables(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User_ImageView.setEnabled(true);
                Setting_ImageView.setEnabled(true);
                Breed_ImageView.setEnabled(true);
                AddBreed_ImageView.setEnabled(true);
            }
        }, 1000);
    }

    private void setClickDisables(){
        User_ImageView.setEnabled(false);
        Setting_ImageView.setEnabled(false);
        Breed_ImageView.setEnabled(false);
        AddBreed_ImageView.setEnabled(false);
    }

    private void onclick(View view) {
        setClickDisables();
        switch(view.getId()){
            case R.id.Breed_ImageView_FemaleMenuActivity:
                intent = new Intent(Activity, FemaleBreedDetailScanRfidActivity.class);
                break;
            case R.id.AddBreed_ImageView_FemaleMenuActivity:
                intent = new Intent(Activity, FemaleAddBreedActivity.class);
                break;
        }
        setClickEnables();
        startActivity(intent);
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