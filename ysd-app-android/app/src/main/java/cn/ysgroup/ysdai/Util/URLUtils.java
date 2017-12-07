package cn.ysgroup.ysdai.Util;

import android.webkit.URLUtil;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by pingia on 2017/2/27.
 */

public final class URLUtils {
    private static final String HTTP_SCHEME = "http";
    private static final String HTTPS_SCHEME = "https";
    private static final String FILE_SCHEME = "file";
    private static final String ASSETS_FILE_PREFIX = FILE_SCHEME + "://" + "/android_asset";

    private static final String UTF8_ENCODING= "utf-8";

    public static String makeURL(String p_url, Map<String, String> params) {
        return makeURL(p_url,getUrlParamString(params,UTF8_ENCODING));
    }

    public static String makeURL(String p_url, Map<String, String> params, String encoding) {
       return makeURL(p_url, getUrlParamString(params,encoding));
    }

    public static String makeURL(String p_url, String urlParamsStr) {

        StringBuffer url = new StringBuffer(p_url);

        if(url.indexOf("?")<0) {
            url.append('?');
        }

        url.append(urlParamsStr);


        return url.toString().replace("?&", "?").replace("&&", "&");

    }

    public static final String getUrlParamString(Map<String, String> params){
        return getUrlParamString(params, UTF8_ENCODING);
    }

    /**
     * 通过key-value的map，根据相应编码，拼接对应的url参数
     * @param params
     * @param encoding
     * @return
     */
    public static final String getUrlParamString(Map<String, String> params, String encoding){

        StringBuffer sb = new StringBuffer();
        for(String name : params.keySet()){

            sb.append('&');

            sb.append(name);

            sb.append('=');

            String value = params.get(name);

            try {
                sb.append(URLEncoder.encode(value, encoding));
            }catch (UnsupportedEncodingException e){
                sb.append(value);
            }

        }

        return sb.toString();
    }


    public static String getAbsoluteUrl(boolean isHttps, String host, int port, String path){
        if (URLUtil.isNetworkUrl(path)) {
            return path;
        } else {
            String protocol;
            if(isHttps){
                protocol = HTTPS_SCHEME;
            }else{
                protocol = HTTP_SCHEME;
            }

            try {
                URL url = new URL(protocol, host, port, path);

                return url.toString();
            }catch (MalformedURLException e){
                throw new WrongURLSyntaxException(e);
            }
        }
    }


    public static String getAbsoluteAssetsURL(String path){
        return ASSETS_FILE_PREFIX  + path;
    }

    private static class WrongURLSyntaxException extends RuntimeException {
        public WrongURLSyntaxException(MalformedURLException exp){
            super("url syntax error: " + exp.getMessage());
        }
    }
}
