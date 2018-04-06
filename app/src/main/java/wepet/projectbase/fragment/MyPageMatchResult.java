package wepet.projectbase.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import wepet.projectbase.R;
import wepet.projectbase.activity.PropertyManager;
import wepet.projectbase.entity.UserMatchEntity;

/**
 * Created by coco on 2016-07-31.
 */
public class MyPageMatchResult extends Fragment {

    private ImageView msize1, msize2, msize3, msize4, msize5;
    private ImageView mfur1, mfur2, mfur3, mfur4, mfur5;
    private ImageView msociability1, msociability2, msociability3, msociability4, msociability5;
    private ImageView mactivity1, mactivity2, mactivity3, mactivity4, mactivity5;
    private ImageView matchFirstImage, matchSecondImage, matchThirdImage, matchFourthImage;
    private TextView matchResultTitle1, matchResultTitle2;
    private ImageView matchResultExplation;


    public static MyPageMatchResult newInstance(ArrayList<UserMatchEntity> dataObjectArrayList) {

        MyPageMatchResult matchResult = new MyPageMatchResult();

        Bundle b = new Bundle();
        b.putSerializable("datas", dataObjectArrayList);
        matchResult.setArguments(b);

        return matchResult;
    }

    private ArrayList<UserMatchEntity> datas;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        if( b != null)
            datas = (ArrayList<UserMatchEntity>)b.getSerializable("datas");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if( datas != null)
            Log.e("MyPageMatchResult", String.valueOf(datas.size()));
        View v = inflater.inflate(R.layout.mypage_match_result_fragment,container,false);

        matchResultTitle1 = (TextView)v.findViewById(R.id.mypage_match_result_ftitle);
        matchResultTitle2 = (TextView)v.findViewById(R.id.mypage_match_result_ftitle2);

        msize1 = (ImageView)v.findViewById(R.id.msize1);
        msize2 = (ImageView)v.findViewById(R.id.msize2);
        msize3 = (ImageView)v.findViewById(R.id.msize3);
        msize4 = (ImageView)v.findViewById(R.id.msize4);
        msize5 = (ImageView)v.findViewById(R.id.msize5);

        mfur1 = (ImageView)v.findViewById(R.id.mfur1);
        mfur2 = (ImageView)v.findViewById(R.id.mfur2);
        mfur3 = (ImageView)v.findViewById(R.id.mfur3);
        mfur4 = (ImageView)v.findViewById(R.id.mfur4);
        mfur5 = (ImageView)v.findViewById(R.id.mfur5);

        msociability1 = (ImageView)v.findViewById(R.id.msociability1);
        msociability2 = (ImageView)v.findViewById(R.id.msociability2);
        msociability3 = (ImageView)v.findViewById(R.id.msociability3);
        msociability4 = (ImageView)v.findViewById(R.id.msociability4);
        msociability5 = (ImageView)v.findViewById(R.id.msociability5);

        mactivity1 = (ImageView)v.findViewById(R.id.mactivity1);
        mactivity2 = (ImageView)v.findViewById(R.id.mactivity2);
        mactivity3 = (ImageView)v.findViewById(R.id.mactivity3);
        mactivity4 = (ImageView)v.findViewById(R.id.mactivity4);
        mactivity5 = (ImageView)v.findViewById(R.id.mactivity5);

        matchFirstImage = (ImageView)v.findViewById(R.id.mypage_match_result_fimage);
        matchSecondImage = (ImageView)v.findViewById(R.id.mmatch_second_image);
        matchThirdImage = (ImageView)v.findViewById(R.id.mmatch_third_image);
        matchFourthImage = (ImageView)v.findViewById(R.id.mmatch_fourth_image);

        UserMatchEntity object = datas.get(0);
        UserMatchEntity object1 = datas.get(1);
        UserMatchEntity object2 = datas.get(2);
        UserMatchEntity object3 = datas.get(3);



        matchResultExplation = (ImageView)v.findViewById(R.id.mypage_match_result_explation);

        if(object.sociable == 5){
            msociability1.setImageResource(R.drawable.full_heart);
            msociability2.setImageResource(R.drawable.full_heart);
            msociability3.setImageResource(R.drawable.full_heart);
            msociability4.setImageResource(R.drawable.full_heart);
            msociability5.setImageResource(R.drawable.full_heart);
        }else if(object.sociable==4){
            msociability1.setImageResource(R.drawable.full_heart);
            msociability2.setImageResource(R.drawable.full_heart);
            msociability3.setImageResource(R.drawable.full_heart);
            msociability4.setImageResource(R.drawable.full_heart);
            msociability5.setImageResource(R.drawable.gray_heart);
        }else if(object.sociable==3){
            msociability1.setImageResource(R.drawable.full_heart);
            msociability2.setImageResource(R.drawable.full_heart);
            msociability3.setImageResource(R.drawable.full_heart);
            msociability4.setImageResource(R.drawable.gray_heart);
            msociability5.setImageResource(R.drawable.gray_heart);
        }else if(object.sociable==2){
            msociability1.setImageResource(R.drawable.full_heart);
            msociability2.setImageResource(R.drawable.full_heart);
            msociability3.setImageResource(R.drawable.gray_heart);
            msociability4.setImageResource(R.drawable.gray_heart);
            msociability5.setImageResource(R.drawable.gray_heart);
        }else if(object.sociable==1){
            msociability1.setImageResource(R.drawable.full_heart);
            msociability2.setImageResource(R.drawable.gray_heart);
            msociability3.setImageResource(R.drawable.gray_heart);
            msociability4.setImageResource(R.drawable.gray_heart);
            msociability5.setImageResource(R.drawable.gray_heart);
        }

        if(object.size == 5){
            msize1.setImageResource(R.drawable.full_heart);
            msize2.setImageResource(R.drawable.full_heart);
            msize3.setImageResource(R.drawable.full_heart);
            msize4.setImageResource(R.drawable.full_heart);
            msize5.setImageResource(R.drawable.full_heart);
        }else if(object.size == 4){
            msize1.setImageResource(R.drawable.full_heart);
            msize2.setImageResource(R.drawable.full_heart);
            msize3.setImageResource(R.drawable.full_heart);
            msize4.setImageResource(R.drawable.full_heart);
            msize5.setImageResource(R.drawable.gray_heart);
        }else if(object.size ==3){
            msize1.setImageResource(R.drawable.full_heart);
            msize2.setImageResource(R.drawable.full_heart);
            msize3.setImageResource(R.drawable.full_heart);
            msize4.setImageResource(R.drawable.gray_heart);
            msize5.setImageResource(R.drawable.gray_heart);
        }else if(object.size == 2){
            msize1.setImageResource(R.drawable.full_heart);
            msize2.setImageResource(R.drawable.full_heart);
            msize3.setImageResource(R.drawable.gray_heart);
            msize4.setImageResource(R.drawable.gray_heart);
            msize5.setImageResource(R.drawable.gray_heart);
        }else if (object.size == 1){
            msize1.setImageResource(R.drawable.full_heart);
            msize2.setImageResource(R.drawable.gray_heart);
            msize3.setImageResource(R.drawable.gray_heart);
            msize4.setImageResource(R.drawable.gray_heart);
            msize5.setImageResource(R.drawable.gray_heart);
        }

        if(object.molt == 5){
            mfur1.setImageResource(R.drawable.full_heart);
            mfur2.setImageResource(R.drawable.full_heart);
            mfur3.setImageResource(R.drawable.full_heart);
            mfur4.setImageResource(R.drawable.full_heart);
            mfur5.setImageResource(R.drawable.full_heart);
        }else if(object.molt==4){
            mfur1.setImageResource(R.drawable.full_heart);
            mfur2.setImageResource(R.drawable.full_heart);
            mfur3.setImageResource(R.drawable.full_heart);
            mfur4.setImageResource(R.drawable.full_heart);
            mfur5.setImageResource(R.drawable.gray_heart);
        }else if(object.molt==3){
            mfur1.setImageResource(R.drawable.full_heart);
            mfur2.setImageResource(R.drawable.full_heart);
            mfur3.setImageResource(R.drawable.full_heart);
            mfur4.setImageResource(R.drawable.gray_heart);
            mfur5.setImageResource(R.drawable.gray_heart);
        }else if(object.molt==2){
            mfur1.setImageResource(R.drawable.full_heart);
            mfur2.setImageResource(R.drawable.full_heart);
            mfur3.setImageResource(R.drawable.gray_heart);
            mfur4.setImageResource(R.drawable.gray_heart);
            mfur5.setImageResource(R.drawable.gray_heart);
        } else if (object.molt == 1) {
            mfur1.setImageResource(R.drawable.full_heart);
            mfur2.setImageResource(R.drawable.gray_heart);
            mfur3.setImageResource(R.drawable.gray_heart);
            mfur4.setImageResource(R.drawable.gray_heart);
            mfur5.setImageResource(R.drawable.gray_heart);
        }

        if (object.active == 5){
            mactivity1.setImageResource(R.drawable.full_heart);
            mactivity2.setImageResource(R.drawable.full_heart);
            mactivity3.setImageResource(R.drawable.full_heart);
            mactivity4.setImageResource(R.drawable.full_heart);
            mactivity5.setImageResource(R.drawable.full_heart);
        }else if(object.active==4){
            mactivity1.setImageResource(R.drawable.full_heart);
            mactivity2.setImageResource(R.drawable.full_heart);
            mactivity3.setImageResource(R.drawable.full_heart);
            mactivity4.setImageResource(R.drawable.full_heart);
            mactivity5.setImageResource(R.drawable.gray_heart);
        }else if(object.active==3){
            mactivity1.setImageResource(R.drawable.full_heart);
            mactivity2.setImageResource(R.drawable.full_heart);
            mactivity3.setImageResource(R.drawable.full_heart);
            mactivity4.setImageResource(R.drawable.gray_heart);
            mactivity5.setImageResource(R.drawable.gray_heart);
        }else if(object.active == 2){
            mactivity1.setImageResource(R.drawable.full_heart);
            mactivity2.setImageResource(R.drawable.full_heart);
            mactivity3.setImageResource(R.drawable.gray_heart);
            mactivity4.setImageResource(R.drawable.gray_heart);
            mactivity5.setImageResource(R.drawable.gray_heart);
        }else if(object.active == 1){
            mactivity1.setImageResource(R.drawable.full_heart);
            mactivity2.setImageResource(R.drawable.gray_heart);
            mactivity3.setImageResource(R.drawable.gray_heart);
            mactivity4.setImageResource(R.drawable.gray_heart);
            mactivity5.setImageResource(R.drawable.gray_heart);
        }

        if(object.breed == 1){
            matchFirstImage.setImageResource(R.drawable.maltese1);
            matchResultTitle1.setText("1등 말티즈");
            matchResultTitle2.setText("한눈으로 보는 말티즈!");
            matchResultExplation.setImageResource(R.drawable.maltese_explain);
        }else if(object.breed ==2){
            matchFirstImage.setImageResource(R.drawable.minipin1);
            matchResultExplation.setImageResource(R.drawable.minipin_explain);
            matchResultTitle1.setText("1등 미니핀");
            matchResultTitle2.setText("한눈으로 보는 미니핀!");
        }else if(object.breed == 3){
            matchFirstImage.setImageResource(R.drawable.yorkshire1);
            matchResultExplation.setImageResource(R.drawable.yorkshire_explain);
            matchResultTitle1.setText("1등 요크셔");
            matchResultTitle2.setText("한눈으로 보는 요크셔!");
        }else if(object.breed == 4){
            matchFirstImage.setImageResource(R.drawable.chihuahua1);
            matchResultExplation.setImageResource(R.drawable.chihuahua_explain);
            matchResultTitle1.setText("1등 치와와");
            matchResultTitle2.setText("한눈으로 보는 치와와!");
        }else if(object.breed == 5){
            matchFirstImage.setImageResource(R.drawable.poodle1);
            matchResultExplation.setImageResource(R.drawable.puddle_explain);
            matchResultTitle1.setText("1등 푸들");
            matchResultTitle2.setText("한눈으로 보는 푸들!");
        }else if(object.breed == 6){
            matchFirstImage.setImageResource(R.drawable.dachshund1);
            matchResultExplation.setImageResource(R.drawable.dachshund_explain);
            matchResultTitle1.setText("1등 닥스훈트");
            matchResultTitle2.setText("한눈으로 보는 닥스훈트!");
        }else if(object.breed == 7){
            matchFirstImage.setImageResource(R.drawable.schnauzer1);
            matchResultExplation.setImageResource(R.drawable.schnauzer_explain);
            matchResultTitle1.setText("1등 슈나우저");
            matchResultTitle2.setText("한눈으로 보는 슈나우저!");
        }else if(object.breed == 8){
            matchFirstImage.setImageResource(R.drawable.bichon1);
            matchResultExplation.setImageResource(R.drawable.bichon_explain);
            matchResultTitle1.setText("1등 비숑");
            matchResultTitle2.setText("한눈으로 보는 비숑!");
        }else if(object.breed == 9){
            matchFirstImage.setImageResource(R.drawable.shitzu1);
            matchResultExplation.setImageResource(R.drawable.shitzu_explain);
            matchResultTitle1.setText("1등 시츄");
            matchResultTitle2.setText("한눈으로 보는 시츄!");
        }else if(object.breed == 10){
            matchFirstImage.setImageResource(R.drawable.pomeranian1);
            matchResultExplation.setImageResource(R.drawable.pomeranian_explain);
            matchResultTitle1.setText("1등 포메리안");
            matchResultTitle2.setText("한눈으로 보는 포메리안!");
        }else if(object.breed == 11){
            matchFirstImage.setImageResource(R.drawable.bulldog_1);
            matchResultExplation.setImageResource(R.drawable.bulldog_explain);
            matchResultTitle1.setText("1등 불독");
            matchResultTitle2.setText("한눈으로 보는 불독!");
        }else if(object.breed == 12){
            matchFirstImage.setImageResource(R.drawable.bullterrier1);
            matchResultExplation.setImageResource(R.drawable.bullterrier_explain);
            matchResultTitle1.setText("1등 불테리어");
            matchResultTitle2.setText("한눈으로 보는 불테리어!");
        }else if(object.breed == 13){
            matchFirstImage.setImageResource(R.drawable.beagle1);
            matchResultExplation.setImageResource(R.drawable.biggile_explain);
            matchResultTitle1.setText("1등 비글");
            matchResultTitle2.setText("한눈으로 보는 비글!");
        }else if(object.breed == 14){
            matchFirstImage.setImageResource(R.drawable.cocker1);
            matchResultExplation.setImageResource(R.drawable.coca_explain);
            matchResultTitle1.setText("1등 코카");
            matchResultTitle2.setText("한눈으로 보는 코카!");
        }else if(object.breed == 15){
            matchFirstImage.setImageResource(R.drawable.jindo_1);
            matchResultExplation.setImageResource(R.drawable.jindo_explain);
            matchResultTitle1.setText("1등 진돗개");
            matchResultTitle2.setText("한눈으로 보는 진돗개!");
        }else if(object.breed == 16){
            matchFirstImage.setImageResource(R.drawable.poongsan1);
            matchResultExplation.setImageResource(R.drawable.poongsan_explain);
            matchResultTitle1.setText("1등 풍산개");
            matchResultTitle2.setText("한눈으로 보는 풍산개!");
        }else if(object.breed == 17){
            matchFirstImage.setImageResource(R.drawable.siba1);
            matchResultExplation.setImageResource(R.drawable.siba_explain);
            matchResultTitle1.setText("1등 시바견");
            matchResultTitle2.setText("한눈으로 보는 시바견!");
        }else if(object.breed == 18){
            matchFirstImage.setImageResource(R.drawable.welshcorgi1);
            matchResultExplation.setImageResource(R.drawable.welshcorgi_explain);
            matchResultTitle1.setText("1등 웰시코기");
            matchResultTitle2.setText("한눈으로 보는 웰시코기!");
        }else if(object.breed == 19){
            matchFirstImage.setImageResource(R.drawable.retriever_1);
            matchResultExplation.setImageResource(R.drawable.retriever_explain);
            matchResultTitle1.setText("1등 리트리버");
            matchResultTitle2.setText("한눈으로 보는 리트리버!");
        }else if(object.breed == 20){
            matchFirstImage.setImageResource(R.drawable.malamute1);
            matchResultExplation.setImageResource(R.drawable.malamute_explain);
            matchResultTitle1.setText("1등 말라뮤트");
            matchResultTitle2.setText("한눈으로 보는 말라뮤트!");
        }else if(object.breed == 21){
            matchFirstImage.setImageResource(R.drawable.husky1);
            matchResultExplation.setImageResource(R.drawable.husky_explain);
            matchResultTitle1.setText("1등 허스키");
            matchResultTitle2.setText("한눈으로 보는 허스키!");
        }else if(object.breed == 22){
            matchFirstImage.setImageResource(R.drawable.shepherd1);
            matchResultExplation.setImageResource(R.drawable.shepaherd_explain);
            matchResultTitle1.setText("1등 세퍼트");
            matchResultTitle2.setText("한눈으로 보는 세퍼트!");
        }else if(object.breed == 23){
            matchFirstImage.setImageResource(R.drawable.rottweiler1);
            matchResultExplation.setImageResource(R.drawable.rottweiler_explain);
            matchResultTitle1.setText("1등 로트바일러");
            matchResultTitle2.setText("한눈으로 보는 로트바일러!");
        }

        if (object1.breed==1){
            matchSecondImage.setImageResource(R.drawable.maltese_234);
        }else if(object1.breed==2){
            matchSecondImage.setImageResource(R.drawable.minipin_234);
        }else if(object1.breed==3){
            matchSecondImage.setImageResource(R.drawable.yorkshire_234);
        }else if(object1.breed==4){
            matchSecondImage.setImageResource(R.drawable.chihuahua_234);
        }else if(object1.breed==5){
            matchSecondImage.setImageResource(R.drawable.poodle_234);
        }else if(object1.breed==6){
            matchSecondImage.setImageResource(R.drawable.dachshund_234);
        }else if(object1.breed==7){
            matchSecondImage.setImageResource(R.drawable.schnauzer_234);
        }else if(object1.breed==8){
            matchSecondImage.setImageResource(R.drawable.bichon_234);
        }else if(object1.breed==9){
            matchSecondImage.setImageResource(R.drawable.shitzu_234);
        }else if(object1.breed==10){
            matchSecondImage.setImageResource(R.drawable.pomeranian_234);
        }else if(object1.breed==11){
            matchSecondImage.setImageResource(R.drawable.bulldog_234);
        }else if(object1.breed==12){
            matchSecondImage.setImageResource(R.drawable.bullterrier_234);
        }else if(object1.breed==13){
            matchSecondImage.setImageResource(R.drawable.beagle_234);
        }else if(object1.breed==14){
            matchSecondImage.setImageResource(R.drawable.cocker_234);
        }else if(object1.breed==15){
            matchSecondImage.setImageResource(R.drawable.jindo_234);
        }else if(object1.breed==16){
            matchSecondImage.setImageResource(R.drawable.poongsan_234);
        }else if(object1.breed==17){
            matchSecondImage.setImageResource(R.drawable.siba_234);
        }else if(object1.breed==18){
            matchSecondImage.setImageResource(R.drawable.welsh_corgi_234);
        }else if(object1.breed==19){
            matchSecondImage.setImageResource(R.drawable.retriever_234);
        }else if(object1.breed==20){
            matchSecondImage.setImageResource(R.drawable.malamute_234);
        }else if(object1.breed==21){
            matchSecondImage.setImageResource(R.drawable.husky_234);
        }else if(object1.breed==22){
            matchSecondImage.setImageResource(R.drawable.shepherd_234);
        }else if(object1.breed==23){
            matchSecondImage.setImageResource(R.drawable.rottweiler_234);
        }

        if (object2.breed==1){
            matchThirdImage.setImageResource(R.drawable.maltese_234);
        }else if(object2.breed==2){
            matchThirdImage.setImageResource(R.drawable.minipin_234);
        }else if(object2.breed==3){
            matchThirdImage.setImageResource(R.drawable.yorkshire_234);
        }else if(object2.breed==4){
            matchThirdImage.setImageResource(R.drawable.chihuahua_234);
        }else if(object2.breed==5){
            matchThirdImage.setImageResource(R.drawable.poodle_234);
        }else if(object2.breed==6){
            matchThirdImage.setImageResource(R.drawable.dachshund_234);
        }else if(object2.breed==7){
            matchThirdImage.setImageResource(R.drawable.schnauzer_234);
        }else if(object2.breed==8){
            matchThirdImage.setImageResource(R.drawable.bichon_234);
        }else if(object2.breed==9){
            matchThirdImage.setImageResource(R.drawable.shitzu_234);
        }else if(object2.breed==10){
            matchThirdImage.setImageResource(R.drawable.pomeranian_234);
        }else if(object2.breed==11){
            matchThirdImage.setImageResource(R.drawable.bulldog_234);
        }else if(object2.breed==12){
            matchThirdImage.setImageResource(R.drawable.bullterrier_234);
        }else if(object2.breed==13){
            matchThirdImage.setImageResource(R.drawable.beagle_234);
        }else if(object2.breed==14){
            matchThirdImage.setImageResource(R.drawable.cocker_234);
        }else if(object2.breed==15){
            matchThirdImage.setImageResource(R.drawable.jindo_234);
        }else if(object2.breed==16){
            matchThirdImage.setImageResource(R.drawable.poongsan_234);
        }else if(object2.breed==17){
            matchThirdImage.setImageResource(R.drawable.siba_234);
        }else if(object2.breed==18){
            matchThirdImage.setImageResource(R.drawable.welsh_corgi_234);
        }else if(object2.breed==19){
            matchThirdImage.setImageResource(R.drawable.retriever_234);
        }else if(object2.breed==20){
            matchThirdImage.setImageResource(R.drawable.malamute_234);
        }else if(object2.breed==21){
            matchThirdImage.setImageResource(R.drawable.husky_234);
        }else if(object2.breed==22){
            matchThirdImage.setImageResource(R.drawable.shepherd_234);
        }else if(object2.breed==23){
            matchThirdImage.setImageResource(R.drawable.rottweiler_234);
        }

        if (object3.breed==1){
            matchFourthImage.setImageResource(R.drawable.maltese_234);
        }else if(object3.breed==2){
            matchFourthImage.setImageResource(R.drawable.minipin_234);
        }else if(object3.breed==3){
            matchFourthImage.setImageResource(R.drawable.yorkshire_234);
        }else if(object3.breed==4){
            matchFourthImage.setImageResource(R.drawable.chihuahua_234);
        }else if(object3.breed==5){
            matchFourthImage.setImageResource(R.drawable.poodle_234);
        }else if(object3.breed==6){
            matchFourthImage.setImageResource(R.drawable.dachshund_234);
        }else if(object3.breed==7){
            matchFourthImage.setImageResource(R.drawable.schnauzer_234);
        }else if(object3.breed==8){
            matchFourthImage.setImageResource(R.drawable.bichon_234);
        }else if(object3.breed==9){
            matchFourthImage.setImageResource(R.drawable.shitzu_234);
        }else if(object3.breed==10){
            matchFourthImage.setImageResource(R.drawable.pomeranian_234);
        }else if(object3.breed==11){
            matchFourthImage.setImageResource(R.drawable.bulldog_234);
        }else if(object3.breed==12){
            matchFourthImage.setImageResource(R.drawable.bullterrier_234);
        }else if(object3.breed==13){
            matchFourthImage.setImageResource(R.drawable.beagle_234);
        }else if(object3.breed==14){
            matchFourthImage.setImageResource(R.drawable.cocker_234);
        }else if(object3.breed==15){
            matchFourthImage.setImageResource(R.drawable.jindo_234);
        }else if(object3.breed==16){
            matchFourthImage.setImageResource(R.drawable.poongsan_234);
        }else if(object3.breed==17){
            matchFourthImage.setImageResource(R.drawable.siba_234);
        }else if(object3.breed==18){
            matchFourthImage.setImageResource(R.drawable.welsh_corgi_234);
        }else if(object3.breed==19){
            matchFourthImage.setImageResource(R.drawable.retriever_234);
        }else if(object3.breed==20){
            matchFourthImage.setImageResource(R.drawable.malamute_234);
        }else if(object3.breed==21){
            matchFourthImage.setImageResource(R.drawable.husky_234);
        }else if(object3.breed==22){
            matchFourthImage.setImageResource(R.drawable.shepherd_234);
        }else if(object3.breed==23){
            matchFourthImage.setImageResource(R.drawable.rottweiler_234);
        }

        return v;
    }


}
