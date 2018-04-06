package wepet.projectbase.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import wepet.projectbase.GetContext;
import wepet.projectbase.R;

/**
 * Created by minsoo on 2016-08-25.
 */
public class RecommentGeneralRecyclerViewDialog extends Dialog {

    public Dialog dialog = this;

    static int pid;
    static int rid;
    MypageMyComment activity;
    RelativeLayout del_btn;
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public interface notifyData {
        void notifyData(MyWritingActivity.WritingAdapter adapter);
    }


    public RecommentGeneralRecyclerViewDialog(Context context, int pid, int rid, MypageMyComment activity) {

        super(context);
        this.pid = pid;
        this.mCallback = (CallbackListener) context;
        this.rid = rid;
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
        setContentView(R.layout.general_remove_dialog);

        del_btn = (RelativeLayout)findViewById(R.id.outlayout1);

        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("다이얼로그", "글 삭제");
                new AsyncCommentDel().execute(pid,rid);
                dialog.dismiss();
                if (mCallback != null)
                    mCallback.whenDelete();

            }
        });



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
            Toast.makeText(GetContext.getContext(), "글이 종결 되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
