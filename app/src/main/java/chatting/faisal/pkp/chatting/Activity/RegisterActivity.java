package chatting.faisal.pkp.chatting.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import chatting.faisal.pkp.chatting.Function.HttpRequest;
import chatting.faisal.pkp.chatting.Function.SessionManagerUtil;
import chatting.faisal.pkp.chatting.R;


public class RegisterActivity extends AppCompatActivity {
    private EditText name;
    private EditText email;
    private EditText password;
    private String fcmToken;
    private ProgressDialog pDialog;
    private SessionManagerUtil util;
    private HttpRequest http;
    private Context context;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = getApplicationContext();
        http = new HttpRequest(context);
        util = new SessionManagerUtil(this);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.login_btn);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        getToken();
        fcmToken = util.getFcmToken();

        SessionManagerUtil util = new SessionManagerUtil(context);
        String userSave = util.sessionUserGet("email");
        String passSave = util.sessionUserGet("password");

        email.setText(userSave);
        password.setText(passSave);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void registerBtn(View view) {
        register();
    }


    private void register() {
        pDialog.setMessage("Login");
        showProgressDialog();
//        if (!connectionManager.isConnect()) {
//            offlineMode();
//            return;
//        }
//        fcmToken = util.getFcmToken();
//        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(context.TELEPHONY_SERVICE);
//        String imei = telephonyManager.getDeviceId();
        http.register(
                name.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                fcmToken,
                new HttpRequest.SuccessCallback() {
                    @Override
                    public void onHttpPostSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("1")) {
                                try {
                                    saveToSession(new JSONObject(jsonObject.getString("data")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                JSONObject error = new JSONObject(jsonObject.getString("data"));
                                String name = null, email = null, password = null;

                                try {
                                    name = new JSONArray(error.getString("name")).get(0).toString();
                                } catch (Exception e) {
                                    name = "name ok";

                                }
                                try {
                                    email = new JSONArray(error.getString("email")).get(0).toString();
                                } catch (Exception e) {
                                    email = "email ok";
                                }
                                try {
                                    password = new JSONArray(error.getString("password")).get(0).toString();
                                } catch (Exception e) {
                                    password = "password ok";

                                }
                                Toast.makeText(RegisterActivity.this, name+ " "+email + " " + password, Toast.LENGTH_LONG).show();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        hideProgressDialog();
                    }
                },
                new HttpRequest.ErrorCallback() {
                    @Override
                    public void onHttpPostError(VolleyError error) {
                        hideProgressDialog();
                    }
                });
    }

    private void showProgressDialog() {
        pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    private void saveToSession(JSONObject jsonObject) throws JSONException {
        goToHome();
        String name = jsonObject.getString("name");
        String collId = jsonObject.getString("coll_id");
        String email = this.email.getText().toString();
        String pass = password.getText().toString();
        String token = jsonObject.getString("token");

        util.sessionUserSave("userId", collId);
        util.sessionUserSave("nama", name);
        util.sessionUserSave("email", email);
        util.sessionUserSave("password", pass);
        util.sessionUserSave("token", token);
        util.sessionUserSave("isLogin", "true");

    }

    private void goToHome() {
        //finish();
        startActivity(new Intent(this, ChatListActivity.class));
    }

    private void getToken() {
        String token = FirebaseInstanceId.getInstance().getToken();
        if (token == null) return;
        if (!token.equals(null)) {
            util.saveFcmToken(token);
        }
    }


}

