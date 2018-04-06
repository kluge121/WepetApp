package wepet.projectbase.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import wepet.projectbase.CustomRecyclerViewDialog;
import wepet.projectbase.GetContext;
import wepet.projectbase.R;
import wepet.projectbase.entity.MypageWritingObject;

/**
 * Created by ccei on 2016-07-28.
 */
public class MyWritingActivity extends FontActivity implements CustomRecyclerViewDialog.CallbackListener{


    ImageView bookmarkBack;
    ArrayList<MypageWritingObject> data2 = new ArrayList<MypageWritingObject>();
    ArrayList<MypageWritingObject> tempData3 = new ArrayList<MypageWritingObject>();
    String responseResultValue;
    ImageView emptyWindow;


    boolean check = false;
    private RecyclerView writingRecycler;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    static String[] breed = {"기타", "말티즈", "미니핀", "요크셔", "치와와", "푸들", "닥스훈트", "슈나우저", "비숑", "시츄", "포메라니안",
            "불독", "불테리어", "비글", "코카", "진돗개", "풍산개", "시바견", "웰시코기", "리트리버", "말라뮤트", "허스키", "세퍼트", "로트바일러", "믹스견"};
    static String[] region = {"전체", "서울", "경기", "인천", "강원", "대전", "세종", "충남", "충북", "부산", "울산", "경남", "경북", "대구", "광주", "전남", "전북", "제주도"};
    static String[] state1 = {"", "공고중", "주인을 찾았어요", "안락사", "기간만료"};
    static String[] gender = {"", "수컷", "암컷", "gender err"};

    MyWritingActivity activity = this;

    @Override
    public void whenDelete() {
        Log.e("체크","새로고침확인");
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writing_recycler);
        emptyWindow = (ImageView)findViewById(R.id.empty_window3) ;
        new AsyncMypageWritingInsert().execute();
        bookmarkBack =(ImageView)findViewById(R.id.writingBack);
        Toolbar toolbar = (Toolbar)findViewById(R.id.writingToolbar);
        bookmarkBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        writingRecycler = (RecyclerView) findViewById(R.id.writingRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        writingRecycler.setHasFixedSize(true);
        writingRecycler.setLayoutManager(layoutManager);



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }




    public class AsyncMypageWritingInsert extends AsyncTask<MypageWritingObject, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(MypageWritingObject... mypageWritingObjects) {

            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            Request request = new Request.Builder() //보내는 곳 설정
                    .url("http://52.78.104.95:3000/users/"+ PropertyManager.getInstance().getUid()+"/posts")
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();

            try {
                Response response = okHttpClient.newCall(request).execute();
                JSONObject responsedJson = null;
                JSONArray responsedArray = null;

                try {
                    responsedJson = new JSONObject(response.body().string());
                    responsedArray = responsedJson.getJSONArray("data");
                    Log.e("dlog", responsedArray.length() + "");
                    for (int i = 0; i < responsedArray.length(); i++) {

                        MypageWritingObject object = new MypageWritingObject();

                        object.breed = responsedArray.getJSONObject(i).getInt("breed");
                        object.bookmark_num = responsedArray.getJSONObject(i).getInt("bookmark_num");
                        object.category = responsedArray.getJSONObject(i).getInt("category");
                        object.date = responsedArray.getJSONObject(i).getString("date");
                        object.gender = responsedArray.getJSONObject(i).getInt("gender");
                        object.img_thumb = responsedArray.getJSONObject(i).getString("img_thumb");
                        object.pid = responsedArray.getJSONObject(i).getInt("pid");
                        object.reply_num = responsedArray.getJSONObject(i).getInt("reply_num");
                        object.region = responsedArray.getJSONObject(i).getInt("region");
                        object.state = responsedArray.getJSONObject(i).getInt("state");

                        tempData3.add(object);
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
            data2 = tempData3;
            writingRecycler.setAdapter(new WritingAdapter(data2));
            super.onPostExecute(responseResultValue);
        }
    }

    public class WritingAdapter extends RecyclerView.Adapter<WritingAdapter.ViewHolder> {
        private ArrayList<MypageWritingObject> mDataset;
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

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
                writerDel = (ImageView)view.findViewById(R.id.booklist_del);

            }

            public void setView(MypageWritingObject data) {

                this.itemExplantionKindText.setText(breed[data.breed]);
                this.itemExplantionUniText.setText(gender[data.gender]);
                this.itemExplantionLocationText.setText(region[data.region]);
                this.itemBookMarkNum.setText(data.bookmark_num.toString());
                this.itemRecommetNum.setText(data.reply_num.toString());
                this.itemCorrnetStat.setText("");
            }
        }

        public WritingAdapter(ArrayList<MypageWritingObject> myWritingDataset) {
            mDataset = myWritingDataset;
        }

        @Override
        public WritingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_recycler_main, parent, false);
            Drawable alpha = v.findViewById(R.id.shape).getBackground();
            alpha.setAlpha(140);
            return new ViewHolder(v);
        }



        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final MypageWritingObject valueObj = mDataset.get(position);
            holder.setView(mDataset.get(position));
            Drawable alpha = holder.itemout.getBackground();

            if (valueObj.state != 1) {
                holder.itemCorrnetStat.setText(state1[valueObj.state]);
                holder.itemCorrnetStat.setVisibility(View.VISIBLE);
                holder.itemout.setVisibility(View.VISIBLE);
                alpha.setAlpha(200);
            }else {
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

                    break;
            }
            holder.writerDel.setVisibility(View.VISIBLE);
            Glide.with(GetContext.getContext()).load(mDataset.get(position).img_thumb).into(holder.itemWindowImage);  //픽셀값으로 변경



            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailPage.class);
                    intent.putExtra("pid", mDataset.get(position).pid);
                    view.getContext().startActivity(intent);
                }
            });

            holder.writerDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    CustomRecyclerViewDialog dialog = new CustomRecyclerViewDialog(mContext,mDataset.get(position).pid,mDataset.get(position).category,activity);
                    dialog.show();
                }
            });
        }


        @Override
        public int getItemCount() {
            if(this.mDataset.size()==0){
                emptyWindow.setVisibility(View.VISIBLE);
            }else{
                emptyWindow.setVisibility(View.INVISIBLE);
            }
            return this.mDataset.size();
        }


        public class AsyncWriteDel extends AsyncTask<Integer,Void,String>{

            JSONObject jsonObject1 = new JSONObject();
            JSONObject jsonObject2;

            @Override
            protected String doInBackground(Integer... pid) {

                OkHttpClient client = new OkHttpClient();

                try {
                    jsonObject1.put("msg", "success");
                    jsonObject1.put("uid", PropertyManager.getInstance().getUid());
                } catch (Exception e) {
                    Log.e("MyWritingActivity", "내가 쓴글 삭제 객체에 담 던 중 오류");
                }
                RequestBody body = RequestBody.create(JSON, jsonObject1.toString());
                Request request = new Request.Builder()
                        .url("http://52.78.104.95:3000/posts/"+pid[0])
                        .delete(body)
                        .build();
                String responsedStr;
                try {
                    Response response = client.newCall(request).execute();
                    responsedStr = response.body().string();
                    jsonObject2 = new JSONObject(responsedStr);
                    return jsonObject2.getString("msg");
                }catch (Exception e) {
                    Log.e("MyWritingActivity", "내가 쓴글 삭제 어씽크 받아오던 중 오류");
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(GetContext.getContext(),s,Toast.LENGTH_SHORT).show();
            }
        }



    }
    private Context mContext = this;

}
