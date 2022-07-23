package com.example.pig_application2.Application.SettingActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pig_application2.R;
import com.example.pig_application2.othersClass.RfidScanner.RfidScanner;

public class SettingActivity extends AppCompatActivity {
    private final Integer backScreenButtonID = 4;
    private final SettingActivity Activity = this;
    private Button Submit_Button;
    private Spinner LevelRfid_Spinner;
    private RfidScanner rfidScanner;
    private Integer levelRfid = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initView();
        initObjects();
        initListeners();
        setLevelRfid_Spinner();
    }

    private void initView() {
        Submit_Button     = findViewById(R.id.Submit_Button_SettingActivity);
        LevelRfid_Spinner = findViewById(R.id.LevelRfid_Spinner_SettingActivity);
    }

    private void initObjects(){
        rfidScanner = new RfidScanner();
        levelRfid = rfidScanner.getPowerLevel();
    }

    private void initListeners(){
        Submit_Button.setOnClickListener(Activity::submit);
    }

    private void setClickEnables(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Submit_Button.setEnabled(true);
            }
        }, 1000);
    }

    private void setClickDisables(){
        Submit_Button.setEnabled(false);
    }

    private void setLevelRfid_Spinner(){
        LevelRfid_Spinner.setSelection(levelRfid-1);
    }

    private void submit(View view) {
        setClickDisables();
        levelRfid = Integer.valueOf(LevelRfid_Spinner.getSelectedItemPosition()+1);
        if(rfidScanner.setPowerLevel(levelRfid)){
            Activity.finish();
        }
        else{
            Toast toastResponse = Toast.makeText(Activity, "ไม่สำเร็จ", Toast.LENGTH_LONG);
            toastResponse.show();
        }
        setClickEnables();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setClickDisables();
        if (event.getRepeatCount() == 0) {
            if (keyCode == backScreenButtonID) {
                setClickEnables();
                Activity.finish();
            }
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }
}