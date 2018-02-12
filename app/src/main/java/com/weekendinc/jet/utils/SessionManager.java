package com.weekendinc.jet.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.weekendinc.jet.LoginActivity;
import com.weekendinc.jet.model.pojo.ResultLogin;

import java.util.HashMap;

import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.config.AppConfig;
import coid.customer.pickupondemand.jet.model.Login;
import coid.customer.pickupondemand.jet.model.UserProfile;
import coid.customer.pickupondemand.jet.model.db.DBQuery;

public class SessionManager {

    Context _context;

    public static final String ACCESS_TOKEN = "access_token";
    public static final String TOKEN_TYPE = "token_type";
    public static final String REFRESH_TOKEN = "refresh_token";

    public SessionManager(Context context) {
        this._context = context;
    }

    public void createLoginSession(ResultLogin resultLogin){}

    public boolean isLoggedIn()
    {
        return DBQuery.getSingle(Login.class) != null;
    }

    public void checkLogin() {
        if (!this.isLoggedIn())
        {
            navigateToLogin();
        }
    }

    public HashMap<String, String> getLoginDetails() {
        HashMap<String, String> token = new HashMap<>();
        Login login = DBQuery.getSingle(Login.class);
        if (isLoggedIn())
        {
            token.put(ACCESS_TOKEN, login.getAccessToken());
            token.put(TOKEN_TYPE, login.getTokenType());
            token.put(REFRESH_TOKEN, login.getRefreshToken());
        }
        else
        {
            token.put(ACCESS_TOKEN, "");
            token.put(TOKEN_TYPE, "");
            token.put(REFRESH_TOKEN, "");
        }
        return token;
    }

    public void logoutUser()
    {
        DBQuery.truncate(Login.class);
        DBQuery.truncate(UserProfile.class);
        Utility.OneSignal.deleteTags();
        navigateToLogin();
    }

    public void navigateToLogin()
    {
        Intent intent = new Intent(_context, LoginActivity.class);
        if (_context instanceof FragmentActivity)
        {
            FragmentActivity fa = (FragmentActivity) _context;
            fa.startActivityForResult(intent, AppConfig.LOGIN_ACTIVITY_REQUEST_CODE);
        }
    }
}
