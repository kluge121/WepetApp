package wepet.projectbase.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import wepet.projectbase.R;
import wepet.projectbase.entity.DetailSearchEntity;

public class SearchMissing extends FontActivity {
    int breed, gender, age, region, neuter, vaccin;

    private ImageView adopt_breeds, adopt_gender, adopt_age, adopt_live, adopt_neutralize, adopt_inoculation;
    private TextView breedInfo, genderInfo, ageInfo, localInfo, neutralizeInfo, vaccinInfo;

    private ImageButton backBtn;

    private ImageView searchAdopt;

    final int ADOPT_DIALOG_RADIO1 = 1;
    final int ADOPT_DIALOG_RADIO2 = 2;
    final int ADOPT_DIALOG_RADIO3 = 3;
    final int ADOPT_DIALOG_RADIO4 = 4;
    final int ADOPT_DIALOG_RADIO5 = 5;
    final int ADOPT_DIALOG_RADIO6 = 6;

    public DetailSearchEntity entity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_missing_layout);

        Toolbar toolbar = (Toolbar)findViewById(R.id.missing_toolbar);
        toolbar.setContentInsetsAbsolute(0,0);

        backBtn =(ImageButton)findViewById(R.id.missing_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        breedInfo = (TextView)findViewById(R.id.breedInfo1);
        genderInfo = (TextView)findViewById(R.id.genderInfo1);
        ageInfo = (TextView)findViewById(R.id.ageInfo1);
        localInfo = (TextView)findViewById(R.id.localInfo1);
        neutralizeInfo = (TextView)findViewById(R.id.neutralizeInfo1);
        vaccinInfo = (TextView)findViewById(R.id.vaccinInfo1);


        searchAdopt = (ImageView)findViewById(R.id.missingBtn);
        searchAdopt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent1 = getIntent();
                entity = new DetailSearchEntity();
                entity.tab = intent1.getExtras().getInt("tab");
                entity.breed = breed;
                entity.gender = gender;
                entity.age = age;
                entity.neuter = neuter;
                entity.region = region;
                entity.vaccin = vaccin;


                Main.DetailSearchCheck = 1;
                Intent intent = new Intent(getApplication(),Main.class);
                intent.putExtra("data",entity);
                startActivity(intent);
                finish();
            }
        });

        adopt_breeds = (ImageView)findViewById(R.id.missing_breeds);
        adopt_breeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(ADOPT_DIALOG_RADIO1);
            }
        });

        adopt_gender = (ImageView)findViewById(R.id.missing_gender);
        adopt_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(ADOPT_DIALOG_RADIO2);
            }
        });

        adopt_age = (ImageView)findViewById(R.id.missing_age);
        adopt_age.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showDialog(ADOPT_DIALOG_RADIO3);
            }
        });

        adopt_live = (ImageView)findViewById(R.id.missing_live);
        adopt_live.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showDialog(ADOPT_DIALOG_RADIO4);
            }
        });

        adopt_neutralize = (ImageView)findViewById(R.id.missing_neutralize);
        adopt_neutralize.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showDialog(ADOPT_DIALOG_RADIO5);
            }
        });

        adopt_inoculation = (ImageView)findViewById(R.id.missing_inoculation);
        adopt_inoculation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showDialog(ADOPT_DIALOG_RADIO6);
            }
        });
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        Log.d("test", "OnCreateDialog");

        switch (id) {
            case ADOPT_DIALOG_RADIO1:
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(SearchMissing.this);
                final String str[] = {"기타","말티즈","미니핀","요크셔","치와와","푸들","닥스훈트","슈나우저","비숑","시츄","포메라니안",
                        "불독","불테리어","비글","코카","진돗개","풍산개","시바견","웰시코기","리트리버","말라뮤트","허스키","세퍼트","로트바일러","믹스견"};
                builder.setTitle("동물의 견종")
                        .setPositiveButton("선택",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        breedInfo.setText(str[breed]);

                                    }
                                })
                        .setNegativeButton("취소", null)
                        .setSingleChoiceItems(str, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                breed = which;

                            }
                        });// 리스너

                return builder.create(); // 다이얼로그 생성한 객체 리턴

            case ADOPT_DIALOG_RADIO2:
                AlertDialog.Builder builder1 =
                        new AlertDialog.Builder(SearchMissing.this);
                final String str1[] = {"전체","수컷","암컷"};
                builder1.setTitle("동물의 성별은?")
                        .setPositiveButton("선택", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                genderInfo.setText(str1[gender]);
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setSingleChoiceItems(str1, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                gender = which;
                            }
                        });// 리스너

                return builder1.create(); // 다이얼로그 생성한 객체 리턴

            case ADOPT_DIALOG_RADIO3:
                AlertDialog.Builder builder2 =
                        new AlertDialog.Builder(SearchMissing.this);
                final String str2[] = {"전체", "0~1살","2~3살", "4~8살", "9~11살", "12살 이상"};
                builder2.setTitle("동물의 나이는?")
                        .setPositiveButton("선택",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        ageInfo.setText(str2[age]);
                                    }
                                })
                        .setNegativeButton("취소", null)
                        .setSingleChoiceItems(str2,-1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                age = which;
                            }
                        });// 리스너


                return builder2.create(); // 다이얼로그 생성한 객체 리턴
            case ADOPT_DIALOG_RADIO4:
                AlertDialog.Builder builder3 =
                        new AlertDialog.Builder(SearchMissing.this);
                final String str3[] = {"전체","서울","경기","인천","강원","대전","세종","충남","충북","부산","울산","경남","경북","대구","광주","전남","전북","제주도"};
                builder3.setTitle("거주지역은?")
                        .setPositiveButton("선택", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                localInfo.setText(str3[region]);
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setSingleChoiceItems(str3, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                region = which;
                            }
                        });// 리스너
                return builder3.create(); // 다이얼로그 생성한 객체 리턴

            case ADOPT_DIALOG_RADIO5:
                AlertDialog.Builder builder4 =
                        new AlertDialog.Builder(SearchMissing.this);
                final String str4[] =  {"전체", "중성화함","중성화안함"};
                builder4.setTitle("중성화 여부는?")
                        .setPositiveButton("선택",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        neutralizeInfo.setText(str4[neuter]);
                                    }
                                })
                        .setNegativeButton("취소", null)
                        .setSingleChoiceItems(str4, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                neuter = which;
                            }
                        });// 리스너
                return builder4.create(); // 다이얼로그 생성한 객체 리턴

            case  ADOPT_DIALOG_RADIO6:
                AlertDialog.Builder builder5 =
                        new AlertDialog.Builder(SearchMissing.this);
                final String str5[] =  {"접종차수모름","접종차수1차","접종차수2차","접종차수3차","접종차수4차","접종차수5차"};
                builder5.setTitle("동물의 예방접종 차수는?")
                        .setPositiveButton("선택", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                vaccinInfo.setText(str5[vaccin]);
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setSingleChoiceItems(str5, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                vaccin = which;
                            }
                        });// 리스너
                return builder5.create(); // 다이얼로그 생성한 객체 리턴
        }
        return super.onCreateDialog(id);
    }

}
