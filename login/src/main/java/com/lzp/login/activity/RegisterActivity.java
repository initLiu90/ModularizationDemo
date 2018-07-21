package com.lzp.login.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lzp.core.base.BaseActivity;
import com.lzp.login.R;

public class RegisterActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_registe);

//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        BaseFragment fragment = (BaseFragment) manager.findFragmentByTag(RegisteFragment.TAG);
//        if (fragment == null) {
//            fragment = RegisteFragment.createInstance();
//            transaction.add(R.id.activity_registe_content, fragment, RegisteFragment.TAG);
//        } else if (!fragment.isVisible()) {
//            transaction.show(fragment);
//        }
//        transaction.commitAllowingStateLoss();
    }
}
