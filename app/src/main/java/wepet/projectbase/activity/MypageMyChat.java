package wepet.projectbase.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import wepet.projectbase.R;
import wepet.projectbase.SampleListSetting;
import wepet.projectbase.adapter.Mypage_Chat_Adapter;

public class MypageMyChat extends FontActivity {

    RecyclerView chatRecycler;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_chat);

        CheckBox btncheck = (CheckBox) findViewById(R.id.btncheck);
        TextView txtDelete = (TextView)findViewById(R.id.txtDelete);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar2);
        toolbar.setContentInsetsAbsolute(0, 0);

        backBtn = (ImageButton)findViewById(R.id.profile_chat_back);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        chatRecycler = (RecyclerView)findViewById(R.id.recyclerView);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        chatRecycler.setHasFixedSize(true);
        chatRecycler.setLayoutManager(layoutManager);

        chatRecycler.setAdapter(new Mypage_Chat_Adapter(SampleListSetting.sample_mypage_Chat));
    }
}
