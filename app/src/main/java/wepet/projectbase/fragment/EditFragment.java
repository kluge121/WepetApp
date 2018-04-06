package wepet.projectbase.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
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
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import wepet.projectbase.GetContext;
import wepet.projectbase.R;
import wepet.projectbase.StringToCode;
import wepet.projectbase.activity.ChangeCodeToName;
import wepet.projectbase.activity.PropertyManager;
import wepet.projectbase.entity.PostEditMainListEntity;
import wepet.projectbase.entity.UpLoadValueObject;

/**
 * Created by ccei on 2016-07-28.
 */
public class EditFragment extends Fragment implements View.OnClickListener {


    int temp1, temp2, temp3;


    boolean image1B;
    boolean image2B;
    boolean image3B;
    boolean image4B;
    static String tmpImage1;
    static String tmpImage2;
    static String tmpImage3;
    static String tmpImage4;
    static ArrayList<String> tmpImageArray;
    public static int genderCheck = 0;
    public static int neutralizeCheck = 0;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private ImageView mPhotoImageView;
    static boolean selection1 = false, selection2 = false, selection3 = false, selection4 = false;
    private ImageView write_check1, write_check2, write_search;
    private ImageView picturepluse1, picturepluse2, picturepluse3, picturepluse4;
    private ImageView neutralize_yes, neutralize_no, gender_man, gender_girl;
    private static TextView vaccination_info, local_info, write_breeds, content;
    private static EditText age_info;
    static PostEditMainListEntity entityObject = new PostEditMainListEntity();

    public EditFragment() {
    }

    int pid;
    private ImageView image1, image2, image3, image4;
    private static String uImage1, uImage2, uImage3, uImage4;
    View mView;
    public static ArrayList<UpLoadValueObject> upLoadfiles = new ArrayList<UpLoadValueObject>();


    public static EditFragment newInstance(int pid) {
        EditFragment editF = new EditFragment();
        Bundle b = new Bundle();
        b.putInt("pid", pid);
        editF.setArguments(b);
        return editF;
    }


    ArrayList<ImageView> arrayList = new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.write_fragment_one, container, false);
        Bundle b = new Bundle(getArguments());

        pid = b.getInt("pid");
        genderCheck = 1;
        neutralizeCheck=1;
        new AsyncDetailGet().execute(pid);

        picturepluse1 = (ImageView) view.findViewById(R.id.image1_content);
        picturepluse2 = (ImageView) view.findViewById(R.id.image2_content);
        picturepluse3 = (ImageView) view.findViewById(R.id.image3_content);
        picturepluse4 = (ImageView) view.findViewById(R.id.image4_content);

        picturepluse1.setOnClickListener(new View.OnClickListener() {
            @Override
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

                dialog = new android.app.AlertDialog.Builder(getActivity())
                        .setTitle("업로드할 이미지 선택")
                        .setPositiveButton("앨범선택", albumListener)
                        .setNegativeButton("사진촬영", cameraListener)
                        .setNeutralButton("취소", cancelListener).create();

                dialog.show();
            }
        });


        picturepluse2.setOnClickListener(this);
        picturepluse3.setOnClickListener(this);
        picturepluse4.setOnClickListener(this);


        write_check1 = (ImageView) view.findViewById(R.id.write_check1);
        write_check2 = (ImageView) view.findViewById(R.id.write_check2);

        write_search = (ImageView) view.findViewById(R.id.write_search);
        write_breeds = (TextView) view.findViewById(R.id.write_breeds);

        gender_man = (ImageView) view.findViewById(R.id.gender_man);
        gender_girl = (ImageView) view.findViewById(R.id.gender_girl);
        age_info = (EditText) view.findViewById(R.id.agecontent);
        local_info = (TextView) view.findViewById(R.id.local_info);
        neutralize_yes = (ImageView) view.findViewById(R.id.neutralize_yes);
        neutralize_no = (ImageView) view.findViewById(R.id.neutralize_no);
        vaccination_info = (TextView) view.findViewById(R.id.vaccination_info);
        content = (TextView) view.findViewById(R.id.content);


        ((ImageView) view.findViewById(R.id.write_check1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogSelectOption();
            }
        });

        ((ImageView) view.findViewById(R.id.write_check2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogSelectOption2();
            }
        });

        ((ImageView) view.findViewById(R.id.write_search)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogSelectOption3();
            }
        });

        ((ImageView) view.findViewById(R.id.neutralize_no)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selection1 == false) {
                    neutralize_no.setSelected(true);
                    selection1 = true;
                    neutralize_yes.setSelected(false);
                    selection2 = false;
                    neutralizeCheck = 1;

                } else if (selection1 == true) {
                    neutralize_no.setSelected(false);
                    selection1 = false;
                    neutralizeCheck = 0;
                }
            }
        });

        ((ImageView) view.findViewById(R.id.neutralize_yes)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selection2 == false) {
                    neutralize_yes.setSelected(true);
                    selection2 = true;
                    neutralize_no.setSelected(false);
                    selection1 = false;
                    neutralizeCheck = 1;

                } else if (selection2 == true) {
                    neutralize_yes.setSelected(false);
                    selection2 = false;
                    neutralizeCheck = 0;
                }
            }
        });
        ((ImageView) view.findViewById(R.id.gender_man)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selection3 == false) {
                    gender_man.setSelected(true);
                    selection3 = true;
                    gender_girl.setSelected(false);
                    selection4 = false;
                    genderCheck = 1;

                } else if (selection3 == true) {
                    gender_man.setSelected(false);
                    selection3 = false;
                    genderCheck = 0;
                }
            }
        });
        ((ImageView) view.findViewById(R.id.gender_girl)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selection4 == false) {
                    gender_girl.setSelected(true);
                    selection4 = true;
                    gender_man.setSelected(false);
                    selection3 = false;
                    genderCheck = 1;

                } else if (selection4 == true) {
                    gender_girl.setSelected(false);
                    selection4 = false;
                    genderCheck = 0;
                }
            }
        });
        upLoadfiles.clear();
        return view;
    }


    private void DialogSelectOption() {
        final String items1[] = {"전체", "서울", "경기", "인천", "강원", "대전", "세종", "충남", "충북", "부산", "울산", "경남", "경북", "대구", "광주", "전남", "전북", "제주도"};
        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
        ab.setTitle("지역");
        ab.setSingleChoiceItems(items1, 0,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 각 리스트를 선택했을때
                    }
                }).setPositiveButton("선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
                        local_info.setText(items1[temp1]);
                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancel 버튼 클릭시
                    }
                }).setSingleChoiceItems(items1, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                temp1 = whichButton;
            }
        });
        ab.show();
    }

    private void DialogSelectOption2() {
        final String items2[] = {"1차", "2차", "3차", "4차", "5차", "몰라요"};
        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
        ab.setTitle("예방접종");
        ab.setSingleChoiceItems(items2, 0,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 각 리스트를 선택했을때

                    }
                }).setPositiveButton("선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
                        vaccination_info.setText(items2[temp2]);
                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancel 버튼 클릭시
                    }
                }).setSingleChoiceItems(items2, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                temp2 = whichButton;
            }
        });// 리스너;
        ab.show();
    }

    private void DialogSelectOption3() {
        final String items3[] = {"기타", "말티즈", "미니핀", "요크셔", "치와와", "푸들", "닥스훈트", "슈나우저", "비숑", "시츄", "포메라니안",
                "불독", "불테리어", "비글", "코카", "진돗개", "풍산개", "시바견", "웰시코기", "리트리버", "말라뮤트", "허스키", "세퍼트", "로트바일러", "믹스견"};
        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
        ab.setTitle("견종");
        ab.setSingleChoiceItems(items3, 0,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 각 리스트를 선택했을때
                    }
                }).setPositiveButton("선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
                        write_breeds.setText(items3[temp3]);
                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancel 버튼 클릭시
                    }
                }).setSingleChoiceItems(items3, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                temp3 = whichButton;
            }
        });
        ab.show();


    }


    static public PostEditMainListEntity writeTab() {

        entityObject.img = upLoadfiles;
        entityObject.breed = StringToCode.breedCodeReturn(write_breeds.getText().toString());
        entityObject.gender = StringToCode.genderCodeReturn(selection3, genderCheck);
        entityObject.age = age_info.getText().toString();
        entityObject.region = StringToCode.regionCodeReturn(local_info.getText().toString());
        entityObject.neuter = StringToCode.neuterCodeReturn(selection2, neutralizeCheck);
        entityObject.vaccin = StringToCode.vaccineCodeReturn(vaccination_info.getText().toString());
        entityObject.content = content.getText().toString();
        entityObject.CurrentImageList = tmpImageArray;
        Log.e("오마니",vaccination_info.getText().toString());


        return entityObject;

    }

    Uri currentSelectedUri; //업로드할 현재 이미지에 대한 Uri
    File myImageDir; //카메라로 찍은 사진을 저장할 디렉토리
    String currentFileName;  //파일이름

    //퍼미션관리


    public void onResume() {
        super.onResume();
        if (!isSDCardAvailable()) {
            Toast.makeText(getContext(), "SD 카드가 없어 종료 합니다.", Toast.LENGTH_SHORT).show();
            getActivity().finish();
            return;
        }
        String currentAppPackage = getContext().getPackageName();

        myImageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), currentAppPackage);

        checkPermission();
        checkPermissionC();

        if (!myImageDir.exists()) {
            if (myImageDir.mkdirs()) {
                Toast.makeText(getContext(), " 저장할 디렉토리가 생성 됨", Toast.LENGTH_SHORT).show();
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
    private final int MY_PERMISSION_REQUEST_STORAGE = 100;
    private void checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
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
            if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {

                // 이 권한을 필요한 이유를 설명해야하는가?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.CAMERA)) {

                    // 다이어로그같은것을 띄워서 사용자에게 해당 권한이 필요한 이유에 대해 설명합니다
                    // 해당 설명이 끝난뒤 requestPermissions()함수를 호출하여 권한허가를 요청해야 합니다

                } else {

                    ActivityCompat.requestPermissions(getActivity(),
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

    private void doTakePhotoAction() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //업로드할 파일의 이름
        currentFileName = "upload_" + String.valueOf(System.currentTimeMillis() / 1000) + ".jpg";
        currentSelectedUri = Uri.fromFile(new File(myImageDir, currentFileName));
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentSelectedUri);
        startActivityForResult(cameraIntent, PICK_FROM_CAMERA);
    }//

    private void doTakeAlbumAction() {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }//

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_OK) {
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
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), currentSelectedUri);

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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), currentSelectedUri);
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
            cursor = getActivity().getContentResolver().query(
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

        dialog = new android.app.AlertDialog.Builder(getActivity())
                .setTitle("업로드할 이미지 선택")
                .setPositiveButton("앨범선택", albumListener)
                .setNegativeButton("사진촬영", cameraListener)
                .setNeutralButton("취소", cancelListener).create();

        dialog.show();
    }


    public class AsyncDetailGet extends AsyncTask<Integer, Void, JSONObject> {


        JSONArray jsonArray = null;

        @Override
        protected JSONObject doInBackground(Integer... mPid) {

            JSONObject jsonObject = null;
            try {
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();
                Request request = new Request.Builder()
                        .url("http://52.78.104.95:3000/posts/" + mPid[0] + "?uid=" + PropertyManager.getInstance().getUid())
                        .build();


                Response respone = toServer.newCall(request).execute();
                ResponseBody responseBody = respone.body();
                jsonObject = new JSONObject(responseBody.string());

            } catch (UnknownHostException une) {
                Log.e("fileUpLoad1", une.toString());
            } catch (UnsupportedEncodingException uee) {
                Log.e("fileUpLoad2", uee.toString());
            } catch (Exception e) {
                Log.e("fileUpLoad3", e.toString());
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                int fisrt = 0;
                int second = 0;
                int third = 0;
                int fourth = 0;

                JSONObject data = null;
                JSONArray imgs = null;
                tmpImageArray = new ArrayList<String>();
                if (jsonObject != null && jsonObject.length() > 0) {


                    data = jsonObject.getJSONObject("data");
                    imgs = jsonObject.getJSONArray("imgs");

                    write_breeds.setText(ChangeCodeToName.puppyName(data.getInt("breed")));


                    for (int i = 0; i < imgs.length(); i++) {

                        if (i == 0) {
                            Glide.with(GetContext.getContext()).load(imgs.getJSONObject(i).getString("img")).into(picturepluse1); //픽셀값으로 변경
                            fisrt = 1;
                        }
                        if (i == 1) {
                            Glide.with(GetContext.getContext()).load(imgs.getJSONObject(i).getString("img")).into(picturepluse2); //픽셀값으로 변경
                            second = 1;
                        }
                        if (i == 2) {
                            Glide.with(GetContext.getContext()).load(imgs.getJSONObject(i).getString("img")).into(picturepluse3); //픽셀값으로 변경
                            third = 1;
                        }
                        if (i == 3) {
                            Glide.with(GetContext.getContext()).load(imgs.getJSONObject(i).getString("img")).into(picturepluse4); //픽셀값으로 변경
                            fourth = 1;
                        }
                    }

                    if(fisrt == 1){
                        tmpImage1 = imgs.getString(0);
                        tmpImageArray.add(tmpImage1);
                    }else if(second == 1){
                        tmpImage2 = imgs.getString(1);
                        tmpImageArray.add(tmpImage2);
                    }else if ( third == 1){
                        tmpImage3 = imgs.getString(2);
                        tmpImageArray.add(tmpImage3);
                    }else if(fourth ==1){
                        tmpImage4 = imgs.getString(3);
                        tmpImageArray.add(tmpImage4);
                    }




                    if (data.getInt("gender") == 1) {
                        gender_man.setSelected(true);
                        selection3 = true;
                        gender_girl.setSelected(false);
                        selection4 = false;

                    } else if (data.getInt("gender") == 2) {
                        gender_man.setSelected(false);
                        selection3 = false;
                        gender_girl.setSelected(true);
                        selection4 = true;
                    }

                    Integer tmp = data.getInt("age");
                    age_info.setText(tmp.toString());

                    local_info.setText(ChangeCodeToName.dogRegion(data.getInt("region")));


                    if (data.getInt("neuter") == 1) {
                        neutralize_yes.setSelected(true);
                        selection2 = true;
                        neutralize_no.setSelected(false);
                        selection1 = false;

                    } else if (data.getInt("neuter") == 2) {
                        neutralize_yes.setSelected(false);
                        selection2 = false;
                        neutralize_no.setSelected(true);
                        selection1 = true;
                    }

                    vaccination_info.setText(ChangeCodeToName.dogVacccin(data.getInt("vaccin")));

                    content.setText(data.getString("content"));


                    Log.e("entityObject.img" ,entityObject.img+"");
                    Log.e("entityObject.breed",entityObject.breed+"");
                    Log.e("entityObject.gender",entityObject.gender);
                    Log.e("entityObject.age",entityObject.age);
                    Log.e("entityObject.region",entityObject.region);
                    Log.e("entityObject.neuter",entityObject.neuter+"");
                    Log.e("entityObject.vaccin",entityObject.vaccin+"");
                    Log.e("entityObject.content",entityObject.content+"");
                    Log.e("entityObjectC",entityObject.CurrentImageList+"");







                }

            } catch (Exception e) {
                Log.e("leelog", e.toString());
            }
        }
    } // 정보가져오기 Async


}






























