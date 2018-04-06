package wepet.projectbase.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
public class BookmarkGeneralRecyclerViewDialog extends Dialog {

    public Dialog dialog = this;

    public static String[] title = {"글 수정", "글 종결", "글 삭제"};
    public static LinearLayoutManager linearLayoutManager;
    static int pid;
    MypageMyBookmark activity;
    RelativeLayout del_btn;
    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public interface notifyData {
        void notifyData(MyWritingActivity.WritingAdapter adapter);
    }


    public BookmarkGeneralRecyclerViewDialog(Context context, int pid, MypageMyBookmark activity) {

        super(context);
        this.pid = pid;
        this.mCallback = (CallbackListener) context;
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
                new AsyncAddBookmark().execute(pid);
                dialog.dismiss();
                if (mCallback != null)
                    mCallback.whenDelete();

            }
        });



    }

    public class AsyncAddBookmark extends AsyncTask<Integer, Void, String> {


        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2;

        protected String doInBackground(Integer... pid) {
            OkHttpClient client = new OkHttpClient();


            try {
                jsonObject1.put("msg", "success");
                jsonObject1.put("uid", PropertyManager.getInstance().getUid());
            } catch (Exception e) {
                Log.e("DetailPage", "북마크 어씽크  객체에 담 던 중 오류");
            }

            RequestBody body = RequestBody.create(JSON, jsonObject1.toString());

            Request request = new Request.Builder()
                    .url("http://52.78.104.95:3000/posts/" + pid[0] + "/" + "bookmarks")
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
            Toast.makeText(GetContext.getContext(), "북마크가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

}
