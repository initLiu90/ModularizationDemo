package com.lzp.login.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.lzp.core.base.BaseFragment;
import com.lzp.login.R;
import com.lzp.login.activity.WebActivity;
import com.lzp.login.adapter.Adapter;
import com.lzp.login.adapter.JdLoginAdapter;
import com.lzp.login.adapter.WxLoginAdapter;
import com.lzp.login.utils.Constansts;


public class ThirdPartyLoginFragment extends BaseFragment implements Adapter.Callback {
    public static final String TAG = ThirdPartyLoginFragment.class.getSimpleName();

    private Button mBtnJd, mBtnWx;
    private Adapter mWxAdapter, mJdAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWxAdapter = new WxLoginAdapter(getContext());
        mJdAdapter = new JdLoginAdapter(getContext());
    }

    public static BaseFragment createInstance() {
        BaseFragment fragment = new ThirdPartyLoginFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment_thirdparty, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mBtnJd = (Button) view.findViewById(R.id.fragment_thirdparty_jd);
        mBtnJd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJdAdapter.process(null, ThirdPartyLoginFragment.this);
            }
        });

        mBtnWx = (Button) view.findViewById(R.id.fragment_thirdparty_wx);
        mBtnWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWxAdapter.process(null, ThirdPartyLoginFragment.this);
            }
        });
    }

    @Override
    public void onSuccess(Object data) {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onTranslate(int type, Bundle bundle) {
        if (Constansts.TRANSLATE_TYPE_WEB == type) {
            Intent intent = new Intent(getContext(), WebActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onFail(int failTYpe, String message, Object data) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String errormsg) {
        Toast.makeText(getContext(), errormsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWxAdapter != null) {
            mWxAdapter.finish();
            mWxAdapter = null;
        }
        if (mJdAdapter != null) {
            mJdAdapter.finish();
            mJdAdapter = null;
        }
    }
}
