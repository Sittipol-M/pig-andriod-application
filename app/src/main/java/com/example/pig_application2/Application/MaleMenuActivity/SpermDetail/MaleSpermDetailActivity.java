package com.example.pig_application2.Application.MaleMenuActivity.SpermDetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pig_application2.API.ApiModel.CompanyUserApiModel;
import com.example.pig_application2.API.ApiModel.SpermsApiModel;
import com.example.pig_application2.API.DataModel.CompanyUserModel;
import com.example.pig_application2.API.DataModel.SpermModel;
import com.example.pig_application2.API.RetrofitClient.RetrofitClient;
import com.example.pig_application2.Application.FemaleMenuActivity.BreedDetail.FemaleBreedDetailScanRfidActivity;
import com.example.pig_application2.Application.MaleMenuActivity.SpermDetail.SpermDetailSharedPreference.SpermDetailSharedPreference;
import com.example.pig_application2.R;
import com.example.pig_application2.Session.LoginSession;
import com.example.pig_application2.SharedPreferences.CurrentFarmSharedPreference;
import com.example.pig_application2.SharedPreferences.UserDataSharedPreference;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaleSpermDetailActivity extends AppCompatActivity {
    private final Integer backScreenButtonID = 4;
    private final MaleSpermDetailActivity Activity = this;
    private ListView SpermDetail_ListView;
    private TextView CountSperms_TextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_male_sperm_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initViews();
        httpGetSperms();
        setClickEnables();
    }

    private void initViews() {
        SpermDetail_ListView = findViewById(R.id.SpermDetail_ListView_MaleSpermDetailActivity);
        CountSperms_TextView = findViewById(R.id.CountSperms_TextView_MaleSpermDetailActivity);
    }

    private void setClickDisables(){
        SpermDetail_ListView.setEnabled(false);
    }

    private void setClickEnables(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SpermDetail_ListView.setEnabled(true);
            }
        }, 1000);
    }

    private void httpGetSperms() {
        Call<SpermsApiModel> call = RetrofitClient.getInstance().getApi().getSperms(
                LoginSession.getAuthorizationToken(Activity),
                UserDataSharedPreference.getUserData(Activity).getCompanyName(),
                CurrentFarmSharedPreference.getFarmId(Activity),
                SpermDetailSharedPreference.getRfidCode(Activity)
        );

        call.enqueue(new Callback<SpermsApiModel>() {
            @Override
            public void onResponse(Call<SpermsApiModel> call, Response<SpermsApiModel> response) {
                try {
                    if(response.body().getSuccess()){
                        generateSpermList(response.body().getSperms());
                    } else if(!response.body().getSuccess()) {
                        if(response.body().getErrorType().equals("PigSpermsNotFound")){
                            CountSperms_TextView.setText("จำนวน 0 รายการ");
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
            public void onFailure(Call<SpermsApiModel> call, Throwable t) {
                Toast exceptionToast = Toast.makeText(Activity, "การเชื่อมต่อมีปัญหา", Toast.LENGTH_LONG);
                exceptionToast.show();
                setClickEnables();
            }
        });
    }

    private void generateSpermList(ArrayList<SpermModel> sperms) {
        Integer size = sperms.size();
        CountSperms_TextView.setText("จำนวน "+size+" รายการ");
        String[] spermList = new String[size];
        for (Integer i = 0; i < size; i++) {

            spermList[i] = "การเก็บน้ำเชื้อครั้งที่ " + (i+1);
            SpermDetailSharedPreference.setSelectedSperm(Activity, "การเก็บน้ำเชื้อครั้งที่ " + (i+1));
        }
        ArrayAdapter adapter = new ArrayAdapter(Activity,
                android.R.layout.simple_list_item_1, spermList);
        SpermDetail_ListView.setAdapter(adapter);
        SpermDetail_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setClickDisables();
                SpermDetailSharedPreference.setSpermId(Activity, sperms.get(position).getId());
                SpermDetailSharedPreference.setDateCollectSperm(Activity, sperms.get(position).getDateCollectSperm().getTime());
                Intent MaleSpermShowDetailActivityIntent = new Intent(Activity, MaleSpermShowDetailActivity.class);
                httpCompanyUserData(sperms.get(position).getCompanyUserCollectSperm(), MaleSpermShowDetailActivityIntent);
            }
        });
    }

    private void httpCompanyUserData(String companyUserId, Intent intent){
        Call<CompanyUserApiModel> call = RetrofitClient.getInstance().getApi().getCompanyUser(
                LoginSession.getAuthorizationToken(Activity),
                UserDataSharedPreference.getUserData(Activity).getCompanyName(),
                companyUserId
        );


        call.enqueue(new Callback<CompanyUserApiModel>() {
            @Override
            public void onResponse( Call<CompanyUserApiModel> call,  Response<CompanyUserApiModel> response) {
                try {
                    if(response.body().getSuccess()){
                        CompanyUserModel companyUser = response.body().getCompanyUser();
                        SpermDetailSharedPreference.setCompanyUserCollectSpermName(Activity, companyUser.getName()+" "+companyUser.getSurname());
                        SpermDetailSharedPreference.setCompanyUserCollectSpermId(Activity, companyUserId);
                        startActivity(intent);
                        Activity.finish();
                    }
                    else{
                        setClickEnables();
                    }
                } catch (Exception exception) {
                    Toast exceptionToast = Toast.makeText(Activity, "การเชื่อมต่อมีปัญหา", Toast.LENGTH_LONG);
                    exceptionToast.show();
                    setClickEnables();
                }

            }

            @Override
            public void onFailure( Call<CompanyUserApiModel> call,  Throwable t) {
                Toast failureToast = Toast.makeText(Activity, "การเชื่อมต่อมีปัญหา", Toast.LENGTH_LONG);
                failureToast.show();
                setClickEnables();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setClickDisables();
        if (event.getRepeatCount() == 0) {
            if (keyCode == backScreenButtonID) {
                Intent FemaleScanRfidActivityIntent = new Intent(Activity, FemaleBreedDetailScanRfidActivity.class);
                startActivity(FemaleScanRfidActivityIntent);
                Activity.finish();
            }
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }
}