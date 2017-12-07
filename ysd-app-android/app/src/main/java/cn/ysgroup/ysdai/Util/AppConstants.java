package cn.ysgroup.ysdai.Util;

/**
 * Created by Administrator on 2015/10/22.
 */
public  class AppConstants {

//
      public static final String URL_SUFFIX ="https://www.yueshanggroup.cn/api";
      public static final String IMG_URL_SUFFIX="https://www.yueshanggroup.cn/mobile";
      private static final String API_PATH_PREFIX = URL_SUFFIX + "/rest";

      public static final String JPUSH_TAG_ALL = "tag_all"; //全平台推送 不区分单个用户的推送

      //测试
//      public static final String URL_SUFFIX ="http://192.168.0.215:8080";
//      public static final String IMG_URL_SUFFIX="http://192.168.0.215:8080/mobile";
//
//      public static final String URL_SUFFIX ="http://160836c5u5.iok.la:9988";
//      public static final String IMG_URL_SUFFIX="http://160836c5u5.iok.la:9988/mobile";

//      public static final String URL_SUFFIX ="http://192.168.0.140:8080";
//      public static final String IMG_URL_SUFFIX="http://192.168.0.140:8080/mobile";

//      public static final String URL_SUFFIX ="http://hzgeek.imwork.net:8181";
//      public static final String IMG_URL_SUFFIX="http://hzgeek.imwork.net:8181/mobile";

      public static final String URL_BANKCARD_BIND_PHONE_VCODE = API_PATH_PREFIX + "/bankSignPhoneCode";
      public static final String URL_BANKCARD_BIND_SEND_VCODE_AGAIN = API_PATH_PREFIX + "/bankSignPhoneCodeRepeat";


      //手机号码验证的失效时间（单位：秒）
      public static final int VerifyCodeTimeFuture =60;

      //手机号码验证时候的提醒时间间隔（单位：秒）
      public static final int VerifyCodeTimeInteral =1;

      //预览页面关闭，跳到投资中心
      public static final int ChooseHongBaoOkCode =9092;

      //预览页面关闭，跳到投资中心
      public static final int IntroToInvestCode =7002;
      //预览页面关闭，跳到登录
      public static final int PreviewToLoginCode =8002;
      //预览页面关闭，跳到投资中心
      public static final int PreviewToInvestOthersCode =8001;
      //退出登录回调代码
      public static final int AppLoginEXitCode =9999;
      //邮箱验证回调代码（正在验证中）
      public static final int EmailAuthIng =7070;
      public static final int EmailAuthSuccess =7071;
      //实名认证回调代码（正在审核中）
      public static final int RealAuthIng =6060;
      public static final int RealAuthSuccess =6061;

      //银行卡充值完成后结果回调代码
      public static final int ChargeMoneyResultBack =5050;
      //银行卡绑定完成后结果回调代码
      public static final int BindBankCardResultSuccess =4040;

      //返回账户中心的回调代码
      public static final int backToUserCenterResult =3030;
      //投资成功后的回调代码
      public static final int projectInvestSuccess =2020;

      //修改登录密码成功后的回调
      public static final int modifyLoginSuccess =1010;
      //
      public static final int registerSuccessCode =1020;


      public static final String PHONE_VALIDATECODE_STR_LEN = "6";
      public static final String BANK_VALIDATECODE_STR_LEN = "6";
      public static final String PHONENO_REGEX = "^1[0-9]{10}";  //手机号的限制输入
      public static final String LOGIN_VALIDATECODE_REGEX = "[0-9]{" + PHONE_VALIDATECODE_STR_LEN +  "}";    //登录验证码的限制输入
      public static final String BANK_VALIDATECODE_REGEX = "[0-9]{"+ BANK_VALIDATECODE_STR_LEN + "6}";    //银行预留手机号验证码的限制输入

      public static final String NOT_EMPTY_REGEX = "(\\S){1,}";    //匹配非空字串



      public static final int MESSAGE_LOGIN_EXPIRED = -1;
      public static final int MESSAGE_NETWORK_ERROR = -2;

}
