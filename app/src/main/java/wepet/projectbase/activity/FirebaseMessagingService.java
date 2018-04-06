package wepet.projectbase.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import java.net.URLDecoder;
import java.util.Map;

import wepet.projectbase.R;

/**
 * Created by ccei 34 on 2016-08-20.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";
    String uid, pid, breed;
    int select, recommend;
    String noticemessage = "", message1, message2, message3, message4;
    PendingIntent pendingIntent;

    String nickname, dialogmessage, date, profile_img;

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        Log.e(TAG, remoteMessage.getData() + "");
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        //실제 푸쉬로 넘어온 데이터
        Map<String, String> receiveData = remoteMessage.getData();


        select = Integer.parseInt(receiveData.get("select"));
        if (select == 1){
            uid = receiveData.get("uid");
            pid = receiveData.get("pid");
            breed = receiveData.get("breed");
            recommend = Integer.parseInt(receiveData.get("recommend"));
        }
        if (select == 2){
            profile_img = receiveData.get("profile_img");
            dialogmessage = receiveData.get("dialogmessage");
            nickname = receiveData.get("nickname");
            date = receiveData.get("date");

            Log.e(TAG, nickname);
            Log.e(TAG, date);
            Log.e(TAG, dialogmessage);
            Log.e(TAG, profile_img);
            noticemessage = "성공";

        }

        if (select == 1 && PropertyManager.getInstance().getSetting1().equals("T") && recommend == 1) {
            int breedNum = Integer.parseInt(receiveData.get("breed"));
            String breed = ChangeCodeToName.puppyName(breedNum);
            noticemessage = "1등 " + breed + "에 관한 분양글이 올라왔습니다. 확인하세요!";
            message1 = noticemessage;
            try {
                sendPushNotification(URLDecoder.decode(noticemessage, "UTF-8"));
            } catch (Exception e) {
            }
            Log.e(TAG + "msg :", "recommend==1");
        } else if (select == 1 && PropertyManager.getInstance().getSetting2().equals("T") && recommend == 2) {
            int breedNum = Integer.parseInt(receiveData.get("breed"));
            String breed = ChangeCodeToName.puppyName(breedNum);
            noticemessage = "2등 " + breed + "에 관한 분양글이 올라왔습니다. 확인하세요!";
            try {
                sendPushNotification(URLDecoder.decode(noticemessage, "UTF-8"));
            } catch (Exception e) {
            }
            message2 = noticemessage;
            Log.e(TAG, "recommend==2");
        } else if (select == 1 && PropertyManager.getInstance().getSetting3().equals("T") && recommend == 3) {
            int breedNum = Integer.parseInt(receiveData.get("breed"));
            String breed = ChangeCodeToName.puppyName(breedNum);
            noticemessage = "3등 " + breed + "에 관한 분양글이 올라왔습니다. 확인하세요!";
            try {
                sendPushNotification(URLDecoder.decode(noticemessage, "UTF-8"));
            } catch (Exception e) {
            }
            Log.e(TAG, "recommend==3");
            message3 = noticemessage;
        } else if (select == 1 && PropertyManager.getInstance().getSetting4().equals("T") && recommend == 4) {
            int breedNum = Integer.parseInt(receiveData.get("breed"));
            String breed = ChangeCodeToName.puppyName(breedNum);
            noticemessage = "4등 " + breed + "에 관한 분양글이 올라왔습니다. 확인하세요!";
            message4 = noticemessage;
            try {
                sendPushNotification(URLDecoder.decode(noticemessage, "UTF-8"));
            } catch (Exception e) {
            }
            Log.e(TAG, "recommend==4");
        }

        Log.e("message", noticemessage);
    }


    private void sendPushNotification(String message) {
        System.out.println("received message : " + message);

        if (message.equals(message1)) {
            Log.e(TAG, "pid : " + pid);
            Intent intent = new Intent(this, DetailPage.class);
            intent.putExtra("pid", Integer.parseInt(pid));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        } else if (message.equals(message2)) {
            Log.e(TAG, "pid : " + pid);
            Intent intent = new Intent(this, DetailPage.class);
            intent.putExtra("pid", Integer.parseInt(pid));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        } else if (message.equals(message3)) {
            Log.e(TAG, "pid : " + pid);
            Intent intent = new Intent(this, DetailPage.class);
            intent.putExtra("pid", Integer.parseInt(pid));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        } else if (message.equals(message4)) {
            Log.e(TAG, "pid : " + pid);
            Intent intent = new Intent(this, DetailPage.class);
            intent.putExtra("pid", Integer.parseInt(pid));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.default_1_2).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.lucher_icon_144))
                .setContentTitle("We Pet")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri).setLights(000000255, 500, 2000)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakelock.acquire(5000);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}