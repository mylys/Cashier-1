package com.easygo.cashier.module.login;

import android.content.Intent;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.InitResponse;
import com.easygo.cashier.bean.LoginResponse;
import com.easygo.cashier.module.goods.MainActivity;
import com.niubility.library.base.BaseRxActivity;
import com.niubility.library.widget.imageview.CircleImageView;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseRxActivity<LoginContract.IView, LoginPresenter> implements LoginContract.IView {

    @BindView(R.id.circleImageView)
    CircleImageView circleImageView;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.iv_pwd)
    ImageView ivPwd;
    private boolean isNeedShowPwd = false;


    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected LoginContract.IView createView() {
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {


        ivPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNeedShowPwd = !isNeedShowPwd;
                if (isNeedShowPwd) {
                    //如果选中，显示密码
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });



    }

    @OnClick({R.id.btn_login})
    public void onClick() {
        Toast.makeText(this, "登录成功...", Toast.LENGTH_SHORT).show();

//        mPresenter.init("200155");
//        mPresenter.login(etAccount.getText().toString().trim(), etPassword.getText().toString().trim());


//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        ARouter.getInstance().build(ModulePath.goods).navigation();
    }

    @Override
    public void loginSuccess(LoginResponse result) {

    }

    @Override
    public void loginFailed(Map<String, Object> map) {

    }

    @Override
    public void initSuccess(InitResponse result) {

    }

    @Override
    public void initFailed(Map<String, Object> map) {

    }
}
