package com.example.pig_application2.Application.RfidMenu.PairRfid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pig_application2.API.ApiModel.PigApiModel;
import com.example.pig_application2.API.RetrofitClient.RetrofitClient;
import com.example.pig_application2.Application.RfidMenu.RfidMenuActivity;
import com.example.pig_application2.InputValidation.EditTextValidation;
import com.example.pig_application2.InputValidation.TextViewValidation;
import com.example.pig_application2.R;
import com.example.pig_application2.Session.LoginSession;
import com.example.pig_application2.SharedPreferences.CurrentFarmSharedPreference;
import com.example.pig_application2.SharedPreferences.UserDataSharedPreference;
import com.example.pig_application2.othersClass.RfidScanner.RfidScanner;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PairRfidActivity extends AppCompatActivity {
    private final Integer pistolButtonID = 293;
    private final Integer backScreenButtonID = 4;
    private final PairRfidActivity Activity = this;
    private EditText PigCode_EditText;
    private TextView ShowTagCode_TextView, ShowRfid_TextView;
    private Button ScanRfid_Button, PairRfidWithPig_Button;
    private Spinner SelectSex_Spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_rfid);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initViews();
        initListenersAndEnables();
    }

    private void initViews(){
        ShowTagCode_TextView        = findViewById(R.id.ShowTagCode_TextView_PairRfidActivity);
        PigCode_EditText            = findViewById(R.id.PigCode_EditText_PairRfidActivity);
        ScanRfid_Button             = findViewById(R.id.ScanRfid_Button_PairRfidActivity);
        PairRfidWithPig_Button      = findViewById(R.id.PairRfidWithPig_Button_PairRfidActivity);
        SelectSex_Spinner           = findViewById(R.id.SelectSex_Spinner_PairRfidActivity);
        ShowRfid_TextView           = findViewById(R.id.ShowRfid_TextView_PairRfidActivity);
    }

    private void initListenersAndEnables(){
        PairRfidWithPig_Button.setOnClickListener(Activity::toAddNewPig);
        ScanRfid_Button.setOnClickListener(Activity::scanRfidAndSetTexts);
    }

    private void setClickEnables(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PairRfidWithPig_Button.setEnabled(true);
                ScanRfid_Button.setEnabled(true);
            }
        }, 1000);
    }

    private void setClickDisables(){
        PairRfidWithPig_Button.setEnabled(false);
        ScanRfid_Button.setEnabled(false);
    }

    private void scanRfidAndSetTexts(View view){
        setClickDisables();
        getRfidDataAndSetTexts();
        setClickEnables();
    }

    private boolean isInputValidation(){
        EditTextValidation pigCodeEditTextValidation = new EditTextValidation(PigCode_EditText);
        TextViewValidation rfidCodeTextViewValidation = new TextViewValidation(ShowRfid_TextView);
        TextViewValidation tagCodeTextViewValidation = new TextViewValidation(ShowTagCode_TextView);
        if(!pigCodeEditTextValidation.isAlphaNum() || !pigCodeEditTextValidation.isNotEmpty()){
            return false;
        }
        else if(!tagCodeTextViewValidation.isNotEmpty() || !rfidCodeTextViewValidation.isNotEmpty()){
            return false;
        }
        else {
            return true;
        }
    }

    private void toAddNewPig(View view) {
        setClickDisables();
        if(isInputValidation()){
            httpNewPigWithRfid();
        } else {
            setClickEnables();
        }
    }

    private void httpNewPigWithRfid(){
        String pigSex = null;
        if (SelectSex_Spinner.getSelectedItem().toString().equals("เพศผู้")) {
            pigSex =  "male";
        } else if (SelectSex_Spinner.getSelectedItem().toString().equals("เพศเมีย")) {
            pigSex =  "female";
        }
        JsonObject newPigJsonPost = new JsonObject();
        newPigJsonPost.addProperty("pig_code", PigCode_EditText.getText().toString());
        newPigJsonPost.addProperty("rfid_code", ShowRfid_TextView.getText().toString());
        newPigJsonPost.addProperty("sex", pigSex);


        Call<PigApiModel> call = RetrofitClient.getInstance().getApi().newPigWithRfid(
                LoginSession.getAuthorizationToken(Activity),
                UserDataSharedPreference.getUserData(Activity).getCompanyName(),
                CurrentFarmSharedPreference.getFarmId(Activity),
                newPigJsonPost
        );

        call.enqueue(new Callback<PigApiModel>() {
            @Override
            public void onResponse(Call<PigApiModel> call, Response<PigApiModel> response) {
                try {
                    PigApiModel pigResponseBody = response.body();
                    if (pigResponseBody.getSuccess()) {
                        Toast toast = Toast.makeText(Activity, "เพิ่มหมูสำเร็จ", Toast.LENGTH_LONG);
                        toast.show();

                        Intent PairRfidActivityIntent = new Intent(Activity, Activity.getClass());
                        startActivity(PairRfidActivityIntent);

                    } else {
                        String errorType = pigResponseBody.getErrorType();
                        if(errorType.equals("PigExisted")){
                            Toast toast = Toast.makeText(Activity, "มีข้อมูลหมูแล้ว", Toast.LENGTH_LONG);
                            toast.show();
                        } else if(errorType.equals("LoginExpired")){
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
                Toast failureToast = Toast.makeText(Activity, "การเชื่อมต่อมีปัญหา", Toast.LENGTH_LONG);
                failureToast.show();
                setClickEnables();
            }
        });
    }

    private void getRfidDataAndSetTexts(){
        String[] rfidData;
        try {
            RfidScanner rfidUhf = new RfidScanner();
            rfidData = rfidUhf.getRfidData();
            ShowTagCode_TextView.setText(rfidData[0]);
            ShowRfid_TextView.setText(rfidData[1]);
        }
        catch(Exception e) {
            Toast toast = Toast.makeText(Activity, "ไม่พบ Tag", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setClickDisables();
        if (event.getRepeatCount() == 0) {
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