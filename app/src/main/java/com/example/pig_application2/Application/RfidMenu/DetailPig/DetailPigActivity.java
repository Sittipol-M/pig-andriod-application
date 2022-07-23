package com.example.pig_application2.Application.RfidMenu.DetailPig;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pig_application2.API.ApiModel.PigApiModel;
import com.example.pig_application2.API.RetrofitClient.RetrofitClient;
import com.example.pig_application2.R;
import com.example.pig_application2.Session.LoginSession;
import com.example.pig_application2.SharedPreferences.CurrentFarmSharedPreference;
import com.example.pig_application2.SharedPreferences.UserDataSharedPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPigActivity extends AppCompatActivity {
    private final Integer backScreenButtonID = 4;
    private final DetailPigActivity Activity = this;
    private final String TAGCODE = "tag_code";
    private final String RFIDCODE = "rfid_code";
    private final String PIGCODE = "pig_code";
    private final String SEX = "sex";
    private final String BLOCKCODE = "block_code";
    private final String UNITCODE = "unit_code";
    private TextView ShowPigCode_Textview ,ShowTagCode_Textview, ShowRfidCode_Textview, ShowSex_Textview, ShowBlock_Textview, ShowUnit_Textview;
    private Button Delete_Button;
    private Bundle dataBundle;
    private String pigCode, tagCode, rfidCode, sex, blockCode, unitCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pig);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initViews();
        initObjects();
        initListeners();
        setClickEnables();
        setTextToViews();
    }

    private void initViews(){
        ShowPigCode_Textview    = findViewById(R.id.ShowPigCode_Textview_DetailPigActivity);
        ShowTagCode_Textview    = findViewById(R.id.ShowTagCode_Textview_DetailPigActivity);
        ShowRfidCode_Textview   = findViewById(R.id.ShowRfidCode_Textview_DetailPigActivity);
        ShowSex_Textview        = findViewById(R.id.ShowSex_Textview_DetailPigActivity);
        ShowBlock_Textview      = findViewById(R.id.ShowBlock_Textview_DetailPigActivity);
        ShowUnit_Textview       = findViewById(R.id.ShowUnit_Textview_DetailPigActivity);
        Delete_Button           = findViewById(R.id.Delete_Button_DetailPigActivity);
    }

    private void initObjects(){
        dataBundle  = Activity.getIntent().getExtras();
        tagCode     = dataBundle.getString(TAGCODE);
        pigCode     = dataBundle.getString(PIGCODE);
        rfidCode    = dataBundle.getString(RFIDCODE);
        sex         = dataBundle.getString(SEX);
        blockCode   = dataBundle.getString(BLOCKCODE);
        unitCode    = dataBundle.getString(UNITCODE);
    }

    private void setTextToViews(){
        ShowTagCode_Textview.setText(tagCode);
        ShowPigCode_Textview.setText(pigCode);
        ShowRfidCode_Textview.setText(rfidCode);
        if(sex.equals("male")){
            ShowSex_Textview.setText("เพศผู้");
        }
        else if(sex.equals("female")){
            ShowSex_Textview.setText("เพศเมีย");
        }
        if(blockCode.equals("block_0000")){
            ShowBlock_Textview.setText("ไม่มีซอง");
            ShowBlock_Textview.setTextColor(Color.parseColor("#d10202"));
        }
        else {
            ShowBlock_Textview.setText(blockCode);
        }
        if(unitCode.equals("unit_0000")){
            ShowUnit_Textview.setText("ไม่มียูนิต");
            ShowUnit_Textview.setTextColor(Color.parseColor("#d10202"));
        }
        else {
            ShowUnit_Textview.setText(unitCode);
        }
    }

    private void initListeners(){
        Delete_Button.setOnClickListener(Activity::deletePigConfirmation);
    }

    private void setClickEnables(){
        Delete_Button.setEnabled(true);
    }

    private void setClickDisables(){
        Delete_Button.setEnabled(false);
    }

    private void deletePigConfirmation(View view) {
        setClickDisables();
        final AlertDialog.Builder deleteAlert = new AlertDialog.Builder(this);
        deleteAlert.setTitle("ยืนยันเพื่อลบ ?");
        deleteAlert.setMessage("กรุณายืนยัน");
        deleteAlert.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setClickEnables();
            }
        });
        deleteAlert.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog , int arg1){
                httpDeletePig();
            }
        });
        deleteAlert.show();
    }

    private void httpDeletePig(){
        Call<PigApiModel> call = RetrofitClient.getInstance().getApi().deletePig(
                LoginSession.getAuthorizationToken(Activity),
                UserDataSharedPreference.getUserData(Activity).getCompanyName(),
                CurrentFarmSharedPreference.getFarmId(Activity),
                rfidCode
        );
        call.enqueue(new Callback<PigApiModel>() {
            @Override
            public void onResponse(Call<PigApiModel> call, Response<PigApiModel> response) {
                try{
                    if(response.body().getSuccess()){
                        Toast toastResponse = Toast.makeText(Activity, "ลบข้อมูลเรียบร้อย", Toast.LENGTH_SHORT);
                        toastResponse.show();

                        Intent DetailPigScanRfidActivityIntent = new Intent(Activity, DetailPigScanRfidActivity.class);
                        startActivity(DetailPigScanRfidActivityIntent);
                    } else if( !response.body().getSuccess()){
                        if(response.body().getErrorType().equals("LoginExpired")){
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getRepeatCount() == 0) {
            setClickDisables();
            if (keyCode == backScreenButtonID) {
                Intent DetailPigScanRfidActivityIntent = new Intent(Activity, DetailPigScanRfidActivity.class);
                startActivity(DetailPigScanRfidActivityIntent);
                setClickEnables();
            }
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }

}