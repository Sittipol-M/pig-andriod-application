package com.example.pig_application2.Application;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pig_application2.Application.FemaleMenuActivity.FemaleMenuActivity;
import com.example.pig_application2.Application.MaleMenuActivity.MaleMenuActivity;
import com.example.pig_application2.Application.RfidMenu.RfidMenuActivity;
import com.example.pig_application2.Application.SettingActivity.SettingActivity;
import com.example.pig_application2.Application.UserActivity.UserActivity;
import com.example.pig_application2.Application.VaccineMenu.VaccineMenuActivity;
import com.example.pig_application2.R;
import com.example.pig_application2.SharedPreferences.CurrentFarmSharedPreference;

public class MenuActivity extends AppCompatActivity {
    private final Integer backScreenButtonID = 4;
    private ImageView RfidMenuActivity_ImageView, VaccineMenuActivity_ImageView, FemaleBreederMenuActivity_ImageView,
            MaleBreedMenuActivity_ImageView, User_ImageView, Setting_ImageView;
    private TextView ShowFarmName_ImageView;
    private final MenuActivity Activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initViews();
        initListeners();
        setTextsToTextViews();
    }
    private void initViews(){
        User_ImageView                          = findViewById(R.id.User_ImageView_MenuActivity);
        Setting_ImageView                       = findViewById(R.id.Setting_ImageView_MenuActivity);
        RfidMenuActivity_ImageView              = findViewById(R.id.RfidMenuActivity_ImageView_MenuActivity);
        VaccineMenuActivity_ImageView           = findViewById(R.id.VaccineMenuActivity_ImageView_MenuActivity);
        FemaleBreederMenuActivity_ImageView     = findViewById(R.id.FemaleBreederMenuActivity_ImageView_MenuActivity);
        MaleBreedMenuActivity_ImageView         = findViewById(R.id.MaleBreederMenuActivity_ImageView_MenuActivity);
        ShowFarmName_ImageView                  = findViewById(R.id.ShowFarmName_TextView_MenuActivity);
    }

    private void initListeners(){
        User_ImageView.setOnClickListener(Activity::toUserActivity);
        Setting_ImageView.setOnClickListener(Activity::toSettingActivity);
        RfidMenuActivity_ImageView.setOnClickListener(Activity::toRfidMenuActivity);
        VaccineMenuActivity_ImageView.setOnClickListener(Activity::toVaccineMenuActivity);
        FemaleBreederMenuActivity_ImageView.setOnClickListener(Activity::toFemaleMenuActivity);
        MaleBreedMenuActivity_ImageView.setOnClickListener(Activity::toMaleMenuActivity);
    }

    private void setClickEnables(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User_ImageView.setEnabled(true);
                Setting_ImageView.setEnabled(true);
                RfidMenuActivity_ImageView.setEnabled(true);
                VaccineMenuActivity_ImageView.setEnabled(true);
                FemaleBreederMenuActivity_ImageView.setEnabled(true);
                MaleBreedMenuActivity_ImageView.setEnabled(true);
            }
        }, 1000);
    }

    private void setClickDisables(){
        User_ImageView.setEnabled(false);
        Setting_ImageView.setEnabled(false);
        RfidMenuActivity_ImageView.setEnabled(false);
        VaccineMenuActivity_ImageView.setEnabled(false);
        FemaleBreederMenuActivity_ImageView.setEnabled(false);
        MaleBreedMenuActivity_ImageView.setEnabled(false);
    }

    private void setTextsToTextViews(){
        ShowFarmName_ImageView.setText(CurrentFarmSharedPreference.getFarmName(Activity));
    }

    private void toMaleMenuActivity(View view){
        setClickDisables();
        Intent MaleMenuActivityIntent = new Intent(Activity, MaleMenuActivity.class);
        startActivity(MaleMenuActivityIntent);
        setClickEnables();
    }

    private void toFemaleMenuActivity(View view){
        setClickDisables();
        Intent FemaleMenuActivityIntent = new Intent(Activity, FemaleMenuActivity.class);
        startActivity(FemaleMenuActivityIntent);
        setClickEnables();
    }

    private void toRfidMenuActivity(View view){
        setClickDisables();
        Intent RfidMenuActivityIntent = new Intent(Activity, RfidMenuActivity.class);
        startActivity(RfidMenuActivityIntent);
        setClickEnables();
    }

    private void toVaccineMenuActivity(View view){
        setClickDisables();
        Intent VaccineMenuActivityIntent = new Intent(Activity, VaccineMenuActivity.class);
        startActivity(VaccineMenuActivityIntent);
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
                Intent FarmSelectionActivityIntent = new Intent(Activity, FarmSelectionActivity.class);
                startActivity(FarmSelectionActivityIntent);
                setClickEnables();
            }
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }

}