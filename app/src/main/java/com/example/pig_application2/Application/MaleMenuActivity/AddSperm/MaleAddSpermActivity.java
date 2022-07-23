package com.example.pig_application2.Application.MaleMenuActivity.AddSperm;

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
import com.example.pig_application2.Application.FemaleMenuActivity.AddBreed.FemaleAddBreedActivity;
import com.example.pig_application2.Application.FemaleMenuActivity.AddBreed.FemaleAddBreedScanCompanyUserQrcodeActivity;
import com.example.pig_application2.Application.FemaleMenuActivity.FemaleMenuActivity;
import com.example.pig_application2.Application.MaleMenuActivity.AddSperm.AddSpermSharedPreferences.AddSpermSharedPreferences;
import com.example.pig_application2.Application.MaleMenuActivity.MaleMenuActivity;
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

import org.w3c.dom.Text;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaleAddSpermActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private final Integer pistolButtonID = 293;
    private final Integer backScreenButtonID = 4;
    private final MaleAddSpermActivity Activity = this;
    private TextView ShowTagCode_TextView, ShowRfid_TextView, ShowSpermCollectDate_TextView, ShowSpermCollectTime_TextView, ShowCompanyUserCollectSpermName;
    private Button ScanRfid_Button, SetDate_Button, SetTime_Button, ScanCompanyUserCollectSperm_Button, Submit_Button;
    private String[] rfidData;
    private FormatDateAndTime addSpermDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_male_add_sperm);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initViews();
        initListeners();
        initObjects();
        setTextToTextViews();
    }

    private void initViews(){
        ShowTagCode_TextView            = findViewById(R.id.ShowTagCode_TextView_MaleAddSpermActivity);
        ShowRfid_TextView               = findViewById(R.id.ShowRfid_TextView_MaleAddSpermActivity);
        ShowCompanyUserCollectSpermName = findViewById(R.id.ShowCompanyUserCollectSpermName_TextView_MaleAddSpermActivity);
        ShowSpermCollectDate_TextView   = findViewById(R.id.ShowSpermCollectDate_TextView_MaleAddSpermActivity);
        ShowSpermCollectTime_TextView   = findViewById(R.id.ShowSpermCollectTime_TextView_MaleAddSpermActivity);
        ScanRfid_Button                 = findViewById(R.id.ScanRfid_Button_MaleAddSpermActivity);
        ScanCompanyUserCollectSperm_Button = findViewById(R.id.ScanCompanyUserCollectSperm_Button_MaleAddSpermActivity);
        SetDate_Button                  = findViewById(R.id.SetDate_Button_MaleAddSpermActivity);
        SetTime_Button                  = findViewById(R.id.SetTime_Button_MaleAddSpermActivity);
        Submit_Button                   = findViewById(R.id.Submit_Button_MaleAddSpermActivity);
    }

    private void initObjects(){
        long dateAndTimeFormSharedPreference = AddSpermSharedPreferences.getSpermDateAndTime(Activity);
        if(dateAndTimeFormSharedPreference != 0){
            addSpermDate.setDateAndTimeWithMillis(AddSpermSharedPreferences.getSpermDateAndTime(Activity));
        } else {
            addSpermDate = new FormatDateAndTime(new Date());
        }
    }

    private void setTextToTextViews(){
        ShowTagCode_TextView.setText(AddSpermSharedPreferences.getTagCode(Activity));
        ShowRfid_TextView.setText(AddSpermSharedPreferences.getRfidCode(Activity));
        ShowCompanyUserCollectSpermName.setText(AddSpermSharedPreferences.getCompanyUserName(Activity));
        ShowSpermCollectDate_TextView.setText(addSpermDate.getFormatDate());
        ShowSpermCollectTime_TextView.setText(addSpermDate.getFormatTime());
    }

    private void initListeners(){
        ScanRfid_Button.setOnClickListener(Activity::scanRfid);
        ScanCompanyUserCollectSperm_Button.setOnClickListener(Activity::scanCompanyUser);
        SetDate_Button.setOnClickListener(Activity::showDatePickerDialog);
        SetTime_Button.setOnClickListener(Activity::showTimePickerDialog);
        Submit_Button.setOnClickListener(Activity::toAddSperm);
    }

    private void scanCompanyUser(View view) {
        setClickDisables();
        Intent MaleAddSpermScanCompanyUserQrcodeActivityIntent = new Intent(Activity, MaleAddSpermScanCompanyUserQrcodeActivity.class);
        startActivity(MaleAddSpermScanCompanyUserQrcodeActivityIntent);
        setClickEnables();
    }

    private boolean isInputValidate(){
        TextViewValidation tagCodeTextViewValidation = new TextViewValidation(ShowTagCode_TextView);
        TextViewValidation rfidCodeTextViewValidation = new TextViewValidation(ShowRfid_TextView);
        TextViewValidation companyUserTextViewValidation = new TextViewValidation(ShowCompanyUserCollectSpermName);
        if(!tagCodeTextViewValidation.isNotEmpty() || !rfidCodeTextViewValidation.isNotEmpty()){
            return false;
        } else if (!companyUserTextViewValidation.isNotEmpty()){
            return false;
        } else {
            return true;
        }
    }

    private void setClickEnables(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ScanRfid_Button.setEnabled(true);
                SetDate_Button.setEnabled(true);
                SetTime_Button.setEnabled(true);
                Submit_Button.setEnabled(true);
                ScanCompanyUserCollectSperm_Button.setEnabled(true);
            }
        }, 1000);
    }

    private void setClickDisables(){
        ScanRfid_Button.setEnabled(false);
        SetDate_Button.setEnabled(false);
        SetTime_Button.setEnabled(false);
        Submit_Button.setEnabled(false);
        ScanCompanyUserCollectSperm_Button.setEnabled(false);
    }

    private void toAddSperm(View view){
        setClickDisables();
        if(isInputValidate()){
            httpAddSperm();
        } else {
            setClickEnables();
        }
    }

    private void scanRfid(View view) {
        setClickEnables();
        getRfidDataAndSetText();
        setClickDisables();
    }

    private void getRfidDataAndSetText(){
        try {
            RfidScanner rfidUhf = new RfidScanner();
            rfidData = rfidUhf.getRfidData();
            AddSpermSharedPreferences.setTagCode(Activity, rfidData[0]);
            AddSpermSharedPreferences.setRfidCode(Activity, rfidData[1]);
            ShowTagCode_TextView.setText(rfidData[0]);
            ShowRfid_TextView.setText(rfidData[1]);
        }
        catch(Exception e) {
            Toast toast = Toast.makeText(Activity, "ไม่พบ Tag", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void httpAddSperm(){
        JsonObject newSperm = new JsonObject();
        newSperm.addProperty("date_collect_sperm", addSpermDate.getDate().toString());
        newSperm.addProperty("companyUser_collect_sperm", AddSpermSharedPreferences.getCompanyUserID(Activity));
        Call<ResponseModel> call = RetrofitClient.getInstance().getApi().addSperm(
                LoginSession.getAuthorizationToken(Activity),
                UserDataSharedPreference.getUserData(Activity).getCompanyName(),
                CurrentFarmSharedPreference.getFarmId(Activity),
                ShowRfid_TextView.getText().toString(),
                newSperm
        );
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                try {
                    if(response.body().getSuccess()){
                        Toast toastResponse = Toast.makeText(Activity, "เพิ่มข้อมูลเรียบร้อย", Toast.LENGTH_SHORT);
                        toastResponse.show();

                        AddSpermSharedPreferences.clearData(Activity);

                        Intent MaleAddSpermActivityIntent = new Intent(Activity, MaleAddSpermActivity.class);
                        startActivity(MaleAddSpermActivityIntent);
                    } else if(!response.body().getSuccess()) {
                        if(response.body().getErrorType().equals("PigNotFound")){
                            Toast toast = Toast.makeText(Activity, "ไม่พบสุกร", Toast.LENGTH_LONG);
                            toast.show();
                        }else if(response.body().getErrorType().equals("LoginExpired")){
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
        addSpermDate.setDate(year, month, dayOfMonth);
        AddSpermSharedPreferences.setSpermDateAndTime(Activity, addSpermDate.getDateAsMillis());
        ShowSpermCollectDate_TextView.setText(addSpermDate.getFormatDate());
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        addSpermDate.setTime(hourOfDay, minute);
        AddSpermSharedPreferences.setSpermDateAndTime(Activity, addSpermDate.getDateAsMillis());
        ShowSpermCollectTime_TextView.setText(addSpermDate.getFormatTime());
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
        if (event.getRepeatCount() == 0) {
            setClickDisables();
            if (keyCode == pistolButtonID) {
                getRfidDataAndSetTexts();
                setClickEnables();
            }
            else if (keyCode == backScreenButtonID) {
                Intent MaleMenuActivityIntent = new Intent(Activity, MaleMenuActivity.class);
                AddSpermSharedPreferences.clearData(Activity);
                startActivity(MaleMenuActivityIntent);
                setClickEnables();
            }
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }
}