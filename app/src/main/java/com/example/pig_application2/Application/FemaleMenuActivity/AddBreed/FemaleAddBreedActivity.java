package com.example.pig_application2.Application.FemaleMenuActivity.AddBreed;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pig_application2.API.ApiModel.ResponseModel;
import com.example.pig_application2.API.RetrofitClient.RetrofitClient;
import com.example.pig_application2.Application.FemaleMenuActivity.AddBreed.AddBreedSharedPreference.AddBreedSharedPreference;
import com.example.pig_application2.Application.FemaleMenuActivity.FemaleMenuActivity;
import com.example.pig_application2.Application.RfidMenu.RfidMenuActivity;
import com.example.pig_application2.InputValidation.TextViewValidation;
import com.example.pig_application2.R;
import com.example.pig_application2.Session.LoginSession;
import com.example.pig_application2.SharedPreferences.CurrentFarmSharedPreference;
import com.example.pig_application2.SharedPreferences.UserDataSharedPreference;
import com.example.pig_application2.othersClass.FormatDateAndTime.FormatDateAndTime;
import com.example.pig_application2.othersClass.PigDatePicker.PigDatePicker;
import com.example.pig_application2.othersClass.PigDatePicker.PigTimePicker;
import com.example.pig_application2.othersClass.RfidScanner.RfidScanner;
import com.google.gson.JsonObject;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FemaleAddBreedActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private final Integer pistolButtonID = 293;
    private final Integer backScreenButtonID = 4;
    private final FemaleAddBreedActivity Activity = this;
    private TextView ShowTagCode_TextView, ShowRfid_TextView, ShowBreedDate_TextView, ShowBreedTime_TextView, ShowCompanyUserBreederName_TextView, ShowSpermCode_TextView;
    private Button SetDate_Button, SetTime_Button, ScanCompanyUserId_Button, ScanSpermQrcode_Button, ScanRfid_Button, Submit_Button;
    private FormatDateAndTime breedDateAndTime;
    private String[] rfidData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female_add_breed);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initViews();
        initListeners();
        initObjects();
        setTextToTextViews();
    }

    private void initViews(){
        ShowTagCode_TextView                = findViewById(R.id.ShowTagCode_TextView_FemaleAddBreedActivity);
        ShowRfid_TextView                   = findViewById(R.id.ShowRfid_TextView_FemaleAddBreedActivity);
        ShowBreedDate_TextView              = findViewById(R.id.ShowWeanDate_TextView_FemaleAddBreedActivity);
        ShowBreedTime_TextView              = findViewById(R.id.ShowBornTime_TextView_FemaleAddBreedActivity);
        ShowCompanyUserBreederName_TextView = findViewById(R.id.ShowCompanyUserBreederName_TextView_FemaleAddBreedActivity);
        ShowSpermCode_TextView              = findViewById(R.id.ShowSpermCode_TextView_FemaleAddBreedActivity);
        SetDate_Button                      = findViewById(R.id.SetDate_Button_FemaleAddBreedActivity);
        SetTime_Button                      = findViewById(R.id.SetTime_Button_FemaleAddBreedActivity);
        ScanCompanyUserId_Button            = findViewById(R.id.ScanCompanyUserId_Button_FemaleAddBreedActivity);
        ScanSpermQrcode_Button              = findViewById(R.id.ScanSpermQrcode_Button_FemaleAddBreedActivity);
        ScanRfid_Button                     = findViewById(R.id.ScanRfid_Button_FemaleAddBreedActivity);
        Submit_Button                       = findViewById(R.id.Submit_Button_FemaleAddBreedActivity);
    }

    private void initObjects(){

        long dateAndTimeFormSharedPreference = AddBreedSharedPreference.getBreedDateAndTime(Activity);
        if(dateAndTimeFormSharedPreference != 0){
            breedDateAndTime.setDateAndTimeWithMillis(dateAndTimeFormSharedPreference);
        } else {
            breedDateAndTime = new FormatDateAndTime(new Date());
        }
    }

    private void setTextToTextViews(){
        ShowTagCode_TextView.setText(AddBreedSharedPreference.getTagCode(Activity));
        ShowRfid_TextView.setText(AddBreedSharedPreference.getRfidCode(Activity));
        ShowBreedDate_TextView.setText(breedDateAndTime.getFormatDate());
        ShowBreedTime_TextView.setText(breedDateAndTime.getFormatTime());
        ShowCompanyUserBreederName_TextView.setText(AddBreedSharedPreference.getCompanyUserName(Activity));
        ShowSpermCode_TextView.setText(AddBreedSharedPreference.getSpermCode(Activity));
    }

    private boolean isInputValidate(){
        TextViewValidation tagCodeTextViewValidation = new TextViewValidation(ShowTagCode_TextView);
        TextViewValidation rfidCodeTextViewValidation = new TextViewValidation(ShowRfid_TextView);
        TextViewValidation companyUserBreederNameTextViewValidation = new TextViewValidation(ShowCompanyUserBreederName_TextView);
        TextViewValidation spermCodeTextViewValidation = new TextViewValidation(ShowSpermCode_TextView);

        if(!tagCodeTextViewValidation.isNotEmpty() || !rfidCodeTextViewValidation.isNotEmpty()){
            return false;
        } else if (!companyUserBreederNameTextViewValidation.isNotEmpty()) {
            return false;
        } else if (!spermCodeTextViewValidation.isNotEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private void initListeners(){
        ScanRfid_Button.setOnClickListener(Activity::scanRfid);
        SetDate_Button.setOnClickListener(Activity::showDatePickerDialog);
        SetTime_Button.setOnClickListener(Activity::showTimePickerDialog);
        ScanCompanyUserId_Button.setOnClickListener(Activity::scanCompanyUser);
        ScanSpermQrcode_Button.setOnClickListener(Activity::scanSperm);
        Submit_Button.setOnClickListener(Activity::addNewBreed);
    }

    private void setClickEnables(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ScanRfid_Button.setEnabled(true);
                SetDate_Button.setEnabled(true);
                SetTime_Button.setEnabled(true);
                ScanCompanyUserId_Button.setEnabled(true);
                ScanSpermQrcode_Button.setEnabled(true);
                Submit_Button.setEnabled(true);
            }
        }, 1000);
    }

    private void setClickDisables(){
        ScanRfid_Button.setEnabled(false);
        SetDate_Button.setEnabled(false);
        SetTime_Button.setEnabled(false);
        ScanCompanyUserId_Button.setEnabled(false);
        ScanSpermQrcode_Button.setEnabled(false);
        Submit_Button.setEnabled(false);
    }

    private void addNewBreed(View view) {
        setClickDisables();
        if(isInputValidate()){
            httpAddBreed();
        } else {
            setClickEnables();
        }
    }

    private void httpAddBreed(){
        JsonObject newBreed = new JsonObject();
        newBreed.addProperty("breed_date", breedDateAndTime.getDate().toString());
        newBreed.addProperty("sperm_code", ShowSpermCode_TextView.getText().toString());
        newBreed.addProperty("companyUser_breeder_id", AddBreedSharedPreference.getCompanyUserID(Activity));
        Call<ResponseModel> call = RetrofitClient.getInstance().getApi().addBreed(
                LoginSession.getAuthorizationToken(Activity),
                UserDataSharedPreference.getUserData(Activity).getCompanyName(),
                CurrentFarmSharedPreference.getFarmId(Activity),
                ShowRfid_TextView.getText().toString(),
                newBreed
        );

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                try {
                    if(response.body().getSuccess()){
                        Toast toastResponse = Toast.makeText(Activity, "เพิ่มข้อมูลเรียบร้อย", Toast.LENGTH_SHORT);
                        toastResponse.show();

                        AddBreedSharedPreference.clearData(Activity);

                        Intent FemaleAddBreedActivityIntent = new Intent(Activity, FemaleAddBreedActivity.class);
                        startActivity(FemaleAddBreedActivityIntent);
                    } else if(!response.body().getSuccess()){
                        if(response.body().getErrorType().equals("PigNotFound")){
                            Toast toast = Toast.makeText(Activity, "ไม่พบสุกร", Toast.LENGTH_LONG);
                            toast.show();
                        } else if(response.body().getErrorType().equals("LoginExpired")){
                            LoginSession.clearDataInLoginSession(Activity);
                        }
                    }
                } catch (Exception exception) {
                    Toast exceptionToast = Toast.makeText(Activity, "การเชื่อมต่อมีปัญหา", Toast.LENGTH_LONG);
                    exceptionToast.show();
                }
                setClickEnables();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast failureToast = Toast.makeText(Activity, "การเชื่อมต่อมีปัญหา", Toast.LENGTH_LONG);
                failureToast.show();
                setClickEnables();
            }
        });
    }

    private void scanRfid(View view) {
        setClickDisables();
        getRfidDataAndSetTexts();
        setClickEnables();
    }

    private void scanCompanyUser(View view){
        setClickDisables();
        Intent FemaleAddBreedScanCompanyUserQrcodeActivityIntent = new Intent(Activity, FemaleAddBreedScanCompanyUserQrcodeActivity.class);
        startActivity(FemaleAddBreedScanCompanyUserQrcodeActivityIntent);
        setClickEnables();
    }

    private void scanSperm(View view){
        setClickDisables();
        Intent FemaleAddBreedScanSpermQrcodeActivityIntent = new Intent(Activity, FemaleAddBreedScanSpermQrcodeActivity.class);
        startActivity(FemaleAddBreedScanSpermQrcodeActivityIntent);
        setClickEnables();
    }


    private void showDatePickerDialog(View view) {
        setClickDisables();
        PigDatePicker mDatePickerDialogFragment;
        mDatePickerDialogFragment = new PigDatePicker();
        mDatePickerDialogFragment.show(getSupportFragmentManager(),"DATE PICK");
        setClickEnables();
    }

    private void showTimePickerDialog(View view) {
        setClickDisables();
        PigTimePicker mTimePickerDialogFragment;
        mTimePickerDialogFragment = new PigTimePicker();
        mTimePickerDialogFragment.show(getSupportFragmentManager(),"TIME PICK");
        setClickEnables();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        breedDateAndTime.setDate(year, month, dayOfMonth);
        AddBreedSharedPreference.setBreedDateAndTime(Activity, breedDateAndTime.getDateAsMillis());
        ShowBreedDate_TextView.setText(breedDateAndTime.getFormatDate());
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        breedDateAndTime.setTime(hourOfDay, minute);
        AddBreedSharedPreference.setBreedDateAndTime(Activity, breedDateAndTime.getDateAsMillis());
        ShowBreedTime_TextView.setText(breedDateAndTime.getFormatTime());
    }

    private void getRfidDataAndSetTexts(){
        try {
            RfidScanner rfidUhf = new RfidScanner();
            rfidData = rfidUhf.getRfidData();
            AddBreedSharedPreference.setTagCode(Activity, rfidData[0]);
            AddBreedSharedPreference.setRfidCode(Activity, rfidData[1]);
            ShowTagCode_TextView.setText(rfidData[0]);
            ShowRfid_TextView.setText(rfidData[1]);
        }
        catch(Exception e) {
            Toast toast = Toast.makeText(Activity, "ไม่พบ Tag", Toast.LENGTH_SHORT);
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
                Intent FemaleMenuActivityIntent = new Intent(Activity, FemaleMenuActivity.class);
                startActivity(FemaleMenuActivityIntent);
                AddBreedSharedPreference.clearData(Activity);
                setClickEnables();
            }
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }
}