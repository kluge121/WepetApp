package wepet.projectbase.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import wepet.projectbase.ChangeCodeToName;
import wepet.projectbase.GetContext;
import wepet.projectbase.R;
import wepet.projectbase.entity.GetDetailRecommentEntity;
import wepet.projectbase.entity.OtherProfileEntityObject;
import wepet.projectbase.fragment.RecommentList;

public class DetailPage extends FontActivity implements RecommentList.RecommnetListRecyclerViewAdapter.DeleteRecommentCallback {

    ImageView profileImage;
    TextView profileName;
    TextView profileTime;
    TextView book_Num;
    TextView summaryContext;
    TextView mainContext;



    ArrayList<Integer> imgId;
    ArrayList<String> image;
    private static int bookmarkCheck;

    private ImageButton backBtn;
    private ImageButton recommentBtn;
    private ImageView profileBtn;
    public EditText recommentEdit;
    public ImageButton bookmarkButton;
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    ViewPager viewPager;


    private ArrayList<ImageView> dots;
    private ViewPager mViewPager;
    private static Integer NUM_PAGES;
    Intent intent;
    public static Integer pid;
    public static String uid;
    Integer position;

    ArrayList<OtherProfileEntityObject> data4 = new ArrayList<OtherProfileEntityObject>();
    RecommentList fragment = new RecommentList();


    @Override
    public void DeleteRecommentCallback(final ArrayList<GetDetailRecommentEntity> list, final GetDetailRecommentEntity object, final RecommentList.RecommnetListRecyclerViewAdapter adapter) {

        AlertDialog dialog = null;

        DialogInterface.OnClickListener dialog_recomment_delete = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                new AsyncCommentDel().execute(object.rid);
                list.remove(object);
                adapter.notifyDataSetChanged();
            }
        };
        DialogInterface.OnClickListener dialog_recomment_delet_cancle = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        };

        dialog = new AlertDialog.Builder(this)
                .setTitle("댓글 삭제")
                .setPositiveButton("삭제", dialog_recomment_delete)
                .setNegativeButton("취소", dialog_recomment_delet_cancle)
                .setCancelable(true).create();

        dialog.show();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);


        bookmarkButton = (ImageButton) findViewById(R.id.detail_bookmark_btn);


        profileImage = (ImageView) findViewById(R.id.detail_page_writer_pofile_image);
        profileName = (TextView) findViewById(R.id.detail_page_writer_name);
        profileTime = (TextView) findViewById(R.id.detail_page_writer_time);
        summaryContext = (TextView) findViewById(R.id.summery_datail);
        mainContext = (TextView) findViewById(R.id.detail_context);
        book_Num = (TextView) findViewById(R.id.detail_bookmark_num);


        intent = getIntent();
        pid = intent.getExtras().getInt("pid");
        position = intent.getExtras().getInt("position");

        if (intent.hasExtra("uid")) {
            uid = intent.getExtras().getString("uid");
        }


        mViewPager = (ViewPager) findViewById(R.id.detail_viewpager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        //back 키  상세보기 > 메인화면
        backBtn = (ImageButton) findViewById(R.id.button_exit);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recommentBtn = (ImageButton) findViewById(R.id.recomment_regist);
        recommentEdit = (EditText) findViewById(R.id.recomment_edit_context);

        recommentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (recommentEdit.getText() != null && recommentEdit.getText().toString().replace(" ", "").length() != 0) {
                    recommentBtn.setImageResource(R.drawable.detail_recomment_input);
                } else {
                    recommentBtn.setImageResource(R.drawable.detail_recomment_non_input);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        recommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recommentEdit.getText() != null && recommentEdit.getText().toString().replace(" ", "").length() != 0) {
                    String tmp = recommentEdit.getText().toString();
                    new AsyncInsertRecomment().execute(tmp);
                    recommentBtn.setImageResource(R.drawable.detail_recomment_non_input);
                } else {
                    Toast.makeText(getApplicationContext(), "댓글을 입력해 주십시오", Toast.LENGTH_SHORT).show();
                }
                recommentEdit.setText("");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.detach(fragment);
                fragmentTransaction.attach(fragment);
                fragmentTransaction.commit();
                getSupportFragmentManager().popBackStack();
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(recommentEdit.getWindowToken(), 0);


            }
        });
        profileBtn = (ImageView) findViewById(R.id.detail_page_writer_pofile_image);

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailPage.this, IndividualProfile.class);
                intent.putExtra("uid", uid);
                intent.putExtra("writer", 1);
                Log.e("체크", "uid : " + uid);
                startActivity(intent);
            }
        });

        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookmarkButton.setEnabled(false);
                if (bookmarkCheck == 1) {
                    bookmarkButton.setImageResource(R.drawable.detail_book_non_click);
                    new AsyncAddBookmark().execute();
                    new AsyncDetailGetRefresh().execute(pid.toString());


                } else if (bookmarkCheck == 0) {
                    bookmarkButton.setImageResource(R.drawable.detail_book_click);
                    new AsyncAddBookmark().execute();
                    new AsyncDetailGetRefresh().execute(pid.toString());


                }
            }
        });

        new AsyncDetailGet().execute(pid.toString());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.recomment_container, fragment); // 부착하는 역할
        fragmentTransaction.commit();


    }


    public void addDots() {

        dots = new ArrayList<>();
        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.dotlayout);

        for (int i = 0; i < NUM_PAGES; i++) {
            ImageView dot = new ImageView(this);

            if (i != 0) {
                dot.setImageDrawable(getResources().getDrawable(R.drawable.indicator_non_select));
            } else {
                dot.setImageDrawable(getResources().getDrawable(R.drawable.indicator_select));
            }

            dot.setScaleType(ImageView.ScaleType.FIT_XY);
            dot.setPadding(10, 0, 10, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            dotsLayout.addView(dot, params);
            dots.add(dot);
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    } // 인디케이터 생성 메소드

    public void selectDot(int idx) {
        Resources res = getResources();
        for (int i = 0; i < NUM_PAGES; i++) {
            int drawableId = (i == idx) ? (R.drawable.indicator_select) : (R.drawable.indicator_non_select);
            Drawable drawable = res.getDrawable(drawableId);
            dots.get(i).setImageDrawable(drawable);
            dots.get(i).setScaleType(ImageView.ScaleType.FIT_XY);
            dots.get(i).setPadding(10, 0, 10, 0);
        }
    } // 인디케이터 관리 메소드

    private static class pagerAdapter extends PagerAdapter {


        public Context context;
        private ArrayList<String> detailImage;

        public pagerAdapter(Context context, ArrayList<String> detailImage) {
            this.detailImage = detailImage;
            this.context = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            ImageView imageView = new ImageView(context);

            int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            Glide.with(GetContext.getContext()).load(detailImage.get(position)).into(imageView); //픽셀값으로 변경

            ((ViewPager) container).addView(imageView, 0);

            return imageView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }


        @Override
        public int getCount() {
            return detailImage.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    } // 그림보기 뷰페이저

    public class AsyncDetailGet extends AsyncTask<String, Void, JSONObject> {


        JSONArray jsonArray = null;

        @Override
        protected JSONObject doInBackground(String... mPid) {

            JSONObject jsonObject = null;
            try {
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();
                Request request = new Request.Builder()
                        .url("http://52.78.104.95:3000/posts/" + mPid[0] + "?uid=" + PropertyManager.getInstance().getUid())
                        .build();
                Log.e("체크", "http://52.78.104.95:3000/posts/" + mPid[0] + "?uid=" + PropertyManager.getInstance().getUid());
                Response respone = toServer.newCall(request).execute();
                ResponseBody responseBody = respone.body();
                jsonObject = new JSONObject(responseBody.string());

            } catch (UnknownHostException une) {
                Log.e("fileUpLoad1", une.toString());
            } catch (UnsupportedEncodingException uee) {
                Log.e("fileUpLoad2", uee.toString());
            } catch (Exception e) {
                Log.e("fileUpLoad3", e.toString());
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {

                    JSONObject data = null;
                    JSONArray imgs = null;


                    if (jsonObject != null && jsonObject.length() > 0) {

                        data = jsonObject.getJSONObject("data");
                        imgs = jsonObject.getJSONArray("imgs");


                    Log.e("tag", "현재보고있는글 pid : " + data.getInt("pid"));
                    Log.e("tag", "현재보고있는글 uid : " + data.getString("uid"));
                    Log.e("tag", "현재보고있는글 state : " + data.getInt("state"));

                    StringBuilder builder = new StringBuilder()
                            .append("#" + ChangeCodeToName.puppyName(data.getInt("breed")))
                            .append("#" + ChangeCodeToName.dogGender(data.getInt("gender")))
                            .append("#" + data.getInt("age") + "살")
                            .append("#" + ChangeCodeToName.dogRegion(data.getInt("region")))
                            .append("#" + ChangeCodeToName.dogNeuter(data.getInt("neuter")))
                            .append("#" + ChangeCodeToName.dogVacccin(data.getInt("vaccin")));
                    String s = builder.toString();



                        if(!data.getString("profile_thumb").equals("")){
                            Glide.with(GetContext.getContext()).load(data.getString("profile_thumb")).into(profileImage); //픽셀값으로 변경
                        }


                    image = new ArrayList<String>();
                    imgId = new ArrayList<Integer>();

                    for(int i = 0 ; i < imgs.length();i++) {
                        Log.e("url체크","url " + i + " "+imgs.getJSONObject(i).getString("img"));
                        if (imgs.getJSONObject(i).getString("img").length() > 0) {
                            image.add(imgs.getJSONObject(i).getString("img"));
                            imgId.add(imgs.getJSONObject(i).getInt("id"));
                        }
                    }



                    NUM_PAGES = image.size();
                    addDots(); // 사진 인디케이터 생성
                    viewPager = (ViewPager) findViewById(R.id.detail_viewpager);
                    pagerAdapter adapter = new pagerAdapter(getApplicationContext(), image);
                    viewPager.setAdapter(adapter);


                    uid = data.getString("uid");
                    profileName.setText(data.getString("nickname"));
                    profileTime.setText(data.getString("date"));
                    summaryContext.setText(s);
                    mainContext.setText(data.getString("content"));
                    book_Num.setText(data.getInt("bookmark_num") + "");



                    bookmarkCheck = data.getInt("bookmark");
                    if (bookmarkCheck == 1) {
                        bookmarkButton.setImageResource(R.drawable.detail_book_click);

                    } else if (bookmarkCheck == 0) {
                        bookmarkButton.setImageResource(R.drawable.detail_book_non_click);
                    }

                        Log.e("끝마무리", "체크");
                }

            } catch (Exception e) {
                Log.e("leelog1", e.toString());
            }

        }
    } // 정보가져오기 Async

    public class AsyncInsertRecomment extends AsyncTask<String, Void, String> {


        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2;

        @Override
        protected String doInBackground(String... content) {

            OkHttpClient client = new OkHttpClient();

            try {
                jsonObject1.put("msg", "success");
                jsonObject1.put("uid", PropertyManager.getInstance().getUid()); // 후에 로그인한 계정으로 연결해서 줄것.
                jsonObject1.put("content", content[0]);

//
            } catch (Exception e) {

            }

            RequestBody body = RequestBody.create(JSON, jsonObject1.toString());

            Log.e("tag", "올릴려는 댓글pid : " + pid);
            Request request = new Request.Builder()
                    .url("http://52.78.104.95:3000/posts/" + pid + "/replies")
                    .post(body)
                    .build();

            String responsedStr;

            try {
                Response response = client.newCall(request).execute();
                responsedStr = response.body().string();
                jsonObject2 = new JSONObject(responsedStr);
                return jsonObject2.getString("msg");

            } catch (Exception e) {

            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(DetailPage.this, s, Toast.LENGTH_SHORT).show();
        }
    }

    public class AsyncAddBookmark extends AsyncTask<Void, Void, String> {


        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2;

        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();


            try {
                jsonObject1.put("msg", "success");
                jsonObject1.put("uid", PropertyManager.getInstance().getUid());
            } catch (Exception e) {
                Log.e("DetailPage", "북마크 어씽크  객체에 담 던 중 오류");
            }

            RequestBody body = RequestBody.create(JSON, jsonObject1.toString());

            Request request = new Request.Builder()
                    .url("http://52.78.104.95:3000/posts/" + pid + "/" + "bookmarks")
                    .post(body)
                    .build();

            String responsedStr;
            try {
                Response response = client.newCall(request).execute();
                responsedStr = response.body().string();
                jsonObject2 = new JSONObject(responsedStr);
                return jsonObject2.getString("msg");
            } catch (Exception e) {
                Log.e("DetailPage", "북마크 어씽크 받아오던 중 오류");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            bookmarkButton.setEnabled(true);
        }
    }

    public class AsyncDetailGetRefresh extends AsyncTask<String, Void, JSONObject> {

        JSONObject data = null;
        JSONArray imgs = null;
        JSONArray jsonArray = null;

        @Override
        protected JSONObject doInBackground(String... mPid) {

            JSONObject jsonObject = null;
            try {
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();
                Request request = new Request.Builder()
                        .url("http://52.78.104.95:3000/posts/" + mPid[0] + "?uid=" + PropertyManager.getInstance().getUid())
                        .build();

                Response respone = toServer.newCall(request).execute();
                ResponseBody responseBody = respone.body();
                jsonObject = new JSONObject(responseBody.string());

            } catch (UnknownHostException une) {
                Log.e("fileUpLoad1", une.toString());
            } catch (UnsupportedEncodingException uee) {
                Log.e("fileUpLoad2", uee.toString());
            } catch (Exception e) {
                Log.e("fileUpLoad3", e.toString());
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {

                data = jsonObject.getJSONObject("data");
                imgs = jsonObject.getJSONArray("imgs");

                if (jsonObject != null && jsonObject.length() > 0) {

                    Log.e("tag", "현재보고있는글 pid : " + data.getInt("pid"));

                    StringBuilder builder = new StringBuilder()
                            .append("#" + ChangeCodeToName.puppyName(data.getInt("breed")))
                            .append("#" + ChangeCodeToName.dogGender(data.getInt("gender")))
                            .append("#" + data.getInt("age") + "살")
                            .append("#" + ChangeCodeToName.dogRegion(data.getInt("region")))
                            .append("#" + ChangeCodeToName.dogNeuter(data.getInt("neuter")))
                            .append("#" + ChangeCodeToName.dogVacccin(data.getInt("vaccin")));
                    String s = builder.toString();


                    image = new ArrayList<String>();

                    NUM_PAGES = image.size();


                    uid = data.getString("uid");
                    profileName.setText(data.getString("nickname"));
                    profileTime.setText(data.getString("date"));
                    summaryContext.setText(s);
                    mainContext.setText(data.getString("content"));
                    book_Num.setText(data.getInt("bookmark_num") + "");


                    bookmarkCheck = data.getInt("bookmark");
                    if (bookmarkCheck == 1) {
                        bookmarkButton.setImageResource(R.drawable.detail_book_click);
                    } else if (bookmarkCheck == 0) {
                        bookmarkButton.setImageResource(R.drawable.detail_book_non_click);
                    }


                }

            } catch (Exception e) {
                Log.e("leelog2", e.toString());
            }

        }
    } // 정보가져오기 Async

    public class AsyncCommentDel extends AsyncTask<Integer, Void, String> {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2;

        @Override
        protected String doInBackground(Integer... rid) {

            OkHttpClient client = new OkHttpClient();

            try {
                jsonObject.put("msg", "success");
                jsonObject.put("uid", PropertyManager.getInstance().getUid());
            } catch (Exception e) {

            }
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());

            Request request = new Request.Builder()
                    .url("http://52.78.104.95:3000/posts/" + DetailPage.pid.toString() + "/replies/" + rid[0])
                    .delete(body)
                    .build();

            String responsedStr;
            try {
                Response response = client.newCall(request).execute();
                responsedStr = response.body().string();
                jsonObject2 = new JSONObject(responsedStr);
                return jsonObject2.getString("msg");
            } catch (Exception e) {
                Log.e("상세보기 댓글 지우기 리스폰오류", e + "");
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equalsIgnoreCase("success")) {
                Toast.makeText(GetContext.getContext(), "댓글이 삭제되었습니다", Toast.LENGTH_SHORT).show();
            }
        }
    } //댓글삭제 어씽크


}





















