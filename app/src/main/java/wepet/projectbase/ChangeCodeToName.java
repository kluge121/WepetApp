package wepet.projectbase;

import android.util.Log;

/**
 * Created by ccei on 2016-08-10.
 */
public class ChangeCodeToName {
    private static final String TAG = "ChangeCodeToName";
    public static String puppyName (int code) {
        String [] breed = { "기타","말티즈","미니핀","요크셔","치와와","푸들","닥스훈트","슈나우저","비숑","시츄","포메라니안",
                "불독","불테리어","비글","코카","진돗개","풍산개","시바견","웰시코기","리트리버","말라뮤트","허스키","세퍼트","로트바일러","믹스견" };
        if(code >=breed.length ) {
            Log.e(TAG, "breed code err");
        }
        return breed[code];
    }

    public static String dogRegion(int code){
        String [] region = {"전체","서울","경기","인천","강원","대전","세종","충남","충북","부산","울산","경남","경북","대구","광주","전남","전북","제주도"};
        if(code >=region.length ) {
            Log.e(TAG, "region code err");
        }
        return region[code];
    }

    public static String dogState(int code){
        String [] state = {"","공고중","분양 완료","안락사","기간만료"};
        if(code >=state.length ) {
            Log.e(TAG, "state code err");
        }
        return state[code];
    }

    public static String dogGender(int code){
        String [] gender={"","수컷","암컷","gender err"};
        if(code >=gender.length ) {
            Log.e(TAG, "gender code err");
        }
        return gender[code];
    }

    public static String dogNeuter(int code){
        String [] neuter = {"","중성화함","중성화안함","neuter err"};
        if(code >=neuter.length ) {
            Log.e(TAG, "neuter code err");
        }
        return neuter[code];
    }

    public static String dogVacccin(int code){
        String [] vaccin = {"몰라요","1차","2차","3차","4차","5차"};
        if(code >=vaccin.length ) {
            Log.e(TAG, "vaccin code err");
        }
        return vaccin[code];
    }

}
