package cn.ysgroup.ysdai.Util;

public class Validator {

    /**
     * 手机号是否合法
     * @param phoneno
     * @return
     */
    public static boolean validatePhoneno(String phoneno) {

        if (StringUtils.matchRegex(phoneno, AppConstants.PHONENO_REGEX)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 手机验证码是否合法
     * @param vCode
     * @return
     */
    public static boolean validateVCode(String vCode) {


        if (StringUtils.matchRegex(vCode, AppConstants.LOGIN_VALIDATECODE_REGEX)) {
            return true;
        } else {
            return false;
        }

    }
}
