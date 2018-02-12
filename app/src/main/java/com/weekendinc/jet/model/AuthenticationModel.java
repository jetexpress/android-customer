package com.weekendinc.jet.model;

import com.weekendinc.jet.model.pojo.Profile;
import com.weekendinc.jet.model.pojo.ResultChangeFullName;
import com.weekendinc.jet.model.pojo.ResultChangePassword;
import com.weekendinc.jet.model.pojo.ResultLogin;
import com.weekendinc.jet.model.pojo.ResultRegister;
import com.weekendinc.jet.network.APIJet;

import rx.Observable;

public class AuthenticationModel {
    static private AuthenticationModel instance;

    static public AuthenticationModel getInstance() {
        if (instance == null)
            instance = new AuthenticationModel();
        return instance;
    }

    public Observable<ResultLogin> doLogin(String username, String password) {
        return APIJet.getInstance().submitLogin(username, password);
    }

    public Observable<ResultRegister> doRegister(String username, String password, String confirmPassword, String fullName, String email) {
        return APIJet.getInstance().submitRegistration(username, password, confirmPassword, fullName, email);
    }

    public Observable<Profile> doGetProfile(String accessToken) {
        return APIJet.getInstance().submitProfile(accessToken);
    }

    public Observable<ResultChangePassword> doChangePassword(String username, String oldPassword, String newPassword) {
        return APIJet.getInstance().submitChangePassword(username, oldPassword, newPassword);
    }

    public Observable<ResultChangeFullName> doChangeName(String accessToken, String fullName) {
        return APIJet.getInstance().submitChangeName(accessToken, fullName);
    }

}
