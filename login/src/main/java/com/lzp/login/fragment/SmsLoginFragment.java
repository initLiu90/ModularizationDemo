package com.lzp.login.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lzp.core.base.BaseFragment;
import com.lzp.library.util.CountdownTimer;
import com.lzp.login.R;
import com.lzp.login.adapter.Adapter;
import com.lzp.login.adapter.CheckSmsCodeLogin;
import com.lzp.login.adapter.GetSmsAAdapter;

public class SmsLoginFragment extends BaseFragment implements View.OnClickListener, Adapter.Callback {
    public static final String TAG = SmsLoginFragment.class.getSimpleName();

    private Button mBtnLogin, mBtnSmsCode;
    private EditText mEtPhone, mEtSmsCode;

    private Adapter mGetSmsAdapter, mCheckSmsAdapter;
    private byte mState = 1;//1 获取短信验证码，2 验证短信验证码
    private static final byte STATE_GETSMS = 1;
    private static final byte STATE_CHECKSMS = 2;

    private CountdownTimer mCountDown = new CountdownTimer() {
        @Override
        public void onTick(long millisUntilFinished) {
            mBtnSmsCode.setText(String.valueOf(millisUntilFinished));
        }

        @Override
        public void onFinish() {
            mBtnSmsCode.setEnabled(true);
            mBtnSmsCode.setText(getResources().getText(R.string.login_get_smscode));
        }
    };

    public static BaseFragment createInstance() {
        BaseFragment fragment = new SmsLoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment_smslogin, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mBtnLogin = (Button) view.findViewById(R.id.fragment_smslogin_login);
        mBtnLogin.setOnClickListener(this);

        mBtnSmsCode = (Button) view.findViewById(R.id.fragment_smslogin_getcode);
        mBtnSmsCode.setOnClickListener(this);

        mEtPhone = (EditText) view.findViewById(R.id.fragment_smslogin_phone);
        mEtSmsCode = (EditText) view.findViewById(R.id.fragment_smslogin_code);
    }

    @Override
    public void onClick(View v) {
        if (mBtnLogin == v) {
            checkCodeAndlogin();
        } else if (mBtnSmsCode == v) {
            getSmsCode();
        }
    }

    private void checkCodeAndlogin() {
        String phone = mEtPhone.getText().toString();
        String smsCode = mEtSmsCode.getText().toString();

        if (TextUtils.isEmpty(phone) || !phone.startsWith("1") || phone.length() < 11 || phone.length() > 12) {
            Toast.makeText(getContext(), "手机号码格式错误", Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(smsCode)) {
            Toast.makeText(getContext(), "短信验证码不能为空", Toast.LENGTH_LONG).show();
            return;
        }

        if (mCheckSmsAdapter == null) {
            mCheckSmsAdapter = new CheckSmsCodeLogin();
        }

        Adapter.Argument args = new Adapter.Argument();
        args.arg1 = phone;
        args.arg2 = smsCode;
        mCheckSmsAdapter.process(args, this);
        mState = STATE_CHECKSMS;
    }

    private void getSmsCode() {
        String phone = mEtPhone.getText().toString();
        if (TextUtils.isEmpty(phone) || !phone.startsWith("1") || phone.length() < 11 || phone.length() > 12) {
            Toast.makeText(getContext(), "手机号码格式错误", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mGetSmsAdapter == null) {
            mGetSmsAdapter = new GetSmsAAdapter();
        }

        if (!mCountDown.isRunning()) {
            Adapter.Argument args = new Adapter.Argument();
            args.arg1 = phone;
            mGetSmsAdapter.process(args, this);
        }
        mState = STATE_GETSMS;
    }

    @Override
    public void onSuccess(Object data) {
        if (mState == STATE_GETSMS) {
            mBtnSmsCode.setEnabled(false);
            mCountDown.start((int) data, 1);
        } else {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }

    @Override
    public void onError(String errormsg) {
        Toast.makeText(getContext(), errormsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFail(int failType, String message, Object data) {
        Toast.makeText(getContext(), "failType=" + failType + "message=" + message, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onTranslate(int type, Bundle bundle) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCountDown.cancel();
        if (mGetSmsAdapter != null) {
            mGetSmsAdapter.finish();
            mGetSmsAdapter = null;
        }
        if (mCheckSmsAdapter != null) {
            mCheckSmsAdapter.finish();
            ;
            mCheckSmsAdapter = null;
        }
    }
}
