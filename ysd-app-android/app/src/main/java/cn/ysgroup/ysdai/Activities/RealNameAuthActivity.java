package cn.ysgroup.ysdai.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import cn.ysgroup.ysdai.Adapters.BankListAdapter;
import cn.ysgroup.ysdai.Beans.bank.BankCard;
import cn.ysgroup.ysdai.Beans.bank.BankList;
import cn.ysgroup.ysdai.Beans.bank.RbOrderBean;
import cn.ysgroup.ysdai.Beans.bank.ZSBeen;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.UI.MessageDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.ClickEvent;
import cn.ysgroup.ysdai.Util.IntentConstants;
import cn.ysgroup.ysdai.Util.PreferenceUtil;
import cn.ysgroup.ysdai.Util.ToastUtil;
//绑卡认证

/**
 * 绑卡流程
 * 1、读取银行卡信息，/rest/bankTo，如果if (banklist.getBankId() != null) ；那么说明用户以前绑过卡，现在可以再次绑定。界面绑卡按钮显示：签约中，再次绑定
 * 2、绑定银行卡操作，调用接口：/rest/bankSignSaveHnew，
 * 如果返回是R0002，代表这是招行的一次绑定动作，跳转zsplayactivity加载url来获取订单信息，拼接order的intent参数后跳转到quickplayactivity;
 * 如果是R0001，则直接从响应中获取order作为intent参数跳转到quickplayactivity.
 */
public class RealNameAuthActivity extends MyBaseActivity implements ListView.OnItemClickListener {
    private final String TAG = "绑卡认证";
    IconFontTextView realNameAuthToolbarBack;
    TextView realNameAuthToolbarTitle;
    Toolbar realNameAuthToolbar;
    EditText realNameAuthNameInput;
    EditText realNameAuthIdInput;
    LinearLayout realNameAuthFormLayout;
    ImageView addBankBankIcon;
    IconFontTextView addBankBankScroll;
    TextView addBankBankName;
    RelativeLayout addBankBankLayout;
    EditText addBankCardCardId;
    TextView bankCardPopupText;
    EditText realNameAuthSafePassInput;
    Button addBankCardButton;
    CardView cardView;
    ScrollView realNameAuthScrollView;
    TextView realNameAuthSafePassText;
    private Gson gson = new Gson();
    private String userId;
    private boolean isFirstName = true;  //是否是第一次进入页面
    private boolean isFirstCard = true;  //是否是第一次进入页面
    private MessageDialog messageDialog;
    private BankList banklist;//银行列表
    private BankCard selectedBank;
    private LoadingDialog loadingDialog;
    private PopupWindow popupWindow;
    private int selectPosition = 200;//进入银行页面默认选择position
    private final static int REQUEST_CODE_BAOFOO_SDK = 100;
    private static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x111://认证提交

                    if (banklist.getRealName() != null) {
                        realNameAuthNameInput.setText(banklist.getRealName());
                    }
                    if (banklist.getIdNo() != null) {
                        realNameAuthIdInput.setText(banklist.getIdNo());
                    }
                    if (banklist.getCardNo() != null) {
                        addBankCardCardId.setText(banklist.getCardNo());
                    }
                    if (banklist.getBankId() != null) {
                        addBankCardButton.setText("签约中，再次绑定");
                        for (int x = 0; x < banklist.getBankCardList().size(); x++) {
                            if (banklist.getBankCardList().get(x).getBankId().equals(banklist.getBankId())) {
                                selectedBank = banklist.getBankCardList().get(x);
                                addBankBankName.setText(banklist.getBankCardList().get(x).getBankName());

                                String limit_str = banklist.getBankCardList().get(x).getBankLimit();
                                if(TextUtils.isEmpty(limit_str)){
                                    bank_limit.setVisibility(View.GONE);
                                    updateLayoutParams(false);
                                }else{
                                    bank_limit.setVisibility(View.VISIBLE);
                                    bank_limit.setText(limit_str);
                                    updateLayoutParams(true);
                                }

                                int resId = getResources().getIdentifier("bank_" + selectedBank.getBankId().toLowerCase(), "mipmap", getPackageName());
                                addBankBankIcon.setImageResource(resId);
                                selectPosition = x;
                            }
                        }

                    }

                    addBankBankLayout.setOnClickListener(

                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //进入银行选择页面
                                    Intent intent = new Intent(RealNameAuthActivity.this, SelectBankActivity.class);
                                    intent.putExtra("bean", banklist);
                                    intent.putExtra("selectPosition", selectPosition);
                                    startActivity(intent);

//                                    if (popupWindow == null) {
//                                        initPopuptWindow();
//                                        popupWindow.showAsDropDown(addBankBankLayout, 0, 0);
//
//                                    } else if (popupWindow.isShowing()) {
//                                        popupWindow.dismiss();
//                                    } else {
//                                        popupWindow.showAsDropDown(addBankBankLayout, 0, 0);
//                                    }
                                }
                            }

                    );
                    if (banklist.getStatus() != null && banklist.getStatus().equals("0")) {
                        realNameAuthSafePassText.setText("输入交易密码");
                    }

                    break;
                case 0x222:
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                        loadingDialog = null;
                    }
                    break;
                default:
                    break;

            }
        }
    };
    private EditText phone;
    private String order;
    private String type;
    private String cardIdText;
    private String bankNameText;
    private String realNameText;
    private String safePassText;
    private String realIdText;
    private TextView bankNamePopupText;
    private RealNameAuthActivityBroadCast broadCast;
    private TextView bank_limit;
    private String inverstMark;
    private String selectedIdArrayString;
    private String safePass;
    private String tenderMoney;
    private int projectId;
    private String project;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name_auth);
        initView();
        initToolBar();
        initListener();


        userId = getIntent().getStringExtra("userId");
        String url = AppConstants.URL_SUFFIX + "/rest/bankTo";
        RequestForBankData(url);


    }

    private void initListener() {
        realNameAuthIdInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    bankNamePopupText.setVisibility(View.GONE);
                } else {
                    bankNamePopupText.setVisibility(View.VISIBLE);
                }
            }
        });

        realNameAuthIdInput.addTextChangedListener(
                //下面的黄色字体
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() > 0) {
                            String orignText = s.toString();

                            StringBuilder resultText = new StringBuilder();
                            while (orignText.length() > 4) {
                                String fourCharText = orignText.substring(0, 4);
                                resultText.append(fourCharText + "   ");
                                orignText = orignText.substring(4, orignText.length());
                            }
                            resultText.append(orignText);
                            bankNamePopupText.setText(resultText.toString());
                            if (isFirstName) {
                                isFirstName = false;
                            } else {
                                bankNamePopupText.setVisibility(View.VISIBLE);
                            }
                        } else {
                            bankNamePopupText.setText("");
                            //bankCardPopupText.setVisibility(View.INVISIBLE);
                        }
                    }
                }

        );

        addBankCardCardId.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            bankCardPopupText.setVisibility(View.VISIBLE);
                            realNameAuthScrollView.scrollTo(0, realNameAuthFormLayout.getMeasuredHeight());
                        } else {
                            bankCardPopupText.setVisibility(View.GONE);
                        }
                    }
                }
        );
        addBankCardCardId.addTextChangedListener(
                //下面的黄色字体
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() > 0) {
                            String orignText = s.toString();

                            StringBuilder resultText = new StringBuilder();
                            while (orignText.length() > 4) {
                                String fourCharText = orignText.substring(0, 4);
                                resultText.append(fourCharText + "   ");
                                orignText = orignText.substring(4, orignText.length());
                            }
                            resultText.append(orignText);
                            bankCardPopupText.setText(resultText.toString());

                            if (isFirstCard) {
                                isFirstCard = false;
                            } else {
                                bankCardPopupText.setVisibility(View.VISIBLE);
                            }
                        } else {
                            bankCardPopupText.setText("");
                            //bankCardPopupText.setVisibility(View.INVISIBLE);
                        }
                    }
                }

        );
        addBankCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBank();
            }
        });
    }


    private void initView() {
        inverstMark = getIntent().getStringExtra("inverstMark");

        selectedIdArrayString = getIntent().getStringExtra("selectedIdArrayString");
        tenderMoney = getIntent().getStringExtra("tenderMoney");
        projectId = getIntent().getIntExtra("projectId", 0);
        project = getIntent().getStringExtra("project");


        phone = (EditText) findViewById(R.id.real_name_auth_name_phone);
        realNameAuthToolbarBack = (IconFontTextView) findViewById(R.id.real_name_auth_toolbar_back);
        realNameAuthToolbarTitle = (TextView) findViewById(R.id.real_name_auth_toolbar_title);
        realNameAuthToolbar = (Toolbar) findViewById(R.id.real_name_auth_toolbar);
        realNameAuthNameInput = (EditText) findViewById(R.id.real_name_auth_name_input);
        realNameAuthIdInput = (EditText) findViewById(R.id.real_name_auth_id_input);
        realNameAuthFormLayout = (LinearLayout) findViewById(R.id.real_name_auth_form_layout);
        addBankBankIcon = (ImageView) findViewById(R.id.add_bank_bank_icon);
        addBankBankScroll = (IconFontTextView) findViewById(R.id.add_bank_bank_scroll);
        addBankBankName = (TextView) findViewById(R.id.add_bank_bank_name);
        addBankBankLayout = (RelativeLayout) findViewById(R.id.add_bank_bank_layout);
        addBankCardCardId = (EditText) findViewById(R.id.add_bank_card_card_id);
        bankCardPopupText = (TextView) findViewById(R.id.bank_card_popup_text);
        bankNamePopupText = (TextView) findViewById(R.id.bank_name_popup_text);
        realNameAuthSafePassInput = (EditText) findViewById(R.id.real_name_auth_safe_pass_input);
        addBankCardButton = (Button) findViewById(R.id.add_bank_card_button);
        cardView = (CardView) findViewById(R.id.card_view);
        realNameAuthScrollView = (ScrollView) findViewById(R.id.real_name_auth_scroll_view);
        realNameAuthSafePassText = (TextView) findViewById(R.id.real_name_auth_safe_pass_text);
        bank_limit = (TextView) findViewById(R.id.add_bank_bank_limit);
        broadCast = new RealNameAuthActivityBroadCast();
        IntentFilter intentFilter = new IntentFilter("RealNameAuthActivityBroadCast");
        registerReceiver(broadCast, intentFilter);

        String userPhone = getSharedPreferences("UserId", Context.MODE_PRIVATE).getString("UserId", null);
        if (userPhone != null) {
            phone.setText(userPhone);
        }
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void initToolBar() {
        realNameAuthToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(realNameAuthToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    protected void initPopuptWindow() {
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        View popupWindow_view = getLayoutInflater().inflate(R.layout.bank_list_popup_window_lauout, null, false);

        ListView bankListView = (ListView) popupWindow_view.findViewById(R.id.bank_popup_window_list_view);
        BankListAdapter myAdapter = new BankListAdapter(this, banklist.getBankCardList());
        bankListView.setAdapter(myAdapter);
        bankListView.setOnItemClickListener(this);

        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        popupWindow = new PopupWindow(popupWindow_view, addBankBankLayout.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                return false;
            }
        });
    }

    public void AddBank() {
        //如果快速点击了两次
        if (ClickEvent.isFastDoubleClick(R.id.add_bank_card_button)) {
            return;
        }

        cardIdText = addBankCardCardId.getText().toString();
        bankNameText = addBankBankName.getText().toString();
//        String bankbranchText = addBankBankBranch.getText().toString();
        realNameText = realNameAuthNameInput.getText().toString();
        realIdText = realNameAuthIdInput.getText().toString();
        safePassText = realNameAuthSafePassInput.getText().toString();
        safePass = safePassText;
        if (cardIdText == null || cardIdText.trim().equals("")) {
            Toast.makeText(this, "请填写银行卡号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (bankNameText == null || bankNameText.trim().equals("")) {
            Toast.makeText(this, "请选择银行", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.getText().toString() == null || !isMobile(phone.getText().toString())) {
            Toast.makeText(RealNameAuthActivity.this, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (realNameText == null || realNameText.trim().equals("")) {
            Toast.makeText(RealNameAuthActivity.this, "请填写姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (realIdText == null || realIdText.trim().equals("")) {
            Toast.makeText(RealNameAuthActivity.this, "请填写身份证号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (safePassText == null || safePassText.trim().equals("")) {
            Toast.makeText(RealNameAuthActivity.this, "请填写交易密码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (safePassText.length() < 6) {
            Toast.makeText(RealNameAuthActivity.this, "交易密码长度不符合要求", Toast.LENGTH_SHORT).show();
            return;
        }


        if (selectedBank == null) {
            Toast.makeText(this, "绑定失败,请刷新数据后重试！", Toast.LENGTH_SHORT).show();
            return;
        }
        //0-表示增加，1表示修改
        type = "0";

        if (banklist.getBankId() != null && !banklist.getBankId().equals("")) {
            type = "1";
        }

        //请求绑定英航卡
        String url = AppConstants.URL_SUFFIX + "/rest/bankSignSaveHnew";
        RequestForRechargeToBand(url);
        addBankCardButton.setEnabled(false);
    }

    /**
     * 获取银行卡数据
     *
     * @param basicUrl
     */
    public void RequestForBankData(String basicUrl) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                build();
        Request request = new Request.Builder().url(basicUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (e != null && e.getMessage() != null) {
                            Log.i(TAG, e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s = response.body().string();
                Log.i(TAG, s);

                final BankList resultBean = JSON.parseObject(s, BankList.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultBean.getRcd().equals("R0001")) {
                            banklist = resultBean;
                            if (banklist != null) {
                                Message message = new Message();
                                message.what = 0x111;
                                message.obj = "";
                                mHandler.sendMessage(message);
                            }
                        } else {
                            Toast.makeText(RealNameAuthActivity.this, resultBean.getRmg(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        selectedBank = (BankCard) parent.getAdapter().getItem(position);
        int resId = getResources().getIdentifier("bank_" + selectedBank.getBankId().toLowerCase(), "mipmap", getPackageName());
        addBankBankIcon.setImageResource(resId);
        addBankBankName.setText(selectedBank.getBankName());
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }

    }

    //绑卡
    public void RequestForRechargeToBand(String url) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
            loadingDialog.show();
        }
        OkHttpClient client = new OkHttpClient();
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        formEncodingBuilder.
                add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                add("btype", type).
                add("cashBank.cardNo", cardIdText).
                add("cashBank.bankName", selectedBank.getBankName()).
                add("bankId", selectedBank.getBankId()).
                add("realName", realNameText).
                add("idNo", realIdText).
                add("registerTime", banklist.getRegisterTime()).
                add("cardNo", cardIdText.replace(" ", "")).
                add("safepwd", safePassText).
                add("phone", phone.getText().toString()).
                add("money", getIntent().getStringExtra("money"));
        if (userId != null) {
            formEncodingBuilder.add("username", userId);
        }
        RequestBody body = formEncodingBuilder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (e != null && e.getMessage() != null) {
                            Log.i(TAG, e.getMessage());
                        }
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s = response.body().string();
                Log.i(TAG, s);
                String rcd = null;
                try {
                    JSONObject object = new JSONObject(s);
                    rcd = object.getString("rcd");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (rcd != null && rcd.equals("R0002")) {
                    //招行卡
                    ZSBeen zsBeen = gson.fromJson(s, ZSBeen.class);
                    Intent intent = new Intent(RealNameAuthActivity.this, ZsPlayActivity.class);
                    intent.putExtra("zsBeen", zsBeen);
                    if (inverstMark != null)
                        intent.putExtra("inverstMark", inverstMark);
                    intent.putExtra("tenderMoney", tenderMoney);
                    intent.putExtra("selectedIdArrayString", selectedIdArrayString);
                    intent.putExtra("project", project);
                    intent.putExtra("projectId", projectId);
                    intent.putExtra("safePass", safePass);
                    startActivityForResult(intent, 100);
                    mHandler.sendEmptyMessage(0x222);
                } else {
                    final RbOrderBean bean = gson.fromJson(s, RbOrderBean.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                                loadingDialog = null;
                            }
                            if (bean.getRcd().equals("R0001")) {
                                toBankCardVcode(bean);
                            } else {
                                addBankCardButton.setEnabled(true);
                                Toast.makeText(RealNameAuthActivity.this, bean.getRmg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 跳转绑卡获取银行验证码界面
     * @param bean
     */
    private void toBankCardVcode(RbOrderBean bean){
        order = bean.getOrder_no();
        Intent intent = new Intent();
        intent.setClass(this, BankCardBindVerifyActivity.class);
        intent.putExtra("order", order);

        startActivityForResult(intent, IntentConstants.BANKCARD_BIND_REQUEST_CODE);
    }

    /**
     * 绑卡完毕 ，跳转支付界面
     * @param resultIntent
     */
    private void getResultFromBankCardVCode(Intent resultIntent){
        //进入支付页面  绑卡完毕
//        order = resultIntent.getStringExtra("order");
//        Intent intent = new Intent(RealNameAuthActivity.this, QuicklyPlayActivity.class);
//        intent.putExtra("order", order);
//        if (inverstMark != null) {
//            intent.putExtra("inverstMark", inverstMark);
//            intent.putExtra("tenderMoney", tenderMoney);
//            intent.putExtra("selectedIdArrayString", selectedIdArrayString);
//            intent.putExtra("project", project);
//            intent.putExtra("projectId", projectId);
//            intent.putExtra("safePass", safePass);
//
//        }
//        RealNameAuthActivity.this.startActivity(intent);
        ToastUtil.showToast(this,"绑卡成功");
        finish();
    }


    @Override
    protected void onDestroy() {
//        sendBroadcast(new Intent("LoginContentBroadCast"));
        super.onDestroy();
        unregisterReceiver(broadCast);
    }

    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 222) {
            finish();
        }else{
            if(requestCode == IntentConstants.BANKCARD_BIND_REQUEST_CODE && resultCode == RESULT_OK){
                getResultFromBankCardVCode(data);
            }
        }
    }

    class RealNameAuthActivityBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("mark").equals("selectBank")) {
                int position = intent.getIntExtra("position", 100);
                selectPosition = position;
                BankCard bankCard = (BankCard) intent.getSerializableExtra("BankCard");
                int resId = getResources().getIdentifier("bank_" + bankCard.getBankId().toLowerCase(), "mipmap", getPackageName());
                addBankBankIcon.setImageResource(resId);
                addBankBankName.setText(bankCard.getBankName());

                String limit_str = bankCard.getBankLimit();
                if(TextUtils.isEmpty(limit_str)){
                    bank_limit.setVisibility(View.GONE);
                    updateLayoutParams(false);
                }else{
                    bank_limit.setVisibility(View.VISIBLE);
                    bank_limit.setText(limit_str);
                    updateLayoutParams(true);
                }

                selectedBank = banklist.getBankCardList().get(position);
            }
        }
    }

    private void updateLayoutParams(boolean havelimit){
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)addBankBankName.getLayoutParams();

        if(havelimit){
            lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        }else{
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
        }
    }


}
