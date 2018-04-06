package wepet.projectbase.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import wepet.projectbase.GetContext;
import wepet.projectbase.R;
import wepet.projectbase.entity.DetailSearchEntity;
import wepet.projectbase.entity.MyProfileEntityObject;
import wepet.projectbase.entity.UserMatchEntity;
import wepet.projectbase.fragment.MyPageMatchResult;
import wepet.projectbase.fragment.MypageMatchNoneResult;
import wepet.projectbase.fragment.Tab1Fragment;
import wepet.projectbase.fragment.Tab2Fragment;
import wepet.projectbase.fragment.Tab3Fragment;
import wepet.projectbase.fragment.Tab4Fragment;


public class Main extends FontActivity {

    ImageButton sortBtn1;
    ImageButton sortBtn2;
    ImageButton sortBtn3;
    ImageButton sortBtn4;
    EditText edt1;
    MainListPagerAdapter mainAdapter;
    static ViewPager viewpager;


    RelativeLayout emptyLayout;

    public static Integer sortType = 1;
    public static Integer categoryType = 1;

    ArrayList<MyProfileEntityObject> data3 = new ArrayList<MyProfileEntityObject>();
    ArrayList<UserMatchEntity> tempData = new ArrayList<UserMatchEntity>();

    private TextView textView3;
    private DrawerLayout mDrawerLayout;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    String responseResultValue;
    private ImageButton myMenuOpen;
    private ImageButton searchOpen;
    private RelativeLayout reMatch;
    private ImageButton environmentSetting;

    private RelativeLayout myRecomment;
    private RelativeLayout myChat;
    private RelativeLayout myBookmark;
    private RelativeLayout myWritingBtn;
    private CircleImageView profileSetting;

    private CircleImageView myProfileImg;
    private TextView myNickname;
    private FloatingActionButton mainWrite;

    public static TabLayout tabLayout;
    public static int DetailSearchCheck = 0;
    static DetailSearchEntity entity;
    TextView mainTitle;
    Intent receivedIntent;


    static private final String TAG = "Main";

    ArrayList<UserMatchEntity> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        reMatch = (RelativeLayout) findViewById(R.id.matchlay);
        reMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this, Match.class);
                startActivity(intent);

            }
        });
        new AsyncMyProfileInsert().execute(); // 내 프로필설정

        receivedIntent = getIntent();

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();

        myNickname = (TextView) findViewById(R.id.myNickname);

        textView3 = (TextView) findViewById(R.id.textView3);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setContentInsetsAbsolute(0, 0);

        sortBtn1 = (ImageButton) findViewById(R.id.sort1);
        sortBtn2 = (ImageButton) findViewById(R.id.sort2);
        sortBtn3 = (ImageButton) findViewById(R.id.sort3);
        sortBtn4 = (ImageButton) findViewById(R.id.sort4);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        myMenuOpen = (ImageButton) findViewById(R.id.mypage_open);
        myMenuOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });


        mainWrite = (FloatingActionButton) findViewById(R.id.main_write);
        mainWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WriteTabActivity.class);
                intent.putExtra("tab",tabLayout.getSelectedTabPosition());

                startActivity(intent);
            }

        });

        myBookmark = (RelativeLayout) findViewById(R.id.line1);
        myBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MypageMyBookmark.class);
                startActivity(intent);

            }
        });

        myRecomment = (RelativeLayout) findViewById(R.id.line2);
        myRecomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MypageMyComment.class);
                startActivity(intent);

            }
        });

        myWritingBtn = (RelativeLayout) findViewById(R.id.line4);
        myWritingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this, MyWritingActivity.class);
                startActivity(intent);
            }
        });

        myChat = (RelativeLayout) findViewById(R.id.line3);
        myChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"구현 준비중 입니다.",Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getApplicationContext(), MypageMyChat.class);
//                startActivity(intent);
            }
        });






        environmentSetting = (ImageButton) findViewById(R.id.setting_btn);
        environmentSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EnvironmentSetting.class);
                startActivity(intent);

            }
        });

        myProfileImg = (CircleImageView) findViewById(R.id.myProfileImage);
        myProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyProfileModification.class);
                intent.putExtra("data", data3);
                startActivity(intent);
            }
        });

        Log.e(TAG, data + "");


        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewpager);
        mainTitle = (TextView) findViewById(R.id.main_title);


        searchOpen = (ImageButton) findViewById(R.id.search_btn);
        searchOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (tabLayout.getSelectedTabPosition()) {
                    case 0:
                        Intent intent0 = new Intent(getApplicationContext(), SearchAdopt.class);
                        intent0.putExtra("tab", 1);
                        startActivity(intent0);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getApplicationContext(), SearchMissing.class);
                        intent1.putExtra("tab", 2);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getApplicationContext(), SearchProtect.class);
                        intent2.putExtra("tab", 3);
                        startActivity(intent2);
                        break;
                }
            }
        });


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                switch (tabLayout.getSelectedTabPosition()) {
                    case 0:
                        mainTitle.setText("새 주인을 찾아요");
                        categoryType = 1;
                        searchOpen.setVisibility(View.VISIBLE);
                        tabLayout.getTabAt(0).setIcon(R.drawable.bun_100);
                        tabLayout.getTabAt(1).setIcon(R.drawable.miss_50);
                        tabLayout.getTabAt(3).setIcon(R.drawable.match_50);
                        tabLayout.getTabAt(2).setIcon(R.drawable.protect_50);
                        tabFragment1.getAsyncInstance().execute(Main.categoryType);

                        break;
                    case 1:
                        mainTitle.setText("찾아주세요   ");
                        categoryType = 2;
                        tabLayout.getTabAt(0).setIcon(R.drawable.bun_50);
                        tabLayout.getTabAt(1).setIcon(R.drawable.miss_100);
                        tabLayout.getTabAt(3).setIcon(R.drawable.match_50);
                        tabLayout.getTabAt(2).setIcon(R.drawable.protect_50);
                        searchOpen.setVisibility(View.VISIBLE);
                        tabFragment2.getAsyncInstance().execute(Main.categoryType);

                        break;
                    case 2:
                        mainTitle.setText("보호중이에요  ");
                        categoryType = 3;
                        tabLayout.getTabAt(0).setIcon(R.drawable.bun_50);
                        tabLayout.getTabAt(1).setIcon(R.drawable.miss_50);
                        tabLayout.getTabAt(3).setIcon(R.drawable.match_50);
                        tabLayout.getTabAt(2).setIcon(R.drawable.protect_100);
                        searchOpen.setVisibility(View.VISIBLE);
                        tabFragment3.getAsyncInstance().execute(Main.categoryType);

                        break;
                    case 3:
                        mainTitle.setText("새주인을 찾아요");
                        categoryType = 4;
                        tabLayout.getTabAt(0).setIcon(R.drawable.bun_50);
                        tabLayout.getTabAt(1).setIcon(R.drawable.miss_50);
                        tabLayout.getTabAt(3).setIcon(R.drawable.match_100);
                        tabLayout.getTabAt(2).setIcon(R.drawable.protect_50);
                        searchOpen.setVisibility(View.INVISIBLE);
                        tabFragment4.getAsyncInstance().execute();

                        break;

                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
        });

        mainAdapter = new MainListPagerAdapter(getSupportFragmentManager());
        //탭 툴바 텍스트 변경 메소드


        sortBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (tabLayout.getSelectedTabPosition()) {
                    case 0:
                        categoryType = 1;
                        break;
                    case 1:
                        categoryType = 2;
                        break;
                    case 2:
                        categoryType = 3;
                        break;
                    case 3:
                        categoryType = 4;
                        break;
                }


                sortBtn1.setImageResource(R.drawable.recent_click);
                sortBtn2.setImageResource(R.drawable.approaching_non_click);
                sortBtn3.setImageResource(R.drawable.recomment_non_click);
                sortBtn4.setImageResource(R.drawable.bookmark_non_click);
                sortType = 1;
                mainAdapter.notifyDataSetChanged();
            }
        });
        sortBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (tabLayout.getSelectedTabPosition()) {
                    case 0:
                        categoryType = 1;
                        break;
                    case 1:
                        categoryType = 2;
                        break;
                    case 2:
                        categoryType = 3;
                        break;
                    case 3:
                        categoryType = 4;
                        break;
                }
                sortBtn1.setImageResource(R.drawable.recent_non_click);
                sortBtn2.setImageResource(R.drawable.approaching_click);
                sortBtn3.setImageResource(R.drawable.recomment_non_click);
                sortBtn4.setImageResource(R.drawable.bookmark_non_click);
                sortType = 2;
                mainAdapter.notifyDataSetChanged();
            }
        });

        sortBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (tabLayout.getSelectedTabPosition()) {
                    case 0:
                        categoryType = 1;
                        break;
                    case 1:
                        categoryType = 2;
                        break;
                    case 2:
                        categoryType = 3;
                        break;
                    case 3:
                        categoryType = 4;
                        break;
                }
                sortBtn1.setImageResource(R.drawable.recent_non_click);
                sortBtn2.setImageResource(R.drawable.approaching_non_click);
                sortBtn3.setImageResource(R.drawable.recomment_click);
                sortBtn4.setImageResource(R.drawable.bookmark_non_click);
                sortType = 3;
                mainAdapter.notifyDataSetChanged();
            }
        });
        sortBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (tabLayout.getSelectedTabPosition()) {
                    case 0:
                        categoryType = 1;
                        break;
                    case 1:
                        categoryType = 2;
                        break;
                    case 2:
                        categoryType = 3;
                        break;
                    case 3:
                        categoryType = 4;
                        break;
                }
                sortBtn1.setImageResource(R.drawable.recent_non_click);
                sortBtn2.setImageResource(R.drawable.approaching_non_click);
                sortBtn3.setImageResource(R.drawable.recomment_non_click);
                sortBtn4.setImageResource(R.drawable.bookmark_click);
                sortType = 4;
                mainAdapter.notifyDataSetChanged();
            }
        });

        entity = new DetailSearchEntity();
        if (DetailSearchCheck == 1) {
            Intent intent = getIntent();
            entity = (DetailSearchEntity) intent.getSerializableExtra("data");
        }
        if (viewpager != null) {
            setTabViewPager(viewpager);
        }
        new AsyncMyMatch().execute();

    }


    Tab1Fragment tabFragment1;
    Tab2Fragment tabFragment2;
    Tab3Fragment tabFragment3;
    Tab4Fragment tabFragment4;

    @Override
    protected void onResume() {
        super.onResume();

        if(PropertyManager.getInstance().getfirst()==100)
        {
            reMatch.setVisibility(View.INVISIBLE);
        }else {
            reMatch.setVisibility(View.VISIBLE);
        }


    }

    private void setTabViewPager(ViewPager viewPager) {
        mainAdapter = new MainListPagerAdapter(getSupportFragmentManager());

        if (DetailSearchCheck == 1) {
            tabFragment1 = Tab1Fragment.newInstanceDetail(entity);
            tabFragment2 = Tab2Fragment.newInstanceDetail(entity);
            tabFragment3 = Tab3Fragment.newInstanceDetail(entity);
            tabFragment4 = Tab4Fragment.newInstanceDetail(entity);
            mainAdapter.appendFragment(tabFragment1);
            mainAdapter.appendFragment(tabFragment2);
            mainAdapter.appendFragment(tabFragment3);
            mainAdapter.appendFragment(tabFragment4);
            sortBtn1.setImageResource(R.drawable.recent_click);
            sortBtn2.setImageResource(R.drawable.approaching_non_click);
            sortBtn3.setImageResource(R.drawable.recomment_non_click);
            sortBtn4.setImageResource(R.drawable.bookmark_non_click);
            viewPager.setAdapter(mainAdapter);
            viewPager.setCurrentItem(entity.tab - 1);


        } else {
            tabFragment1 = Tab1Fragment.newInstance();
            tabFragment2 = Tab2Fragment.newInstance();
            tabFragment3 = Tab3Fragment.newInstance();
            tabFragment4 = Tab4Fragment.newInstance();
            mainAdapter.appendFragment(tabFragment1);
            mainAdapter.appendFragment(tabFragment2);
            mainAdapter.appendFragment(tabFragment3);
            mainAdapter.appendFragment(tabFragment4);
            viewPager.setAdapter(mainAdapter);
        }


    }

    private static class MainListPagerAdapter extends FragmentStatePagerAdapter {

        private final ArrayList<Fragment> listFragments = new ArrayList<Fragment>();

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public MainListPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void appendFragment(Tab1Fragment fragment1) {
            listFragments.add(fragment1);
        }
        public void appendFragment(Tab2Fragment fragment2) {
            listFragments.add(fragment2);
        }
        public void appendFragment(Tab3Fragment fragment3) {
            listFragments.add(fragment3);
        }
        public void appendFragment(Tab4Fragment fragment4) {
            listFragments.add(fragment4);
        }

        @Override
        public int getCount() {
            return listFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return listFragments.get(position);
        }

//        public CharSequence getPageTitle(int position) {
//            return tabTitle.get(position);
//        }

    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        long intervalTime = currentTime - backPressedTime;

        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
                super.onBackPressed();
            } else {
                backPressedTime = currentTime;
                Toast.makeText(this, "뒤로 버튼을 한번 더 누르면 종료 됩니다.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public class AsyncMyProfileInsert extends AsyncTask<MyProfileEntityObject, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(MyProfileEntityObject... myProfileEntityObjects) {

            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            Request request = new Request.Builder() //보내는 곳 설정
                    .url("http://52.78.104.95:3000/users/" + PropertyManager.getInstance().getUid())
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();

            try {
                Response response = okHttpClient.newCall(request).execute();
                JSONObject responsedJson = null;
                JSONObject responsedJsonData = null;

                try {
                    responsedJson = new JSONObject(response.body().string());
                    responsedJsonData = responsedJson.getJSONObject("data");

                    MyProfileEntityObject object = new MyProfileEntityObject();

                    object.nickname = responsedJsonData.getString("nickname");
                    object.profile_thumb = responsedJsonData.getString("profile_thumb");
                    object.uid = responsedJsonData.getString("uid");

                    data3.add(object);

                } catch (Exception e) {
                    Log.e("dlog", e.toString());
                }
            } catch (Exception e) {

            }
            Log.e("dlog", data3.size() + "");
            return responseResultValue;
        }

        @Override
        protected void onPostExecute(String s) {

            MyProfileEntityObject object = data3.get(0);
            if (object.profile_thumb.length() > 0) {
                Glide.with(GetContext.getContext()).load(object.profile_thumb).into(myProfileImg); //픽셀값으로 변경
            }

            myNickname.setText(object.nickname);
            super.onPostExecute(s);
        }
    }

    public class AsyncMyMatch extends AsyncTask<UserMatchEntity, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(UserMatchEntity... userMatchEntities) {

            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            Request request = new Request.Builder() //보내는 곳 설정
                    .url("http://52.78.104.95:3000/users/" + PropertyManager.getInstance().getUid() + "/curation")
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            try {
                Response response = okHttpClient.newCall(request).execute();
                JSONObject responsedJson = null;
                JSONArray responsedArray = null;
                responsedJson = new JSONObject(response.body().string());
                responsedArray = responsedJson.getJSONArray("data");

                for (int i = 0; i < responsedArray.length(); i++) {

                    UserMatchEntity object = new UserMatchEntity();

                    object.breed = responsedArray.getJSONObject(i).getInt("breed");
                    object.active = responsedArray.getJSONObject(i).getInt("active");
                    object.sociable = responsedArray.getJSONObject(i).getInt("sociable");
                    object.molt = responsedArray.getJSONObject(i).getInt("molt");
                    object.size = responsedArray.getJSONObject(i).getInt("size");

                    tempData.add(object);
                }

            } catch (Exception e) {

            }
            return responseResultValue;
        }

        @Override
        protected void onPostExecute(String s) {
            data = tempData;
            if (data.size() != 0) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                MyPageMatchResult myPageMatchResult = MyPageMatchResult.newInstance(data);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.drawer_layout_match_result, myPageMatchResult);
                fragmentTransaction.commitAllowingStateLoss();
                //myMenuOpen.performClick();

            } else if (data.size() == 0) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                MypageMatchNoneResult myPageMatchNoneResult = new MypageMatchNoneResult();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.add(R.id.drawer_layout_match_result, myPageMatchNoneResult);
                fragmentTransaction.commit();

                textView3.setText("매칭하러가기");
            }
            super.onPostExecute(s);
        }
    }

}
