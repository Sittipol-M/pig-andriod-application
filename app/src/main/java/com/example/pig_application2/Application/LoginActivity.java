package com.example.pig_application2.Application;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pig_application2.API.ApiModel.UserApiModel;
import com.example.pig_application2.API.RetrofitClient.RetrofitClient;
import com.example.pig_application2.InputValidation.EditTextValidation;
import com.example.pig_application2.R;
import com.example.pig_application2.Session.LoginSession;
import com.example.pig_application2.SharedPreferences.UserDataSharedPreference;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private final Integer backScreenButtonID = 4;
    private EditText Username_EditText, Password_EditText;
    private Button Login_Button;
    private final LoginActivity Activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initViews();
        initListeners();
        //check if still have session
        if(LoginSession.checkLoginSession(Activity)){
            Intent FarmSelectionActivityIntent = new Intent(Activity, FarmSelectionActivity.class);
            startActivity(FarmSelectionActivityIntent);
        }
    }


    private void initViews() {
        Login_Button        = findViewById(R.id.Login_Button_LoginActivity);
        Username_EditText   = findViewById(R.id.Username_EditText_LoginActivity);
        Password_EditText   = findViewById(R.id.Password_EditText_LoginActivity);
    }

    private void setClickEnables(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Login_Button.setEnabled(true);
            }
        }, 500);
    }

    private void setClickDisables(){
        Login_Button.setEnabled(false);
    }

    private void initListeners() {
        Login_Button.setOnClickListener(Activity::login);
    }

    private boolean isInputValidate(){
        EditTextValidation usernameEditTextValidation = new EditTextValidation(Username_EditText);
        EditTextValidation passwordEditTextValidation = new EditTextValidation(Password_EditText);

        //check for username
        if( !usernameEditTextValidation.isNotEmpty() || !usernameEditTextValidation.isAlphaNum() || !usernameEditTextValidation.isMin(6)
        ){
            return false;
        }
        //check for password
        if( !passwordEditTextValidation.isNotEmpty() ||!passwordEditTextValidation.isMin(6)){
            return false;
        }
        return true;
    }

    private void login(View view) {
        setClickDisables();
        if(isInputValidate()){
            httpLogin(Username_EditText.getText().toString(), Password_EditText.getText().toString());
        } else {
            setClickEnables();
        }
    }

    private void httpLogin(String username, String password) {

        JsonObject loginJsonPost =  new JsonObject();
            loginJsonPost.addProperty("username",username);
            loginJsonPost.addProperty("password",password);

        Call<UserApiModel> call = RetrofitClient.getInstance().getApi().login(loginJsonPost);
            call.enqueue(new Callback<UserApiModel>() {
                @Override
                public void onResponse(Call<UserApiModel> call, Response<UserApiModel> response) {
                    try {
                        if(response.body().getSuccess()){
                            //store authentication token in loginSession
                            LoginSession.writeDataToLoginSession(Activity, username, response.headers().get("Authorization"));
                            //store user data in UserDataSharedPreference
                            UserDataSharedPreference.writeUserData(Activity,response.body().getUser());
                            //check if still have session
                            if(LoginSession.checkLoginSession(Activity)){
                                Intent FarmSelectionActivityIntent = new Intent(Activity, FarmSelectionActivity.class);
                                startActivity(FarmSelectionActivityIntent);
                            }
                        }
                        else {
                            if(response.body().getErrorType().equals("InvalidUsername")){
                                Toast toast = Toast.makeText(Activity, "ไม่พบ username", Toast.LENGTH_LONG);
                                toast.show();
                            } else if(response.body().getErrorType().equals("InvalidPassword")){
                                Toast toast = Toast.makeText(Activity, "password ผิด", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    } catch (Exception exception){
                        Toast exceptionToast = Toast.makeText(Activity, "การเชื่อมต่อมีปัญหา", Toast.LENGTH_LONG);
                        exceptionToast.show();
                    }
                    setClickEnables();
                }

                @Override
                public void onFailure(Call<UserApiModel> call, Throwable t) {
                    Toast toast = Toast.makeText(Activity, "การเชื่อมต่อมีปัญหา", Toast.LENGTH_LONG);
                    toast.show();
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
