package chatting.faisal.pkp.chatting.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import chatting.faisal.pkp.chatting.Activity.LoginActivity;
import chatting.faisal.pkp.chatting.Function.LocalBroadcastConstant;
import chatting.faisal.pkp.chatting.R;

/**
 * Created by Faisal on 2/14/2017.
 */

public class ChatMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Calling method to generate notification
        //sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
        Map<String, String> data = remoteMessage.getData();

        //you can get your text message here.
        String type = null;
        try {

//            $o->type = 0;
//            $o->message= $message;
//            $o->sender = $sender;

            JSONObject payload = new JSONObject(new JSONObject(data).getString("payload"));
            type = payload.getString("type");
            String title = "";
            String body = "";
            switch (type) {
                //new chat
                case "0":
                    JSONObject message = payload.getJSONObject("message");
                    JSONObject sender = payload.getJSONObject("sender");
                    title = sender.getString("name");
                    body = message.getString("text");

                    Intent intent = new Intent(LocalBroadcastConstant.NEW_CHAT);
                    intent.putExtra("sender", sender.toString());
                    intent.putExtra("message", message.toString());
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    break;
            }

          //  sendNotification(body, title);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody, String title) {
        Intent openIntent = new Intent(this, LoginActivity.class);
        openIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingOpenIntent = PendingIntent.getActivity(this, 0, openIntent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(false)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingOpenIntent)
                .setPriority(2)
                .setVibrate(new long[]{100, 100, 100, 100})
                .setLights(Color.GRAY, 3000, 3000)
                .setVisibility(Notification.VISIBILITY_PUBLIC);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
