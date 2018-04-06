package wepet.projectbase.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import wepet.projectbase.GetContext;
import wepet.projectbase.R;
import wepet.projectbase.entity.OtherProfileEntityObject;
import wepet.projectbase.fragment.IndividualProfileFragment;

public class IndividualProfile extends FontActivity {


    private ImageButton bookBtn;
    private ImageButton recommentBtn;
    private ImageButton writeBtn;
    private ImageButton backBtn;


    private TextView otherProfileName;
    private CircleImageView otherProfileImage;

    public static int dCheckType = 0;
    Intent intent;
    static String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_profile);

        intent = getIntent();

        if (intent.hasExtra("writer")) {   //글쓴이 눌러서 넘어 왔을 때때, 제대로 넘어왔을시 1
            uid = intent.getExtras().getString("uid");
        } else if(intent.hasExtra("recommenter")){
            uid = intent.getExtras().getString("uid");
        }

        otherProfileName = (TextView) findViewById(R.id.individual_profile_title);
        otherProfileImage = (CircleImageView) findViewById(R.id.individual_profile_image);


        Toolbar toolbar = (Toolbar) findViewById(R.id.individual_profile_toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);

        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final IndividualProfileFragment fragment = new IndividualProfileFragment();
        fragmentTransaction.add(R.id.individ_container, fragment);
        fragmentTransaction.commit();

        backBtn = (ImageButton) findViewById(R.id.individual_profile_back);
        bookBtn = (ImageButton) findViewById(R.id.individual_book_btn);
        recommentBtn = (ImageButton) findViewById(R.id.individual_recomment_btn);
        writeBtn = (ImageButton) findViewById(R.id.individual_write_btn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dCheckType = 0;
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                IndividualProfileFragment fragment = new IndividualProfileFragment();
                fragmentTransaction.replace(R.id.individ_container, fragment);
                fragmentTransaction.commit();
                bookBtn.setImageResource(R.drawable.individual_bookmark_select);
                recommentBtn.setImageResource(R.drawable.individual_recomment_non);
                writeBtn.setImageResource(R.drawable.individual_write_non);


            }
        });

        recommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dCheckType = 1;
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                IndividualProfileFragment fragment = new IndividualProfileFragment();
                fragmentTransaction.replace(R.id.individ_container, fragment);
                fragmentTransaction.commit();
                bookBtn.setImageResource(R.drawable.individual_bookmark_non);
                recommentBtn.setImageResource(R.drawable.individual_recomment_select);
                writeBtn.setImageResource(R.drawable.individual_write_non);


            }
        });

        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dCheckType = 2;
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                IndividualProfileFragment fragment = new IndividualProfileFragment();
                fragmentTransaction.replace(R.id.individ_container, fragment);
                fragmentTransaction.commit();
                bookBtn.setImageResource(R.drawable.individual_bookmark_non);
                recommentBtn.setImageResource(R.drawable.individual_recomment_non);
                writeBtn.setImageResource(R.drawable.individual_write_select);

            }
        });
        new AsyncOtherProfile().execute();
    }

    public static String getUid() {
        return uid;
    }


    public class AsyncOtherProfile extends AsyncTask<OtherProfileEntityObject, Integer, OtherProfileEntityObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected OtherProfileEntityObject doInBackground(OtherProfileEntityObject... otherProfileEntityObjects) {
            JSONObject responsedJson = null;
            JSONObject responsedJsonData = null;
            OtherProfileEntityObject object = null;

            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");



            try {
                OkHttpClient toSever = new OkHttpClient.Builder()
                        .connectTimeout(7, TimeUnit.SECONDS)
                        .connectTimeout(7, TimeUnit.SECONDS)
                        .build();


                Request request = new Request.Builder() //보내는 곳 설정
                        .url("http://52.78.104.95:3000/users/" + uid)
                        .get()
                        .build();

                Response response = toSever.newCall(request).execute();
                ResponseBody responseBody = response.body();
                responsedJson = new JSONObject(response.body().string());

                responsedJsonData = responsedJson.getJSONObject("data");


                if (response.isSuccessful()) {
                    object = new OtherProfileEntityObject();
                    object.nickname = responsedJsonData.getString("nickname");
                    object.profile_thumb =responsedJsonData.getString("profile_thumb");

                    return object;

                }

            } catch (UnknownHostException une) {
                Log.e("fileUpLoad1", une.toString());
            } catch (UnsupportedEncodingException uee) {
                Log.e("fileUpLoad2", uee.toString());
            } catch (Exception e) {
                Log.e("fileUpLoad3", e.toString());
            }

            return null;
        }

            @Override
            protected void onPostExecute (OtherProfileEntityObject s){

                otherProfileName.setText(s.nickname);
                if(s.profile_thumb.equals("")!=true){
                    Glide.with(GetContext.getContext()).load(s.profile_thumb).into(otherProfileImage); //픽셀값으로 변경
                    Log.e("프로필바뀜?","5"+s.profile_thumb+"5");
                }

                super.onPostExecute(s);
            }
        } // 글쓴이 프로필사진 클릭시 글쓴이 프로필정보
    }
