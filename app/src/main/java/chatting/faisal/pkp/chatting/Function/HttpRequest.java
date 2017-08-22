package chatting.faisal.pkp.chatting.Function;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import chatting.faisal.pkp.chatting.Activity.LoginActivity;
import chatting.faisal.pkp.chatting.Function.SessionManagerUtil;
import chatting.faisal.pkp.chatting.R;

/**
 * Created by Faisal on 12/12/2016.
 */

public class HttpRequest {
    private Context ctx;
    private SessionManagerUtil util;

    public HttpRequest(Context context) {
        ctx = context;
        util = new SessionManagerUtil(ctx);
    }

    private void httpHandler(
            String url,
            final Map map,
            int method,
            final SuccessCallback callback1,
            final ErrorCallback callback2) {

        final String token = "Bearer {" + util.sessionUserGet("token") + "}";


        StringRequest stringRequest = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            int status = new JSONObject(response).getInt("status");
                           // String msg = new JSONObject(response).getString("message");
                            if (status != 1) {
                              //  Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
                                Intent i = new Intent(ctx, LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                ctx.startActivity(i);
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callback1.onHttpPostSuccess(response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback2.onHttpPostError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json");
                params.put("Authorization", token);
                params.put("X-Requested-With", "XMLHttpRequest");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);
    }

    public void sendMessage(
            String id,
            String receiverId,
            String text,
            String attachmentUrl,
            final SuccessCallback callback1,
            final ErrorCallback callback2
    ) {
        String url = ctx.getString(R.string.send_message);

        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("receiver_id", receiverId);
        map.put("text", text);
        if (attachmentUrl != null)
            map.put("attachment_url", attachmentUrl);
        httpHandler(url, map, Request.Method.POST, new SuccessCallback() {
            @Override
            public void onHttpPostSuccess(String result) {
                callback1.onHttpPostSuccess(result);
            }
        }, new ErrorCallback() {
            @Override
            public void onHttpPostError(VolleyError error) {
                callback2.onHttpPostError(error);
            }
        });
    }

    public void chatHistory(
            String receiverId,
            final SuccessCallback callback1,
            final ErrorCallback callback2
    ) {
        String url = ctx.getString(R.string.message_history);

        Map<String, String> map = new HashMap<>();
        map.put("receiver_id", receiverId);
        httpHandler(url, map, Request.Method.POST, new SuccessCallback() {
            @Override
            public void onHttpPostSuccess(String result) {
                callback1.onHttpPostSuccess(result);
            }
        }, new ErrorCallback() {
            @Override
            public void onHttpPostError(VolleyError error) {
                callback2.onHttpPostError(error);
            }
        });
    }


    public void login(
            String email,
            String password,
            String fcmToken,
            final SuccessCallback callback1,
            final ErrorCallback callback2
    ) {
        String url = ctx.getString(R.string.login_url);

        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password);
        map.put("fcm_token", fcmToken);
        httpHandler(url, map, Request.Method.POST, new SuccessCallback() {
            @Override
            public void onHttpPostSuccess(String result) {
                callback1.onHttpPostSuccess(result);
            }
        }, new ErrorCallback() {
            @Override
            public void onHttpPostError(VolleyError error) {
                callback2.onHttpPostError(error);
            }
        });
    }

    public void register(
            String name,
            String email,
            String password,
            String fcmToken,
            final SuccessCallback callback1,
            final ErrorCallback callback2
    ) {
        String url = ctx.getString(R.string.register_url);

        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("email", email);
        map.put("password", password);
        map.put("fcm_token", fcmToken);
        httpHandler(url, map, Request.Method.POST, new SuccessCallback() {
            @Override
            public void onHttpPostSuccess(String result) {
                callback1.onHttpPostSuccess(result);
            }
        }, new ErrorCallback() {
            @Override
            public void onHttpPostError(VolleyError error) {
                callback2.onHttpPostError(error);
            }
        });
    }

    public void chatList(
            final SuccessCallback callback1,
            final ErrorCallback callback2
    ) {
        String url = ctx.getString(R.string.chat_list);
        Map<String, String> map = new HashMap<>();
        httpHandler(url, map, Request.Method.GET, new SuccessCallback() {
            @Override
            public void onHttpPostSuccess(String result) {
                callback1.onHttpPostSuccess(result);
            }
        }, new ErrorCallback() {
            @Override
            public void onHttpPostError(VolleyError error) {
                callback2.onHttpPostError(error);
            }
        });
    }


    public interface SuccessCallback {
        void onHttpPostSuccess(String result);
    }

    public interface ErrorCallback {
        void onHttpPostError(VolleyError error);
    }


}
