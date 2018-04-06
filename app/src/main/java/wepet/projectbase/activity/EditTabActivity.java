package wepet.projectbase.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import wepet.projectbase.entity.PostEditMainListEntity;
import wepet.projectbase.entity.UpLoadValueObject;
import wepet.projectbase.fragment.EditFragment;

import static android.util.Log.e;

/**
 * Created by ccei on 2016-07-28.
 */
public class EditTabActivity extends FontActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ImageView backBtn;
    private TextView regist;
    static Integer checkTab;
    public static PostEditMainListEntity entity;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
    static int pid;
    int category;
    int sendCategory;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_eidt_layout);

        Intent intent = getIntent();
        pid = intent.getIntExtra("pid", 0);
        category = intent.getIntExtra("category", 0);


        tabLayout = (TabLayout) findViewById(R.id.tabs1);
        if (category == 1) {
            tabLayout.addTab(tabLayout.newTab().setText("분양"));
            sendCategory = 1;
        } else if (category == 2) {
            tabLayout.addTab(tabLayout.newTab().setText("실종"));
            sendCategory = 2;
        } else if (category == 3) {
            tabLayout.addTab(tabLayout.newTab().setText("보호"));
            sendCategory = 3;
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EditFragment framgment = new EditFragment();
        framgment = EditFragment.newInstance(pid);
        fragmentTransaction.add(R.id.fargment_container, framgment); // 부착하는 역할
        fragmentTransaction.commit();


        checkTab = tabLayout.getSelectedTabPosition();


        regist = (TextView) findViewById(R.id.writeregist);
        regist = (TextView) findViewById(R.id.writeregist);
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regist.setEnabled(false);

                Log.e("entityObject.img", entity.img + "");
                Log.e("entityObject.breed", entity.breed + "");
                Log.e("entityObject.gender", entity.gender);
                Log.e("entityObject.age", entity.age);
                Log.e("entityObject.region", entity.region);
                Log.e("entityObject.neuter", entity.neuter + "");
                Log.e("entityObject.vaccin", entity.vaccin + "");
                Log.e("entityObject.content", entity.content + "");
                Log.e("entityObjectC", entity.CurrentImageList + "");


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
                } else {
                    regist.setEnabled(true);
                }

            }
        });
        backBtn = (ImageView) findViewById(R.id.write_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditFragment.neutralizeCheck = 0;
                EditFragment.genderCheck = 0;
                finish();
            }
        });
    }

    public class AsyncWriteInsert extends AsyncTask<PostEditMainListEntity, Integer, String> {

        private final MediaType IMAGE_MIME_TYPE = MediaType.parse("image/*");

        @Override
        protected String doInBackground(PostEditMainListEntity... mainListWriteEntities) {
            PostEditMainListEntity writeEntity = mainListWriteEntities[0];
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
                builder.addFormDataPart("breed", writeEntity.breed);
                builder.addFormDataPart("gender", writeEntity.gender);
                builder.addFormDataPart("age", writeEntity.age);
                builder.addFormDataPart("region", writeEntity.region);
                builder.addFormDataPart("neuter", writeEntity.neuter);
                builder.addFormDataPart("vaccin", writeEntity.vaccin);
                builder.addFormDataPart("content", writeEntity.content);

//                Log.e("수정uid",PropertyManager.getInstance().getUid());
//                Log.e("수정category",checkTab.toString());
//                Log.e("수정breed",writeEntity.breed);
//                Log.e("수정gender",writeEntity.gender);
//                Log.e("age", writeEntity.age);
//                Log.e("region", writeEntity.region);
//                Log.e("neuter", writeEntity.neuter);
//                Log.e("vaccin", writeEntity.vaccin);
//                Log.e("content", writeEntity.content);

                Log.e("지점체크", "1");

                int fileSize = writeEntity.CurrentImageList.size();


                for (int i = 0; i < fileSize; i++) {

                    Log.e("지점체크", "2" + i);


                        if (writeEntity.img.size() > 0) {
                            File file = writeEntity.img.get(i).file;
                            if (writeEntity.CurrentImageList.get(i).length() < 1 && file.getName() == null) {
                                builder.addFormDataPart("img_state", "1");
                                Log.e("이미지 id", i + 1 + "번째는 : 1");
                            } else if (writeEntity.CurrentImageList.get(i).length() < 1 && file.getName().length() > 1) {
                                builder.addFormDataPart("img_state", "2");
                                builder.addFormDataPart("img", file.getName(), RequestBody.create(IMAGE_MIME_TYPE, file));
                                Log.e("이미지 id", i + 1 + "번째는 : 2");
                            } else if (writeEntity.CurrentImageList.get(i).length() > 1 && file.getName() == null) {
                                builder.addFormDataPart("img_state", "1");
                                Log.e("이미지 id", i + 1 + "번째는 : 3-1");
                            } else if (writeEntity.CurrentImageList.get(i).length() > 1 && file.getName().length() > 1) {
                                builder.addFormDataPart("img_state", "4");
                                builder.addFormDataPart("img", file.getName(), RequestBody.create(IMAGE_MIME_TYPE, file));
                                Log.e("이미지 id", i + 1 + "번째는 : 4");
                            }
                        } else {
                            builder.addFormDataPart("img_state", "1");
                            Log.e("이미지 id", "기본으로 1");
                        }

//                    builder.addFormDataPart("img", file.getName(), RequestBody.create(IMAGE_MIME_TYPE, file));
//                    Log.e("이미지 파앨명", file.getName());


                }
                Log.e("지점체크", "3");
                RequestBody fileUploadBody = builder.build();

                Request request = new Request.Builder()
                        .url("http://52.78.104.95:3000/posts/" + pid)
                        .put(fileUploadBody)
                        .build();
                Log.e("tag", "http://52.78.104.95:3000/posts/" + pid);

                response = toServer.newCall(request).execute();
                Log.e("체크체크체", response + "5");
                boolean flag = response.isSuccessful();

                int responseCode = response.code();
                if (flag) {
                    e("response결과", responseCode + "---" + response.message()); //읃답에 대한 메세지(OK)
                    e("response응답바디", response.body().string()); //json으로 변신
                    return "success";
                }
            } catch (UnknownHostException une) {
                Log.e("aa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                Log.e("bb", uee.toString());
            } catch (Exception e) {
                Log.e("cc", e.toString());
            } finally {
                if (response != null) {
                    response.close();
                }
            }

            return "fail";
        }

        @Override
        protected void onPostExecute(String s) {

            Log.e("체크", s);
            super.onPostExecute(s);
            if (s.equalsIgnoreCase("success")) {
                Toast.makeText(EditTabActivity.this, "파일업로드에 성공했습니다", Toast.LENGTH_LONG).show();
                int fileSize = EditFragment.upLoadfiles.size();

                for (int i = 0; i < fileSize; i++) {
                    UpLoadValueObject fileValue = EditFragment.upLoadfiles.get(i);
                    if (fileValue.tempFiles) {
                        fileValue.file.deleteOnExit(); //임시파일을 삭제한다
                    }
                }
            } else {
                Toast.makeText(EditTabActivity.this, "파일업로드에 실패했습니다", Toast.LENGTH_LONG).show();
            }
            regist.setEnabled(true);
        }
    }


    private boolean validityCheck(PostEditMainListEntity entity) {


        if (entity.CurrentImageList.size() == 0 && entity.img.size() == 0) {
            Toast.makeText(getApplicationContext(), "최소 1장의 사진을 올려주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (entity.breed == null) {
            Toast.makeText(getApplicationContext(), "견종을 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Integer.parseInt(entity.gender) == 3) {
            Log.e("성별", entity.gender);
            Toast.makeText(getApplicationContext(), "성별을 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (entity.age.length() <= 0) {
            Toast.makeText(getApplicationContext(), "나이를 정확히 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (entity.region == null) {
            Toast.makeText(getApplicationContext(), "지역을 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Integer.parseInt(entity.neuter) == 3) {
            Toast.makeText(getApplicationContext(), "중성화 여부를 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (entity.vaccin.equals("")) {
            Toast.makeText(getApplicationContext(), "예방접종 여부를 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (entity.content.length() < 20) {
            Toast.makeText(getApplicationContext(), "자세한 내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        regist.setEnabled(true);
        EditFragment.neutralizeCheck = 0;
        EditFragment.genderCheck = 0;
    }

}
