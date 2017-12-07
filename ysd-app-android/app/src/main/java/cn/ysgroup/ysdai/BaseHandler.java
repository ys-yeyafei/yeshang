package cn.ysgroup.ysdai;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import cn.ysgroup.ysdai.Activities.LoginActivity;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.ToastUtil;

/**
 * Created by zenglulin on 2017/9/26.
 */

public class BaseHandler extends Handler {

    private Activity mActivity;
    public BaseHandler(Activity activity){
        this.mActivity = activity;
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case AppConstants.MESSAGE_LOGIN_EXPIRED:
                onLoginExpired();
                break;
            case AppConstants.MESSAGE_NETWORK_ERROR:
                onNetWorkError();
                break;
        }
    }

    private void onLoginExpired(){
        mActivity.startActivityForResult(new Intent(mActivity, LoginActivity.class), 1400);
        mActivity.overridePendingTransition(R.anim.activity_up, R.anim.activity_down);
        Toast.makeText(mActivity, "登录已过期，请重新登录", Toast.LENGTH_SHORT).show();
        mActivity.finish();
    }

    private void onNetWorkError(){
        ToastUtil.showToast(mActivity, "网络错误");
    }

}
