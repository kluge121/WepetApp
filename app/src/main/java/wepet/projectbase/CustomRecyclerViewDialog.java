package wepet.projectbase;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import wepet.projectbase.activity.MyWritingActivity;
import wepet.projectbase.activity.PropertyManager;

/**
 * Created by minsoo on 2016-08-25.
 */
public class CustomRecyclerViewDialog extends Dialog {

    public Dialog dialog = this;

    public static String[] title = { "글 종결", "글 삭제"};
    public static LinearLayoutManager linearLayoutManager;
    RecyclerView rv;
    static int pid;
    static int category;
    MyWritingActivity activity;


    public interface notifyData {
        void notifyData(MyWritingActivity.WritingAdapter adapter);
    }


    public CustomRecyclerViewDialog(Context context, int pid, int category, MyWritingActivity activity) {

        super(context);
        this.pid = pid;
        this.mCallback = (CallbackListener) context;
        this.category = category;
        this.activity = activity;
    }

    private static CallbackListener mCallback = null;

    public interface CallbackListener {
        void whenDelete();
    }

    public static void setDeleteCallback(CallbackListener callbackListener) {
        mCallback = callbackListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_dialog_activty);
        rv = (RecyclerView) findViewById(R.id.mainListRecyclerview);
        linearLayoutManager = new LinearLayoutManager(GetContext.getContext());
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(new DialogListAdapter());

    }


    public class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.ViewHolder> {


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recycler_dialog, parent, false);
            return new ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.setView(title[position]);
            if (position == 1) {
                holder.divider.setVisibility(View.INVISIBLE);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    if (position == 0) {
                                                        Log.e("다이얼로그", "글 종결");
                                                        new AsyncStateChange().execute(pid, 2);// state 2로 체인지;
                                                        if (mCallback != null)
                                                            mCallback.whenDelete();
                                                        dialog.dismiss();


                                                    }
//                                                    else if (position == 0) {
//                                                        Log.e("다이얼로그", "글 수정");
//                                                        Intent intent = new Intent(getContext(), EditTabActivity.class);
//                                                        intent.putExtra("pid", pid);
//                                                        intent.putExtra("category",category);
//                                                        activity.startActivity(intent);
//                                                        dialog.dismiss();
//
//
//                                                    }
                                                    else if (position == 1) {
                                                        Log.e("다이얼로그", "글 삭제");
                                                        new AsyncWriteDel().execute(pid);
                                                        dialog.dismiss();
                                                        if (mCallback != null)
                                                            mCallback.whenDelete();


                                                    }

                                                }
                                            }

            );
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView itemList;
            public final ImageView divider;

            public ViewHolder(View view) {
                super(view);
                view.setBackgroundResource(R.drawable.ripple_background);
                mView = view;
                itemList = (TextView) view.findViewById(R.id.recycler_item_title);
                divider = (ImageView) view.findViewById(R.id.divider_dialog);
            }

            public void setView(String data) {
                this.itemList.setText(data);

            }
        }


    }

    public static class AsyncWriteDel extends AsyncTask<Integer, Void, String> {

        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2;
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

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
                    .url("http://52.78.104.95:3000/posts/" + pid[0])
                    .delete(body)
                    .build();
            String responsedStr;
            try {
                Response response = client.newCall(request).execute();
                responsedStr = response.body().string();
                jsonObject2 = new JSONObject(responsedStr);
                return jsonObject2.getString("msg");
            } catch (Exception e) {
                Log.e("MyWritingActivity", "내가 쓴글 삭제 어씽크 받아오던 중 오류");
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(GetContext.getContext(), "글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }


    public static class AsyncStateChange extends AsyncTask<Integer, Void, String> {

        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2;
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        @Override
        protected String doInBackground(Integer... PidANDState) {

            OkHttpClient client = new OkHttpClient();

            try {
                jsonObject1.put("pid", PidANDState[0]);
                jsonObject1.put("uid", PropertyManager.getInstance().getUid());
                jsonObject1.put("state", PidANDState[1]);

            } catch (Exception e) {
                Log.e("MyWritingActivity", "내가 쓴글 삭제 객체에 담 던 중 오류");
            }
            RequestBody body = RequestBody.create(JSON, jsonObject1.toString());
            Request request = new Request.Builder()
                    .url("http://52.78.104.95:3000/posts/" + PidANDState[0] + "/state")
                    .put(body)
                    .build();
            Log.e("어씽크체크", "state: " + PidANDState[1]);
            Log.e("어씽크체크", "http://52.78.104.95:3000/posts/" + PidANDState[0] + "/state");

            String responsedStr;
            try {
                Response response = client.newCall(request).execute();
                responsedStr = response.body().string();
                jsonObject2 = new JSONObject(responsedStr);
                return jsonObject2.getString("msg");
            } catch (Exception e) {
                Log.e("MyWritingActivity", "내가 쓴글 삭제 어씽크 받아오던 중 오류");
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(GetContext.getContext(),"글이 종결되었습니다." , Toast.LENGTH_SHORT).show();
        }
    }
}
