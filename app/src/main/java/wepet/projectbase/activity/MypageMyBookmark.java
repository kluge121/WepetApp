package wepet.projectbase.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import wepet.projectbase.GetContext;
import wepet.projectbase.R;
import wepet.projectbase.entity.MypageBookmarkObject;

public class MypageMyBookmark extends FontActivity implements BookmarkGeneralRecyclerViewDialog.CallbackListener {

    RecyclerView bookRecycler;

    private ImageView backBtn;

    public ImageView emptyWindow2;
    MypageMyBookmark activity = this;
    static String[] breed = {"기타", "말티즈", "미니핀", "요크셔", "치와와", "푸들", "닥스훈트", "슈나우저", "비숑", "시츄", "포메라니안",
            "불독", "불테리어", "비글", "코카", "진돗개", "풍산개", "시바견", "웰시코기", "리트리버", "말라뮤트", "허스키", "세퍼트", "로트바일러", "믹스견"};
    static String[] region = {"전체", "서울", "경기", "인천", "강원", "대전", "세종", "충남", "충북", "부산", "울산", "경남", "경북", "대구", "광주", "전남", "전북", "제주도"};
    static String[] state1 = {"", "공고중", "주인을 찾았어요", "안락사", "기간만료"};
    static String[] gender = {"", "수컷", "암컷", "gender err"};

    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    ArrayList<MypageBookmarkObject> data1 = new ArrayList<MypageBookmarkObject>();
    ArrayList<MypageBookmarkObject> tempData1 = new ArrayList<MypageBookmarkObject>();
    String responseResultValue;

    @Override
    public void whenDelete() {
        Log.e("체크", "새로고침확인");
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_book);
        emptyWindow2 = (ImageView) findViewById(R.id.empty_window);

        new AsyncPageBookMarkInsert().execute();

        CheckBox btncheck = (CheckBox) findViewById(R.id.btncheck);
        TextView txtDelete = (TextView) findViewById(R.id.txtDelete);

        Toolbar toolbar = (Toolbar) findViewById(R.id.bookmarkToolbar);
        toolbar.setContentInsetsAbsolute(0, 0);

        backBtn = (ImageView) findViewById(R.id.bookmarkBack);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bookRecycler = (RecyclerView) findViewById(R.id.bmRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        bookRecycler.setHasFixedSize(true);
        bookRecycler.setLayoutManager(layoutManager);


    }

    public class AsyncPageBookMarkInsert extends AsyncTask<MypageBookmarkObject, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(MypageBookmarkObject... mypageBookmarkObjects) {

            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            Request request = new Request.Builder() //보내는 곳 설정
                    .url("http://52.78.104.95:3000/users/" + PropertyManager.getInstance().getUid() + "/bookmarks")
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();

            try {
                Response response = okHttpClient.newCall(request).execute();
                JSONObject responsedJson = null;
                JSONArray responsedArray = null;

                try {
                    responsedJson = new JSONObject(response.body().string());
                    responsedArray = responsedJson.getJSONArray("data");
                    for (int i = 0; i < responsedArray.length(); i++) {

                        MypageBookmarkObject object = new MypageBookmarkObject();

                        object.breed = responsedArray.getJSONObject(i).getInt("breed");
                        object.uid = responsedArray.getJSONObject(i).getString("uid");
                        object.bookmark_num = responsedArray.getJSONObject(i).getInt("bookmark_num");
                        object.category = responsedArray.getJSONObject(i).getInt("category");
                        object.date = responsedArray.getJSONObject(i).getString("date");
                        object.gender = responsedArray.getJSONObject(i).getInt("gender");
                        object.img_thumb = responsedArray.getJSONObject(i).getString("img_thumb");
                        object.pid = responsedArray.getJSONObject(i).getInt("pid");
                        object.reply_num = responsedArray.getJSONObject(i).getInt("reply_num");
                        object.region = responsedArray.getJSONObject(i).getInt("region");
                        object.state = responsedArray.getJSONObject(i).getInt("state");

                        tempData1.add(object);
                    }
                } catch (Exception e) {
                    Log.e("dlog", e.toString());
                }
            } catch (Exception e) {

            }
            return responseResultValue;
        }

        @Override
        protected void onPostExecute(String responseResultValue) {
            data1 = tempData1;
            bookRecycler.setAdapter(new Mypage_Bookmark_Adapter(data1));
            super.onPostExecute(responseResultValue);
        }
    }


    class Mypage_Bookmark_Adapter extends RecyclerView.Adapter<Mypage_Bookmark_Adapter.ViewHolder> {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        ArrayList<MypageBookmarkObject> items;


        public Mypage_Bookmark_Adapter(ArrayList<MypageBookmarkObject> items) {

            this.items = items;
        }


        @Override
        public Mypage_Bookmark_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_recycler_main, parent, false);
            Drawable alpha = v.findViewById(R.id.shape).getBackground();
            alpha.setAlpha(140);
            return new ViewHolder(v);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final MypageBookmarkObject valueObj = items.get(position);
            holder.setView(items.get(position));
            Drawable alpha = holder.itemout.getBackground();

            if (valueObj.state != 1) {
                holder.itemCorrnetStat.setText(state1[valueObj.state]);
                holder.itemCorrnetStat.setVisibility(View.VISIBLE);
                holder.itemout.setVisibility(View.VISIBLE);
                alpha.setAlpha(200);
            } else {
                holder.itemout.setVisibility(View.GONE);
            }

            switch (valueObj.state) {
                case 2:
                    holder.itemflower.setVisibility(View.GONE);
                    // alpha.setAlpha(200);
                    break;
                case 3:
                    //holder.itemout.setVisibility(View.VISIBLE);
                    holder.itemflower.setVisibility(View.VISIBLE);
                    // alpha.setAlpha(200);
                    break;
                case 4:
                    //holder.itemout.setVisibility(View.VISIBLE);
                    // holder.itemCorrnetStat.setVisibility(View.VISIBLE);
                    holder.itemflower.setVisibility(View.GONE);
                    ;
                    break;
            }
            holder.writerDel.setVisibility(View.VISIBLE);
            Glide.with(GetContext.getContext()).load(items.get(position).img_thumb).into(holder.itemWindowImage); //픽셀값으로


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailPage.class);
                    intent.putExtra("pid", items.get(position).pid);
                    intent.putExtra("uid", items.get(position).uid);
                    view.getContext().startActivity(intent);
                }
            });

            holder.writerDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    BookmarkGeneralRecyclerViewDialog dialog = new BookmarkGeneralRecyclerViewDialog(mContext, items.get(position).pid, activity);
                    dialog.show();

                }
            });

        }

        Mypage_Bookmark_Adapter adapter = this;


        @Override
        public int getItemCount() {
            if (this.items.size() == 0) {
                emptyWindow2.setVisibility(View.VISIBLE);
            } else {
                emptyWindow2.setVisibility(View.INVISIBLE);
            }
            return this.items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {


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
            public final ImageView writerDel;


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
                writerDel = (ImageView) view.findViewById(R.id.booklist_del);

            }

            public void setView(MypageBookmarkObject data) {

                this.itemExplantionKindText.setText(breed[data.breed]);
                this.itemExplantionUniText.setText(gender[data.gender]);
                this.itemExplantionLocationText.setText(region[data.region]);
                this.itemBookMarkNum.setText(data.bookmark_num.toString());
                this.itemRecommetNum.setText(data.reply_num.toString());
                this.itemCorrnetStat.setText("");
            }
        }




    }

    private Context mContext = this;
}
