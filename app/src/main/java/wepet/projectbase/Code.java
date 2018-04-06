package wepet.projectbase;

/**
 * Created by ccei on 2016-08-05.
 */
 public class Code {



    public static String breedCodeReturn(String breed) {

        switch (breed) {

            case "기타":
                return "0";
            case "말티즈":
                return "1";
            case "미니핀":
                return "2";
            case "요크셔":
                return "3";
            case "치와와":
                return "4";
            case "푸들":
                return "5";
            case "닥스훈트":
                return "6";
            case "슈나우저":
                return "7";
            case "비숑":
                return "8";
            case "시츄":
                return "9";
            case "포메라니안":
                return "10";
            case "불독":
                return "11";
            case "불테리어":
                return "12";
            case "비글":
                return "13";
            case "코카":
                return "14";
            case "진돗개":
                return "15";
            case "풍산개":
                return "16";
            case "시바견":
                return "17";
            case "웰시코기":
                return "18";
            case "리트리버":
                return "19";
            case "말라뮤트":
                return "20";
            case "허스키":
                return "21";
            case "세퍼트":
                return "22";
            case "로트바일러":
                return "23";
        }

        return null;
    }
    public static String genderCodeReturn(Boolean boy) {
        if (boy == true) return "1";
        else {
            return "2";
        }

    }
    public static String regionCodeReturn(String region) {
        switch (region) {

            case "전체":
                return "0";
            case "서울":
                return "1";
            case "경기":
                return "2";
            case "인천":
                return "3";
            case "강원":
                return "4";
            case "대전":
                return "5";
            case "세종":
                return "6";
            case "충남":
                return "7";
            case "충북":
                return "8";
            case "부산":
                return "9";
            case "울산":
                return "10";
            case "경남":
                return "11";
            case "경북":
                return "12";
            case "대구":
                return "13";
            case "광주":
                return "14";
            case "전남":
                return "15";
            case "전북":
                return "16";
            case "제주도":
                return "17";
        }
        return null;

    }
    public static String neuterCodeReturn(Boolean neuter) {
        if (neuter == true) return "1";
        else {
            return "2";
        }

    }
    public static String vaccineCodeReturn(String vaccine){

        switch (vaccine){
            case "1차":
                return "1";
            case "2차":
                return "2";
            case "3차":
                return "3";
            case "4차":
                return "4";
            case "5차":
                return "5";
            case "몰라요":
                return "0";

        }
        return null;
    }
}

