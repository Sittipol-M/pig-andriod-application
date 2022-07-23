package com.example.pig_application2.Application.VaccineMenu.VaccinationUnit;

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
import com.example.pig_application2.Application.VaccineMenu.VaccinationUnit.NewVaccinationUnitSharedPreference.NewVaccinationUnitSharedPreference;
import com.example.pig_application2.Application.VaccineMenu.VaccineMenuActivity;
import com.example.pig_application2.InputValidation.TextViewValidation;
import com.example.pig_application2.R;
import com.example.pig_application2.Session.LoginSession;
import com.example.pig_application2.SharedPreferences.CurrentFarmSharedPreference;
import com.example.pig_application2.SharedPreferences.UserDataSharedPreference;
import com.example.pig_application2.othersClass.FormatDateAndTime.FormatDateAndTime;
import com.example.pig_application2.othersClass.PigDatePicker.PigDatePicker;
import com.example.pig_application2.othersClass.PigDatePicker.PigTimePicker;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;


public class VaccinationUnitActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private final Integer backScreenButtonID = 4;
    private final VaccinationUnitActivity Activity = this;
    private TextView  ShowVaccineLot_TextView, ShowUnitCode_TextView, ShowVaccinationDate_TextView, ShowVaccinationTime_TextView;
    private Button Submit_Button, ScanVaccineLotCodeQrcode_Button, ScanUnitCode_Button, SetDate_Button, SetTime_Button;
    private FormatDateAndTime vaccinationDateAndTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination_unit);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initViews();
        initObject();
        initListeners();
        setTextToTextViews();
    }

    private void initViews(){
        //TextView
        ShowVaccineLot_TextView         = findViewById(R.id.ShowVaccineLot_TextView_VaccinationUnitActivity);
        ShowUnitCode_TextView           = findViewById(R.id.ShowUnitCode_TextView_VaccinationUnitActivity);
        ShowVaccinationDate_TextView    = findViewById(R.id.ShowVaccinationDate_TextView_VaccinationUnitActivity);
        ShowVaccinationTime_TextView    = findViewById(R.id.ShowVaccinationTime_TextView_VaccinationUnitActivity);
        //Button
        ScanVaccineLotCodeQrcode_Button = findViewById(R.id.ScanVaccineLotCodeQrcode_Button_VaccinationUnitActivity);
        ScanUnitCode_Button             = findViewById(R.id.ScanUnitCode_Button_VaccinationUnitActivity);
        SetDate_Button                  = findViewById(R.id.SetDate_Button_VaccinationUnitActivity);
        SetTime_Button                  = findViewById(R.id.SetTime_Button_VaccinationUnitActivity);
        Submit_Button                   = findViewById(R.id.Submit_Button_VaccinationUnitActivity);
    }


    private void initObject(){
        long dateAndTimeFormSharedPreference = NewVaccinationUnitSharedPreference.getVaccinationDateAndTime(Activity);
        if(dateAndTimeFormSharedPreference != 0){
            vaccinationDateAndTime.setDateAndTimeWithMillis(dateAndTimeFormSharedPreference);
        } else {
            vaccinationDateAndTime = new FormatDateAndTime(new Date());
        }
    }

    private void initListeners(){
        ScanVaccineLotCodeQrcode_Button.setOnClickListener(Activity::scanVaccineLotCode);
        ScanUnitCode_Button.setOnClickListener(Activity::scanUnitCode);
        SetDate_Button.setOnClickListener(Activity::showDatePickerDialog);
        SetTime_Button.setOnClickListener(Activity::showTimePickerDialog);
        Submit_Button.setOnClickListener(Activity::submit);
    }

    private void setClickEnables(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ScanVaccineLotCodeQrcode_Button.setEnabled(true);
                ScanUnitCode_Button.setEnabled(true);
                SetDate_Button.setEnabled(true);
                SetTime_Button.setEnabled(true);
                Submit_Button.setEnabled(true);
            }
        }, 1000);
    }

    private void setClickDisables(){
        ScanVaccineLotCodeQrcode_Button.setEnabled(false);
        ScanUnitCode_Button.setEnabled(false);
        SetDate_Button.setEnabled(false);
        SetTime_Button.setEnabled(false);
        Submit_Button.setEnabled(false);
    }

    private boolean isInputValidate(){
        TextViewValidation unitCodeTextViewValidation = new TextViewValidation(ShowUnitCode_TextView);
        TextViewValidation vaccineLotTextViewValidation = new TextViewValidation(ShowVaccineLot_TextView);
        if(!unitCodeTextViewValidation.isNotEmpty()){
            return false;
        } else if(!vaccineLotTextViewValidation.isNotEmpty()){
            return false;
        } else {
            return true;
        }
    }

    private void setTextToTextViews(){
        ShowUnitCode_TextView.setText(NewVaccinationUnitSharedPreference.getUnitCode(Activity));
        ShowVaccineLot_TextView.setText(NewVaccinationUnitSharedPreference.getVaccineLotCode(Activity));
        ShowVaccinationDate_TextView.setText(vaccinationDateAndTime.getFormatDate());
        ShowVaccinationTime_TextView.setText(vaccinationDateAndTime.getFormatTime());
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

    private void scanVaccineLotCode(View view) {
        setClickDisables();
        Intent VaccinationUnitScanQrcodeVaccineActivityIntent = new Intent(Activity, VaccinationUnitScanQrcodeVaccineActivity.class);
        startActivity(VaccinationUnitScanQrcodeVaccineActivityIntent);
        setClickEnables();
    }

    private void scanUnitCode(View view) {
        setClickDisables();
        Intent VaccinationUnitScanQrCodeUnitActivityIntent = new Intent(Activity, VaccinationUnitScanQrCodeUnitActivity.class);
        startActivity(VaccinationUnitScanQrCodeUnitActivityIntent);
        setClickEnables();
    }


    private void submit(View view) {
        setClickDisables();
        if(isInputValidate()){
            httpNewVaccinationUnit();
        } else {
            setClickEnables();
        }
    }

    private void httpNewVaccinationUnit(){
        JsonObject newVaccinationUnit = new JsonObject();
        newVaccinationUnit.addProperty("vaccine_lot_code", ShowVaccineLot_TextView.getText().toString());
        newVaccinationUnit.addProperty("vaccination_date", vaccinationDateAndTime.getDate().toString());
        Call<ResponseModel> call = RetrofitClient.getInstance().getApi().newVaccinationUnit(
                LoginSession.getAuthorizationToken(Activity),
                UserDataSharedPreference.getUserData(Activity).getCompanyName(),
                CurrentFarmSharedPreference.getFarmId(Activity),
                ShowUnitCode_TextView.getText().toString(),
                newVaccinationUnit
        );

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                try{
                    if(response.body().getSuccess()){
                        NewVaccinationUnitSharedPreference.clearData(Activity);

                        Toast toastResponse = Toast.makeText(getApplicationContext(), "อัพเดทข้อมูลเรียบร้อย", Toast.LENGTH_SHORT);
                        toastResponse.show();

                        Intent VaccinationUnitActivityIntent = new Intent(Activity, VaccinationUnitActivity.class);
                        startActivity(VaccinationUnitActivityIntent);
                    }else{
                        if(response.body().getErrorType().equals("PigsNotFound")){
                            Toast toastResponse = Toast.makeText(Activity, "ไม่พบหมู", Toast.LENGTH_SHORT);
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
                Toast failureToast = Toast.makeText(Activity, "การเชื่อมต่อมีปัญหา", Toast.LENGTH_LONG);
                failureToast.show();
                setClickEnables();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getRepeatCount() == 0) {
            setClickDisables();
            if (keyCode == backScreenButtonID) {
                Intent VaccineMenuActivityIntent = new Intent(this, VaccineMenuActivity.class);
                startActivity(VaccineMenuActivityIntent);

                NewVaccinationUnitSharedPreference.clearData(Activity);
                setClickEnables();
            }
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        vaccinationDateAndTime.setDate(year, month, dayOfMonth);
        NewVaccinationUnitSharedPreference.setVaccinationDateAndTime(Activity, vaccinationDateAndTime.getDateAsMillis());
        ShowVaccinationDate_TextView.setText(vaccinationDateAndTime.getFormatDate());
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        vaccinationDateAndTime.setTime(hourOfDay, minute);
        NewVaccinationUnitSharedPreference.setVaccinationDateAndTime(Activity, vaccinationDateAndTime.getDateAsMillis());
        ShowVaccinationTime_TextView.setText(vaccinationDateAndTime.getFormatTime());
    }
}