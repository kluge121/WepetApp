package wepet.projectbase.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import wepet.projectbase.activity.DetailPage;
import wepet.projectbase.activity.IndividualProfile;
import wepet.projectbase.entity.OtherProfileBookObject;
import wepet.projectbase.entity.OtherProfileCmtObject;
import wepet.projectbase.entity.OtherProfileWriteObject;


public class IndividualProfileFragment extends Fragment {

    RecyclerView recyclerView;
    MultiRecyclerViewAdapter mAdapter;
    static String uid;
    RelativeLayout outLayout;
    static IndividualProfile owner;
    static ImageView emptyBook1;
    static ImageView emptyComment2;
    static ImageView emptyWrite3;

    static String[] breed = {"기타", "말티즈", "미니핀", "요크셔", "치와와", "푸들", "닥스훈트", "슈나우저", "비숑", "시츄", "포메라니안",
            "불독", "불테리어", "비글", "코카", "진돗개", "풍산개", "시바견", "웰시코기", "리트리버", "말라뮤트", "허스키", "세퍼트", "로트바일러", "믹스견"};
    static String[] region = {"전체", "서울", "경기", "인천", "강원", "대전", "세종", "충남", "충북", "부산", "울산", "경남", "경북", "대구", "광주", "전남", "전북", "제주도"};
    static String[] state1 = {"", "공고중", "새 주인을 찾았어요", "안락사", "기간만료"};
    static String[] gender = {"", "수컷", "암컷", "gender err"};
    String responseResultValue2;


    public static ArrayList<OtherProfileBookObject> data5 = new ArrayList<OtherProfileBookObject>();
    public static ArrayList<OtherProfileCmtObject> data6 = new ArrayList<OtherProfileCmtObject>();
    public static ArrayList<OtherProfileWriteObject> data7 = new ArrayList<OtherProfileWriteObject>();

    ArrayList<OtherProfileBookObject> tempData1 = new ArrayList<OtherProfileBookObject>();
    ArrayList<OtherProfileCmtObject> tempData2 = new ArrayList<OtherProfileCmtObject>();
    ArrayList<OtherProfileWriteObject> tempData3 = new ArrayList<OtherProfileWriteObject>();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        uid = IndividualProfile.getUid();
        outLayout = (RelativeLayout) inflater.inflate(R.layout.individual_profile_fragment, container, false);
        recyclerView = (RecyclerView) outLayout.findViewById(R.id.individual_profile_fragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(GetContext.getContext()));
        emptyBook1 = (ImageView) outLayout.findViewById(R.id.empty_book);
        emptyComment2 = (ImageView) outLayout.findViewById(R.id.empty_comment);
        emptyWrite3 = (ImageView) outLayout.findViewById(R.id.empty_writer);
        new AsyncBookmarkEntity().execute();
        owner = (IndividualProfile) getActivity();
        //InitData();

        return outLayout;
    }

    public static class MultiRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int VIEW_TYPE_BOOK = 0;
        private static final int VIEW_TYPE_RECOMMNET = 1;
        private static final int VIEW_TYPE_WRITE = 2;


        CheckBox checkBox;
        View view;

        ArrayList<OtherProfileBookObject> list_bookmark = data5;
        ArrayList<OtherProfileCmtObject> list_recomment = data6;
        ArrayList<OtherProfileWriteObject> list_write = data7;

        public void add(OtherProfileBookObject data) {
            list_bookmark.add(data);
            notifyDataSetChanged();
            IndividualProfile.dCheckType = 0;
        }

        public void add(OtherProfileCmtObject data) {
            list_recomment.add(data);
            notifyDataSetChanged();
            IndividualProfile.dCheckType = 1;
        }

        public void add(OtherProfileWriteObject data) {
            list_write.add(data);
            notifyDataSetChanged();
            IndividualProfile.dCheckType = 2;
        }

        @Override
        public int getItemViewType(int position) {

            if (IndividualProfile.dCheckType == 0) {
                OtherProfileBookObject data = list_bookmark.get(position);
                return 0;
            } else if (IndividualProfile.dCheckType == 1) {
                OtherProfileCmtObject data = list_recomment.get(position);
                return 1;
            } else if (IndividualProfile.dCheckType == 2) {
                OtherProfileWriteObject data = list_write.get(position);
                return 2;
            }

            return super.getItemViewType(position);
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Drawable alpha = null;
            View view = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            switch (viewType) {
                case VIEW_TYPE_BOOK:
                    view = inflater.inflate(R.layout.main_list_recycler_main, parent, false);
                    alpha = view.findViewById(R.id.shape).getBackground();
                    alpha.setAlpha(140);
                    return new IndividualBookHolder(view);

                case VIEW_TYPE_RECOMMNET:
                    view = inflater.inflate(R.layout.individual_list_recycler_recomment, parent, false);
                    return new IndividualRecommentHolder(view);

                case VIEW_TYPE_WRITE:
                    view = inflater.inflate(R.layout.main_list_recycler_main, parent, false);
                    alpha = view.findViewById(R.id.shape).getBackground();
                    alpha.setAlpha(140);
                    return new IndividualWriteHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            switch (getItemViewType(position)) {

                case VIEW_TYPE_BOOK:
                    ((IndividualBookHolder) holder).setView(data5.get(position));
                    Glide.with(GetContext.getContext()).load(data5.get(position).img_thumb).into(((IndividualBookHolder) holder).itemWindowImage);

                    final OtherProfileBookObject valueObj = data5.get(position);
                    ((IndividualBookHolder) holder).setView(data5.get(position));
                    Drawable alpha = ((IndividualBookHolder) holder).itemout.getBackground();

                    if (valueObj.state != 1) {
                        ((IndividualBookHolder) holder).itemCorrnetStat.setText(state1[valueObj.state]);
                        ((IndividualBookHolder) holder).itemCorrnetStat.setVisibility(View.VISIBLE);
                        ((IndividualBookHolder) holder).itemout.setVisibility(View.VISIBLE);
                        alpha.setAlpha(200);
                    }else {
                        ((IndividualBookHolder) holder).itemout.setVisibility(View.GONE);
                    }

                    switch (valueObj.state) {
                        case 2:
                            ((IndividualBookHolder) holder).itemflower.setVisibility(View.GONE);
                            // alpha.setAlpha(200);
                            break;
                        case 3:
                            //holder.itemout.setVisibility(View.VISIBLE);
                            ((IndividualBookHolder) holder).itemflower.setVisibility(View.VISIBLE);
                            // alpha.setAlpha(200);
                            break;
                        case 4:
                            //holder.itemout.setVisibility(View.VISIBLE);
                            // holder.itemCorrnetStat.setVisibility(View.VISIBLE);
                            ((IndividualBookHolder) holder).itemflower.setVisibility(View.GONE);

                            break;
                    }

                    ((IndividualBookHolder) holder).mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(view.getContext(), DetailPage.class);
                            intent.putExtra("pid", data5.get(position).pid);
                            owner.startActivity(intent);

                        }
                    });
                    break;

                case VIEW_TYPE_RECOMMNET:
                    ((IndividualRecommentHolder) holder).setView(data6.get(position));

                    ((IndividualRecommentHolder) holder).mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(view.getContext(), DetailPage.class);
                            intent.putExtra("pid", data6.get(position).pid);
                            owner.startActivity(intent);
                        }
                    });
                    break;

                case VIEW_TYPE_WRITE:
                    ((IndividualWriteHolder) holder).setView(data7.get(position));

                    final OtherProfileWriteObject valueObj1 = data7.get(position);
                    ((IndividualWriteHolder) holder).setView(data7.get(position));
                    Drawable alpha2 = ((IndividualWriteHolder) holder).itemout.getBackground();

                    if (valueObj1.state != 1) {
                        ((IndividualWriteHolder) holder).itemCorrnetStat.setText(state1[valueObj1.state]);
                        ((IndividualWriteHolder) holder).itemCorrnetStat.setVisibility(View.VISIBLE);
                        ((IndividualWriteHolder) holder).itemout.setVisibility(View.VISIBLE);
                        alpha2.setAlpha(200);
                    }else {
                        ((IndividualWriteHolder) holder).itemout.setVisibility(View.GONE);
                    }

                    switch (valueObj1.state) {
                        case 2:
                            ((IndividualWriteHolder) holder).itemflower.setVisibility(View.GONE);
                            // alpha.setAlpha(200);
                            break;
                        case 3:
                            //holder.itemout.setVisibility(View.VISIBLE);
                            ((IndividualWriteHolder) holder).itemflower.setVisibility(View.VISIBLE);
                            // alpha.setAlpha(200);
                            break;
                        case 4:
                            //holder.itemout.setVisibility(View.VISIBLE);
                            // holder.itemCorrnetStat.setVisibility(View.VISIBLE);
                            ((IndividualWriteHolder) holder).itemflower.setVisibility(View.GONE);

                            break;
                    }


                    Glide.with(GetContext.getContext()).load(data7.get(position).img_thumb).into(((IndividualWriteHolder) holder).itemWindowImage); //픽셀값으로 변경
                    ((IndividualWriteHolder) holder).mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(view.getContext(), DetailPage.class);
                            intent.putExtra("pid", data7.get(position).pid);
                            owner.startActivity(intent);
                        }
                    });
                    break;

            }
        }

        @Override
        public int getItemCount() {
            if (IndividualProfile.dCheckType == 0) {
                if (list_bookmark.size() == 0) {
                    emptyBook1.setVisibility(View.VISIBLE);
                    emptyComment2.setVisibility(View.INVISIBLE);
                    emptyWrite3.setVisibility(View.INVISIBLE);
                } else {
                    emptyBook1.setVisibility(View.INVISIBLE);
                }
                return list_bookmark.size();
            } else if (IndividualProfile.dCheckType == 1) {
                if (list_recomment.size() == 0) {
                    emptyBook1.setVisibility(View.INVISIBLE);
                    emptyComment2.setVisibility(View.VISIBLE);
                    emptyWrite3.setVisibility(View.INVISIBLE);
                } else {
                    emptyComment2.setVisibility(View.INVISIBLE);
                }
                return list_recomment.size();
            } else if (IndividualProfile.dCheckType == 2) {
                if (list_write.size() == 0) {
                    emptyBook1.setVisibility(View.INVISIBLE);
                    emptyComment2.setVisibility(View.INVISIBLE);
                    emptyWrite3.setVisibility(View.VISIBLE);
                } else {
                    emptyWrite3.setVisibility(View.INVISIBLE);
                }
                return list_write.size();
            }
            return 0;
        }

        //Multi ViewHolder class
        public class IndividualBookHolder extends RecyclerView.ViewHolder {

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


            public IndividualBookHolder(View view) {
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

            public void setView(OtherProfileBookObject data) {

                this.itemExplantionKindText.setText(breed[data.breed]);
                this.itemExplantionUniText.setText(gender[data.gender]);
                this.itemExplantionLocationText.setText(region[data.region]);
                this.itemBookMarkNum.setText(data.bookmark_num.toString());
                this.itemRecommetNum.setText(data.reply_num.toString());
                this.itemCorrnetStat.setText("");
            }
        }

        public class IndividualRecommentHolder extends RecyclerView.ViewHolder {

            public final View mView;
            public final TextView itemRecommentContext;


            public IndividualRecommentHolder(View mView) {
                super(mView);
                this.mView = mView;
                this.itemRecommentContext = (TextView) mView.findViewById(R.id.recomment_content);
            }

            public void setView(OtherProfileCmtObject data) {

                this.itemRecommentContext.setText(data.content + "");
            }
        }

        public class IndividualWriteHolder extends RecyclerView.ViewHolder {

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


            public IndividualWriteHolder(View view) {
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


            public void setView(OtherProfileWriteObject data) {

                this.itemExplantionKindText.setText(breed[data.breed]);
                this.itemExplantionUniText.setText(gender[data.gender]);
                this.itemExplantionLocationText.setText(region[data.region]);
                this.itemBookMarkNum.setText(data.bookmark_num.toString());
                this.itemRecommetNum.setText(data.reply_num.toString());
                this.itemCorrnetStat.setText("");
            }
        }
    }

    public class AsyncBookmarkEntity extends AsyncTask<OtherProfileBookObject, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(OtherProfileBookObject... otherProfileBookObjects) {

            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            Request request = new Request.Builder() //보내는 곳 설정
                    .url("http://52.78.104.95:3000/users/" + uid + "/bookmarks") //수정
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

                        OtherProfileBookObject object = new OtherProfileBookObject();

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

                        tempData1.add(object);
                    }
                } catch (Exception e) {
                    Log.e("dlog", e.toString());
                }
            } catch (Exception e) {

            }

            Request request2 = new Request.Builder() //보내는 곳 설정
                    .url("http://52.78.104.95:3000/users/" + uid + "/replies") //수정
                    .build();

            OkHttpClient okHttpClient2 = new OkHttpClient();

            try {
                Response response2 = okHttpClient2.newCall(request2).execute();
                JSONObject responsedJson = null;
                JSONArray responsedArray = null;

                try {
                    responsedJson = new JSONObject(response2.body().string());
                    responsedArray = responsedJson.getJSONArray("data");
                    Log.e("dlog", responsedArray.length() + "");
                    for (int i = 0; i < responsedArray.length(); i++) {

                        OtherProfileCmtObject object = new OtherProfileCmtObject();


                        object.date = responsedArray.getJSONObject(i).getString("date");
                        object.pid = responsedArray.getJSONObject(i).getInt("pid");
                        object.content = responsedArray.getJSONObject(i).getString("content");
                        object.rid = responsedArray.getJSONObject(i).getInt("rid");

                        tempData2.add(object);

                    }
                } catch (Exception e) {
                    Log.e("dlog", e.toString());
                }
            } catch (Exception e) {

            }

            Request request3 = new Request.Builder() //보내는 곳 설정
                    .url("http://52.78.104.95:3000/users/" + uid + "/posts") //수정
                    .build();

            OkHttpClient okHttpClient3 = new OkHttpClient();

            try {

                Response response3 = okHttpClient3.newCall(request3).execute();
                JSONObject responsedJson = null;
                JSONArray responsedArray = null;

                try {
                    responsedJson = new JSONObject(response3.body().string());
                    responsedArray = responsedJson.getJSONArray("data");
                    Log.e("dlog", responsedArray.length() + "");
                    for (int i = 0; i < responsedArray.length(); i++) {
                        OtherProfileWriteObject object = new OtherProfileWriteObject();

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
            Log.e("dlog", data5.size() + "");
            Log.e("dlog", data6.size() + "");
            Log.e("dlog", data7.size() + "");

            return responseResultValue2;
        }

        @Override
        protected void onPostExecute(String s) {

            data5 = tempData1;
            data6 = tempData2;
            data7 = tempData3;

            mAdapter = new MultiRecyclerViewAdapter();
            recyclerView.setAdapter(mAdapter);

            super.onPostExecute(s);
        }
    }
}























