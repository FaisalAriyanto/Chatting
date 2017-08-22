package chatting.faisal.pkp.chatting.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import chatting.faisal.pkp.chatting.Function.ConnectionManager;
import chatting.faisal.pkp.chatting.Function.HttpRequest;
import chatting.faisal.pkp.chatting.Function.SessionManagerUtil;
import chatting.faisal.pkp.chatting.R;


public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private String fcmToken;
    private ProgressDialog pDialog;
    private CheckBox rememberMe;
    private SessionManagerUtil util;
    private HttpRequest http;
    private ConnectionManager connectionManager;
    private Context context;
    private Button loginBtn;

    private static final int CAMERA_REQUEST_CODE = 0;
    private static final int STORRAGE_REQUEST_CODE = 1;
    private static final int LOCATION_REQUEST_CODE = 2;
    private static final int PHONE_REQUEST_CODE = 3;
    private static final int CALL_REQUEST_CODE = 4;

    private boolean IS_CAMERA_GRANTED = false;
    private boolean IS_STORAGE_GRANTED = false;
    private boolean IS_LOCATION_GRANTED = false;
    private boolean IS_PHONE_GRANTED = false;
    private boolean IS_CALL_GRANTED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();
        requestAllPermission();
        http = new HttpRequest(context);
        util = new SessionManagerUtil(this);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        rememberMe = (CheckBox) findViewById(R.id.cb_remember_me);
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

    private void requestAllPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    alertRationale(context.getString(R.string.rationale_storage), 0);
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORRAGE_REQUEST_CODE);
                }
            } else {
                IS_STORAGE_GRANTED = true;
            }

            if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    alertRationale(context.getString(R.string.rationale_camera), 1);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);

                }
            } else {
                IS_CAMERA_GRANTED = true;
            }
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    alertRationale(context.getString(R.string.rationale_location), 2);
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                }
            } else {
                IS_LOCATION_GRANTED = true;
            }

            if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                    alertRationale(context.getString(R.string.rationale_phone), 3);
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PHONE_REQUEST_CODE);
                }
            } else {
                IS_PHONE_GRANTED = true;
            }

            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                    alertRationale(context.getString(R.string.rationale_call), 4);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_REQUEST_CODE);
                }
            } else {
                IS_CALL_GRANTED = true;
            }


        }
    }


    private void alertRationale(String msg, final int code) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        // set title
        alertDialogBuilder.setTitle("Ijin");

        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Setuju", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            switch (code) {
                                case 0:
                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORRAGE_REQUEST_CODE);
                                    break;
                                case 1:
                                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                                    break;
                                case 2:
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                                    break;
                                case 3:
                                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PHONE_REQUEST_CODE);
                                    break;
                                case 4:
                                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST_CODE);
                                    break;
                            }
                        }
                    }
                })
                .setNegativeButton("Tidak setuju", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case STORRAGE_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("", "");
                    IS_STORAGE_GRANTED = true;
                } else {
                    Log.d("", "");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case CAMERA_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("", "");
                    IS_CAMERA_GRANTED = true;
                } else {
                    Log.d("", "");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("", "");
                    IS_LOCATION_GRANTED = true;
                } else {
                    Log.d("", "");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case PHONE_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("", "");
                    IS_PHONE_GRANTED = true;
                } else {
                    Log.d("", "");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case CALL_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("", "");
                    IS_CALL_GRANTED = true;
                } else {
                    Log.d("", "");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void loginBtn(View view) {
        login();
    }


    private void login() {
        pDialog.setMessage("Login");
        showProgressDialog();
        http.login(
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
                                    saveToSession(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            else {
                                Toast.makeText(LoginActivity.this, status, Toast.LENGTH_LONG).show();
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
        String id = jsonObject.getString("id");
        String email = this.email.getText().toString();
        String pass = password.getText().toString();
        String token = jsonObject.getString("token");

        util.sessionUserSave("userId", id);
        util.sessionUserSave("nama", name);
        util.sessionUserSave("email", email);
        util.sessionUserSave("password", pass);
        util.sessionUserSave("token", token);
        util.sessionUserSave("isLogin", "true");

        if (rememberMe.isChecked()) {
            util.sessionUserSave("rememberLogin", "true");
        } else {
            util.sessionUserSave("rememberLogin", "false");
        }
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
    public void goToRegister(View view){
        startActivity(new Intent(this, RegisterActivity.class));
    }

}

