package wepet.projectbase.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import wepet.projectbase.CustomLoadingDialog;
import wepet.projectbase.R;
import wepet.projectbase.entity.MatchDataObject;
import wepet.projectbase.entity.MatchEntityObject;
import wepet.projectbase.entity.UserMatchEntity;

public class Match extends FontActivity {
    CustomLoadingDialog customProgressDialog;

    int houseSelect, dogSize, dogFur, dogNature, dogFamily, dogActivity;
    private ImageButton result_btn;
    private ImageView house1, house2;
    private ImageView dog1, dog2, dog3;
    private ImageView fur1, fur2;
    private ImageView character1, character2, character3, character4;
    private ImageView family1, family2, old_family, baby_family;
    private ImageView activityYes, activityNo;
    ArrayList<UserMatchEntity> resultData;
    static String responseResultValue;

    MatchEntityObject entity;

    ArrayList<MatchDataObject> data = new ArrayList<MatchDataObject>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_select);

        result_btn = (ImageButton) findViewById(R.id.match_result_btn);
        house1 = (ImageView) findViewById(R.id.house1);
        house2 = (ImageView) findViewById(R.id.house2);
        dog1 = (ImageView) findViewById(R.id.dog1);
        dog2 = (ImageView) findViewById(R.id.dog2);
        dog3 = (ImageView) findViewById(R.id.dog3);
        fur1 = (ImageView) findViewById(R.id.fur1);
        fur2 = (ImageView) findViewById(R.id.fur2);
        character1 = (ImageView) findViewById(R.id.character1);
        character2 = (ImageView) findViewById(R.id.character2);
        character3 = (ImageView) findViewById(R.id.character3);
        character4 = (ImageView) findViewById(R.id.character4);
        family1 = (ImageView) findViewById(R.id.family1);
        old_family = (ImageView) findViewById(R.id.oldfamily);
        family2 = (ImageView) findViewById(R.id.family2);
        baby_family = (ImageView) findViewById(R.id.babyfamily);
        activityYes = (ImageView) findViewById(R.id.activityYes);
        activityNo = (ImageView) findViewById(R.id.activityNo);

        resultData = new ArrayList<UserMatchEntity>();

        result_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                result_btn.setEnabled(false);
                entity = new MatchEntityObject();
                entity.home = houseSelect;
                entity.exercise = dogActivity;
                entity.family = dogFamily;
                entity.hair = dogFur;
                entity.nature = dogNature;
                entity.size = dogSize;
                Log.e("체크","거주형태 : " +  entity.home + "%");
                Log.e("체크","강아지크기 : " +  entity.size + "%");
                if(validityCheck(entity)){
                    new AsyncMatchInsert().execute(entity);
                }

            }
        });

        house1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                house1.setImageResource(R.drawable.appartment2);
                house2.setImageResource(R.drawable.house1);
                houseSelect = 1;
            }
        });

        house2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                house1.setImageResource(R.drawable.appartment1);
                house2.setImageResource(R.drawable.house2);
                houseSelect = 2;
            }
        });

        dog1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dog1.setImageResource(R.drawable.small_dog2);
                dog2.setImageResource(R.drawable.medium_dog1);
                dog3.setImageResource(R.drawable.big_dog1);
                dogSize = 1;
            }
        });

        dog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dog2.setImageResource(R.drawable.medium_dog2);
                dog1.setImageResource(R.drawable.small_dog1);
                dog3.setImageResource(R.drawable.big_dog1);
                dogSize = 2;
            }
        });

        dog3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dog2.setImageResource(R.drawable.medium_dog1);
                dog1.setImageResource(R.drawable.small_dog1);
                dog3.setImageResource(R.drawable.big_dog2);
                dogSize = 3;
            }
        });

        fur1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fur1.setImageResource(R.drawable.dog_yes2);
                fur2.setImageResource(R.drawable.dog_no1);
                dogFur = 1;
            }
        });

        fur2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fur1.setImageResource(R.drawable.dog_yes1);
                fur2.setImageResource(R.drawable.dog_no2);
                dogFur = 2;
            }
        });

        character1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character1.setImageResource(R.drawable.funny_dog2);
                character2.setImageResource(R.drawable.careful_dog1);
                character3.setImageResource(R.drawable.friendly_dog1);
                character4.setImageResource(R.drawable.quite_dog1);
                dogNature = 1;
            }
        });

        character2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character1.setImageResource(R.drawable.funny_dog1);
                character2.setImageResource(R.drawable.careful_dog2);
                character3.setImageResource(R.drawable.friendly_dog1);
                character4.setImageResource(R.drawable.quite_dog1);
                dogNature = 2;
            }
        });

        character3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character1.setImageResource(R.drawable.funny_dog1);
                character2.setImageResource(R.drawable.careful_dog1);
                character3.setImageResource(R.drawable.friendly_dog2);
                character4.setImageResource(R.drawable.quite_dog1);
                dogNature = 3;
            }
        });

        character4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character1.setImageResource(R.drawable.funny_dog1);
                character2.setImageResource(R.drawable.careful_dog1);
                character3.setImageResource(R.drawable.friendly_dog1);
                character4.setImageResource(R.drawable.quite_dog2);
                dogNature = 4;
            }
        });

        family1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                family1.setImageResource(R.drawable.one_family2);
                old_family.setImageResource(R.drawable.old_family1);
                family2.setImageResource(R.drawable.two_family1);
                baby_family.setImageResource(R.drawable.baby_family1);
                dogFamily = 1;
            }
        });

        old_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                family1.setImageResource(R.drawable.one_family1);
                old_family.setImageResource(R.drawable.old_family2);
                family2.setImageResource(R.drawable.two_family1);
                baby_family.setImageResource(R.drawable.baby_family1);
                dogFamily = 2;
            }
        });

        family2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                family1.setImageResource(R.drawable.one_family1);
                old_family.setImageResource(R.drawable.old_family1);
                family2.setImageResource(R.drawable.two_family2);
                baby_family.setImageResource(R.drawable.baby_family1);
                dogFamily = 3;
            }
        });

        baby_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                family1.setImageResource(R.drawable.one_family1);
                old_family.setImageResource(R.drawable.old_family1);
                family2.setImageResource(R.drawable.two_family1);
                baby_family.setImageResource(R.drawable.baby_family2);
                dogFamily = 4;
            }
        });

        activityYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityYes.setImageResource(R.drawable.activity_yes2);
                activityNo.setImageResource(R.drawable.activity_no1);
                dogActivity = 1;
            }
        });

        activityNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityYes.setImageResource(R.drawable.activity_yes1);
                activityNo.setImageResource(R.drawable.activity_no2);
                dogActivity = 2;
            }
        });
    }

    public class AsyncMatchInsert extends AsyncTask<MatchEntityObject, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(MatchEntityObject... matchEntityObjects) {

            MatchEntityObject matchEntity = matchEntityObjects[0];

            String uid = PropertyManager.getInstance().getUid();
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("uid", uid);
                jsonObject.put("home", matchEntity.home);//받을때는필요없음
                jsonObject.put("size", matchEntity.size);
                jsonObject.put("hair", matchEntity.hair);
                jsonObject.put("nature", matchEntity.nature);
                jsonObject.put("family", matchEntity.family);
                jsonObject.put("exercise", matchEntity.exercise);
            } catch (Exception e) {
                Log.e("lee", e.toString());
            }

            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            RequestBody body = RequestBody.create(JSON, jsonObject.toString());//받을때는필요없음

            Request request = new Request.Builder() //보내는 곳 설정
                    .url("http://52.78.104.95:3000/users/me/curation")
                    .post(body)//받을때는필요없음
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            try {
                Response response = okHttpClient.newCall(request).execute();
                JSONObject responsedJson = null;
                JSONArray responsedArray = null;
                responsedJson = new JSONObject(response.body().string());
                responsedArray = responsedJson.getJSONArray("data");

                for (int i = 0; i < responsedArray.length(); i++) {
                    MatchDataObject object = new MatchDataObject();

                    object.breed = responsedArray.getJSONObject(i).getInt("breed");
                    object.active = responsedArray.getJSONObject(i).getInt("active");
                    object.score = responsedArray.getJSONObject(i).getInt("score");
                    object.sociable = responsedArray.getJSONObject(i).getInt("sociable");
                    object.molt = responsedArray.getJSONObject(i).getInt("molt");
                    object.size = responsedArray.getJSONObject(i).getInt("size");

                    data.add(object);
                }

            } catch (Exception e) {

            }
            return responseResultValue;
        }

        @Override
        protected void onPostExecute(String responseResultValue) {

            customProgressDialog = new CustomLoadingDialog(Match.this);
            customProgressDialog.getWindow().setBackgroundDrawable
                    (new ColorDrawable(android.graphics.Color.TRANSPARENT));
            customProgressDialog.show();
            result_btn.setEnabled(true);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Match.this, MatchResult.class);
                    intent.putExtra("data", data);
                    startActivity(intent);
                    finish();
                    customProgressDialog.dismiss();
                }
            }, 2000);

            //startActivity(new Intent(Match.this, MatchResult.class));
            super.onPostExecute(responseResultValue);
        }
    }

    private boolean validityCheck(MatchEntityObject entity) {
        if (entity.home == 0) {
            Toast.makeText(getApplicationContext(), "거주 형태를 선택해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (entity.size == 0) {
            Toast.makeText(getApplicationContext(), "크기를 선택해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (entity.hair == 0) {
            Toast.makeText(getApplicationContext(), "털 빠짐 여부를 선택해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (entity.nature == 0) {
            Toast.makeText(getApplicationContext(), "강아지 성격을 선택해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (entity.family == 0) {
            Toast.makeText(getApplicationContext(), "가족구성원 형태를 선택해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (entity.exercise == 0) {
            Toast.makeText(getApplicationContext(), "활동 여부를 선택해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
