package com.example.pig_application2.Application.VaccineMenu;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.pig_application2.Application.MenuActivity;
import com.example.pig_application2.Application.SettingActivity.SettingActivity;
import com.example.pig_application2.Application.UserActivity.UserActivity;
import com.example.pig_application2.Application.VaccineMenu.VaccinationDetail.VaccinationDetailActivity;
import com.example.pig_application2.Application.VaccineMenu.VaccinationDetail.VaccinationDetailScanRfidActivity;
import com.example.pig_application2.Application.VaccineMenu.VaccinationEach.VaccinationEachActivity;
import com.example.pig_application2.Application.VaccineMenu.VaccinationUnit.VaccinationUnitActivity;
import com.example.pig_application2.R;

public class VaccineMenuActivity extends AppCompatActivity {
    private final Integer backScreenButtonID = 4;
    private final VaccineMenuActivity Activity = this;
    private Intent intent;
    private ImageView VaccineEach_ImageView, VaccineUnit_ImageView, User_ImageView, Setting_ImageView, VaccineDetail_ImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination_menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initViews();
        initListenersAndEnables();
    }
    private void initViews(){
        VaccineEach_ImageView       = findViewById(R.id.VaccineEach_ImageView_VaccineMenuActivity);
        VaccineUnit_ImageView       = findViewById(R.id.VaccineUnit_ImageView_VaccineMenuActivity);
        User_ImageView              = findViewById(R.id.User_ImageView_VaccineMenuActivity);
        VaccineDetail_ImageView     = findViewById(R.id.VaccineDetail_ImageView_VaccineMenuActivity);
        Setting_ImageView           = findViewById(R.id.Setting_ImageView_VaccineMenuActivity);
    }

    private void initListenersAndEnables(){
        VaccineEach_ImageView.setOnClickListener(Activity::onclick);
        VaccineUnit_ImageView.setOnClickListener(Activity::onclick);
        VaccineDetail_ImageView.setOnClickListener(Activity::onclick);
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
                VaccineEach_ImageView.setEnabled(true);
                VaccineUnit_ImageView.setEnabled(true);
                VaccineDetail_ImageView.setEnabled(true);
            }
        }, 1000);
    }

    private void setClickDisables(){
        User_ImageView.setEnabled(false);
        Setting_ImageView.setEnabled(false);
        VaccineEach_ImageView.setEnabled(false);
        VaccineUnit_ImageView.setEnabled(false);
        VaccineDetail_ImageView.setEnabled(false);
    }

    private void onclick(View view) {
        setClickDisables();
        switch (view.getId()){
            case R.id.VaccineEach_ImageView_VaccineMenuActivity:
                intent  = new Intent(Activity, VaccinationEachActivity.class);
                break;
            case R.id.VaccineUnit_ImageView_VaccineMenuActivity:
                intent = new Intent(Activity, VaccinationUnitActivity.class);
                break;
            case R.id.VaccineDetail_ImageView_VaccineMenuActivity:
                intent = new Intent(Activity, VaccinationDetailScanRfidActivity.class);
                break;
        }
        startActivity(intent);
        setClickEnables();
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