package wepet.projectbase.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import wepet.projectbase.R;
import wepet.projectbase.entity.PostWriteMainListEntity;
import wepet.projectbase.entity.UpLoadValueObject;
import wepet.projectbase.fragment.WriteFragment;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-07-28.
 */
public class
WriteTabActivity extends FontActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ImageView backBtn;
    private TextView regist;
    static Integer checkTab;
    int currentTab;
    static String responseResultValue;
    PostWriteMainListEntity entity;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        regist.setEnabled(true);
        WriteFragment.neutralizeCheck = 0;
        WriteFragment.genderCheck = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissionC();
        checkPermissionT();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_tab_layout);

        tabLayout = (TabLayout) findViewById(R.id.tabs1);
        tabLayout.addTab(tabLayout.newTab().setText("분양"));
        tabLayout.addTab(tabLayout.newTab().setText("실종"));
        tabLayout.addTab(tabLayout.newTab().setText("보호"));

        Intent intent = getIntent();
        currentTab = intent.getIntExtra("tab", 0);
        if (currentTab == 3) {
            currentTab = 0;
        }
        tabLayout.getTabAt(currentTab).select();


        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        WriteFragment framgment = new WriteFragment();
        fragmentTransaction.add(R.id.fargment_container, framgment); // 부착하는 역할
        fragmentTransaction.commit();


        checkTab = tabLayout.getSelectedTabPosition();


        regist = (TextView) findViewById(R.id.writeregist);
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regist.setEnabled(false);
                entity = WriteFragment.writeTab();
                if (validityCheck(entity)) {
                    checkTab = tabLayout.getSelectedTabPosition() + 1;
                    if (checkTab == 1) {
                        new AsyncWriteInsert().execute(entity);
                    }
                    if (checkTab == 2) {
                        new AsyncWriteInsert().execute(entity);

                    }
                    if (checkTab == 3) {
                        new AsyncWriteInsert().execute(entity);
                    }
                    entity = null;
                    finish();
                    startActivity(new Intent(getApplicationContext(),Main.class));
                } else {
                    regist.setEnabled(true);
                }

            }
        });
        backBtn = (ImageView) findViewById(R.id.write_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WriteFragment.neutralizeCheck = 0;
                WriteFragment.genderCheck = 0;
                regist.setEnabled(true);
                finish();

            }
        });
        regist.setEnabled(true);
    }



    public class AsyncWriteInsert extends AsyncTask<PostWriteMainListEntity, Integer, String> {

        private final MediaType IMAGE_MIME_TYPE = MediaType.parse("image/*");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(PostWriteMainListEntity... mainListWriteEntities) {
            PostWriteMainListEntity writeEntity = mainListWriteEntities[0];
            Response response = null;

            try {
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();

                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                builder.addFormDataPart("uid", PropertyManager.getInstance().getUid());
                builder.addFormDataPart("category", checkTab.toString());
                builder.addFormDataPart("video", "");
                builder.addFormDataPart("breed", writeEntity.breed);
                builder.addFormDataPart("gender", writeEntity.gender);
                builder.addFormDataPart("age", writeEntity.age);
                builder.addFormDataPart("region", writeEntity.region);
                builder.addFormDataPart("neuter", writeEntity.neuter);
                builder.addFormDataPart("vaccin", writeEntity.vaccin);
                builder.addFormDataPart("content", writeEntity.content);

                int fileSize = writeEntity.img.size();

                for (int i = 0; i < fileSize; i++) {
                    File file = writeEntity.img.get(i).file;
                    builder.addFormDataPart("img", file.getName(), RequestBody.create(IMAGE_MIME_TYPE, file));
                }
                RequestBody fileUploadBody = builder.build();

                Request request = new Request.Builder()
                        .url("http://52.78.104.95:3000/posts")
                        .post(fileUploadBody)
                        .build();

                response = toServer.newCall(request).execute();
                boolean flag = response.isSuccessful();
                int responseCode = response.code();
                if (flag) {
                    e("response결과", responseCode + "---" + response.message()); //읃답에 대한 메세지(OK)
                    e("response응답바디", response.body().string()); //json으로 변신
                    return "success";
                }
            } catch (UnknownHostException une) {
                e("aa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("bb", uee.toString());
            } catch (Exception e) {
                e("cc", e.toString());
            } finally {
                if (response != null) {
                    response.close();
                }
            }

            return "fail";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equalsIgnoreCase("success")) {
                Toast.makeText(WriteTabActivity.this, "글이 등록되었습니다.", Toast.LENGTH_LONG).show();
                int fileSize = WriteFragment.upLoadfiles.size();

                for (int i = 0; i < fileSize; i++) {
                    UpLoadValueObject fileValue = WriteFragment.upLoadfiles.get(i);
                    if (fileValue.tempFiles) {
                        fileValue.file.deleteOnExit(); //임시파일을 삭제한다
                    }
                }
            } else {
                Toast.makeText(WriteTabActivity.this, "글 등록에 실패했습니다. 네트워크 상태를 확인해 주세요", Toast.LENGTH_LONG).show();
            }
            regist.setEnabled(true);
        }
    }

    private boolean validityCheck(PostWriteMainListEntity entity) {

        if (entity.img.size() == 0) {
            Toast.makeText(getApplicationContext(), "최소 1장의 사진을 올려주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (entity.breed == null) {

            Toast.makeText(getApplicationContext(), "견종을 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Integer.parseInt(entity.gender) == 3) {
            Toast.makeText(getApplicationContext(), "성별을 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (entity.age.length() <= 0) {
            Toast.makeText(getApplicationContext(), "나이를 정확히 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (entity.region == null) {
            Toast.makeText(getApplicationContext(), "지역을 선택해 주세요.", Toast.LENGTH_SHORT).show();
            Log.e("유효성", entity.gender + "%");
            return false;
        } else if (Integer.parseInt(entity.neuter) == 3) {
            Toast.makeText(getApplicationContext(), "중성화 여부를 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (entity.vaccin == null) {
            Toast.makeText(getApplicationContext(), "예방접종 여부를 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (entity.content.length() <0) {
            Toast.makeText(getApplicationContext(), "자세한 내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;


    }

    private void checkPermissionT() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to write the permission.

                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        100);

            } else {
                //사용자가 언제나 허락
            }
        }

    }
    private void checkPermissionC() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
// Activity에서 실행하는경우
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {

                // 이 권한을 필요한 이유를 설명해야하는가?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)) {

                    // 다이어로그같은것을 띄워서 사용자에게 해당 권한이 필요한 이유에 대해 설명합니다
                    // 해당 설명이 끝난뒤 requestPermissions()함수를 호출하여 권한허가를 요청해야 합니다

                } else {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            200);

                    // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다

                }
            }

        }

    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    //사용자가 퍼미션을 OK했을 경우

                } else {



                    //사용자가 퍼미션을 거절했을 경우
                }
                break;
            case 200:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 허가
                    // 해당 권한을 사용해서 작업을 진행할 수 있습니다
                } else {
                    // 권한 거부
                    // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
                }
                return;
        }
    }


}
