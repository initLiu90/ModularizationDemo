package com.lzp.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.lzp.core.base.BaseActivity;
import com.lzp.core.base.BaseFragment;
import com.lzp.login.R;
import com.lzp.login.fragment.PwdLoginFragment;
import com.lzp.login.fragment.SmsLoginFragment;
import com.lzp.login.fragment.ThirdPartyLoginFragment;
import com.lzp.login.utils.UserUtil;

public class LoginActivity extends BaseActivity {
    private Button mBtnPwd, mBtnSms, mBtnRegiste, mBtnThird;
    private BaseFragment mCurFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_login);


        if (UserUtil.getWJLoginHelper().hasLogin()) {
            setResult(RESULT_OK);
            finish();
            return;
        }

        initView();
        showFragment(PwdLoginFragment.TAG);
    }

    private void initView() {
        mBtnRegiste = (Button) findViewById(R.id.activity_login_registe);
        mBtnRegiste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });

        mBtnPwd = (Button) findViewById(R.id.activity_login_pwd);
        mBtnPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(PwdLoginFragment.TAG);
            }
        });

        mBtnSms = (Button) findViewById(R.id.activity_login_sms);
        mBtnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(SmsLoginFragment.TAG);
            }
        });

        mBtnThird = (Button) findViewById(R.id.activity_login_third);
        mBtnThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(ThirdPartyLoginFragment.TAG);
            }
        });
    }

    private void showFragment(String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        BaseFragment targetFragment = (BaseFragment) manager.findFragmentByTag(tag);

        if (mCurFragment != null && (targetFragment == mCurFragment)) return;

        if (targetFragment == null) {
            if (tag.equals(PwdLoginFragment.TAG)) {
                targetFragment = PwdLoginFragment.createInstance();
            } else if (tag.equals(SmsLoginFragment.TAG)) {
                targetFragment = SmsLoginFragment.createInstance();
            }
            else if (tag.equals(ThirdPartyLoginFragment.TAG)) {
                targetFragment = ThirdPartyLoginFragment.createInstance();
            }
            transaction.add(R.id.activity_login_content, targetFragment, tag);
        } else {
            if (!targetFragment.isVisible()) {
                transaction.show(targetFragment);
            }
        }
        if (mCurFragment != null) {
            transaction.hide(mCurFragment);
        }
        mCurFragment = targetFragment;
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
