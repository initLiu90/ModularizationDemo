package com.lzp.login.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lzp.core.base.BaseFragment;
import com.lzp.library.util.BitmapUtil;
import com.lzp.login.R;
import com.lzp.login.activity.WebActivity;
import com.lzp.login.adapter.Adapter;
import com.lzp.login.adapter.PwdLoginAdapter;
import com.lzp.login.utils.Constansts;

import jd.wjlogin_sdk.model.PicDataInfo;
import jd.wjlogin_sdk.util.MD5;

public class PwdLoginFragment extends BaseFragment implements View.OnClickListener, Adapter.Callback {
    public static final String TAG = PwdLoginFragment.class.getSimpleName();

    private Button mBtnLogin;
    private EditText mEtAccount;
    private EditText mEtPwd;
    private View mAutoCodeLayout;
    private EditText mEtAutoCode;
    private ImageView mImageAutoCode;

    private PicDataInfo mPicDataInfo;

    private PwdLoginAdapter mAdapter;

    private Bitmap mBitmap;

    public static BaseFragment createInstance() {
        BaseFragment fragment = new PwdLoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new PwdLoginAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment_pwdlogin, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mEtAccount = (EditText) view.findViewById(R.id.fragment_login_account);
        mEtPwd = (EditText) view.findViewById(R.id.fragment_login_pwd);
        mBtnLogin = (Button) view.findViewById(R.id.fragment_login_login);
        mBtnLogin.setOnClickListener(this);

        //初始化验证码layout
        mAutoCodeLayout = view.findViewById(R.id.fragment_login_autoCodeLayout);
        mEtAutoCode = (EditText) view.findViewById(R.id.fragment_login_autoCode);
        mImageAutoCode = (ImageView) view.findViewById(R.id.fragment_login_imageViewAutoCode);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnLogin) {
            login();
        }
    }

    private void login() {
        if (!checkInput()) return;

        String account = mEtAccount.getText().toString().trim();
        String pwd = MD5.encrypt32(mEtPwd.getText().toString().trim());
        if (mPicDataInfo != null) {
            mPicDataInfo.setAuthCode(mEtAutoCode.getText().toString().trim());
        }
        Adapter.Argument arguments = new Adapter.Argument();
        arguments.arg1 = account;
        arguments.arg2 = pwd;
        arguments.arg3 = mPicDataInfo;
        mAdapter.process(arguments, this);
    }


    /***************************处理登录回调 start***************************/
    @Override
    public void onSuccess(Object data) {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onTranslate(int type, Bundle bundle) {
        if (Constansts.TRANSLATE_TYPE_WEB == type) {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onFail(int failTYpe, String message, Object data) {
        if (Constansts.FAIL_TYPE_HIDE_PICDATA == failTYpe) {
            mPicDataInfo = null;
            mAutoCodeLayout.setVisibility(View.GONE);
        } else if (Constansts.FAIL_TYPE_SHOW_PICDATA == failTYpe) {
            mPicDataInfo = (PicDataInfo) data;
            mAutoCodeLayout.setVisibility(View.VISIBLE);
            updateAutoCodeImage();
        } else {
            getActivity().setResult(Activity.RESULT_CANCELED);
            getActivity().finish();
        }
    }

    @Override
    public void onError(String errormsg) {
        getActivity().setResult(Activity.RESULT_CANCELED);
        getActivity().finish();
    }

    /***************************处理登录回调 end***************************/

    private boolean checkInput() {
        if (TextUtils.isEmpty(mEtAccount.getText().toString().trim())) {
            Toast.makeText(getActivity(), "请输出用户名", Toast.LENGTH_SHORT).show();
            mEtAccount.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(mEtPwd.getText().toString().trim())) {
            Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
            mEtPwd.requestFocus();
            return false;
        }

        if (mPicDataInfo != null && TextUtils.isEmpty(mEtAutoCode.getText().toString().trim())) {
            Toast.makeText(getActivity(), "请输入验证码", Toast.LENGTH_SHORT).show();
            mEtAutoCode.requestFocus();
            return false;
        }
        return true;
    }


    private void updateAutoCodeImage() {
        recycleBitmap();
        mBitmap = BitmapUtil.createBitmap(mPicDataInfo.getsPicData(), Bitmap.Config.RGB_565, mImageAutoCode
                .getWidth(), mImageAutoCode.getHeight());
        mImageAutoCode.setImageBitmap(mBitmap);
    }

    private void recycleBitmap() {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mImageAutoCode.setImageBitmap(null);
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recycleBitmap();
        if (mAdapter != null) {
            mAdapter.finish();
        }
    }
}
