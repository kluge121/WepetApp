package wepet.projectbase.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import okhttp3.Response;
import okhttp3.ResponseBody;
import wepet.projectbase.GetContext;
import wepet.projectbase.R;
import wepet.projectbase.activity.DetailPage;
import wepet.projectbase.activity.IndividualProfile;
import wepet.projectbase.activity.PropertyManager;
import wepet.projectbase.entity.GetDetailRecommentEntity;

/**
 * Created by ccei on 2016-07-27.
 */
public class RecommentList extends Fragment {

    static DetailPage owner;
    public static RecyclerView rv;
    public static String uid;
    public static int recommentSize;
    public static LinearLayoutManager linearLayoutManager;
    static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    static int commentDelRid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        new AsyncCommentGet().execute(DetailPage.pid.toString());
        owner = (DetailPage) getActivity();
        View v = inflater.inflate(R.layout.recomment_fragment, container, false);
        rv = (RecyclerView) v.findViewById(R.id.recommentListRecylcerview);
        linearLayoutManager = new LinearLayoutManager(GetContext.getContext());
        rv.setLayoutManager(linearLayoutManager);


        return v;

    }


    public static int getRecommentSize() {
        return recommentSize;
    }


    public static class RecommnetListRecyclerViewAdapter extends RecyclerView.Adapter<RecommnetListRecyclerViewAdapter.ViewHolder> {


        ArrayList<GetDetailRecommentEntity> recommentList = new ArrayList<GetDetailRecommentEntity>();


        public interface DeleteRecommentCallback {
            void DeleteRecommentCallback(ArrayList<GetDetailRecommentEntity> list, GetDetailRecommentEntity object, RecommnetListRecyclerViewAdapter adapter);
        }

        public RecommnetListRecyclerViewAdapter(ArrayList<GetDetailRecommentEntity> recommentList) {
            this.recommentList = recommentList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommnet_list_recycler, parent, false);


            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.setView(recommentList.get(position));
            if (recommentList.get(position).profile_thumb.length() > 0) {
                Glide.with(GetContext.getContext()).load(recommentList.get(position).profile_thumb).into(holder.itemProfileImage); //픽셀값으로 변경
            }
//            else {
//                holder.itemProfileImage.setEnabled(false);
//            }


            holder.itemProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.itemProfileId.length() > 0) {
                        Intent intent = new Intent(view.getContext(), IndividualProfile.class);
                        intent.putExtra("uid", holder.tmpUid);

                        intent.putExtra("recommenter", 2);

                        ActivityCompat.startActivity(owner, intent, null);
                    }
                }
            });


            holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (PropertyManager.getInstance().getUid().equals(holder.tmpUid)) {
                        owner.DeleteRecommentCallback(recommentList, recommentList.get(position), adapter);
                    }
                    return false;
                }
            });


        }

        RecommnetListRecyclerViewAdapter adapter = this;


        public int getItemCount() {
            return recommentList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView itemProfileImage;
            public final TextView itemProfileId;
            public final TextView itemRecommentContent;
            public final TextView itemDate;
            public String imgUrl;
            public String tmpUid;

            public ViewHolder(View view) {
                super(view);
                view.setBackgroundResource(R.drawable.ripple_background);
                mView = view;
                itemProfileImage = (ImageView) view.findViewById(R.id.recomment_profile);
                itemProfileId = (TextView) view.findViewById(R.id.recomment_id);
                itemRecommentContent = (TextView) view.findViewById(R.id.recomment_content);
                itemDate = (TextView) view.findViewById(R.id.recomment_date);
            }

            public void setView(GetDetailRecommentEntity data) {
                imgUrl = data.profile_thumb;
                this.itemProfileId.setText(data.nickname);
                this.itemRecommentContent.setText(data.content);
                tmpUid = data.uid;
                this.itemDate.setText(data.date);

            }
        }


    }


    public class AsyncCommentGet extends AsyncTask<String, Void, JSONObject> {

        JSONObject jsonObject;
        JSONArray jsonArrayRecomment;
        ArrayList<GetDetailRecommentEntity> jsonRecommentList;

        @Override
        protected JSONObject doInBackground(String... mPid) {

            OkHttpClient toServer = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url("http://52.78.104.95:3000/posts/" + mPid[0])
                    .build();


            try {
                Response respone = toServer.newCall(request).execute();
                ResponseBody responseBody = respone.body();
                jsonObject = new JSONObject(responseBody.string());
            } catch (UnknownHostException une) {
                Log.e("ddddd1", une.toString());
            } catch (UnsupportedEncodingException uee) {
                Log.e("ddddd2", uee.toString());
            } catch (Exception e) {
                Log.e("ddddd3", e.toString());
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject buf) {

            jsonRecommentList = new ArrayList<GetDetailRecommentEntity>();
            try {
                if (buf != null && buf.length() > 0) {
                    jsonArrayRecomment = buf.getJSONArray("replies");

                    if (jsonArrayRecomment != null && jsonArrayRecomment.length() > 0) {
                        recommentSize = jsonArrayRecomment.length();
                        for (int i = 0; i < jsonArrayRecomment.length(); i++) {
                            JSONObject bufferObj = jsonArrayRecomment.getJSONObject(i);
                            GetDetailRecommentEntity getDetailRecommentEntity = new GetDetailRecommentEntity();
                            getDetailRecommentEntity.pid = bufferObj.getInt("pid");
                            getDetailRecommentEntity.rid = bufferObj.getInt("rid");
                            commentDelRid = bufferObj.getInt("rid");
                            getDetailRecommentEntity.uid = bufferObj.getString("uid");
                            getDetailRecommentEntity.nickname = bufferObj.getString("nickname");
                            getDetailRecommentEntity.profile_thumb = bufferObj.getString("profile_thumb");
                            getDetailRecommentEntity.date = bufferObj.getString("date");
                            getDetailRecommentEntity.content = bufferObj.getString("content");
                            jsonRecommentList.add(getDetailRecommentEntity);

                        }

                    } else {
                        GetDetailRecommentEntity getDetailRecommentEntity = new GetDetailRecommentEntity();
                        getDetailRecommentEntity.pid = null;
                        getDetailRecommentEntity.uid = null;
                        getDetailRecommentEntity.nickname = "";
                        getDetailRecommentEntity.profile_thumb = "";
                        getDetailRecommentEntity.date = "";
                        getDetailRecommentEntity.content = "첫 댓글을 달아보세요!";
                        jsonRecommentList.add(getDetailRecommentEntity);
                    }
                }
            } catch (Exception e) {
                Log.e("tag글상세보기", "JSON파싱 중 에러발생", e);

            }
            if (jsonRecommentList != null && jsonRecommentList.size() > 0) {
                rv.setAdapter(new RecommnetListRecyclerViewAdapter(jsonRecommentList));
            }

        }
    }


}
