package wepet.projectbase.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import wepet.projectbase.R;

/**
 * Created by ccei on 2016-07-28.
 */
public class EnvironmentSetting extends FontActivity {
    private ImageButton settingExit;
    private RelativeLayout enviromentLayout1, enviromentLayout2, enviromentLayout3, enviromentLayout4, noticeLayout;
    private ToggleButton toggleButton1, toggleButton2, toggleButton3, toggleButton4,toggleButton5;
    private ImageView enviromentLine;
    boolean select = false;
    boolean select1 = false, select2 = false, select3 = false, select4 = false;
    String TAG = "EnviromentSetting";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment_setting);


        settingExit = (ImageButton)findViewById(R.id.environment_backey);
        settingExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }});

        final Toolbar toolbar =(Toolbar)findViewById(R.id.environmenttoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setContentInsetsAbsolute(0,0);

        enviromentLayout1 = (RelativeLayout)findViewById(R.id.enviromentLayout1);
        enviromentLayout2 = (RelativeLayout)findViewById(R.id.enviromentLayout2);
        enviromentLayout3 = (RelativeLayout)findViewById(R.id.enviromentLayout3);
        enviromentLayout4 = (RelativeLayout)findViewById(R.id.enviromentLayout4);
        noticeLayout = (RelativeLayout)findViewById(R.id.noticeLayout);

        enviromentLine = (ImageView)findViewById(R.id.enviromentLine);

        toggleButton2 = (ToggleButton)findViewById(R.id.toggleButton2);
        toggleButton3 = (ToggleButton)findViewById(R.id.toggleButton3);
        toggleButton4 = (ToggleButton)findViewById(R.id.toggleButton4);
        toggleButton5 = (ToggleButton)findViewById(R.id.toggleButton5);

        toggleButton1 = (ToggleButton)findViewById(R.id.toggleButton1);
        Log.e("click!", toggleButton1+"");
        Log.e("click!", PropertyManager.getInstance().getSetting1()+"");
        Log.e("click!", PropertyManager.getInstance().getSetting2()+"");
        Log.e("click!", PropertyManager.getInstance().getSetting3()+"");
        Log.e("click!", PropertyManager.getInstance().getSetting4()+"");

        Log.e("click!", ((PropertyManager.getInstance().getSetting1().compareTo("T")==0)?true:false)+"");
        Log.e("click!", ((PropertyManager.getInstance().getSetting2().compareTo("T")==0)?true:false)+"");
        Log.e("click!", ((PropertyManager.getInstance().getSetting3().compareTo("T")==0)?true:false)+"");
        Log.e("click!", ((PropertyManager.getInstance().getSetting4().compareTo("T")==0)?true:false)+"");

        boolean t1 = (PropertyManager.getInstance().getSetting1().compareTo("T")==0)?true:false;
        boolean t2 = (PropertyManager.getInstance().getSetting2().compareTo("T")==0)?true:false;
        boolean t3 = (PropertyManager.getInstance().getSetting3().compareTo("T")==0)?true:false;
        boolean t4 = (PropertyManager.getInstance().getSetting4().compareTo("T")==0)?true:false;


        toggleButton2.setChecked(t1);
        toggleButton3.setChecked(t2);
        toggleButton4.setChecked(t3);
        toggleButton5.setChecked(t4);
        if( t1 || t2 || t3 || t4 ){
            toggleButton1.setChecked(true);
            enviromentLayout1.setVisibility(View.VISIBLE);
            enviromentLayout2.setVisibility(View.VISIBLE);
            enviromentLayout3.setVisibility(View.VISIBLE);
            enviromentLayout4.setVisibility(View.VISIBLE);
            noticeLayout.setVisibility(View.GONE);
        }


//        if (PropertyManager.getInstance().getSetting1()==null){
//            toggleButton1.setChecked(false);
//            toggleButton2.setChecked(false);
//            toggleButton3.setChecked(false);
//            toggleButton4.setChecked(false);
//            toggleButton5.setChecked(false);
//        }
        toggleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(select == false){
                    toggleButton1.setChecked(true);
                    toggleButton2.setChecked(true);
                    toggleButton3.setChecked(true);
                    toggleButton4.setChecked(true);
                    toggleButton5.setChecked(true);

                    select = true;

                    PropertyManager.getInstance().setSetting1("T");
                    PropertyManager.getInstance().setSetting2("T");
                    PropertyManager.getInstance().setSetting3("T");
                    PropertyManager.getInstance().setSetting4("T");

                    enviromentLayout1.setVisibility(View.VISIBLE);
                    enviromentLayout2.setVisibility(View.VISIBLE);
                    enviromentLayout3.setVisibility(View.VISIBLE);
                    enviromentLayout4.setVisibility(View.VISIBLE);
                    noticeLayout.setVisibility(View.GONE);

                }else if (select == true){
                    toggleButton1.setChecked(false);
                    toggleButton2.setChecked(false);
                    toggleButton3.setChecked(false);
                    toggleButton4.setChecked(false);
                    toggleButton5.setChecked(false);

                    select=false;

                    PropertyManager.getInstance().setSetting1("F");
                    PropertyManager.getInstance().setSetting2("F");
                    PropertyManager.getInstance().setSetting3("F");
                    PropertyManager.getInstance().setSetting4("F");

                    enviromentLayout1.setVisibility(View.GONE);
                    enviromentLayout2.setVisibility(View.GONE);
                    enviromentLayout3.setVisibility(View.GONE);
                    enviromentLayout4.setVisibility(View.GONE);
                    noticeLayout.setVisibility(View.VISIBLE);

                }
            }
        });

        //1등 setting
        toggleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select1==false){
                    toggleButton2.setChecked(true);
                    PropertyManager.getInstance().setSetting1("T");
                    select1=true;
                }else if (select1==true){
                    toggleButton2.setChecked(false);
                    PropertyManager.getInstance().setSetting1("F");
                    select1=false;
                }
            }
        });

        //2등 setting
        toggleButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select2==false){
                    toggleButton3.setChecked(true);
                    PropertyManager.getInstance().setSetting2("T");
                    select2=true;
                }else if (select2==true){
                    toggleButton3.setChecked(false);
                    PropertyManager.getInstance().setSetting2("F");
                    select2=false;
                }
            }
        });
        toggleButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select3==false){
                    toggleButton4.setChecked(true);
                    PropertyManager.getInstance().setSetting3("T");
                    select3=true;
                }else if (select3==true){
                    toggleButton4.setChecked(false);
                    PropertyManager.getInstance().setSetting3("F");
                    select3=false;
                }
            }
        });
        toggleButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (select4==false){
                    toggleButton5.setChecked(true);
                    PropertyManager.getInstance().setSetting4("T");
                    select4=true;
                }else if (select4==true){
                    toggleButton5.setChecked(false);
                    PropertyManager.getInstance().setSetting4("F");
                    select4=false;
                }
            }
        });

    }
}
