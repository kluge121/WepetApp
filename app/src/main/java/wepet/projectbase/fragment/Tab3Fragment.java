package wepet.projectbase.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import wepet.projectbase.GetContext;
import wepet.projectbase.R;
import wepet.projectbase.ToastMessage;
import wepet.projectbase.activity.DetailPage;
import wepet.projectbase.activity.Main;
import wepet.projectbase.entity.DetailSearchEntity;
import wepet.projectbase.entity.GetMainListEntity;


/**
 * Created by coco on 2016-07-24.
 */
public class Tab3Fragment extends Fragment {
    static Main owner;
    public static MainListRecyclerViewAdapter Adapter;

    public RecyclerView r;
    static ArrayList<GetMainListEntity> jsonAdoptList3;
    DetailSearchEntity entity;
    SwipeRefreshLayout swipeRefreshLayout;

    int searchState = 0;
    final int REGULAR_SEARCH = 0;
    final int DETAIL_SEARCH = 1;


    static String[] breed = {"기타", "말티즈", "미니핀", "요크셔", "치와와", "푸들", "닥스훈트", "슈나우저", "비숑", "시츄", "포메라니안",
            "불독", "불테리어", "비글", "코카", "진돗개", "풍산개", "시바견", "웰시코기", "리트리버", "말라뮤트", "허스키", "세퍼트", "로트바일러", "믹스견"};
    static String[] region = {"전체", "서울", "경기", "인천", "강원", "대전", "세종", "충남", "충북", "부산", "울산", "경남", "경북", "대구", "광주", "전남", "전북", "제주도"};
    static String[] state3 = {"", "공고중", "주인을 다시 만났어요", "안락사", "기간만료"};
    static String[] gender = {"", "수컷", "암컷", "gender err"};




    public static Tab3Fragment newInstanceDetail(DetailSearchEntity entity) {

        Tab3Fragment mainListFragment = new Tab3Fragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", entity);
        mainListFragment.setArguments(bundle);
        return mainListFragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Main.DetailSearchCheck == 1) {
            entity = (DetailSearchEntity) getArguments().getSerializable("data");
            Main.DetailSearchCheck = 0;
            searchState = DETAIL_SEARCH;

        } else {
            searchState = REGULAR_SEARCH;
        }
    }

    public static Tab3Fragment newInstance() {

        Tab3Fragment mainListFragment = new Tab3Fragment();
        return mainListFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        owner = (Main) getActivity();
        final View v = inflater.inflate(R.layout.main_fragment, container, false);
        r = (RecyclerView) v.findViewById(R.id.mainListRecyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.mainlist_swipe);
        swipeRefreshLayout.setColorSchemeColors(Color.rgb(255,153,51),Color.rgb(255,153,51), Color.rgb(255,153,51));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                    new AsyncMainGet().execute(1);
                    r.setLayoutManager(new LinearLayoutManager(GetContext.getContext()));


            }

        });

        if (entity == null) {   // 탭에 위치에 따라서 수정 할 곳
            new AsyncMainGet().execute(1);
            r.setLayoutManager(new LinearLayoutManager(GetContext.getContext()));
        } else if (entity != null) {


            new AsyncMainGetDetailSearch().execute(entity);
            r.setLayoutManager(new LinearLayoutManager(GetContext.getContext()));
            entity =null;

        }
        return v;
    }


    public static class MainListRecyclerViewAdapter extends RecyclerView.Adapter<MainListRecyclerViewAdapter.ViewHolder> {


        ArrayList<GetMainListEntity> mainArrayList = new ArrayList<GetMainListEntity>();

        public MainListRecyclerViewAdapter(Context context, ArrayList<GetMainListEntity> mainArrayList) {
            this.mainArrayList = mainArrayList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_recycler_main, parent, false);
            Drawable alpha = v.findViewById(R.id.shape).getBackground();
            alpha.setAlpha(140);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final GetMainListEntity valueObj = mainArrayList.get(position);
            holder.setView(mainArrayList.get(position));
            Drawable alpha = holder.itemout.getBackground();
            if (valueObj.state != 1) {
                holder.itemout.setVisibility(View.VISIBLE);
                holder.itemCorrnetStat.setText(state3[mainArrayList.get(position).state]);
                alpha.setAlpha(200);
            }else {
                holder.itemout.setVisibility(View.GONE);
            }

            switch (valueObj.state) {

                case 2:
                    holder.itemflower.setVisibility(View.GONE);
                    break;
                case 3:
                    holder.itemflower.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    holder.itemflower.setVisibility(View.GONE);
                    break;

            }



            Glide.with(GetContext.getContext()).load(mainArrayList.get(position).img_thumb).into(holder.itemWindowImage); //픽셀값으로 변경

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailPage.class);
                    intent.putExtra("pid", jsonAdoptList3.get(position).pid);
                    intent.putExtra("position", position);
                    intent.putExtra("uid", jsonAdoptList3.get(position).uid);
                    ActivityCompat.startActivity(owner, intent, null);


                }
            });



        }

        @Override
        public int getItemCount() {
            return mainArrayList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {


            public final View mView;
            public final ImageView itemWindowImage;
            public final TextView itemExplantionKindText;
            public final TextView itemExplantionUniText;
            public final TextView itemExplantionLocationText;
            public final TextView itemBookMarkNum;
            public final TextView itemRecommetNum;
            public final TextView itemCorrnetStat;
            public final ImageView itemflower;
            public final ImageView itemout;


            public ViewHolder(View view) {
                super(view);
                mView = view;

                itemWindowImage = (ImageView) view.findViewById(R.id.item_window_image);
                itemExplantionKindText = (TextView) view.findViewById(R.id.item_explantion_kind);
                itemBookMarkNum = (TextView) view.findViewById(R.id.item_bookmark_num);
                itemRecommetNum = (TextView) view.findViewById(R.id.item_recomment_num);
                itemExplantionUniText = (TextView) view.findViewById(R.id.item_explantion_uni);
                itemExplantionLocationText = (TextView) view.findViewById(R.id.item_explantion_location);
                itemCorrnetStat = (TextView) view.findViewById(R.id.item_corrent_state);
                itemflower = (ImageView) view.findViewById(R.id.flower);
                itemout = (ImageView) view.findViewById(R.id.outfucusing);

            }

            public void setView(GetMainListEntity data) {

                this.itemExplantionKindText.setText(breed[data.breed]);
                this.itemExplantionUniText.setText(gender[data.gender]);
                this.itemExplantionLocationText.setText(region[data.region]);
                this.itemBookMarkNum.setText(data.bookmark_num.toString());
                this.itemRecommetNum.setText(data.reply_num.toString());
                this.itemCorrnetStat.setText("");
            }
        }

    }


    public static ArrayList<GetMainListEntity> getWritingRequestAdot(StringBuilder buf) {

        JSONObject jsonObject;
        JSONArray jsonArray = null;
        jsonAdoptList3 = null;

        try {
            jsonAdoptList3 = new ArrayList<GetMainListEntity>();
            jsonObject = new JSONObject(buf.toString());
            jsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject bufferObj = jsonArray.getJSONObject(i);
                GetMainListEntity getMainListEntity = new GetMainListEntity();
                getMainListEntity.pid = bufferObj.getInt("pid");
                getMainListEntity.pid_animal = bufferObj.getString("pid_animal");
                getMainListEntity.uid = bufferObj.getString("uid");
                getMainListEntity.category = bufferObj.getInt("category");
                getMainListEntity.date = bufferObj.getString("date");
                getMainListEntity.date_end = bufferObj.getString("date_end");
                getMainListEntity.img_thumb = bufferObj.getString("img_thumb");
                getMainListEntity.breed = bufferObj.getInt("breed");
                getMainListEntity.gender = bufferObj.getInt("gender");
                getMainListEntity.age = bufferObj.getInt("age");
                getMainListEntity.region = bufferObj.getInt("region");
                getMainListEntity.bookmark_num = bufferObj.getInt("bookmark_num");
                getMainListEntity.reply_num = bufferObj.getInt("reply_num");
                getMainListEntity.state = bufferObj.getInt("state");
                jsonAdoptList3.add(getMainListEntity);
            }
            return jsonAdoptList3;
        } catch (JSONException je) {
            Log.e("tag글목록", "JSON파싱 중 에러발생", je);
        }
        return null;
    }

    public AsyncMainGet getAsyncInstance() {
        return new AsyncMainGet();
    } // 탭에 위치에 따라서 수정 할 곳

    public class AsyncMainGet extends AsyncTask<Integer, Integer, ArrayList<GetMainListEntity>> {

        protected ArrayList<GetMainListEntity> doInBackground(Integer... params) {


            try {
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();

                Request request = new Request.Builder()
                        .url("http://52.78.104.95:3000/posts?category=" + 3 + "&sort=" + Main.sortType)
                        .build();
                Log.e("test1", params[0].toString());
                Log.e("typecheck", Main.sortType.toString());

                Response respone = toServer.newCall(request).execute();
                ResponseBody responseBody = respone.body();
                //responseBody.toString();
                if (respone.isSuccessful()) {

                    return getWritingRequestAdot(new StringBuilder(responseBody.string()));
                }

            } catch (UnknownHostException une) {
                Log.e("fileUpLoad", une.toString());
            } catch (UnsupportedEncodingException uee) {
                Log.e("fileUpLoad", uee.toString());
            } catch (Exception e) {
                Log.e("fileUpLoad", e.toString());
            }
            return null;

        }

        @Override
        protected void onPostExecute(ArrayList<GetMainListEntity> result) {
            try {
                if (result != null && result.size() > 0) {
                    Adapter = new MainListRecyclerViewAdapter(owner, result);
                    r.setAdapter(Adapter);
                    Adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);

                }

            } catch (Exception e) {
                Log.e("222222", "1111111");
            }
        }
    }

    public class AsyncMainGetDetailSearch extends AsyncTask<DetailSearchEntity, Integer, ArrayList<GetMainListEntity>> {

        //String category=null;
        protected ArrayList<GetMainListEntity> doInBackground(DetailSearchEntity... params) {


            DetailSearchEntity entity = new DetailSearchEntity();
            entity = params[0];


            try {
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();


                Request request = new Request.Builder()
                        .url("http://52.78.104.95:3000/posts?category=3" + "&gender=" + entity.gender + "&age" + entity.age + "&region=" +
                                entity.region + "&neuter=" + entity.neuter + "&vaccin=" + entity.vaccin + "&breed=" + entity.breed + "&sort=1&page=1&count=10")
                        .build();

                Response respone = toServer.newCall(request).execute();
                ResponseBody responseBody = respone.body();

                if (respone.isSuccessful()) {


                    return getWritingRequestAdot(new StringBuilder(responseBody.string()));
                }

            } catch (UnknownHostException une) {
                Log.e("fileUpLoad", une.toString());
            } catch (UnsupportedEncodingException uee) {
                Log.e("fileUpLoad", uee.toString());
            } catch (Exception e) {
                Log.e("fileUpLoad", e.toString());
            }
            return null;

        }

        @Override
        protected void onPostExecute(ArrayList<GetMainListEntity> result) {
            try {
                if (result != null && result.size() > 0) {

                    Adapter = new MainListRecyclerViewAdapter(owner, result);
                    r.setAdapter(Adapter);
                    Adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "글 목록 보기 정렬 되었습니다", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);

                } else {
                    Toast.makeText(owner.getApplicationContext(), ToastMessage.NOT_SEARCH_LIST, Toast.LENGTH_SHORT).show();
                    searchState = REGULAR_SEARCH;
                }

            } catch (Exception e) {
                Log.e("222222", "1111111");
            }
        }
    }


}