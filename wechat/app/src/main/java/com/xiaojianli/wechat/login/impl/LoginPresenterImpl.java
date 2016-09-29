package com.xiaojianli.wechat.login.impl;

import com.xiaojianli.wechat.login.logginpreseneter.ILoginPresenter;
import com.xiaojianli.wechat.login.loginmodule.LoginModule;

/**
 * Created by lixiaojian on 16/9/29.
 */
public class LoginPresenterImpl implements ILoginPresenter,LoginModule.LoginResult{

    public interface LoginState {

        public void LoginSuccess();
        public void LoginFailed();
        public void LoginError();
    }


    private LoginState mLoginState;
    private LoginModule mLM;
    public LoginPresenterImpl(LoginState L) {
        this.mLoginState = L;
        mLM = new LoginModule(this);
    }


    @Override
    public void LoginSuccess() {
        mLoginState.LoginSuccess();
    }

    @Override
    public void LoginFailed() {
        mLoginState.LoginFailed();

    }

    @Override
    public void LoginError() {
        mLoginState.LoginError();
    }

    @Override
    public void Login(String info) {
        mLM.Login(info);
    }
}
