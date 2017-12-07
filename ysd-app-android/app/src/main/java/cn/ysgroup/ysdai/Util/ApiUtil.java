package cn.ysgroup.ysdai.Util;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.Map;

/**
 * Created by zenglulin on 2017/9/26.
 */

public class ApiUtil {
    private ApiUtil(){

    }

    public static ApiUtil getInstance(){
        return SingletonHolder.instance;
    }

    static class SingletonHolder{
        private static final ApiUtil instance = new ApiUtil();
    }

    public void sendPostRequest(String url, Map<String,String> params, Callback callback){
        OkHttpClient client = new OkHttpClient();
        FormEncodingBuilder  builder = new FormEncodingBuilder();

        if(null != params) {
            for (String paramName : params.keySet()) {
                builder.add(paramName, params.get(paramName));
            }
        }


        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public void sendGetRequest(String url, Map<String,String> params, Callback callback){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(URLUtils.makeURL(url,params)).build();

        Call call = client.newCall(request);
        call.enqueue(callback);

    }
}
