package wepet.projectbase.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import wepet.projectbase.R;
import wepet.projectbase.activity.Match;

/**
 * Created by ccei on 2016-08-15.
 */
public class MypageMatchNoneResult extends Fragment {


    ImageView notYetMatch;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mypage_match_result_fragment2, container, false);
        notYetMatch = (ImageView) v.findViewById(R.id.not_yet);
        notYetMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Match.class);
                startActivity(intent);
            }
        });
        return v;
    }


}
