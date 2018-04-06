package wepet.projectbase.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import wepet.projectbase.R;

public class SearchMatch extends FontActivity {
    int temp;

    private ImageButton backBtn;

    private ImageView btn_gender, btn_age, btn_live, btn_neutralize, btn_inoculation;

    final int DIALOG_RADIO1 = 1;
    final int DIALOG_RADIO2 = 2;
    final int DIALOG_RADIO3 = 3;
    final int DIALOG_RADIO4 = 4;
    final int DIALOG_RADIO5 = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_match_layout);

        Toolbar toolbar = (Toolbar)findViewById(R.id.match_toolbar);
        toolbar.setContentInsetsAbsolute(0,0);

        backBtn =(ImageButton)findViewById(R.id.match_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();;
            }
        });


        btn_gender = (ImageView)findViewById(R.id.btn_gender);
        btn_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_RADIO1);
            }
        });

        btn_age = (ImageView)findViewById(R.id.btn_age);
        btn_age.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_RADIO2);
            }
        });

        btn_live = (ImageView)findViewById(R.id.btn_live);
        btn_live.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_RADIO3);
            }
        });

        btn_neutralize = (ImageView)findViewById(R.id.btn_neutralize);
        btn_neutralize.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_RADIO4);
            }
        });

        btn_inoculation = (ImageView)findViewById(R.id.btn_inoculation);
        btn_inoculation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_RADIO5);
            }
        });
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        Log.d("test", "OnCreateDialog");

        switch (id) {
            case DIALOG_RADIO1:
                AlertDialog.Builder builder1 =
                        new AlertDialog.Builder(SearchMatch.this);
                final String str1[] = {"여아", "남아", "둘다"};
                builder1.setTitle("동물의 성별은?")
                        .setPositiveButton("선택",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(),
                                                str1[temp] + "을 선택했음",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton("취소", null)
                        .setSingleChoiceItems(str1, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                temp = which;
                            }
                        });// 리스너

                return builder1.create(); // 다이얼로그 생성한 객체 리턴

            case DIALOG_RADIO2:
                AlertDialog.Builder builder2 =
                        new AlertDialog.Builder(SearchMatch.this);
                final String str2[] = {"1~10", "10~20", "20~30"};
                builder2.setTitle("동물의 나이는??")
                        .setPositiveButton("선택",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(),
                                                str2[temp] + "을 선택했음",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton("취소", null)
                        .setSingleChoiceItems(str2,-1, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                temp = which;
                                            }
                                        });// 리스너


                return builder2.create(); // 다이얼로그 생성한 객체 리턴
            case DIALOG_RADIO3:
                AlertDialog.Builder builder3 =
                        new AlertDialog.Builder(SearchMatch.this);
                final String str3[] = {"주택", "아파트", "어딘가"};
                builder3.setTitle("거주지역은?")
                        .setPositiveButton("선택", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(),
                                                str3[temp] + "을 선택했음",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton("취소", null)
                        .setSingleChoiceItems(str3, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                temp = which;
                            }
                        });// 리스너
                return builder3.create(); // 다이얼로그 생성한 객체 리턴

            case DIALOG_RADIO4:
                AlertDialog.Builder builder4 =
                        new AlertDialog.Builder(SearchMatch.this);
                final String str4[] = {"했어요","안했어요"};
                builder4.setTitle("중성화 여부는?")
                        .setPositiveButton("선택",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(),
                                                str4[temp] + "을 선택했음",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton("취소", null)
                        .setSingleChoiceItems(str4, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                temp = which;
                            }
                        });// 리스너
                return builder4.create(); // 다이얼로그 생성한 객체 리턴

            case  DIALOG_RADIO5:
                AlertDialog.Builder builder5 =
                        new AlertDialog.Builder(SearchMatch.this);
                final String str5[] = {"했어요","안했어요"};
                builder5.setTitle("동물의 예방접종 차수는?")
                        .setPositiveButton("선택", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplicationContext(),
                                                str5[temp] + "을 선택했음",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton("취소", null)
                        .setSingleChoiceItems(str5, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                temp = which;
                            }
                        });// 리스너
                return builder5.create(); // 다이얼로그 생성한 객체 리턴
        }

        return super.onCreateDialog(id);
    }

} // end of class
