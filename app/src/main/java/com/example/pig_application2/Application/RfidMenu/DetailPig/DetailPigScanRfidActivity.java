package com.example.pig_application2.Application.RfidMenu.DetailPig;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pig_application2.API.ApiModel.PigApiModel;
import com.example.pig_application2.API.DataModel.PigModel;
import com.example.pig_application2.API.RetrofitClient.RetrofitClient;
import com.example.pig_application2.Application.RfidMenu.RfidMenuActivity;
import com.example.pig_application2.InputValidation.TextViewValidation;
import com.example.pig_application2.R;
import com.example.pig_application2.Session.LoginSession;
import com.example.pig_application2.SharedPreferences.CurrentFarmSharedPreference;
import com.example.pig_application2.SharedPreferences.UserDataSharedPreference;
import com.example.pig_application2.othersClass.RfidScanner.RfidScanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPigScanRfidActivity extends AppCompatActivity {
    private final Integer pistolButtonID = 293;
    private final Integer backScreenButtonID = 4;
    private final DetailPigScanRfidActivity Activity = this;
    private final String TAGCODE = "tag_code";
    private final String RFIDCODE = "rfid_code";
    private final String PIGCODE = "pig_code";
    private final String SEX = "sex";
    private final String BLOCKCODE = "block_code";
    private final String UNITCODE = "unit_code";
    private TextView ShowTagCode_Textview, ShowRfid_Textview;
    private Button ScanRfid_Button, Next_Button;
    private String[] rfidData;
    private Bundle dataBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pig_scan_rfid);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initViews();
        initListeners();
        initObjects();
    }

    private void initViews(){
        ShowTagCode_Textview       = findViewById(R.id.ShowTagCode_Textview_DetailPigScanRfidActivity);
        ShowRfid_Textview          = findViewById(R.id.ShowRfid_Textview_DetailPigScanRfidActivity);
        ScanRfid_Button            = findViewById(R.id.ScanRfid_Button_DetailPigScanRfidActivity);
        Next_Button                = findViewById(R.id.Submit_Button_FemaleBreedUpdateWeanDateActivity);
    }

    private void initListeners(){
        ScanRfid_Button.setOnClickListener(Activity::scanRfidAndSetTexts);
        Next_Button.setOnClickListener(Activity::toShowDetail);
    }

    private void setClickEnables(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ScanRfid_Button.setEnabled(true);
                Next_Button.setEnabled(true);
            }
        }, 1000);
    }

    private void setClickDisables(){
        ScanRfid_Button.setEnabled(false);
        Next_Button.setEnabled(false);
    }

    private void initObjects(){
        dataBundle = new Bundle();
    }

    private void scanRfidAndSetTexts(View view) {
        setClickDisables();
        getRfidDataAndSetTexts();
        setClickEnables();
    }

    private boolean isInputValidate(){
        TextViewValidation tagCodeTextViewValidation = new TextViewValidation(ShowTagCode_Textview);
        TextViewValidation rfidCodeTextViewValidation = new TextViewValidation(ShowRfid_Textview);
        if(!tagCodeTextViewValidation.isNotEmpty() || !rfidCodeTextViewValidation.isNotEmpty()){
            return false;
        } else {
            return true;
        }
    }

    private void toShowDetail(View view) {
        setClickDisables();
        if(isInputValidate()){
            httpGetPig();
        }
        else{
            setClickEnables();
        }
    }

    private void getRfidDataAndSetTexts(){
        try {
            RfidScanner rfidUhf = new RfidScanner();
            rfidData = rfidUhf.getRfidData();
            ShowTagCode_Textview.setText(rfidData[0]);
            ShowRfid_Textview.setText(rfidData[1]);
        }
        catch(Exception e) {
            Toast toast = Toast.makeText(Activity, "ไม่พบ Tag", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void httpGetPig(){
        Call<PigApiModel> call = RetrofitClient.getInstance().getApi().getPig(
                LoginSession.getAuthorizationToken(Activity),
                UserDataSharedPreference.getUserData(Activity).getCompanyName(),
                CurrentFarmSharedPreference.getFarmId(Activity),
                ShowRfid_Textview.getText().toString()
        );
        call.enqueue(new Callback<PigApiModel>() {
            @Override
            public void onResponse(Call<PigApiModel> call, Response<PigApiModel> response) {
                try{
                    if(response.body().getSuccess()){
                        PigModel pig = response.body().getPig();
                        dataBundle.putString(TAGCODE, ShowTagCode_Textview.getText().toString());
                        dataBundle.putString(RFIDCODE, pig.getRfidCode());
                        dataBundle.putString(PIGCODE, pig.getPigCode());
                        dataBundle.putString(SEX, pig.getSex());
                        dataBundle.putString(BLOCKCODE,pig.getBlockCode());
                        dataBundle.putString(UNITCODE,pig.getUnitCode());

                        Intent DetailPigActivityIntent = new Intent(Activity, DetailPigActivity.class);
                        DetailPigActivityIntent.putExtras(dataBundle);
                        startActivity(DetailPigActivityIntent);
                    }
                    else if(!response.body().getSuccess()){
                        if(response.body().getErrorType().equals("PigNotFound")){
                            Toast toastResponse = Toast.makeText(Activity, "ไม่พบสุกร", Toast.LENGTH_LONG);
                            toastResponse.show();
                        } else if(response.body().getErrorType().equals("LoginExpired")){
                            LoginSession.clearDataInLoginSession(Activity);
                        }
                    }
                } catch (Exception exception){
                    Toast exceptionToast = Toast.makeText(Activity, "การเชื่อมต่อมีปัญหา", Toast.LENGTH_LONG);
                    exceptionToast.show();
                }
                setClickEnables();
            }

            @Override
            public void onFailure(Call<PigApiModel> call, Throwable t) {
                Toast failureResponse = Toast.makeText(Activity, "การเชื่อมต่อมีปัญหา", Toast.LENGTH_LONG);
                failureResponse.show();
                setClickEnables();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getRepeatCount() == 0) {
            setClickDisables();
            if (keyCode == pistolButtonID) {
                getRfidDataAndSetTexts();
                setClickEnables();
            }
            else if (keyCode == backScreenButtonID) {
                Intent RfidMenuActivityIntent = new Intent(Activity, RfidMenuActivity.class);
                startActivity(RfidMenuActivityIntent);
                setClickEnables();
            }
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }

}