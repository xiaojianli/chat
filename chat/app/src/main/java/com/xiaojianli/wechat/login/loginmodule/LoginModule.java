package com.xiaojianli.wechat.login.loginmodule;

import com.xiaojianli.wechat.login.LoginThread;

/**
 * Created by lixiaojian on 16/9/29.
 */
public class LoginModule {

    public interface LoginResult {
        void LoginSuccess();
        void LoginFailed();
        void LoginError();
    }

    private LoginResult mLoginResult;
    public LoginModule(LoginResult L) {
        this.mLoginResult = L;
    }
    public int Login(String info) {
        new LoginThread(info,mLoginResult).start();
        return 0;
    }
}
