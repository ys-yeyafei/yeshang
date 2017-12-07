package cn.ysgroup.ysdai.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.ysgroup.ysdai.BaseHandler;
import cn.ysgroup.ysdai.Beans.BaseBean;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.Util.ApiUtil;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.PreferenceUtil;
import cn.ysgroup.ysdai.Util.ToastUtil;

/**
 * Created by zenglulin on 2017/9/26.
 */

public class BankCardBindVerifyActivity extends MyBaseActivity{
    @Bind(R.id.bank_card_bind_getcode_btn)
    Button getCodeBtn;

    @Bind(R.id.bank_card_bind_verify_code)
    EditText codeInput;

    @Bind(R.id.bank_card_bind_sure_btn)
    Button bankCardSureBtn;


    private CountDownTimer mTimer;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card_verify);
        ButterKnife.bind(this);

    }

    public void onNewIntent(Intent newIntent){
        setIntent(newIntent);
        resetUI();
    }

    /**
     * 一般在fragment所属activity的onNewIntent方法中调用
     */
    public void resetUI() {
        getCodeBtn.setEnabled(true);
        getCodeBtn.setText("获取验证码");
        codeInput.setText("");
        stopVCodeTimer();
    }

    @OnClick(R.id.bank_card_bind_getcode_btn)
    public void doGetCodeBtn(){
        getCodeBtn.setEnabled(false);
        requestSendVCodeAagin();

    }

    @OnClick(R.id.bank_card_bind_sure_btn)
    public void doBankCardBind(){
        if (checkLoginForm()) {
            requestBindBankCard();
        }

    }

    @OnTextChanged(value = R.id.bank_card_bind_verify_code, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterTextChanged(Editable s) {
        if (s.length() < 4) {
            bankCardSureBtn.setBackgroundResource(R.drawable.button_background_normal1);
            bankCardSureBtn.setEnabled(false);
        } else {
            bankCardSureBtn.setBackgroundResource(R.drawable.button_background_normal);
            bankCardSureBtn.setEnabled(true);
        }
    }

    private void requestBindBankCard(){
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
            loadingDialog.show();
        }

        Map<String,String> postParams = new HashMap<>();
        postParams.put("token", PreferenceUtil.getPrefString(this, "loginToken", ""));
        postParams.put("checkCode", codeInput.getText().toString());
        ApiUtil.getInstance().sendPostRequest(AppConstants.URL_BANKCARD_BIND_PHONE_VCODE,
                postParams, bindCardCallback);
    }

    private void requestSendVCodeAagin(){
        Map<String,String> postParams = new HashMap<>();
        postParams.put("token", PreferenceUtil.getPrefString(this, "loginToken", ""));
        ApiUtil.getInstance().sendPostRequest(AppConstants.URL_BANKCARD_BIND_SEND_VCODE_AGAIN,
                postParams, sendVCodeCallback);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        stopVCodeTimer();
    }

    private boolean checkLoginForm() {
        if(!checkInput()){
            return false;
        }
        return true;
    }

    protected boolean checkInput(){
        String vCode = codeInput.getText().toString();
        if (TextUtils.isEmpty(vCode)) {
            ToastUtil.showToast(this, getString(R.string.need_vcode_tip));
            return false;
        } else {
//            if (!Validator.validateVCode(vCode)) {
//                ToastUtil.showToast(this, String.format(getString(R.string.input_vcode_tip), AppConstants.PHONE_VALIDATECODE_STR_LEN));
//                return false;
//            } else {
//            }
        }

        return true;
    }


    private void stopVCodeTimer() {
        if (null != mTimer) {
            mTimer.cancel();
        }
    }

    private void startVCodeTimer() {

        if (null == mTimer) {
            mTimer = new CountDownTimer(60000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    // TODO Auto-generated method stub
                    getCodeBtn.setText(millisUntilFinished / 1000 + "秒");
                }

                @Override
                public void onFinish() {
                    // TODO Auto-generated method stub
                    getCodeBtn.setEnabled(true);
                    getCodeBtn.setText("重发验证码");
                }
            };
        }

        mTimer.start();
    }

    private Callback sendVCodeCallback = new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {
            mHandler.sendEmptyMessage(AppConstants.MESSAGE_NETWORK_ERROR);
        }

        @Override
        public void onResponse(Response response) throws IOException {

            int code = response.code();

            if(code == HttpURLConnection.HTTP_OK){
                BaseBean result = JSON.parseObject(response.body().string(),BaseBean.class);

                if(result.isOk()){
                    //
                    mHandler.sendEmptyMessage(MESSAGE_SEND_CODE_SUC);
                }else if(result.isLoginExpired()){
                    mHandler.sendEmptyMessage(AppConstants.MESSAGE_LOGIN_EXPIRED);
                }
                else{
                    Message msg = Message.obtain();
                    msg.what = MESSAGE_SEND_CODE_FAIL;
                    msg.obj = result.getRmg();
                    mHandler.sendMessage(msg);
                }
            }else{
                mHandler.sendEmptyMessage(AppConstants.MESSAGE_NETWORK_ERROR);
            }
        }
    };

    private Callback bindCardCallback = new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {
            mHandler.sendEmptyMessage(AppConstants.MESSAGE_NETWORK_ERROR);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            int code = response.code();

            if(code == HttpURLConnection.HTTP_OK){
                BaseBean result = JSON.parseObject(response.body().string(),BaseBean.class);

                if(null != result && result.isOk()){
                    //
                    mHandler.sendEmptyMessage(MESSAGE_BIND_CARD_SUC);
                }else if(result.isLoginExpired()){
                    mHandler.sendEmptyMessage(AppConstants.MESSAGE_LOGIN_EXPIRED);
                }else{
                    Message msg = Message.obtain();
                    msg.what = MESSAGE_BIND_CARD_FAIL;
                    msg.obj = result.getRmg();
                    mHandler.sendMessage(msg);
                }
            }else{
                mHandler.sendEmptyMessage(AppConstants.MESSAGE_NETWORK_ERROR);
            }
        }
    };

    private Handler mHandler = new BaseHandler(this) {

        public void handleMessage(Message msg) {
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
                loadingDialog = null;
            }

            switch (msg.what) {
                case MESSAGE_BIND_CARD_SUC:
                    onBindCardSuc();
                    break;
                case MESSAGE_BIND_CARD_FAIL:
                    onBindCardFail((String)msg.obj);
                    break;
                case MESSAGE_SEND_CODE_SUC:
                    onSendVCodeSuc();
                    break;
                case MESSAGE_SEND_CODE_FAIL:
                    onSendVCodeFail((String)msg.obj);
                    break;

            }


        }
    };

    private void onSendVCodeSuc(){
        ToastUtil.showToast(this, "验证码发送成功,请查收短信");
        startVCodeTimer();  //发验证码接口调用成功后启动倒计时
        codeInput.requestFocus();
        codeInput.requestFocusFromTouch();
    }

    private void onSendVCodeFail(String failReason){
        ToastUtil.showToast(this, failReason);
        getCodeBtn.setEnabled(true);   //验证码发送失败后重置
        getCodeBtn.setText("重发验证码");
    }

    private void onBindCardSuc(){
        Intent intent = new Intent();
        intent.putExtras(getIntent());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void onBindCardFail(String failReason){
        ToastUtil.showToast(this, failReason);
    }




    private static final int MESSAGE_BIND_CARD_SUC = 1;
    private static final int MESSAGE_SEND_CODE_SUC = 2;
    private static final int MESSAGE_BIND_CARD_FAIL = 3;
    private static final int MESSAGE_SEND_CODE_FAIL = 4;

}
