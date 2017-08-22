package chatting.faisal.pkp.chatting.Function;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Faisal on 12/12/2016.
 */

public class SessionManagerUtil {
    private Context ctx;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SessionManagerUtil(Context context) {
        this.ctx = context;
    }

    public void saveConfig(
            String logoutTime,
            String maxPhoto,
            String overdueTask,
            String biayaDenda,
            String biayaAngsuran,
            String biayaTagih) {
        pref = ctx.getSharedPreferences("config", 0);
        editor = pref.edit();

        editor.putString("logout_time", logoutTime);
        editor.putString("max_photo", maxPhoto);
        editor.putString("overdue_task", overdueTask);
        editor.putString("biaya_denda", biayaDenda);
        editor.putString("biaya_angsuran", biayaAngsuran);
        editor.putString("biaya_tagih", biayaTagih);
        editor.putString("is_default", "false");

        editor.commit();
    }

    public void saveMaxSqlite(
            String maxSqlite) {
        pref = ctx.getSharedPreferences("config", 0);
        editor = pref.edit();
        editor.putString("max_sqlite", maxSqlite);
        editor.commit();
    }

    public void saveFingerPrint(
            String useFinger) {
        pref = ctx.getSharedPreferences("config", 0);
        editor = pref.edit();
        editor.putString("useFinger", useFinger);
        editor.commit();
    }


    public Map<String, String> getConfig() {
        pref = ctx.getSharedPreferences("config", 0);
        editor = pref.edit();
        Map<String, String> tes = new HashMap<>();
        tes.put("logout_time", pref.getString("logout_time", "99999"));
        tes.put("max_photo", pref.getString("max_photo", "5"));
        tes.put("overdue_task", pref.getString("overdue_task", "2"));
        tes.put("biaya_denda", pref.getString("biaya_denda", "3"));
        tes.put("biaya_angsuran", pref.getString("biaya_angsuran", "2"));
        tes.put("biaya_tagih", pref.getString("biaya_tagih", "1"));
        tes.put("max_sqlite", pref.getString("max_sqlite", "3"));
        tes.put("useFinger", pref.getString("useFinger", "false"));
        return tes;
    }

    public String defaultConfig() {
        pref = ctx.getSharedPreferences("config", 0);
        editor = pref.edit();

        return pref.getString("is_default", "true");
    }


    public void saveFcmToken(String val) {
        pref = ctx.getSharedPreferences("FcmToken", 0);
        editor = pref.edit();

        editor.putString("Token", val);
        editor.commit();
    }

    public String getFcmToken() {
        pref = ctx.getSharedPreferences("FcmToken", 0);
        editor = pref.edit();

        return pref.getString("Token", null);
    }

    public void sessionUserSave(String name, String val) {
        pref = ctx.getSharedPreferences("user", 0);
        editor = pref.edit();

        editor.putString(name, val);
        editor.commit();
    }

    public void sessionClear() {
        pref = ctx.getSharedPreferences("user", 0);
        editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    public void logout() {
        pref = ctx.getSharedPreferences("user", 0);
        editor = pref.edit();
        editor.remove("isLogin");
        editor.remove("rememberLogin");
        editor.commit();
    }

    //    util.sessionUserSave("userId", userId);
//    util.sessionUserSave("nama", nama);
//    util.sessionUserSave("username", user);
//    util.sessionUserSave("password", pass);
    public String sessionUserGet(String name) {
        pref = ctx.getSharedPreferences("user", 0);
        editor = pref.edit();

        return pref.getString(name, null);
    }

    public void saveBluetoothMAC(String val) {
        pref = ctx.getSharedPreferences("user", 0);
        editor = pref.edit();
        editor.putString("PrinterMAC", val);
        editor.commit();
    }


    public String getBluetoothMAC() {
        pref = ctx.getSharedPreferences("user", 0);
        return pref.getString("PrinterMAC", null);
    }
}
