package wepet.projectbase.activity;

/**
 * Created by ccei on 2016-08-10.
 */
public class ChangeCodeToName {
    public static String puppyName (int code) {
        String puppyName ="";
        String [] breed = {"기타","말티즈","미니핀","요크셔","치와와","푸들","닥스훈트","슈나우저","비숑","시츄","포메라니안",
                "불독","불테리어","비글","코카","진돗개","풍산개","시바견","웰시코기","리트리버","말라뮤트","허스키","세퍼트","로트바일러","믹스견"};

        puppyName=breed[code];

        return puppyName;
    }

    public static String dogRegion(int code){
        String dogRegion = "";
        String [] region = {"전체","서울","경기","인천","강원","대전","세종","충남","충북","부산","울산","경남","경북","대구","광주","전남","전북","제주도"};

        dogRegion = region[code];

        return dogRegion;
    }

    public static String dogVacccin(int code){

        String [] vaccin = {"몰라요","1차","2차","3차","4차","5차"};
        return vaccin[code];
    }

}
