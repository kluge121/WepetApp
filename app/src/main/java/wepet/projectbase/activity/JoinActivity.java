package wepet.projectbase.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import wepet.projectbase.CustomLoadingDialog;
import wepet.projectbase.Manager.MessageData;
import wepet.projectbase.Manager.NetworkManager;
import wepet.projectbase.R;
import wepet.projectbase.entity.JoinEntityObject;

public class JoinActivity extends ActionBarActivity implements View.OnClickListener {

    int temp;
    CustomLoadingDialog customProgressDialog;
    private ImageView check, btnEnroll;
    private TextView editTextLocal;
    private EditText editTextName, editTextPhone, editTextId;
    private CircleImageView circle_image;
    JoinEntityObject entity;

    String uid;
    String uuid;


    final int SIGNUP_DIALOG_RADIO = 1;
    private ImageView mPhotoImageView;

    private final MediaType IMAGE_MIME_TYPE = MediaType.parse("image/*");
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;


    Uri currentSelectedUri; //업로드할 현재 이미지에 대한 Uri
    File myImageDir; //카메라로 찍은 사진을 저장할 디렉토리
    String currentFileName;  //파일이름
    static ArrayList<UpLoadValueObject> upLoadfiles = new ArrayList<>();

    class UpLoadValueObject {
        File file; //업로드할 파일
        boolean tempFiles; //임시파일 유무

        public UpLoadValueObject(File file, boolean tempFiles) {
            this.file = file;
            this.tempFiles = tempFiles;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_layout);


        check = (ImageView) findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(SIGNUP_DIALOG_RADIO);
            }
        });

        editTextLocal = (TextView) findViewById(R.id.editTextLocal);
        btnEnroll = (ImageView) findViewById(R.id.btnEnroll);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextId = (EditText) findViewById(R.id.editTextId);


        btnEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnEnroll.setEnabled(false);
                String nickname = editTextName.getText().toString().trim();
                String phoneNum = editTextPhone.getText().toString().trim();
                uuid = UUID.randomUUID().toString().replace('-', 'a');
                JoinEntityObject object = new JoinEntityObject();
                object.region = temp + 1;
                uid = editTextId.getText().toString().trim();


                if (validityCheck(uid, nickname, phoneNum, editTextLocal.getText().toString())) {
                    PropertyManager.getInstance().setUuid(uuid);
                    PropertyManager.getInstance().setUid(uid);
                    int region = temp + 1;
                    File file = null;
                    if (upLoadfiles.size() != 0) {
                        file = upLoadfiles.get(0).file;
                    }
//                    new AsyncJoinInsert().execute(uuid, uid, nickname, phoneNum);
                    try {
                        NetworkManager.getInstance().joinUser(JoinActivity.this, uid, uuid, nickname, phoneNum, region, file, new NetworkManager.OnResultListener<MessageData>() {
                            @Override
                            public void onSuccess(Request request, MessageData result) {
                                Toast.makeText(JoinActivity.this, result.msg, Toast.LENGTH_LONG).show();
                                btnEnroll.setEnabled(true);
                                Log.e(TAG, result.msg);
                                Log.e(TAG, result.msg.equals("회원가입이 되었습니다.") + "");

                                if (result.msg.equals("회원가입이 되었습니다.")) {

                                    customProgressDialog = new CustomLoadingDialog(JoinActivity.this);
                                    customProgressDialog.getWindow().setBackgroundDrawable
                                            (new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                    customProgressDialog.show();

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {


                                            try {
                                                NetworkManager.getInstance().postAuthLogin(JoinActivity.this, uid, uuid, new NetworkManager.OnResultListener<MessageData>() {

                                                    @Override
                                                    public void onSuccess(Request request, MessageData result) {
                                                        Intent intent = new Intent(JoinActivity.this, Main.class);
                                                        startActivity(intent);
                                                        Log.e(TAG, result.msg);
                                                        customProgressDialog.dismiss();
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onFailure(Request request, int code, Throwable cause) {
                                                        Toast.makeText(JoinActivity.this, "네트워크 상태를 확인하여 주세요.", Toast.LENGTH_SHORT).show();
                                                        Log.e(TAG, "onFailure");
                                                        btnEnroll.setEnabled(true);

                                                    }
                                                });
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, 3000);


                                }


                            }

                            @Override
                            public void onFailure(Request request, int code, Throwable cause) {
                                Toast.makeText(JoinActivity.this, cause.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onFailure");
                                // Toast.makeText(StoryWriteActivity.this, "fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                }else {
                    btnEnroll.setEnabled(true);

                }


            }
        });

        circle_image = (CircleImageView) findViewById(R.id.circle_image);
        circle_image.setOnClickListener(this);

    }

    public void onResume() {
        super.onResume();
        if (!isSDCardAvailable()) {
            Toast.makeText(getApplicationContext(), "SD 카드가 없어 종료 합니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String currentAppPackage = getPackageName();
        checkPermission();
        checkPermissionC();
        myImageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), currentAppPackage);


        if (!myImageDir.exists()) {
            if (myImageDir.mkdirs()) {
                Toast.makeText(getApplicationContext(), " 저장할 디렉토리가 생성 됨", Toast.LENGTH_SHORT).show();
            }
        }


    }

    protected void onStop() {
        super.onStop();
    }

    public boolean isSDCardAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public int exifOrientationToDegrees(int exifOrientation) {

        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public Bitmap rotate(Bitmap bitmap, int degrees) {

        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex) {
                Log.e("프로필수정", "회전중오류");
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
    }

    private static final String TAG = "AppPermission";

    private void doTakePhotoAction() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //업로드할 파일의 이름
        currentFileName = "upload_" + String.valueOf(System.currentTimeMillis() / 1000) + ".jpg";
        currentSelectedUri = Uri.fromFile(new File(myImageDir, currentFileName));
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentSelectedUri);
        startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
    }

    private void doTakeAlbumAction() {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        Bitmap bitmap1;
        switch (requestCode) {

            case PICK_FROM_ALBUM: {

                currentSelectedUri = data.getData();
                if (currentSelectedUri != null) {
                    //실제 Image의 full path name을 얻어온다.
                    if (findImageFileNameFromUri(currentSelectedUri)) {
                        //ArrayList에 업로드할  객체를 추가한다.
                        upLoadfiles.add(new UpLoadValueObject(new File(currentFileName), false));
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), currentSelectedUri);

                            ExifInterface exif = new ExifInterface(currentFileName);
                            int exifOrientation = exif.getAttributeInt(
                                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                            int exifDegree = exifOrientationToDegrees(exifOrientation);
                            bitmap1 = rotate(bitmap, exifDegree);
                            mPhotoImageView.setImageBitmap(bitmap1);
                        } catch (Exception e) {

                        }

                    }
                } else {
                    Bundle extras = data.getExtras();
                    Bitmap returedBitmap = (Bitmap) extras.get("data");
                    if (tempSavedBitmapFile(returedBitmap)) {
                        Log.e("임시이미지파일저장", "저장됨");
                    } else {
                        Log.e("임시이미지파일저장", "실패");
                    }
                }

                break;

            }

            case PICK_FROM_CAMERA: {

                //카메라캡쳐를 이용해 가져온 이미지
                upLoadfiles.add(new UpLoadValueObject(new File(myImageDir, currentFileName), false));
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), currentSelectedUri);
                    ExifInterface exif = new ExifInterface(currentFileName);
                    int exifOrientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int exifDegree = exifOrientationToDegrees(exifOrientation);
                    bitmap1 = rotate(bitmap, exifDegree);
                    mPhotoImageView.setImageBitmap(bitmap1);
                } catch (Exception e) {

                }

                break;
            }
        }
    }


    private boolean tempSavedBitmapFile(Bitmap tempBitmap) {
        boolean flag = false;
        try {
            currentFileName = "upload_" + (System.currentTimeMillis() / 1000);
            String fileSuffix = ".jpg";
            //임시파일을 실행한다.
            File tempFile = File.createTempFile(
                    currentFileName,            // prefix
                    fileSuffix,                   // suffix
                    myImageDir                   // directory
            );
            final FileOutputStream bitmapStream = new FileOutputStream(tempFile);
            tempBitmap.compress(Bitmap.CompressFormat.JPEG, 0, bitmapStream);
            upLoadfiles.add(new UpLoadValueObject(tempFile, true));
            if (bitmapStream != null) {
                bitmapStream.close();
            }
            currentSelectedUri = Uri.fromFile(tempFile);
            flag = true;
        } catch (IOException i) {
            Log.e("저장중 문제발생", i.toString(), i);
        }
        return flag;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private boolean findImageFileNameFromUri(Uri tempUri) {
        boolean flag = false;

        //실제 Image Uri의 절대이름
        String[] IMAGE_DB_COLUMN = {MediaStore.Images.ImageColumns.DATA};
        Cursor cursor = null;
        try {
            //Primary Key값을 추출
            String imagePK = String.valueOf(ContentUris.parseId(tempUri));
            //Image DB에 쿼리를 날린다.
            cursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_DB_COLUMN,
                    MediaStore.Images.Media._ID + "=?",
                    new String[]{imagePK}, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                currentFileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                Log.e("fileName", String.valueOf(currentFileName));
                flag = true;
            }
        } catch (SQLiteException sqle) {
            Log.e("findImage....", sqle.toString(), sqle);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return flag;
    }

    public void onClick(View v) {
        android.app.AlertDialog dialog = null;
        mPhotoImageView = (ImageView) v;
        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                doTakePhotoAction();
            }
        };

        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                doTakeAlbumAction();
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        dialog = new android.app.AlertDialog.Builder(this)
                .setTitle("업로드할 이미지 선택")
                .setPositiveButton("앨범선택", albumListener)
                .setNegativeButton("사진촬영", cameraListener)
                .setNeutralButton("취소", cancelListener).create();

        dialog.show();
    }

    protected Dialog onCreateDialog(int id) {
        Log.d("test", "OnCreateDialog");

        switch (id) {
            case SIGNUP_DIALOG_RADIO:
                AlertDialog.Builder builder3 =
                        new AlertDialog.Builder(JoinActivity.this);
                final String str[] = {"전체","서울","경기","인천","강원","대전","세종","충남","충북","부산","울산","경남","경북","대구","광주","전남","전북","제주도"};
                builder3.setTitle("거주지역은?")
                        .setPositiveButton("선택", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("mainactivity", temp + "");
                                editTextLocal.setText(str[temp]);
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setSingleChoiceItems(str, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                temp = which;
                            }
                        });// 리스너
                return builder3.create(); // 다이얼로그 생성한 객체 리턴
        }

        return super.onCreateDialog(id);
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

    public interface OnResultListener<T> {
        public void onSuccess(Request request, T result);

        public void onFailure(Request request, int code, Throwable cause);
    }

    public class LoginAsnc extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            Response loginresponse = null;
            boolean flagConn = false;
            boolean flagJson = false;
            String uuid = strings[0];
            String uid = strings[1];

            final CallbackObject<MessageData> callbackObject = new CallbackObject<MessageData>();

            try {
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();

                RequestBody loginBody = new FormBody.Builder()
                        .add("uuid", uuid)
                        .add("uid", uid) //기본 쿼리
                        .build();

                Request request = new Request.Builder()
                        .url("http://52.78.104.95:3000/auth/login")
                        .post(loginBody) //반드시 post로
                        .build();

                loginresponse = toServer.newCall(request).execute();

                flagConn = loginresponse.isSuccessful();

                if (flagConn) {
                    JSONObject jsonObject = new JSONObject(loginresponse.body().string());
                    String resultMessage = jsonObject.getString("msg");

                    if (resultMessage.equalsIgnoreCase("success")) {
                        flagJson = true;
                    } else {
                        flagJson = false;
                    }
                }
                return flagJson;

            } catch (Exception e) {
                //e("LoginAsynck", e.toString(),e);
            } finally {
                if (loginresponse != null) {
                    loginresponse.close();
                }
            }
            return flagJson;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }

    private boolean validityCheck(String uid, String nickname, String phone, String region) {


        Log.e("체크", uid.length() + "");

        if (!Pattern.matches("^[a-zA-Z0-9]*$", uid) || uid.length() < 3 || uid.equals("")) {
            Toast.makeText(getApplicationContext(), "올바른 아이디를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return false;

        }
        if (!Pattern.matches("^[가-힣a-zA-Z0-9]*$", nickname) || nickname.length() < 2 || nickname.equals("")) {
            Toast.makeText(getApplicationContext(), "올바른 닉네임를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", phone)) {
            Toast.makeText(getApplicationContext(), "올바른 핸드폰번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (region.length() <= 0) {
            Toast.makeText(getApplicationContext(), "지역을 선택해 주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private final int MY_PERMISSION_REQUEST_STORAGE = 100;

    private void checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to write the permission.

                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_REQUEST_STORAGE);

            } else {
                //사용자가 언제나 허락
            }
        }

    }
    private void checkPermissionC() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
// Activity에서 실행하는경우
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {

                // 이 권한을 필요한 이유를 설명해야하는가?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)) {

                    // 다이어로그같은것을 띄워서 사용자에게 해당 권한이 필요한 이유에 대해 설명합니다
                    // 해당 설명이 끝난뒤 requestPermissions()함수를 호출하여 권한허가를 요청해야 합니다

                } else {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            200);

                    // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다

                }
            }

        }

    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    //사용자가 퍼미션을 OK했을 경우

                } else {

                    Log.d(TAG, "Permission always deny");

                    //사용자가 퍼미션을 거절했을 경우
                }
                break;
            case 200:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 허가
                    // 해당 권한을 사용해서 작업을 진행할 수 있습니다
                } else {
                    // 권한 거부
                    // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
                }
                return;
        }
    }
}



