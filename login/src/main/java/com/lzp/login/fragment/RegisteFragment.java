//package com.lzp.login.fragment;
//
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.lzp.core.base.BaseFragment;
//import com.lzp.library.util.BitmapUtil;
//import com.lzp.login.R;
//import com.lzp.login.processor.RegisteProcessor;
//
//import jd.wjlogin_sdk.model.PicDataInfo;
//
//public class RegisteFragment extends BaseFragment implements RegisteProcessor.Callback {
//    public static final String TAG = RegisteFragment.class.getSimpleName();
//
//    private EditText mEtPhone;
//    private EditText mEtCode;
//    private ImageView mImageCode;
//    private Button mBtnNext;
//    private PicDataInfo mPicDataInfo;
//
//    private RegisteProcessor mProcessor;
//    private Bitmap mBitmap;
//
//    public static BaseFragment createInstance() {
//        BaseFragment fragment = new RegisteFragment();
//        return fragment;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.login_fragment_registe, null);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        mEtPhone = (EditText) view.findViewById(R.id.fragment_registe_phone);
//        mEtCode = (EditText) view.findViewById(R.id.fragment_registe_code);
//        mBtnNext = (Button) view.findViewById(R.id.fragment_registe_next);
//        mImageCode = (ImageView) view.findViewById(R.id.fragment_registe_imagecode);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        //第一次 取验证码的参数，图片验证码为空字符串。
//        mPicDataInfo = new PicDataInfo();
//        mPicDataInfo.setAuthCode("0");
//        mPicDataInfo.setStEncryptKey("");
//
//        mProcessor = new RegisteProcessor(this);
//        mProcessor.getImageCode();
//    }
//
//    @Override
//    public void showImageCode(boolean show, PicDataInfo data) {
//        if (show) {
//            mPicDataInfo = data;
//        }
//        updateImageCode(show);
//    }
//
//    @Override
//    public void onError(String message) {
//        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onFail(int type, String message) {
//        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//    }
//
//    private void updateImageCode(boolean show) {
//        recycleBitmap();
//        if (show) {
//            mImageCode.setVisibility(View.VISIBLE);
//            mBitmap = BitmapUtil.createBitmap(mPicDataInfo.getsPicData(), Bitmap.Config.RGB_565, mImageCode
//                    .getWidth(), mImageCode.getHeight());
//            mImageCode.setImageBitmap(mBitmap);
//        } else {
//            mImageCode.setVisibility(View.GONE);
//        }
//    }
//
//    private void recycleBitmap() {
//        if (mBitmap != null && !mBitmap.isRecycled()) {
//            mImageCode.setImageBitmap(null);
//            mBitmap.recycle();
//            mBitmap = null;
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//}
