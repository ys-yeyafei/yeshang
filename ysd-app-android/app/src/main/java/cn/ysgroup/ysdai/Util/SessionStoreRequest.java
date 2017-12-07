package cn.ysgroup.ysdai.Util;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import cn.ysgroup.ysdai.Application.CustomApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/26.
 */
public class SessionStoreRequest extends JsonObjectRequest {



    public SessionStoreRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        super(method, url, jsonRequest, listener, errorListener);

    }

    public SessionStoreRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }




    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        // 添加下面这行代码
        CustomApplication.newInstance().checkSessionCookie(response.headers);

        JSONObject jb =null;
        try {
            String parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            jb = new JSONObject(parsed);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Response.success(jb, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }

        CustomApplication.newInstance().addSessionCookie(headers);

        return headers;
    }


}
