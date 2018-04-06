package wepet.projectbase.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import wepet.projectbase.GetContext;
import wepet.projectbase.R;
import wepet.projectbase.entity.MyProfileEntityObject;

public class MyProfileModification extends FontActivity implements View.OnClickListener {

    private ImageButton backBtn;

    private ImageView mPhotoImageView;

    private final MediaType IMAGE_MIME_TYPE = MediaType.parse("image/*");
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;

    private ImageButton editButton;
    private EditText nickNameEdit;
    private EditText phoneEdit;

    Uri currentSelectedUri; //업로드할 현재 이미지에 대한 Uri
    File myImageDir; //카메라로 찍은 사진을 저장할 디렉토리
    String currentFileName;  //파일이름
    String pictureName1;
    String pictureName2;
    private static final String TAG = "MyProfileModification";
    int pictureState;

    static ArrayList<UpLoadValueObject> upLoadfiles = new ArrayList<>();

    ArrayList<MyProfileEntityObject> data = new ArrayList<MyProfileEntityObject>();


    private ImageView editImage;
    ArrayList<MyProfileEntityObject> dataPicture;

    MyProfileEntityObject object;

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
        setContentView(R.layout.activity_my_profile_modification);

        nickNameEdit = (EditText) findViewById(R.id.profile_modification_nickname_edittext);
        phoneEdit = (EditText) findViewById(R.id.profile_modification_phonenum_edittext);


        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_modification);
        toolbar.setTitle("");
        toolbar.setContentInsetsAbsolute(0, 0);

        backBtn = (ImageButton) findViewById(R.id.profile_modification_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editImage = (ImageView) findViewById(R.id.profile_modification_image);
        editImage.setOnClickListener(this);

        dataPicture = (ArrayList<MyProfileEntityObject>) getIntent().getSerializableExtra("data");
        object = dataPicture.get(0);

        pictureName1 = object.profile_thumb;

        nickNameEdit.setText(object.nickname);


        editButton = (ImageButton) findViewById(R.id.profile_modification_result);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validityCheck(nickNameEdit.getText().toString(), phoneEdit.getText().toString())) {

                    String uid = PropertyManager.getInstance().getUid();
                    String phone = phoneEdit.getText().toString().trim();
                    String nickname = nickNameEdit.getText().toString().trim();
                    String img_state = "4";
                    Log.e(TAG, upLoadfiles.size() + "");


                    if (upLoadfiles.size() != 0) {
                        File file = upLoadfiles.get(0).file;
                        pictureName2 = file.getName();
                    } else {
                        pictureName2 = null;
                    }


// file1 기존그림 , file2가 새로운그림
                    if (pictureName1.length()<1 && pictureName2==null) {
                        img_state = "" + 1;
                    } else if (pictureName1.length()<1 && pictureName2.length() > 1) {
                        img_state = "" + 2;
                    } else if (pictureName1.length() > 1 && pictureName2==null) {
                        img_state = "" + 1;
                    } else if (pictureName1.length() > 1 && pictureName2.length() > 1) {
                        img_state = "" + 4;
                    }

//                    Log.e(TAG, "img_sate" + img_state);
//                    Log.e(TAG, "onClick!");
                    Log.e("체크1",uid);
                    Log.e("체크2",nickname);
                    Log.e("체크3",phone);
                    Log.e("체크4",img_state);

                    new AsyncProfileEdit().execute(uid, nickname, phone, img_state);


                } else {
                }
            }
        });
        if (object.profile_thumb.length() > 0) {
            Glide.with(GetContext.getContext()).load(object.profile_thumb).into(editImage); //픽셀값으로 변경
        }


        Log.e(TAG, pictureName1 + "");
        upLoadfiles.clear();
    }

    protected void onStop() {
        super.onStop();
    }


    public void onResume() {
        super.onResume();
        if (!isSDCardAvailable()) {
            Toast.makeText(getApplicationContext(), "SD 카드가 없어 종료 합니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String currentAppPackage = getPackageName();

        myImageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), currentAppPackage);

        checkPermission();
        checkPermissionC();

        if (!myImageDir.exists()) {
            if (myImageDir.mkdirs()) {
            }
        }

    }
    public boolean isSDCardAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    private void checkPermission()  {
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

    private void doTakePhotoAction() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //업로드할 파일의 이름
        currentFileName = "upload_" + String.valueOf(System.currentTimeMillis() / 1000) + ".jpg";
        currentSelectedUri = Uri.fromFile(new File(myImageDir, currentFileName));
        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, currentSelectedUri);
        startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
    }//

    private void doTakeAlbumAction() {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }//

    public int exifOrientationToDegrees(int exifOrientation) {
        Log.e("프로필수정", exifOrientation + "");
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
        Log.e("프로필수정", "회전하러들어옴");
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap1;
        if (resultCode != RESULT_OK) {
            return;
        }

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

    private static int getRotation(Context context, Uri selectedImage) {
        int rotation = 0;
        ContentResolver content = context.getContentResolver();


        Cursor mediaCursor = content.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{"orientation", "date_added"}, null, null, "date_added desc");

        if (mediaCursor != null && mediaCursor.getCount() != 0) {
            while (mediaCursor.moveToNext()) {
                rotation = mediaCursor.getInt(0);
                break;
            }
        }
        mediaCursor.close();
        return rotation;
    }

    public class AsyncProfileEdit extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Response response = null;
                Log.e(TAG, "doInBackground!!");

                String uid = params[0];
                String nickname = params[1];
                String phone = params[2];
                String img_state = params[3];
                Log.e(TAG, "doInBackground!!");


                Log.e(TAG, "doInBackground!!");


                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("uid", uid)
                        .addFormDataPart("nickname", nickname)
                        .addFormDataPart("phone", phone)
                        .addFormDataPart("img_state", img_state);
                Log.e(TAG, "doInBackground!!");


                File file = null;
                if (upLoadfiles.size() != 0) {
                    file = upLoadfiles.get(0).file;
                    if (img_state!="1" || img_state!="3") {
                        builder.addFormDataPart("profile_img", file.getName(), RequestBody.create(IMAGE_MIME_TYPE, file));
                    }

                }



                Log.e("filesize", file + "");

                RequestBody body = builder.build();
                Request request = new Request.Builder().url("http://52.78.104.95:3000/users/me")
                        .put(body)
                        .build();

                Log.e(TAG, file + "");
                Log.e(TAG, nickname + "");
                Log.e(TAG, uid + "");
                Log.e(TAG, phone + "");
                Log.e(TAG, img_state + "");

                OkHttpClient okHttpClient = new OkHttpClient();

                okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    Log.e(TAG, "success!");
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Intent intent = new Intent(MyProfileModification.this, Main.class);
            startActivity(intent);
            finish();
            super.onPostExecute(aBoolean);
        }
    }

    private boolean validityCheck(String nickname, String phone) {

        if (!Pattern.matches("^[가-힣a-zA-Z0-9]*$", nickname) || nickname.length() < 2 || nickname.equals("")) {
            Toast.makeText(getApplicationContext(), "올바른 닉네임를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", phone)) {
            Toast.makeText(getApplicationContext(), "올바른 핸드폰번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}