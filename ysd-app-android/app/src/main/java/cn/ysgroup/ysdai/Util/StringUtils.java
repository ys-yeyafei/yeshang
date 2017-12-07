
package cn.ysgroup.ysdai.Util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtils {
	

		
		public static boolean matchRegex(CharSequence input, String regex){
			Pattern pattern = Pattern.compile(regex);
			
			return pattern.matcher(input).matches();
		}

	public static boolean findMatchRegex(CharSequence input, String regex){
		Pattern pattern = Pattern.compile(regex);

		return pattern.matcher(input).find();
	}

	public static int toInt(String str){
		try {
			return Integer.parseInt(str);
		}
		catch (Exception exp){
			return 0;
		}
	}

	public static double toDouble(String str){
		try {
			return Double.parseDouble(str);
		}
		catch (Exception exp){
			return 0d;
		}
	}

	public static double toDouble(String str, double def) {
		double ret = def;
		try{
			ret = Double.parseDouble(str);
		}catch(Exception ex) {
			ret = def;
		}
		return ret;
	}

	public static float toFloat(String str){
		try {
			return Float.parseFloat(str);
		}
		catch (Exception exp){
			return 0f;
		}
	}


	public static long toLong(String str, long def) {
		long ret = def;
		if(str != null) {
			try{
				if(str.toUpperCase().startsWith("0X")) {
					str = str.substring(2);
					ret = Long.parseLong(str, 10);
				} else {
					int index = str.indexOf(".");
					if(index != -1) {
						str = str.substring(0, index);
					}
					ret = Long.parseLong(str);
				}
			}catch(Exception ex){
				ret = def;
			}
		}
		return ret;
	}

	public static boolean toBoolean(String str, boolean def) {
		boolean ret = def;
		if(str != null) {
			try{
				ret = Boolean.parseBoolean(str);
			}catch(Exception ex) {
				ret = def;
			}
		}
		return ret;
	}

	/**
	 * 字符串是否全为数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 字符串是否全为字母
	 * @param str
	 * @return
	 */
	public static boolean isLetter(String str){
		Pattern pattern = Pattern.compile("[A-Za-z]*");
		return pattern.matcher(str).matches();
	}

	public static boolean isBalance(String balance) {
		Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?$");
		Matcher isNum = pattern.matcher(balance);
		return isNum.matches();
	}

	/**
	 * 判断字符是否为中文
	 *
	 * @param c
	 * @return
     */
	public static boolean isChinese(char c){
		if((c >= 0x4e00) && (c <= 0x9fbb)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 计算一个字符串中中文字符的个数
	 *
	 * @param str
	 * @return	中文字符个数
     */
	public static int getChineseCharsCount(String str){
		int count = 0;

		if(!TextUtils.isEmpty(str)){
			for (char c: str.toCharArray()){
				if(isChinese(c)){
					count ++;
				}
			}
		}
		return count;
	}

	/**
	 * 获取字符串所占字符个数
	 *
	 * @param str	字符串
	 * @return		字符个数，字符中的中文按2个字符来计算，其他字符按1个字符来计算
     */
	public static int getStringCharLength(String str){

		if(TextUtils.isEmpty(str)){
			return 0 ;
		}

		int charsCount = 0;
		int len = str.length();
		for (int i =0; i <len;i++){
			char c = str.charAt(i);

			if(isChinese(c)){
				charsCount += 2;	//中文字符占两个字符
			}else{
				charsCount += 1;
			}
		}

		return charsCount;

	}

	/**
	 * 检测字符串是否匹配Luhn算法：可用于检测银行卡卡号
	 * @param cardNo
	 * @return
	 */
	public static boolean matchLuhn(String cardNo) {
		int[] cardNoArr = new int[cardNo.length()];
		for (int i=0; i<cardNo.length(); i++) {
			cardNoArr[i] = Integer.valueOf(String.valueOf(cardNo.charAt(i)));
		}
		for(int i=cardNoArr.length-2;i>=0;i-=2) {
			cardNoArr[i] <<= 1;
			cardNoArr[i] = cardNoArr[i]/10 + cardNoArr[i]%10;
		}
		int sum = 0;
		for(int i=0;i<cardNoArr.length;i++) {
			sum += cardNoArr[i];
		}
		return sum % 10 == 0;
	}

}
