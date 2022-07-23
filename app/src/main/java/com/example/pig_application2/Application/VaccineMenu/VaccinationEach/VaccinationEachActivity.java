package com.example.pig_application2.Application.VaccineMenu.VaccinationEach;

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
import com.example.pig_application2.API.ApiModel.VaccinationsApiModel;
import com.example.pig_application2.API.RetrofitClient.RetrofitClient;
import com.example.pig_application2.Application.VaccineMenu.VaccinationEach.NewVaccinationSharedPreference.NewVaccinationSharedPreference;
import com.example.pig_application2.Application.VaccineMenu.VaccineMenuActivity;
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

public class VaccinationEachActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private final Integer PistolButtonID = 293;
    private final Integer backScreenButtonID = 4;
    private final VaccinationEachActivity Activity = this;
    private TextView ShowTagCode_TextView, ShowRfid_TextView, ShowVaccineLot_TextView, ShowVaccinationDate_TextView, ShowVaccinationTime_TextView;
    private Button ScanRfid_Button, ScanVaccineLotCodeQrcode_Button, SetDate_Button, SetTime_Button, Submit_Button;
    private String[] rfidData;
    private FormatDateAndTime vaccinationDateAndTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination_each);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initViews();
        initObject();
        initListeners();
        setTextToTextViews();
    }

    private void initViews(){
        //TextView
        ShowTagCode_TextView            = findViewById(R.id.ShowTagCode_TextView_VaccinationEachActivity);
        ShowRfid_TextView               = findViewById(R.id.ShowRfid_TextView_VaccinationEachActivity);
        ShowVaccineLot_TextView         = findViewById(R.id.ShowVaccineLot_TextView_VaccinationEachActivity);
        ShowVaccinationDate_TextView    = findViewById(R.id.ShowVaccinationDate_TextView_VaccinationEachActivity);
        ShowVaccinationTime_TextView    = findViewById(R.id.ShowVaccinationTime_TextView_VaccinationEachActivity);
        //Button
        ScanRfid_Button                 = findViewById(R.id.ScanRfid_Button_VaccinationEachActivity);
        ScanVaccineLotCodeQrcode_Button = findViewById(R.id.ScanVaccineLotCodeQrcode_Button_VaccinationEachActivity);
        SetDate_Button                  = findViewById(R.id.SetDate_Button_VaccinationEachActivity);
        SetTime_Button                  = findViewById(R.id.SetTime_Button_VaccinationEachActivity);
        Submit_Button                   = findViewById(R.id.Submit_Button_VaccinationEachActivity);
    }


    private void initObject(){
            long dateAndTimeFormSharedPreference = NewVaccinationSharedPreference.getVaccinationDateAndTime(Activity);
            if(dateAndTimeFormSharedPreference != 0){
                vaccinationDateAndTime.setDateAndTimeWithMillis(dateAndTimeFormSharedPreference);
            } else {
                vaccinationDateAndTime = new FormatDateAndTime(new Date());
            }

    }


    private void initListeners(){
        ScanRfid_Button.setOnClickListener(Activity::scanRfid);
        ScanVaccineLotCodeQrcode_Button.setOnClickListener(Activity::scanVaccineLotCode);
        SetDate_Button.setOnClickListener(Activity::showDatePickerDialog);
        SetTime_Button.setOnClickListener(Activity::showTimePickerDialog);
        Submit_Button.setOnClickListener(Activity::submit);
    }

    private void setClickEnables(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ScanRfid_Button.setEnabled(true);
                ScanVaccineLotCodeQrcode_Button.setEnabled(true);
                SetDate_Button.setEnabled(true);
                SetTime_Button.setEnabled(true);
                Submit_Button.setEnabled(true);
            }
        }, 1000);
    }

    private void setClickDisables(){
        ScanRfid_Button.setEnabled(false);
        ScanVaccineLotCodeQrcode_Button.setEnabled(false);
        SetDate_Button.setEnabled(false);
        SetTime_Button.setEnabled(false);
        Submit_Button.setEnabled(false);
    }

    private void setTextToTextViews(){
        ShowTagCode_TextView.setText(NewVaccinationSharedPreference.getTagCode(Activity));
        ShowRfid_TextView.setText(NewVaccinationSharedPreference.getRfidCode(Activity));
        ShowVaccineLot_TextView.setText(NewVaccinationSharedPreference.getVaccineLotCode(Activity));
        ShowVaccinationDate_TextView.setText(vaccinationDateAndTime.getFormatDate());
        ShowVaccinationTime_TextView.setText(vaccinationDateAndTime.getFormatTime());
    }


    private void scanRfid(View view) {
        setClickDisables();
        getRfidDataAndSetText();
        setClickEnables();
    }

    private void scanVaccineLotCode(View view) {
        setClickDisables();
        Intent VaccinationEachScanQrCodeVaccineActivityIntent = new Intent(Activity, VaccinationEachScanQrCodeVaccineActivity.class);
        startActivity(VaccinationEachScanQrCodeVaccineActivityIntent);
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

    private boolean isInputValidation(){
        TextViewValidation tagCodeTextViewValidation = new TextViewValidation(ShowTagCode_TextView);
        TextViewValidation rfidCodeTextViewValidation = new TextViewValidation(ShowRfid_TextView);
        TextViewValidation vaccineLotCodeTextViewValidation = new TextViewValidation(ShowVaccineLot_TextView);

        if(!tagCodeTextViewValidation.isNotEmpty() || !rfidCodeTextViewValidation.isNotEmpty()){
            return false;
        }
        else if(!vaccineLotCodeTextViewValidation.isNotEmpty() || ! vaccineLotCodeTextViewValidation.isAlphaNum()){
            return false;
        }
        else {
            return true;
        }
    }

    private void submit(View view) {
        setClickDisables();
        if( isInputValidation() ){
            httpNewVaccinationEach();
        } else {
            setClickEnables();
        }
    }

    private void httpNewVaccinationEach () {
        JsonObject newVaccination = new JsonObject();
        newVaccination.addProperty("vaccine_lot_code", ShowVaccineLot_TextView.getText().toString());
        newVaccination.addProperty("vaccination_date", vaccinationDateAndTime.getDate().toString());
        Call<ResponseModel> call = RetrofitClient.getInstance().getApi().newVaccinationEach(
                LoginSession.getAuthorizationToken(Activity),
                UserDataSharedPreference.getUserData(Activity).getCompanyName(),
                CurrentFarmSharedPreference.getFarmId(Activity),
                ShowRfid_TextView.getText().toString(),
                newVaccination
        );

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                try {
                    if(response.body().getSuccess()){
                        NewVaccinationSharedPreference.clearData(Activity);

                        Toast toastResponse = Toast.makeText(Activity, "อัพเดทข้อมูลเรียบร้อย", Toast.LENGTH_LONG);
                        toastResponse.show();

                        Intent VaccinationEachActivityIntent = new Intent(Activity, VaccinationEachActivity.class);
                        startActivity(VaccinationEachActivityIntent);
                    }
                    else {
                        if(response.body().getErrorType().equals("PigNotFound")){
                            Toast toastResponse = Toast.makeText(Activity, "ไม่พบหมู", Toast.LENGTH_LONG);
                            toastResponse.show();
                        }else if(response.body().getErrorType().equals("LoginExpired")){
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
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast toast = Toast.makeText(Activity, "การเชื่อมต่อมีปัญหา", Toast.LENGTH_LONG);
                toast.show();
                setClickEnables();
            }
        });
    }

    private void getRfidDataAndSetText(){
        try {
            RfidScanner rfidUhf = new RfidScanner();
            rfidData = rfidUhf.getRfidData();
            NewVaccinationSharedPreference.setTagCode(Activity, rfidData[0]);
            NewVaccinationSharedPreference.setRfidCode(Activity, rfidData[1]);
            ShowTagCode_TextView.setText(rfidData[0]);
            ShowRfid_TextView.setText(rfidData[1]);
        }
        catch(Exception e) {
            Toast toast = Toast.makeText(Activity, "ไม่พบ Tag", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        vaccinationDateAndTime.setDate(year, month, dayOfMonth);
        NewVaccinationSharedPreference.setVaccinationDateAndTime(Activity, vaccinationDateAndTime.getDateAsMillis());
        ShowVaccinationDate_TextView.setText(vaccinationDateAndTime.getFormatDate());
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        vaccinationDateAndTime.setTime(hourOfDay, minute);
        NewVaccinationSharedPreference.setVaccinationDateAndTime(Activity, vaccinationDateAndTime.getDateAsMillis());
        ShowVaccinationTime_TextView.setText(vaccinationDateAndTime.getFormatTime());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getRepeatCount() == 0) {
            setClickDisables();
            if (keyCode == PistolButtonID) {
                getRfidDataAndSetText();
                setClickEnables();
            }
            else if (keyCode == backScreenButtonID) {
                NewVaccinationSharedPreference.clearData(Activity);

                Intent VaccineMenuActivityIntent = new Intent(Activity, VaccineMenuActivity.class);
                startActivity(VaccineMenuActivityIntent);
                setClickEnables();
            }
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }
}