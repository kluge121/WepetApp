package wepet.projectbase.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import wepet.projectbase.GetContext;
import wepet.projectbase.R;
import wepet.projectbase.entity.MypageDataObject;

public class MypageMyComment extends FontActivity implements RecommentGeneralRecyclerViewDialog.CallbackListener{

    RecyclerView CommentRecycler;
    boolean selection = false;
    private ImageButton backBtn;
    ArrayList<MypageDataObject> tempData2 = new ArrayList<MypageDataObject>();
    ArrayList<MypageDataObject> data = new ArrayList<MypageDataObject>();
    String responseResultValue;
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    ImageView emptyWindow1;
    MypageMyComment activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_recomment);
        emptyWindow1 = (ImageView)findViewById(R.id.empty_window1);
        new AsynMyPageDataInsert().execute();

        CheckBox btncheck = (CheckBox) findViewById(R.id.btncheck);
        TextView txtDelete = (TextView) findViewById(R.id.txtDelete);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setContentInsetsAbsolute(0, 0);

        backBtn = (ImageButton) findViewById(R.id.profile_recommet_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        CommentRecycler = (RecyclerView) findViewById(R.id.recyclerView);

        //아이템을 배치할 레이아웃 매니저를 등록한다.
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        CommentRecycler.setHasFixedSize(true);
        CommentRecycler.setLayoutManager(layoutManager);


    }

    @Override
    public void whenDelete() {
        Log.e("체크","새로고침확인");
        finish();
        startActivity(getIntent());
    }
    public class AsynMyPageDataInsert extends AsyncTask<MypageDataObject, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(MypageDataObject... mypageDataObjects) {

            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            Request request = new Request.Builder() //보내는 곳 설정
                    .url("http://52.78.104.95:3000/users/" + PropertyManager.getInstance().getUid() + "/replies")
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
                        MypageDataObject object = new MypageDataObject();

                        object.content = responsedArray.getJSONObject(i).getString("content");
                        object.date = responsedArray.getJSONObject(i).getString("date");
                        object.pid = responsedArray.getJSONObject(i).getInt("pid");
                        object.rid = responsedArray.getJSONObject(i).getInt("rid");

                        tempData2.add(object);
                    }
                } catch (Exception e) {
                    Log.e("jsonerr", e.toString());
                }
            } catch (Exception e) {

            }
            return responseResultValue;
        }

        @Override
        protected void onPostExecute(String responseResultValue) {
            data = tempData2;
            CommentRecycler.setAdapter(new MyPage_Recomment_Adapter(data));
            super.onPostExecute(responseResultValue);
        }
    }

    public class AsyncCommentDel extends AsyncTask<Integer, Void, String> {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2;

        @Override
        protected String doInBackground(Integer... params) {

            OkHttpClient client = new OkHttpClient();

            try {
                jsonObject.put("msg", "success");
                jsonObject.put("uid", PropertyManager.getInstance().getUid());
            } catch (Exception e) {

            }
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());


            Request request = new Request.Builder()
                    .url("http://52.78.104.95:3000/posts/" + params[0] + "/replies/" + params[1])
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


    class MyPage_Recomment_Adapter extends RecyclerView.Adapter<MyPage_Recomment_Adapter.ViewHolder> {

        ArrayList<MypageDataObject> items;

        public MyPage_Recomment_Adapter(ArrayList<MypageDataObject> items) {
            this.items = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_recycler_recomment, null);
            return new ViewHolder(v);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.setView(items.get(position));
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailPage.class);
                    intent.putExtra("pid", items.get(position).pid);
                    view.getContext().startActivity(intent);
                }
            });

            holder.recommentDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RecommentGeneralRecyclerViewDialog dialog = new RecommentGeneralRecyclerViewDialog(mContext,items.get(position).pid,items.get(position).rid,activity);
                    dialog.show();
                }
            });


        }

        @Override
        public int getItemCount() {
            if(this.items.size()==0){
                emptyWindow1.setVisibility(View.VISIBLE);
            }else{
                emptyWindow1.setVisibility(View.INVISIBLE);
            }
            return this.items.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            public int pid;
            public int uid;
            public final View mView;
            TextView commentInfo;
            public ImageView recommentDel;

            public ViewHolder(View itemView) {
                super(itemView);
                commentInfo = (TextView) itemView.findViewById(R.id.recomment_content);
                mView = itemView;
                recommentDel = (ImageView) itemView.findViewById(R.id.recomment_del);

            }

            public void setView(MypageDataObject data) {
                this.commentInfo.setText(data.content);
            }
        }
    }
    private Context mContext = this;
}
























