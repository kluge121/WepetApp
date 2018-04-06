package wepet.projectbase.activity;

/**
 * Created by ccei on 2016-08-19.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;

import wepet.projectbase.R;
import wepet.projectbase.adapter.MultiAdapter;

public class DialogActivity extends FontActivity {

    ImageView chatBack;

    RecyclerView recyclerView;
    MultiAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    private ArrayList<Chat_Data> myDataset;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        chatBack =(ImageView)findViewById(R.id.chatBack);
        chatBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        myDataset= new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.chatRecycler);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        myDataset.add(new Chat_Data(R.drawable.suji,"안녕하세요","8월5일am9:00",0));
        myDataset.add(new Chat_Data(0,"안녕하세요!!","8월5일am9:01",1));
        myDataset.add(new Chat_Data(R.drawable.suji,"안녕하세요","8월5일am9:00",0));
        myDataset.add(new Chat_Data(0,"안녕하세요!!","8월5일am9:01",1));
        myDataset.add(new Chat_Data(R.drawable.suji,"안녕하세요","8월5일am9:00",0));
        myDataset.add(new Chat_Data(0,"안녕하세요!!","8월5일am9:01",1));
        myDataset.add(new Chat_Data(R.drawable.suji,"안녕하세요","8월5일am9:00",0));
        myDataset.add(new Chat_Data(0,"안녕하세요!!","8월5일am9:01",1));
        myDataset.add(new Chat_Data(R.drawable.suji,"안녕하세요","8월5일am9:00",0));
        myDataset.add(new Chat_Data(0,"안녕하세요!!","8월5일am9:01",1));


        mAdapter = new MultiAdapter(myDataset);

        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}