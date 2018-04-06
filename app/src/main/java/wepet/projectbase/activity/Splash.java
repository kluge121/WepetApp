package wepet.projectbase.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import wepet.projectbase.R;

/**
 * Created by ccei on 2016-08-04.
 */
public class Splash extends Activity{

    Handler hd;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        PropertyManager propertyManager = PropertyManager.getInstance();
        final String uid = propertyManager.getUid();
        final String uuid = propertyManager.getUuid();

        hd = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (!TextUtils.isEmpty(uuid)&&uuid.length()>1){
                    new LoginAsnc().execute(uuid, uid);
                }else {
                    Intent intent = new Intent(Splash.this, JoinActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        //loading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hd.sendEmptyMessageDelayed(0,2000);
    }
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;
    private void checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to write the permission.

                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_REQUEST_STORAGE);

            } else {
                //사용자가 언제나 허락
            }
        }

    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    //사용자가 퍼미션을 OK했을 경우

                } else {


                    //사용자가 퍼미션을 거절했을 경우
                }
                break;
        }
    }

    public class LoginAsnc extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String... strings) {
            Response loginresponse = null;
            boolean flagConn = false;
            boolean flagJson = false;
            String uuid = strings[0];
            String uid = strings[1];

            try {
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();

                RequestBody loginBody = new FormBody.Builder()
                        .add("uuid", uuid)
                        .add("uid", uid) //기본 쿼리
                        .build();

                Request request = new Request.Builder()
                        .url("http://52.78.104.95:3000/auth/login")
                        .post(loginBody) //반드시 post로
                        .build();

                loginresponse = toServer.newCall(request).execute();

                flagConn = loginresponse.isSuccessful();

                if (flagConn) {
                    JSONObject jsonObject = new JSONObject(loginresponse.body().string());
                    String resultMessage = jsonObject.getString("msg");

                    if (resultMessage.equalsIgnoreCase("success")) {
                        flagJson = true;
                    } else {
                        flagJson = false;
                    }
                }
                return flagJson;

            } catch (Exception e) {
                //e("LoginAsynck", e.toString(),e);
            } finally {
                if (loginresponse != null) {
                    loginresponse.close();
                }
            }
            return flagJson;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean){
                Intent i = new Intent(Splash.this, Main.class);
                startActivity(i);
                finish();
            }
        }
    }
//    private void loading()
//    {
//        Handler mHandler = new Handler();
//        mHandler.postDelayed(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                intent = new Intent(getApplicationContext(), Main.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//
//            }
//        }, 2000);
//    }
}







