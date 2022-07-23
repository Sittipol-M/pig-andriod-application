package com.example.pig_application2.Application.RfidMenu;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.pig_application2.Application.MenuActivity;
import com.example.pig_application2.Application.RfidMenu.DetailPig.DetailPigScanRfidActivity;
import com.example.pig_application2.Application.RfidMenu.PairBlockAndUnit.PairBlockAndUnitUpdateBlockAndUnit;
import com.example.pig_application2.Application.RfidMenu.PairRfid.PairRfidActivity;
import com.example.pig_application2.Application.SettingActivity.SettingActivity;
import com.example.pig_application2.Application.UserActivity.UserActivity;
import com.example.pig_application2.R;

public class RfidMenuActivity extends AppCompatActivity {
    private final Integer backScreenButtonID = 4;
    private final RfidMenuActivity Activity = this;
    private ImageView PairRfid_ImageView, PairBlock_ImageView, DetailPig_ImageView, User_ImageView, Setting_ImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfid_menu);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initViews();
        initListeners();
    }

    private void initViews(){
        PairRfid_ImageView      = findViewById(R.id.PairRfid_ImageView_RfidMenuActivity);
        PairBlock_ImageView     = findViewById(R.id.PairBlock_ImageView_RfidMenuActivity);
        DetailPig_ImageView     = findViewById(R.id.DetailPig_ImageView_RfidMenuActivity);
        User_ImageView          = findViewById(R.id.User_ImageView_RfidMenuActivity);
        Setting_ImageView       = findViewById(R.id.Setting_ImageView_RfidMenuActivity);
    }

    private void initListeners(){
        PairBlock_ImageView.setOnClickListener(Activity::toPairBlockAndUnitScanQrcodeActivity);
        PairRfid_ImageView.setOnClickListener(Activity::toPairRfidActivity);
        DetailPig_ImageView.setOnClickListener(Activity::toDetailPigScanRfidActivity);
        User_ImageView.setOnClickListener(Activity::toUserActivity);
        Setting_ImageView.setOnClickListener(Activity::toSettingActivity);
    }

    private void setClickEnables(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PairBlock_ImageView.setEnabled(true);
                PairRfid_ImageView.setEnabled(true);
                DetailPig_ImageView.setEnabled(true);
                User_ImageView.setEnabled(true);
                Setting_ImageView.setEnabled(true);
            }
        }, 1000);
    }

    private void setClickDisables(){
        PairBlock_ImageView.setEnabled(false);
        PairRfid_ImageView.setEnabled(false);
        DetailPig_ImageView.setEnabled(false);
        User_ImageView.setEnabled(false);
        Setting_ImageView.setEnabled(false);
    }


    private void toPairRfidActivity(View view){
        setClickDisables();
        Intent PairRfidActivityIntent = new Intent(Activity, PairRfidActivity.class);
        startActivity(PairRfidActivityIntent);
        setClickEnables();
    }

    private void toPairBlockAndUnitScanQrcodeActivity(View view){
        setClickDisables();
        Intent UpdateBlockAndUnitNameActivityIntent = new Intent(Activity, PairBlockAndUnitUpdateBlockAndUnit.class);
        startActivity(UpdateBlockAndUnitNameActivityIntent);
        setClickEnables();
    }

    private void toDetailPigScanRfidActivity(View view){
        setClickDisables();
        Intent DetailPigScanRfidActivityIntent = new Intent(Activity, DetailPigScanRfidActivity.class);
        startActivity(DetailPigScanRfidActivityIntent);
        setClickEnables();
    }

    private void toSettingActivity(View view) {
        setClickDisables();
        Intent SettingActivityIntent = new Intent(Activity, SettingActivity.class);
        startActivity(SettingActivityIntent);
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