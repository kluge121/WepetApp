package wepet.projectbase.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import wepet.projectbase.R;
import wepet.projectbase.entity.MatchDataObject;

public class MatchResult extends FontActivity {

    private ImageView first_match_image, match_second_image, match_third_image, match_fourth_image;
    private TextView match_text2, match_explation_title;
    private ImageView size1, size2, size3, size4, size5;
    private ImageView fur1, fur2, fur3, fur4, fur5;
    private ImageView sociability1, sociability2, sociability3, sociability4, sociability5;
    private ImageView activity1, activity2, activity3, activity4, activity5;
    private ImageButton match_go;
    String responseResultValue;

    private int active_num;
    private int breed_num1, breed_num2, breed_num3, breed_num4;
    private int fur_num, size_num, sociability_num;

    static final private String TAG = "MatchResult";

    ArrayList<MatchDataObject> resultData;
    RelativeLayout toSNSRoot;
    static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1000;

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                return false;
            }
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_result);

        toSNSRoot = (RelativeLayout) findViewById(R.id.sns_share_root); // 공유 할 레이아웃

        ImageView toSNS = (ImageView) findViewById(R.id.match_share);
        toSNS.setOnClickListener(new View.OnClickListener(){           // 공유하기 버튼 클릭
            @Override
            public void onClick(View view) {
                String fileName = "sns_upload_image_file.jpg";
                File snsShareDir = new File(Environment.getExternalStorageDirectory() +
                        "/sns_share_dir_images/");
                FileOutputStream fos;
                if (Build.VERSION.SDK_INT >= 23) {
                    if(isStoragePermissionGranted()) {
                        toSNSRoot.buildDrawingCache();
                        Bitmap captureView = toSNSRoot.getDrawingCache();

                        try {
                            if (!snsShareDir.exists()) {
                                if (!snsShareDir.mkdirs()) {
                                }
                            }
                            File file = new File(snsShareDir, fileName);
                            if(!file.exists()){
                                file.createNewFile();
                            }
                            fos = new FileOutputStream(file);
                            captureView.compress(Bitmap.CompressFormat.JPEG, 100,fos );

                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("image/*");
                            //intent.putExtra(Intent.EXTRA_SUBJECT, "사진제목");
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

                            Intent target = Intent.createChooser(intent, "공유할 곳을 선택하세요");
                            startActivity(target);

                        } catch (Exception e) {
                            Log.e("onTouch", e.toString(), e);
                        }
                    }
                }else{
                    toSNSRoot.buildDrawingCache();
                    Bitmap captureView = toSNSRoot.getDrawingCache();
                    try {
                        if (!snsShareDir.exists()) {
                            if (!snsShareDir.mkdirs()) {
                            }
                        }
                        File file = new File(snsShareDir, fileName);
                        if(!file.exists()){
                            file.createNewFile();
                        }
                        fos = new FileOutputStream(file);
                        captureView.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/*");
                        //intent.putExtra(Intent.EXTRA_SUBJECT, "사진제목");
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(snsShareDir));

                        Intent target = Intent.createChooser(intent, "공유할 곳을 선택하세요");
                        startActivity(target);

                    } catch (Exception e) {
                        Log.e("onTouch", e.toString(), e);
                    }
                }

            }
        });

        resultData = (ArrayList<MatchDataObject>)getIntent().getSerializableExtra("data");

        MatchDataObject object = resultData.get(0);
        MatchDataObject object1 = resultData.get(1);
        MatchDataObject object2 = resultData.get(2);
        MatchDataObject object3 = resultData.get(3);

        PropertyManager.getInstance().setBreed(resultData.get(0).breed,resultData.get(1).breed,resultData.get(2).breed,resultData.get(3).breed);
        active_num = object.active;
        fur_num = object.molt;
        size_num = object.size;
        sociability_num = object.sociable;
        breed_num1 =object.breed;
        breed_num2 = object1.breed;
        breed_num3 = object2.breed;
        breed_num4 = object3.breed;



        first_match_image = (ImageView)findViewById(R.id.first_match_image);
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.image_scale);
        first_match_image.startAnimation(anim);

        match_second_image = (ImageView)findViewById(R.id.match_second_image);
        match_third_image = (ImageView)findViewById(R.id.match_third_image);
        match_fourth_image = (ImageView)findViewById(R.id.match_fourth_image);

        match_text2 = (TextView)findViewById(R.id.match_text2);
        match_explation_title = (TextView)findViewById(R.id.match_explation_title);

        match_go = (ImageButton)findViewById(R.id.match_go);
        match_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent targetIntent = new Intent(MatchResult.this, Main.class);
                startActivity(targetIntent);

                new Handler().postDelayed(
                        new Runnable(){
                            @Override
                            public void run() {
                                Main.tabLayout.getTabAt(3).select();
                            }
                        }, 100);

                finish();
            }
        });

        size1 = (ImageView)findViewById(R.id.size1);
        size2 = (ImageView)findViewById(R.id.size2);
        size3 = (ImageView)findViewById(R.id.size3);
        size4 = (ImageView)findViewById(R.id.size4);
        size5 = (ImageView)findViewById(R.id.size5);

        fur1 = (ImageView)findViewById(R.id.fur1);
        fur2 = (ImageView)findViewById(R.id.fur2);
        fur3 = (ImageView)findViewById(R.id.fur3);
        fur4 = (ImageView)findViewById(R.id.fur4);
        fur5 = (ImageView)findViewById(R.id.fur5);

        sociability1 = (ImageView)findViewById(R.id.sociability1);
        sociability2 = (ImageView)findViewById(R.id.sociability2);
        sociability3 = (ImageView)findViewById(R.id.sociability3);
        sociability4 = (ImageView)findViewById(R.id.sociability4);
        sociability5 = (ImageView)findViewById(R.id.sociability5);

        activity1 = (ImageView)findViewById(R.id.activity1);
        activity2 = (ImageView)findViewById(R.id.activity2);
        activity3 = (ImageView)findViewById(R.id.activity3);
        activity4 = (ImageView)findViewById(R.id.activity4);
        activity5 = (ImageView)findViewById(R.id.activity5);

        if (object.size == 5){
            size1.setImageResource(R.drawable.full_heart);
            size2.setImageResource(R.drawable.full_heart);
            size3.setImageResource(R.drawable.full_heart);
            size4.setImageResource(R.drawable.full_heart);
            size5.setImageResource(R.drawable.full_heart);
        }else if(object.size==4){
            size1.setImageResource(R.drawable.full_heart);
            size2.setImageResource(R.drawable.full_heart);
            size3.setImageResource(R.drawable.full_heart);
            size4.setImageResource(R.drawable.full_heart);
            size5.setImageResource(R.drawable.gray_heart);
        }else if(object.size==3){
            size1.setImageResource(R.drawable.full_heart);
            size2.setImageResource(R.drawable.full_heart);
            size3.setImageResource(R.drawable.full_heart);
            size4.setImageResource(R.drawable.gray_heart);
            size5.setImageResource(R.drawable.gray_heart);
        }else if(object.size==2){
            size1.setImageResource(R.drawable.full_heart);
            size2.setImageResource(R.drawable.full_heart);
            size3.setImageResource(R.drawable.gray_heart);
            size4.setImageResource(R.drawable.gray_heart);
            size5.setImageResource(R.drawable.gray_heart);
        }else if(object.size==1){
            size1.setImageResource(R.drawable.full_heart);
            size2.setImageResource(R.drawable.gray_heart);
            size3.setImageResource(R.drawable.gray_heart);
            size4.setImageResource(R.drawable.gray_heart);
            size5.setImageResource(R.drawable.gray_heart);
        }

        if (object.molt==5){
            fur1.setImageResource(R.drawable.full_heart);
            fur2.setImageResource(R.drawable.full_heart);
            fur3.setImageResource(R.drawable.full_heart);
            fur4.setImageResource(R.drawable.full_heart);
            fur5.setImageResource(R.drawable.full_heart);
        }else if(object.molt==4){
            fur1.setImageResource(R.drawable.full_heart);
            fur2.setImageResource(R.drawable.full_heart);
            fur3.setImageResource(R.drawable.full_heart);
            fur4.setImageResource(R.drawable.full_heart);
            fur5.setImageResource(R.drawable.gray_heart);
        }else if(object.molt==3){
            fur1.setImageResource(R.drawable.full_heart);
            fur2.setImageResource(R.drawable.full_heart);
            fur3.setImageResource(R.drawable.full_heart);
            fur4.setImageResource(R.drawable.gray_heart);
            fur5.setImageResource(R.drawable.gray_heart);
        }else if(object.molt==2){
            fur1.setImageResource(R.drawable.full_heart);
            fur2.setImageResource(R.drawable.full_heart);
            fur3.setImageResource(R.drawable.gray_heart);
            fur4.setImageResource(R.drawable.gray_heart);
            fur5.setImageResource(R.drawable.gray_heart);
        }else if(object.molt==1){
            fur1.setImageResource(R.drawable.full_heart);
            fur2.setImageResource(R.drawable.gray_heart);
            fur3.setImageResource(R.drawable.gray_heart);
            fur4.setImageResource(R.drawable.gray_heart);
            fur5.setImageResource(R.drawable.gray_heart);
        }

        if (object.active == 5){
            activity1.setImageResource(R.drawable.full_heart);
            activity2.setImageResource(R.drawable.full_heart);
            activity3.setImageResource(R.drawable.full_heart);
            activity4.setImageResource(R.drawable.full_heart);
            activity5.setImageResource(R.drawable.full_heart);
        }else if(object.active==4){
            activity1.setImageResource(R.drawable.full_heart);
            activity2.setImageResource(R.drawable.full_heart);
            activity3.setImageResource(R.drawable.full_heart);
            activity4.setImageResource(R.drawable.full_heart);
            activity5.setImageResource(R.drawable.gray_heart);
        }else if(object.active==3){
            activity1.setImageResource(R.drawable.full_heart);
            activity2.setImageResource(R.drawable.full_heart);
            activity3.setImageResource(R.drawable.full_heart);
            activity4.setImageResource(R.drawable.gray_heart);
            activity5.setImageResource(R.drawable.gray_heart);
        }else if(object.active==2){
            activity1.setImageResource(R.drawable.full_heart);
            activity2.setImageResource(R.drawable.full_heart);
            activity3.setImageResource(R.drawable.gray_heart);
            activity4.setImageResource(R.drawable.gray_heart);
            activity5.setImageResource(R.drawable.gray_heart);
        }else if(object.active==1){
            activity1.setImageResource(R.drawable.full_heart);
            activity2.setImageResource(R.drawable.gray_heart);
            activity3.setImageResource(R.drawable.gray_heart);
            activity4.setImageResource(R.drawable.gray_heart);
            activity5.setImageResource(R.drawable.gray_heart);
        }

        if (object.sociable==5){
            sociability1.setImageResource(R.drawable.full_heart);
            sociability2.setImageResource(R.drawable.full_heart);
            sociability3.setImageResource(R.drawable.full_heart);
            sociability4.setImageResource(R.drawable.full_heart);
            sociability5.setImageResource(R.drawable.full_heart);
        }else if(object.sociable==4){
            sociability1.setImageResource(R.drawable.full_heart);
            sociability2.setImageResource(R.drawable.full_heart);
            sociability3.setImageResource(R.drawable.full_heart);
            sociability4.setImageResource(R.drawable.full_heart);
            sociability5.setImageResource(R.drawable.gray_heart);
        }else if(object.sociable==3){
            sociability1.setImageResource(R.drawable.full_heart);
            sociability2.setImageResource(R.drawable.full_heart);
            sociability3.setImageResource(R.drawable.full_heart);
            sociability4.setImageResource(R.drawable.gray_heart);
            sociability5.setImageResource(R.drawable.gray_heart);
        }else if(object.sociable==2){
            sociability1.setImageResource(R.drawable.full_heart);
            sociability2.setImageResource(R.drawable.full_heart);
            sociability3.setImageResource(R.drawable.gray_heart);
            sociability4.setImageResource(R.drawable.gray_heart);
            sociability5.setImageResource(R.drawable.gray_heart);
        }else if(object.sociable==1){
            sociability1.setImageResource(R.drawable.full_heart);
            sociability2.setImageResource(R.drawable.gray_heart);
            sociability3.setImageResource(R.drawable.gray_heart);
            sociability4.setImageResource(R.drawable.gray_heart);
            sociability5.setImageResource(R.drawable.gray_heart);
        }

        //1등
        if (object.breed==1){
            first_match_image.setImageResource(R.drawable.maltese1);
            match_text2.setText("말티즈");
            match_explation_title.setText("한 눈으로 보는 말티즈!");
        }else if(object.breed==2){
            first_match_image.setImageResource(R.drawable.minipin1);
            match_text2.setText("미니핀");
            match_explation_title.setText("한 눈으로 보는 미니핀!");
        }else if(object.breed==3){
            first_match_image.setImageResource(R.drawable.yorkshire1);
            match_text2.setText("요크셔");
            match_explation_title.setText("한 눈으로 보는 요크셔!");
        }else if(object.breed==4){
            first_match_image.setImageResource(R.drawable.chihuahua1);
            match_text2.setText("치와와");
            match_explation_title.setText("한 눈으로 보는 치와와!");
        }else if(object.breed==5){
            first_match_image.setImageResource(R.drawable.poodle1);
            match_text2.setText("푸들");
            match_explation_title.setText("한 눈으로 보는 푸들!");
        }else if(object.breed==6){
            first_match_image.setImageResource(R.drawable.dachshund1);
            match_text2.setText("닥스훈트");
            match_explation_title.setText("한 눈으로 보는 닥스훈트!");
        }else if(object.breed==7){
            first_match_image.setImageResource(R.drawable.schnauzer1);
            match_text2.setText("슈나우저");
            match_explation_title.setText("한 눈으로 보는 슈나우저!");
        }else if(object.breed==8){
            first_match_image.setImageResource(R.drawable.bichon1);
            match_text2.setText("비숑");
            match_explation_title.setText("한 눈으로 보는 비숑!");
        }else if(object.breed==9){
            first_match_image.setImageResource(R.drawable.shitzu1);
            match_text2.setText("시츄");
            match_explation_title.setText("한 눈으로 보는 시츄!");
        }else if(object.breed==10){
            first_match_image.setImageResource(R.drawable.pomeranian1);
            match_text2.setText("포메리안");
            match_explation_title.setText("한 눈으로 보는 포메리안!");
        }else if(object.breed==11){
            first_match_image.setImageResource(R.drawable.bulldog_1);
            match_text2.setText("불독");
            match_explation_title.setText("한 눈으로 보는 불독!");
        }else if(object.breed==12){
            first_match_image.setImageResource(R.drawable.bullterrier1);
            match_text2.setText("불테리어");
            match_explation_title.setText("한 눈으로 보는 불테리어!");
        }else if(object.breed==13){
            first_match_image.setImageResource(R.drawable.beagle1);
            match_text2.setText("비글");
            match_explation_title.setText("한 눈으로 보는 비글!");
        }else if(object.breed==14){
            first_match_image.setImageResource(R.drawable.cocker1);
            match_text2.setText("코카");
            match_explation_title.setText("한 눈으로 보는 코카!");
        }else if(object.breed==15){
            first_match_image.setImageResource(R.drawable.jindo_1);
            match_text2.setText("진돗개");
            match_explation_title.setText("한 눈으로 보는 진돗개!");
        }else if(object.breed==16){
            first_match_image.setImageResource(R.drawable.poongsan1);
            match_text2.setText("풍산개");
            match_explation_title.setText("한 눈으로 보는 풍산개!");
        }else if(object.breed==17){
            first_match_image.setImageResource(R.drawable.siba1);
            match_text2.setText("시바견");
            match_explation_title.setText("한 눈으로 보는 시바견!");
        }else if(object.breed==18){
            first_match_image.setImageResource(R.drawable.welshcorgi1);
            match_text2.setText("웰시코기");
            match_explation_title.setText("한 눈으로 보는 웰시코기!");
        }else if(object.breed==19){
            first_match_image.setImageResource(R.drawable.retriever_1);
            match_text2.setText("리트리버");
            match_explation_title.setText("한 눈으로 보는 리트리버!");
        }else if(object.breed==20){
            first_match_image.setImageResource(R.drawable.malamute1);
            match_text2.setText("말라뮤트");
            match_explation_title.setText("한 눈으로 보는 말라뮤트!");
        }else if(object.breed==21){
            first_match_image.setImageResource(R.drawable.husky1);
            match_text2.setText("허스키");
            match_explation_title.setText("한 눈으로 보는 허스키!");
        }else if(object.breed==22){
            first_match_image.setImageResource(R.drawable.shepherd1);
            match_text2.setText("세퍼트");
            match_explation_title.setText("한 눈으로 보는 세퍼트!");
        }else if(object.breed==23){
            first_match_image.setImageResource(R.drawable.rottweiler1);
            match_text2.setText("로트바일러");
            match_explation_title.setText("한 눈으로 보는 로트바일러!");
        }

        //2등
        if (object1.breed==1){
            match_second_image.setImageResource(R.drawable.maltese_234);
        }else if(object1.breed==2){
            match_second_image.setImageResource(R.drawable.minipin_234);
        }else if(object1.breed==3){
            match_second_image.setImageResource(R.drawable.yorkshire_234);
        }else if(object1.breed==4){
            match_second_image.setImageResource(R.drawable.chihuahua_234);
        }else if(object1.breed==5){
            match_second_image.setImageResource(R.drawable.poodle_234);
        }else if(object1.breed==6){
            match_second_image.setImageResource(R.drawable.dachshund_234);
        }else if(object1.breed==7){
            match_second_image.setImageResource(R.drawable.schnauzer_234);
        }else if(object1.breed==8){
            match_second_image.setImageResource(R.drawable.bichon_234);
        }else if(object1.breed==9){
            match_second_image.setImageResource(R.drawable.shitzu_234);
        }else if(object1.breed==10){
            match_second_image.setImageResource(R.drawable.pomeranian_234);
        }else if(object1.breed==11){
            match_second_image.setImageResource(R.drawable.bulldog_234);
        }else if(object1.breed==12){
            match_second_image.setImageResource(R.drawable.bullterrier_234);
        }else if(object1.breed==13){
            match_second_image.setImageResource(R.drawable.beagle_234);
        }else if(object1.breed==14){
            match_second_image.setImageResource(R.drawable.cocker_234);
        }else if(object1.breed==15){
            match_second_image.setImageResource(R.drawable.jindo_234);
        }else if(object1.breed==16){
            match_second_image.setImageResource(R.drawable.poongsan_234);
        }else if(object1.breed==17){
            match_second_image.setImageResource(R.drawable.siba_234);
        }else if(object1.breed==18){
            match_second_image.setImageResource(R.drawable.welsh_corgi_234);
        }else if(object1.breed==19){
            match_second_image.setImageResource(R.drawable.retriever_234);
        }else if(object1.breed==20){
            match_second_image.setImageResource(R.drawable.malamute_234);
        }else if(object1.breed==21){
            match_second_image.setImageResource(R.drawable.husky_234);
        }else if(object1.breed==22){
            match_second_image.setImageResource(R.drawable.shepherd_234);
        }else if(object1.breed==23){
            match_second_image.setImageResource(R.drawable.rottweiler_234);
        }

        //3등
        if (object2.breed==1){
            match_third_image.setImageResource(R.drawable.maltese_234);
        }else if(object2.breed==2){
            match_third_image.setImageResource(R.drawable.minipin_234);
        }else if(object2.breed==3){
            match_third_image.setImageResource(R.drawable.yorkshire_234);
        }else if(object2.breed==4){
            match_third_image.setImageResource(R.drawable.chihuahua_234);
        }else if(object2.breed==5){
            match_third_image.setImageResource(R.drawable.poodle_234);
        }else if(object2.breed==6){
            match_third_image.setImageResource(R.drawable.dachshund_234);
        }else if(object2.breed==7){
            match_third_image.setImageResource(R.drawable.schnauzer_234);
        }else if(object2.breed==8){
            match_third_image.setImageResource(R.drawable.bichon_234);
        }else if(object2.breed==9){
            match_third_image.setImageResource(R.drawable.shitzu_234);
        }else if(object2.breed==10){
            match_third_image.setImageResource(R.drawable.pomeranian_234);
        }else if(object2.breed==11){
            match_third_image.setImageResource(R.drawable.bulldog_234);
        }else if(object2.breed==12){
            match_third_image.setImageResource(R.drawable.bullterrier_234);
        }else if(object2.breed==13){
            match_third_image.setImageResource(R.drawable.beagle_234);
        }else if(object2.breed==14){
            match_third_image.setImageResource(R.drawable.cocker_234);
        }else if(object2.breed==15){
            match_third_image.setImageResource(R.drawable.jindo_234);
        }else if(object2.breed==16){
            match_third_image.setImageResource(R.drawable.poongsan_234);
        }else if(object2.breed==17){
            match_third_image.setImageResource(R.drawable.siba_234);
        }else if(object2.breed==18){
            match_third_image.setImageResource(R.drawable.welsh_corgi_234);
        }else if(object2.breed==19){
            match_third_image.setImageResource(R.drawable.retriever_234);
        }else if(object2.breed==20){
            match_third_image.setImageResource(R.drawable.malamute_234);
        }else if(object2.breed==21){
            match_third_image.setImageResource(R.drawable.husky_234);
        }else if(object2.breed==22){
            match_third_image.setImageResource(R.drawable.shepherd_234);
        }else if(object2.breed==23){
            match_third_image.setImageResource(R.drawable.rottweiler_234);
        }

        //4등
        if (object3.breed==1){
            match_fourth_image.setImageResource(R.drawable.maltese_234);
        }else if(object3.breed==2){
            match_fourth_image.setImageResource(R.drawable.minipin_234);
        }else if(object3.breed==3){
            match_fourth_image.setImageResource(R.drawable.yorkshire_234);
        }else if(object3.breed==4){
            match_fourth_image.setImageResource(R.drawable.chihuahua_234);
        }else if(object3.breed==5){
            match_fourth_image.setImageResource(R.drawable.poodle_234);
        }else if(object3.breed==6){
            match_fourth_image.setImageResource(R.drawable.dachshund_234);
        }else if(object3.breed==7){
            match_fourth_image.setImageResource(R.drawable.schnauzer_234);
        }else if(object3.breed==8){
            match_fourth_image.setImageResource(R.drawable.bichon_234);
        }else if(object3.breed==9){
            match_fourth_image.setImageResource(R.drawable.shitzu_234);
        }else if(object3.breed==10){
            match_fourth_image.setImageResource(R.drawable.pomeranian_234);
        }else if(object3.breed==11){
            match_fourth_image.setImageResource(R.drawable.bulldog_234);
        }else if(object3.breed==12){
            match_fourth_image.setImageResource(R.drawable.bullterrier_234);
        }else if(object3.breed==13){
            match_fourth_image.setImageResource(R.drawable.beagle_234);
        }else if(object3.breed==14){
            match_fourth_image.setImageResource(R.drawable.cocker_234);
        }else if(object3.breed==15){
            match_fourth_image.setImageResource(R.drawable.jindo_234);
        }else if(object3.breed==16){
            match_fourth_image.setImageResource(R.drawable.poongsan_234);
        }else if(object3.breed==17){
            match_fourth_image.setImageResource(R.drawable.siba_234);
        }else if(object3.breed==18){
            match_fourth_image.setImageResource(R.drawable.welsh_corgi_234);
        }else if(object3.breed==19){
            match_fourth_image.setImageResource(R.drawable.retriever_234);
        }else if(object3.breed==20){
            match_fourth_image.setImageResource(R.drawable.malamute_234);
        }else if(object3.breed==21){
            match_fourth_image.setImageResource(R.drawable.husky_234);
        }else if(object3.breed==22){
            match_fourth_image.setImageResource(R.drawable.shepherd_234);
        }else if(object3.breed==23){
            match_fourth_image.setImageResource(R.drawable.rottweiler_234);
        }
    }
}
