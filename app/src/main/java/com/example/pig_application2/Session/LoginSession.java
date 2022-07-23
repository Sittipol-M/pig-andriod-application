package com.example.pig_application2.Session;

import static android.content.Context.ACCESSIBILITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.pig_application2.Application.LoginActivity;
import com.example.pig_application2.SharedPreferences.UserDataSharedPreference;

public class LoginSession {
    private static final String MyPreferences = "USERSESSION";
    private static final String USERNAME = "username";
    private static final String TOKEN = "token";

    public static SharedPreferences getPreferences(Context context){
        return context.getSharedPreferences(MyPreferences, MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(Context context){
        return getPreferences(context).edit();
    }

    public static void writeDataToLoginSession(Context context, String username , String token){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(USERNAME, username);
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public static void clearDataInLoginSession(Context context){
        SharedPreferences.Editor editor = getEditor(context);
        editor.clear();
        editor.commit();
        checkLoginSession(context);
    }

    public static String getAuthorizationToken(Context context){
        return getPreferences(context).getString(TOKEN, "");
    }

    public static String getUsername(Context context){
        return getPreferences(context).getString(USERNAME, "");
    }

    public static boolean checkLoginSession(Context context){
        if(getPreferences(context).contains(USERNAME) && getPreferences(context).contains(TOKEN)){
            return true;
        }
        else {
            UserDataSharedPreference.clearData(context);
            if(context.getClass() != LoginActivity.class){
                Intent LoginActivityIntent = new Intent(context, LoginActivity.class);
                context.startActivity(LoginActivityIntent);
                System.out.println("go to login");
            }
            return false;
        }
    }
}
