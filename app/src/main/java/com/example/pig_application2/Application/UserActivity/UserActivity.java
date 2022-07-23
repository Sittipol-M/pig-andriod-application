package com.example.pig_application2.Application.UserActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pig_application2.API.DataModel.UserModel;
import com.example.pig_application2.Application.LoginActivity;
import com.example.pig_application2.R;
import com.example.pig_application2.Session.LoginSession;
import com.example.pig_application2.SharedPreferences.UserDataSharedPreference;


public class UserActivity extends AppCompatActivity {
    private final UserActivity Activity = this;
    private TextView ShowUserName_Textview, ShowCompany_Textview, ShowName_Textview;
    private Button Logout_Button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initViews();
        initListenersAndEnables();
        setTextToTextViews();
    }


    private void initViews() {
        ShowUserName_Textview       = findViewById(R.id.ShowUserName_Textview_UserActivity);
        ShowCompany_Textview        = findViewById(R.id.ShowCompany_Textview_UserActivity);
        ShowName_Textview           = findViewById(R.id.ShowName_Textview_UserActivity);
        Logout_Button               = findViewById(R.id.Logout_Button_UserActivity);
    }

    private void initListenersAndEnables(){
        Logout_Button.setOnClickListener(Activity::logout);
    }

    private void setClickEnables(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Logout_Button.setEnabled(true);
            }
        }, 1000);
    }

    private void setClickDisables(){
        Logout_Button.setEnabled(false);
    }

    private void setTextToTextViews() {
        UserModel user = UserDataSharedPreference.getUserData(Activity);
        ShowUserName_Textview.setText(LoginSession.getUsername(Activity));
        ShowName_Textview.setText(user.getName() + " " +user.getSurname());
        ShowCompany_Textview.setText(user.getCompanyName());
    }


    private void logout(View view) {
        setClickDisables();
        final AlertDialog.Builder logoutAlert = new AlertDialog.Builder(Activity);
        logoutAlert.setTitle("ยืนยันเพื่อออกระบบ ?");
        logoutAlert.setMessage("กรุณายืนยัน");
        logoutAlert.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setClickEnables();
            }
        });
        logoutAlert.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                // clear session
                LoginSession.clearDataInLoginSession(Activity);
                UserDataSharedPreference.clearData(Activity);
                // check if session was death
                LoginSession.checkLoginSession(Activity);
                setClickEnables();
                Activity.finish();
            }
        });
        logoutAlert.show();
    }

}