package com.example.pig_application2.Application;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pig_application2.API.ApiModel.FarmPermissionsApiModel;
import com.example.pig_application2.API.DataModel.FarmPermissionModel;
import com.example.pig_application2.API.DataModel.UserModel;
import com.example.pig_application2.API.RetrofitClient.RetrofitClient;
import com.example.pig_application2.Application.RfidMenu.RfidMenuActivity;
import com.example.pig_application2.Application.SettingActivity.SettingActivity;
import com.example.pig_application2.Application.UserActivity.UserActivity;
import com.example.pig_application2.R;
import com.example.pig_application2.Session.LoginSession;
import com.example.pig_application2.SharedPreferences.CurrentFarmSharedPreference;
import com.example.pig_application2.SharedPreferences.UserDataSharedPreference;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FarmSelectionActivity extends AppCompatActivity {
    private final Integer backScreenButtonID = 4;
    private final FarmSelectionActivity Activity = this;
    private ListView Farm_ListView;
    private TextView CountFarms_TextView;
    private ImageView User_ImageView, Setting_ImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_selection);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initViews();
        initListeners();
        httpGetFarmPermissionsAndDisplayList();
    }

    private void initViews() {
        Farm_ListView       = findViewById(R.id.Farm_ListView_FarmSelectionActivity);
        CountFarms_TextView = findViewById(R.id.CountFarms_TextView_FarmSelectionActivity);
        User_ImageView      = findViewById(R.id.User_ImageView_FarmSelectionActivity);
        Setting_ImageView   = findViewById(R.id.Setting_ImageView_FarmSelectionActivity);
    }

    private void initListeners(){
        User_ImageView.setOnClickListener(Activity::toUserActivity);
        Setting_ImageView.setOnClickListener(Activity::toSettingActivity);
    }

    private void setClickDisables(){
        User_ImageView.setEnabled(false);
        Setting_ImageView.setEnabled(false);
        Farm_ListView.setEnabled(false);
    }

    private void setClickEnables(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User_ImageView.setEnabled(true);
                Setting_ImageView.setEnabled(true);
                Farm_ListView.setEnabled(true);
            }
        }, 1000);
    }

    private void toSettingActivity(View view) {
        setClickDisables();
        Intent SettingActivity = new Intent(Activity, SettingActivity.class);
        startActivity(SettingActivity);
        setClickEnables();
    }

    private void toUserActivity(View view) {
        setClickDisables();
        Intent UserActivityIntent = new Intent(Activity, UserActivity.class);
        startActivity(UserActivityIntent);
        setClickEnables();
    }


    private void httpGetFarmPermissionsAndDisplayList() {
        String authorizationToken = LoginSession.getAuthorizationToken(Activity);
        UserModel user = UserDataSharedPreference.getUserData(Activity);
        Call<FarmPermissionsApiModel> call = RetrofitClient.getInstance().getApi()
                .getFarmPermissions(
                        authorizationToken,
                        user.getCompanyName(),
                        user.getUserId()
                );
        call.enqueue(new Callback<FarmPermissionsApiModel>() {
            @Override
            public void onResponse( Call<FarmPermissionsApiModel> call,  Response<FarmPermissionsApiModel> response) {
                try {
                    if (response.body().getSuccess()) {
                        if (response.body().getFarmPermissions() == null){
                            CountFarms_TextView.setText("จำนวน 0 รายการ");
                            return;
                        }
                        generateFarmsList(response.body().getFarmPermissions());
                    } else if(!response.body().getSuccess()) {
                        if(response.body().getErrorType().equals("FarmPermissionsNotFound")){
                            CountFarms_TextView.setText("จำนวน 0 รายการ");
                        }
                        else if(response.body().getErrorType().equals("LoginExpired")){
                            LoginSession.clearDataInLoginSession(Activity);
                        }
                    }
                }
                catch (Exception exception){
                    System.out.println(exception);
                    Toast exceptionToast = Toast.makeText(Activity, "การเชื่อมต่อมีปัญหา", Toast.LENGTH_LONG);
                    exceptionToast.show();
                }
                setClickEnables();
            }

            @Override
            public void onFailure( Call<FarmPermissionsApiModel> call, Throwable t) {
                System.out.println(t);
                Toast toast = Toast.makeText(Activity, "การเชื่อมต่อมีปัญหา", Toast.LENGTH_LONG);
                toast.show();
                setClickEnables();
            }
        });
    }

    private void generateFarmsList(ArrayList<FarmPermissionModel> farmPermissions){
        Integer size = 0;
        for( Integer i=0 ; i<farmPermissions.size() ; i++){
            if(farmPermissions.get(i).isPermission()){
                size ++;
            }
        }

        CountFarms_TextView.setText("จำนวน "+size+" รายการ");

        String[] farmsList = new String[size];
        for( Integer i=0 ; i<size ; i++){
            if(farmPermissions.get(i).isPermission()){
                farmsList[i] = farmPermissions.get(i).getFarmName();
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(Activity, android.R.layout.simple_list_item_1, farmsList);
        Farm_ListView.setAdapter(adapter);
        Farm_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setClickDisables();
                FarmPermissionModel selectedFarm = farmPermissions.get(position);
                CurrentFarmSharedPreference.setCurrentFarmData(Activity, selectedFarm.getId(), selectedFarm.getFarmName());
                Intent MenuActivityIntent = new Intent(Activity, MenuActivity.class);
                startActivity(MenuActivityIntent);
                setClickEnables();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getRepeatCount() == 0) {
            if (keyCode == backScreenButtonID) {
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }
}