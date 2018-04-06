package wepet.projectbase.Manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import wepet.projectbase.activity.PropertyManager;

public class NetworkManager {
    private static final String TAG ="NetworkManager";
    private static NetworkManager instance;

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    OkHttpClient mClient;
    private static final int MAX_CACHE_SIZE = 10 * 1024 * 1024;

    private NetworkManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        Context context = MyApplication.getContext();

//        File cachefile = new File(context.getExternalCacheDir(), "mycache");
//        if (!cachefile.exists()) {
//            cachefile.mkdirs();
//        }
//        Cache cache = new Cache(cachefile, MAX_CACHE_SIZE);
//        builder.cache(cache);
//
//        CookieManager cookieManager = new CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL);
//        builder.cookieJar(new JavaNetCookieJar(cookieManager));


//        disableCertificateValidation(context, builder);

        mClient = builder.build();
    }



    public void cancelAll() {
        mClient.dispatcher().cancelAll();
    }

    public void cancelTag(Object tag) {
        Dispatcher dispatcher = mClient.dispatcher();
        List<Call> calls = dispatcher.queuedCalls();
        for (Call call : calls) {
            if (call.request().tag().equals(tag)) {
                call.cancel();
            }
        }
        calls = dispatcher.runningCalls();
        for (Call call : calls) {
            if (call.request().tag().equals(tag)) {
                call.cancel();
            }
        }
    }

    public interface OnResultListener<T> {
        public void onSuccess(Request request, T result);

        public void onFailure(Request request, int code, Throwable cause);
    }

    private static final int MESSAGE_SUCCESS = 0;
    private static final int MESSAGE_FAILURE = 1;

    static class NetworkHandler extends Handler {
        public NetworkHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CallbackObject object = (CallbackObject) msg.obj;
            Request request = object.request;
            OnResultListener listener = object.listener;
            switch (msg.what) {
                case MESSAGE_SUCCESS:
                    listener.onSuccess(request, object.result);
                    break;
                case MESSAGE_FAILURE:
                    listener.onFailure(request, -1, object.exception);
                    break;
            }
        }
    }

    Handler mHandler = new NetworkHandler(Looper.getMainLooper());

    static class CallbackObject<T> {
        Request request;
        T result;
        IOException exception;
        OnResultListener<T> listener;
    }

    public void cancelAll(Object tag) {

    }


    private static  final MediaType MEDIA_TYPE = MediaType.parse("image/png");

    private static final String URL_USERS_POST = "http://52.78.104.95:3000/users";
    private static final String URL_AUTH_LOGIN_POST = "http://52.78.104.95:3000/auth/login";
    private static final String URL_POSTS_GET = "http://52.78.104.95:3000/posts?category=%s&gender=%s&age=%s&region=%s&neuter=%s&vaccin=%s&breed=%s&sort=%s&page=%s&count=%s";

    public Request joinUser(Context context, String uid, String uuid, String nickname, String phone, int region, File profile_img, final OnResultListener<MessageData> listener) throws UnsupportedEncodingException{
        String url = URL_USERS_POST;
        String token1;
        final CallbackObject<MessageData> callbackObject = new CallbackObject<MessageData>();

        token1 = PropertyManager.getInstance().getToken();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("uid", uid)
                .addFormDataPart("uuid", uuid)
                .addFormDataPart("nickname", nickname)
                .addFormDataPart("phone", phone)
                .addFormDataPart("token", token1)
                .addFormDataPart("region", ""+region);

        if (profile_img != null) {
            builder.addFormDataPart("profile_img", "photo.jpg", RequestBody.create(MEDIA_TYPE, profile_img));
        }

        RequestBody requestBody = builder.build();


        Request request = new Request.Builder().url(url)
                .tag(context)
                .post(requestBody)
                .build();


        callbackObject.request = request;
        callbackObject.listener = listener;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, String.valueOf(response));
                Gson parser = new Gson();
                MessageData result = parser.fromJson(response.body().string(),MessageData.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS,callbackObject);
                Log.e(TAG, msg.toString());
                mHandler.sendMessage(msg);
            }
        });
        return request;

    }

    public Request postAuthLogin(Context context, String uid, String uuid, final OnResultListener<MessageData> listener) throws UnsupportedEncodingException{
        String url = URL_AUTH_LOGIN_POST;

        final CallbackObject<MessageData> callbackObject = new CallbackObject<MessageData>();
        RequestBody requestBody = new FormBody.Builder()
                .add("uid", uid)
                .add("uuid",uuid)
                .build();

        Request request = new Request.Builder().url(url)
                .tag(context)
                .post(requestBody)
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                MessageData result = parser.fromJson(response.body().string(),MessageData.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS,callbackObject);
                mHandler.sendMessage(msg);
            }
        });
        return request;

    }

//    public Request getAllList(Context context, int page, final OnResultListener<ListDetailResult> listener)throws UnsupportedEncodingException {
//
//
//
//        String url = String.format(URL_ALL_CONTENT, page);
//
//        final CallbackObject<ListDetailResult> callbackObject = new CallbackObject<ListDetailResult>();
//
//        Request request = new Request.Builder().url(url)
//                .tag(context)
//                .build();
//
//        callbackObject.request = request;
//        callbackObject.listener = listener;
//        mClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                callbackObject.exception = e;
//                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
//                mHandler.sendMessage(msg);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Gson parser = new Gson();
//                String text = response.body().string();
//
//                ListDetailResult result = parser.fromJson(text, ListDetailResult.class);
//                callbackObject.result = result;
//                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
//                mHandler.sendMessage(msg);
//            }
//        });
//        return request;
//    }
    /*
    //
    private static  final MediaType MEDIA_TYPE = MediaType.parse("image/png");

    private static final String URL_FORMAT = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/members/me";

    private static final String URL_PROFILE_MEMBER = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/members/%s";

    // Matching and Story Tab URL
    private static final String URL_MATCHING_CONTENT = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts?page=%s&key=%s&flag=%s";

    private static final String URL_ALL_CONTENT = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts?page=%s";

    private static final String URL_STORY_WRITE = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts";

    private static final String URL_STORY_DELETE = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts/%s";

    private static final String URL_STORY_MODIFY = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts/%s";

    private static final String URL_MY_STROY_CONTENT = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts?page=%s&key=%s&flag=%s&mid=%s";

    private static final String URL_POST_CONTENT = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts?page=%s&pid=%s";


    // Comment URL
    private static final String URL_COMMENT_CONTENT = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts/%s/replies?page=%s";

    private static final String URL_COMMENT_WRITE = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts/%s/replies";

    private static final String URL_COMMENT_DELETE = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts/%s/replies/%s";

    private static final String URL_COMMENT_MODIFY = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/posts/%s/replies/%s";
    // SoundCloud URL


    // Local Login URL
    private static final String URL_SIGN_UP = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/members";

    private static final String URL_LOCAL_LOGIN = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/auth/login";

    private static final String URL_LOCAL_LOGOUT = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/auth/logout";

    //Login Function
    public Request logOut(Context context, final OnResultListener<LoginAndSignUpResult> listener) throws UnsupportedEncodingException {
        String url = URL_LOCAL_LOGIN;

        final CallbackObject<LoginAndSignUpResult> callbackObject = new CallbackObject<LoginAndSignUpResult>();


        Request request = new Request.Builder().url(url)
                .tag(context)
                .get()
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                LoginAndSignUpResult result = parser.fromJson(response.body().string(),LoginAndSignUpResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS,callbackObject);
                mHandler.sendMessage(msg);
            }
        });
        return request;
    }


    public Request logIn(Context context, String userName, String passWord , final OnResultListener<LoginAndSignUpResult> listener) throws UnsupportedEncodingException {
        String url = URL_LOCAL_LOGIN;

        final CallbackObject<LoginAndSignUpResult> callbackObject = new CallbackObject<LoginAndSignUpResult>();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", userName)
                .add("password",passWord)
                .build();

        Request request = new Request.Builder().url(url)
                .tag(context)
                .post(requestBody)
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                LoginAndSignUpResult result = parser.fromJson(response.body().string(),LoginAndSignUpResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS,callbackObject);
                mHandler.sendMessage(msg);
            }
        });
        return request;
    }

    public Request signUp(Context context, String emailAddress, String nickName, String passWord, final OnResultListener<LoginAndSignUpResult> listener) throws UnsupportedEncodingException {
        String url = URL_SIGN_UP;

        final CallbackObject<LoginAndSignUpResult> callbackObject = new CallbackObject<LoginAndSignUpResult>();

        RequestBody requestBody = new FormBody.Builder()
                .add("username", emailAddress)
                .add("nickname",nickName)
                .add("password",passWord)
                .build();

        Request request = new Request.Builder().url(url)
                .tag(context)
                .post(requestBody)
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                LoginAndSignUpResult result = parser.fromJson(response.body().string(),LoginAndSignUpResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS,callbackObject);
                mHandler.sendMessage(msg);
            }
        });
        return request;
    }


    // Story Function

     // Story modify
     public Request modifyStory(Context context, int pid, String contents, String photo, final OnResultListener<StoryWriteResult> listener) throws UnsupportedEncodingException {
         String url = String.format(URL_STORY_MODIFY,pid);

         final CallbackObject<StoryWriteResult> callbackObject = new CallbackObject<StoryWriteResult>();


         FormBody.Builder builder = new FormBody.Builder()
                 .add("content", contents);
         if(photo != null){
             builder.add("photo",photo);
         }
         RequestBody requestBody =  builder.build();


         Request request = new Request.Builder().url(url)
                 .tag(context)
                 .put(requestBody)
                 .build();


         callbackObject.request = request;
         callbackObject.listener = listener;

         mClient.newCall(request).enqueue(new Callback() {
             @Override
             public void onFailure(Call call, IOException e) {
                 callbackObject.exception = e;
                 Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                 mHandler.sendMessage(msg);
             }

             @Override
             public void onResponse(Call call, Response response) throws IOException {
                 Gson parser = new Gson();
                 StoryWriteResult result = parser.fromJson(response.body().string(),StoryWriteResult.class);
                 callbackObject.result = result;
                 Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS,callbackObject);
                 mHandler.sendMessage(msg);
             }
         });
         return request;

     }
    // Matching Modify
    public Request modifyMatching(Context context, int pid, String title, String contents, int limitPeo, int decidePeo, String photo, final OnResultListener<StoryWriteResult> listener) throws UnsupportedEncodingException {
        String url = String.format(URL_STORY_MODIFY,pid);

        final CallbackObject<StoryWriteResult> callbackObject = new CallbackObject<StoryWriteResult>();

        RequestBody requestBody = new FormBody.Builder()
                .add("content",contents)
                .add("title",title)
                .add("photo",photo)
                .add("limit_people", "" + limitPeo)
                .add("decide_people","" + decidePeo)
                .build();


        Request request = new Request.Builder().url(url)
                .tag(context)
                .put(requestBody)
                .build();


        callbackObject.request = request;
        callbackObject.listener = listener;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                StoryWriteResult result = parser.fromJson(response.body().string(),StoryWriteResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS,callbackObject);
                mHandler.sendMessage(msg);
            }
        });
        return request;

    }

    // Story delete
    public Request deleteStory(Context context, int pid,
                               final OnResultListener<StoryWriteResult> listener) throws UnsupportedEncodingException {
        String url = String.format(URL_STORY_DELETE, pid);
        final CallbackObject<StoryWriteResult> callbackObject = new CallbackObject<StoryWriteResult>();




        Request request = new Request.Builder().url(url)
                .tag(context)
                .delete()
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                StoryWriteResult result = parser.fromJson(response.body().string(),StoryWriteResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });

        return  request;
    }

    //  Story write

    public Request postStoryWrite(Context context, String title, String content, int limitPeo, int decidePeo, File file, final OnResultListener<StoryWriteResult> listener) throws UnsupportedEncodingException {
        String url = URL_STORY_WRITE;

        final CallbackObject<StoryWriteResult> callbackObject = new CallbackObject<StoryWriteResult>();


        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", title)
                .addFormDataPart("content", content)
                .addFormDataPart("limit_people", "" + limitPeo)
                .addFormDataPart("decide_people", "" + decidePeo);
        if (file != null) {
            builder.addFormDataPart("photo", "photo.jpg", RequestBody.create(MEDIA_TYPE, file));
        }

        RequestBody requestBody = builder.build();


        Request request = new Request.Builder().url(url)
                .tag(context)
                .post(requestBody)
                .build();


        callbackObject.request = request;
        callbackObject.listener = listener;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                StoryWriteResult result = parser.fromJson(response.body().string(),StoryWriteResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS,callbackObject);
                mHandler.sendMessage(msg);
            }
        });
        return request;

    }


    // Story Get

    public Request getMyStroyList(Context context, int page, String key, String people, int mid, final OnResultListener<ListDetailResult> listener) throws UnsupportedEncodingException {
        String url = String.format(URL_MY_STROY_CONTENT, page, URLEncoder.encode(key, "utf-8"), URLEncoder.encode(people, "utf-8"),mid);

        final CallbackObject<ListDetailResult> callbackObject = new CallbackObject<ListDetailResult>();

        Request request = new Request.Builder().url(url)
                .tag(context)
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                String text = response.body().string();

                ListDetailResult result = parser.fromJson(text, ListDetailResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });
        return request;
    }

    // Matching Get
    public Request getMatchingList(Context context, int page, String key, String people, final OnResultListener<ListDetailResult> listener) throws UnsupportedEncodingException {
        String url = String.format(URL_MATCHING_CONTENT, page, URLEncoder.encode(key,"utf-8"), URLEncoder.encode(people,"utf-8"));

        final CallbackObject<ListDetailResult> callbackObject = new CallbackObject<ListDetailResult>();

        Request request = new Request.Builder().url(url)
                .tag(context)
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                String text = response.body().string();

                ListDetailResult result = parser.fromJson(text, ListDetailResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });
        return request;
    }
    public Request getAllList(Context context, int page, final OnResultListener<ListDetailResult> listener)throws UnsupportedEncodingException {



        String url = String.format(URL_ALL_CONTENT, page);

        final CallbackObject<ListDetailResult> callbackObject = new CallbackObject<ListDetailResult>();

        Request request = new Request.Builder().url(url)
                .tag(context)
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                String text = response.body().string();

                ListDetailResult result = parser.fromJson(text, ListDetailResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });
        return request;
    }

    ************************************************************************************
    *


    // Comment Function
    public Request  modifyComment(Context context, int pid, int rid, String contents,
                                  final OnResultListener<CommentResult> listener) throws UnsupportedEncodingException {
        String url = String.format(URL_COMMENT_MODIFY,pid,rid);
        final CallbackObject<CommentResult> callbackObject = new CallbackObject<CommentResult>();


        RequestBody requestBody = new FormBody.Builder()
                .add("content",contents)
                .build();

        Request request = new Request.Builder().url(url)
                .tag(context)
                .put(requestBody)
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                CommentResult result = parser.fromJson(response.body().string(),CommentResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });

        return  request;
    }


    public Request deleteComment(Context context, int pid, int rid,
                                 final OnResultListener<CommentResult> listener) throws UnsupportedEncodingException {
        String url = String.format(URL_COMMENT_DELETE,pid,rid);
        final CallbackObject<CommentResult> callbackObject = new CallbackObject<CommentResult>();




        Request request = new Request.Builder().url(url)
                .tag(context)
                .delete()
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                CommentResult result = parser.fromJson(response.body().string(),CommentResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });

        return  request;
    }

    public Request postCommentWrite(Context context, int pid, String contents,
                                    final OnResultListener<CommentResult> listener) throws UnsupportedEncodingException {
        String url = String.format(URL_COMMENT_WRITE,pid);
        final CallbackObject<CommentResult> callbackObject = new CallbackObject<CommentResult>();


        RequestBody requestBody = new FormBody.Builder()
                .add("content",contents)
                .build();

        Request request = new Request.Builder().url(url)
                .tag(context)
                .post(requestBody)
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                CommentResult result = parser.fromJson(response.body().string(),CommentResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });

        return  request;
    }


    public Request getCommentDetail(Context context, int pid, int page, final OnResultListener<CommentDetailResult> listener) throws UnsupportedEncodingException {
       String url = String.format(URL_COMMENT_CONTENT,pid,page);

       final CallbackObject<CommentDetailResult> callbackObject = new CallbackObject<CommentDetailResult>();

       Request request = new Request.Builder().url(url)
               .tag(context)
               .build();

       callbackObject.request = request;
       callbackObject.listener = listener;

       mClient.newCall(request).enqueue(new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {
               callbackObject.exception = e;
               Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
               mHandler.sendMessage(msg);
           }

           @Override
           public void onResponse(Call call, Response response) throws IOException {
                Gson parser = new Gson();
                String text = response.body().string();
                CommentDetailResult result = parser.fromJson(text,CommentDetailResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
           }
       });
       return request;
   }

      ************************************************************************************


    // Profile Function
    public Request getMemberProfile(Context context, String mid, final OnResultListener<MemberProfileResult> listener) throws UnsupportedEncodingException {
        try {

            String url = String.format(URL_PROFILE_MEMBER, URLEncoder.encode(mid, "utf-8"));


            final CallbackObject<MemberProfileResult> callbackObject = new CallbackObject<MemberProfileResult>();

            Request request = new Request.Builder().url(url)
                    .tag(context)
                    .build();

            callbackObject.request = request;
            callbackObject.listener = listener;
            mClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callbackObject.exception = e;
                    Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                    mHandler.sendMessage(msg);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Gson parser = new Gson();
                    MemberProfileResult result = parser.fromJson(response.body().string(), MemberProfileResult.class);
                    callbackObject.result = result;
                    Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                    mHandler.sendMessage(msg);
                }
            });

            return request;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Request testSSL(Context context, final OnResultListener<String> listener) {
        Request request = new Request.Builder().url("https://192.168.210.51:8443/test.html").build();
        final CallbackObject<String> callbackObject = new CallbackObject<String>();

        callbackObject.request = request;
        callbackObject.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callbackObject.result = response.body().string();
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });

        return request;

    }


    //////////////////////////////////////////////// 인호 추가 //////////////////////////////////////////////////////
    //////////////////////////////////// 준수꺼
    private static final String URL_FORMAT_JOIN = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/members";
    private static final String URL_FORMAT_LOGIN_LINK = "http://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/auth/soundcloud";
    private static final String URL_FORMAT_LOGIN_LOCAL = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/auth/login";
    private static final String URL_FORMAT_LOGOUT_LOCAL = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/auth/logout";

    private static final String URL_FORMAT_PROFILE_ME = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/members/me";
    private static final String URL_FORMAT_PROFILE_OTHER = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/members/%s";
    private static final String URL_FORMAT_TRACK_LIST_OTHER = "http://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/members/1/tracks";
    private static final String URL_FORMAT_TRACK_LIST_ME = "http://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/members/me/tracks";

    private static final String URL_FORMAT_PROFILE_CHANGE = "https://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/members/me";
    private static final String URL_FORMAT_PROFILE_PHOTO_CHANGE = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts";

    private static final String URL_FORMAT_STORY_WRITE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1234";
    private static final String URL_FORMAT_STORY_CHANGE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1234";
    private static final String URL_FORMAT_STORY_DELETE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts?page=1";
    private static final String URL_FORMAT_STORY_SEARCH = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1234/replies";

    private static final String URL_FORMAT_COMMENT_WRITE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1234/replies/1";
    private static final String URL_FORMAT_COMMENT_CHANGE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1234/replies/1";
    private static final String URL_FORMAT_COMMENT_DELETE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1/replies?page=1";
    private static final String URL_FORMAT_COMMENT_DETAIL = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1/photos";
    private static final String URL_FORMAT_LOGIN_SC = "http://ec2-52-79-117-68.ap-northeast-2.compute.amazonaws.com/auth/soundcloud";

    private static final String URL_SC_TRACK = "http://api.soundcloud.com/users/3207/tracks?client_id=855fe8df184bf720b9d8e3a4bfb05caf";

    ////////////////////////////////// 용이형 유알엘주소
//    private static final String URL_FORMAT_JOIN = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/members";
//    private static final String URL_FORMAT_LOGIN_LINK = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/auth/soundcloud";
//    private static final String URL_FORMAT_LOGIN_LOCAL = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/auth/login";
//    private static final String URL_FORMAT_LOGOUT_LOCAL = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/auth/logout";
//
//    private static final String URL_FORMAT_PROFILE_ME = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/members/me";
//    private static final String URL_FORMAT_PROFILE_OTHER = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/members/%s";
//    private static final String URL_FORMAT_TRACK_LIST_OTHER = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/members/1/tracks";
//    private static final String URL_FORMAT_TRACK_LIST_ME = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/members/me/tracks";
//
//    private static final String URL_FORMAT_PROFILE_CHANGE = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/members/me";
//    private static final String URL_FORMAT_PROFILE_PHOTO_CHANGE = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/members/me/photos";
//
//    private static final String URL_FORMAT_STORY_WRITE = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts";
//    private static final String URL_FORMAT_STORY_CHANGE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1234";
//    private static final String URL_FORMAT_STORY_DELETE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1234";
//    private static final String URL_FORMAT_STORY_SEARCH = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts?page=1";
//
//    private static final String URL_FORMAT_COMMENT_WRITE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1234/replies";
//    private static final String URL_FORMAT_COMMENT_CHANGE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1234/replies/1";
//    private static final String URL_FORMAT_COMMENT_DELETE = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1234/replies/1";
//    private static final String URL_FORMAT_COMMENT_DETAIL = "http://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1/replies?page=1";
//    private static final String URL_FORMAT_FILE_UPLOAD = "https://ec2-52-79-43-8.ap-northeast-2.compute.amazonaws.com/posts/1/photos";

    private static final String CLIENT_ID = "855fe8df184bf720b9d8e3a4bfb05caf";
    private static final String CLIENT_SECRET = "aede1edc37a86ede4606326274d1172c";
    private static final String LOGIN_URL = "https://soundcloud.com/connect";
    private static final String SCOPE = "*";
    private static final String RESPONSE_TYPE = "code";
    private static final String GRANT_TYPE = "authorization_code";

    private static final String TOKEN_URL ="https://api.soundcloud.com/oauth2/token";
    private static final int RC_LOGIN = 100;

    public void getAccessToken(String code, final OnResultListener<AccessToken> listener) throws UnsupportedEncodingException {

        final CallbackObject<AccessToken> callbackObject = new CallbackObject<AccessToken>();

        RequestBody body = new FormBody.Builder()
                .add("client_id",CLIENT_ID)
                .add("client_secret", CLIENT_SECRET)
                .add("redirect_uri", BrowserActivity.CALLBACK_URL)
                .add("grant_type", GRANT_TYPE)
                .add("code",code)
                .build();

        Request request = new Request.Builder().url(TOKEN_URL)
                .post(body)
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;

        mClientSC.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Gson gson = new Gson();
//                final AccessToken token = gson.fromJson(response.body().string(), AccessToken.class);
//                getMeInfo(token);
                Gson gson = new Gson();
                String text = response.body().string();
                AccessToken token = gson.fromJson(text, AccessToken.class);
                callbackObject.result = token;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });
    }
    private static final String ME_URL = "https://api.soundcloud.com/me";
    private static final String PARAM_OAUTH_TOKEN = "oauth_token";

    public void getMeInfo(AccessToken token, final OnResultListener<MeInfo> listener) throws UnsupportedEncodingException {

        final CallbackObject<MeInfo> callbackObject = new CallbackObject<MeInfo>();

        StringBuilder sb = new StringBuilder();
        sb.append(ME_URL).append("?").append(PARAM_OAUTH_TOKEN).append("=").append(token.accessToken);
        String url = sb.toString();

        Request request = new Request.Builder().url(url).build();

        callbackObject.request = request;
        callbackObject.listener = listener;

        mClientSC.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Gson gson = new Gson();
//                final MeInfo info = gson.fromJson(response.body().string(), MeInfo.class);
//                Toast.makeText(LoginSoundcloudActivity.this, "info : " + info.username, Toast.LENGTH_SHORT).show();
                Gson gson = new Gson();
                String text = response.body().string();
                final MeInfo info = gson.fromJson(text, MeInfo.class);
                callbackObject.result = info;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);

            }
        });
    }


    public Request getSCTrack(Context context, final OnResultListener<SCTrackInfoData[]> listener) throws UnsupportedEncodingException {

        String url = URL_SC_TRACK;

        final CallbackObject<SCTrackInfoData[]> callbackObject = new CallbackObject<SCTrackInfoData[]>();

        Request request = new Request.Builder().url(url)
                .tag(context)
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                String text = response.body().string();
                SCTrackInfoData[] scTrackData = gson.fromJson(text, SCTrackInfoData[].class);
                callbackObject.result = scTrackData;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });

        return request;
    }

    public Request postLoginSC(Context context, int id, final OnResultListener<LoginAndSignUpResult> listener) throws UnsupportedEncodingException {

        String url = URL_FORMAT_LOGIN_SC;

        final CallbackObject<LoginAndSignUpResult> callbackObject = new CallbackObject<LoginAndSignUpResult>();

        RequestBody requestBody = new FormBody.Builder()
                .add("id", "" + id)
                .build();

        Request request = new Request.Builder().url(url)
                .tag(context)
                .post(requestBody)
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;

        mClientSC.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                String text = response.body().string();
                LoginAndSignUpResult loginAndSignUpResult = gson.fromJson(text, LoginAndSignUpResult.class);
                callbackObject.result = loginAndSignUpResult;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });

        return request;
    }



    public Request getLoginSC(Context context, AccessToken id, final OnResultListener<LoginAndSignUpResult> listener)throws UnsupportedEncodingException {

        String url = String.format(URL_FORMAT_LOGIN_SC, id);

        final CallbackObject<LoginAndSignUpResult> callbackObject = new CallbackObject<LoginAndSignUpResult>();

        Request request = new Request.Builder().url(url)
                .tag(context)
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Gson parser = new Gson();
                String text = response.body().string();

                LoginAndSignUpResult result = parser.fromJson(text, LoginAndSignUpResult.class);
                callbackObject.result = result;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });
        return request;
    }


    public Request getProfileMe(Context context, final OnResultListener<ProfileMe> listener) throws UnsupportedEncodingException {

        String url = URL_FORMAT_PROFILE_ME;

        final CallbackObject<ProfileMe> callbackObject = new CallbackObject<ProfileMe>();

        Request request = new Request.Builder().url(url)
                .tag(context)
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                ProfileMe profileMe = gson.fromJson(response.body().string(), ProfileMe.class);
                callbackObject.result = profileMe;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });

        return request;
    }

    public Request getProfileOther(Context context, int mid, final OnResultListener<ProfileOther> listener) throws UnsupportedEncodingException {

        String url = String.format(URL_FORMAT_PROFILE_OTHER,mid);

        final CallbackObject<ProfileOther> callbackObject = new CallbackObject<ProfileOther>();

        Request request = new Request.Builder().url(url)
                .tag(context)
                .build();

        callbackObject.request = request;
        callbackObject.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                ProfileOther profileOther = gson.fromJson(response.body().string(), ProfileOther.class);
                callbackObject.result = profileOther;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });

        return request;
    }

    //    private static  final MediaType MEDIA_TYPE = MediaType.parse("image/png");
    public Request putProfileChange(Context context, String username, String password, String photo, String nickname, String intro, int genre, int position, File file , final OnResultListener<ProfileChange> listener) throws UnsupportedEncodingException {

        String url = URL_FORMAT_PROFILE_CHANGE;

        final CallbackObject<ProfileChange> callbackObject = new CallbackObject<ProfileChange>();

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", username)
                .addFormDataPart("password", password)
                .addFormDataPart("photo", photo)
                .addFormDataPart("nickname", nickname)
                .addFormDataPart("intro", intro)
                .addFormDataPart("genre", "" + genre)
                .addFormDataPart("position", "" + position);
        if (file != null) {
            builder.addFormDataPart("photo", file.getName(), RequestBody.create(MEDIA_TYPE, file));
        }

        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url)
                .tag(context)
                .put(body)
                .build();


        callbackObject.request = request;
        callbackObject.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = mHandler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                String text = response.body().string();
                ProfileChange profileChange = gson.fromJson(text, ProfileChange.class);
                callbackObject.result = profileChange;
                Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                mHandler.sendMessage(msg);
            }
        });

        return request;
    }

    */

}
