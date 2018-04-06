package wepet.projectbase;

import java.util.ArrayList;

import wepet.projectbase.data.Data_Individual_Recomment;
import wepet.projectbase.data.Data_MyPage_Bookmark;
import wepet.projectbase.data.Data_Mypage_Chat;
import wepet.projectbase.data.Data_Mypage_Recomment;

/**
 * Created by ccei on 2016-07-26.
 */
public class SampleListSetting {


   public static ArrayList<Integer>image_list; // 글 상세보기 이미지 뷰페이저

   public static ArrayList<Data_Individual_Recomment> recomment_list2; // 개인프로필 및 내가 쓴 글 댓글

   public static ArrayList<Data_Mypage_Recomment> sample_mypage_recomment;
   public static ArrayList<Data_Mypage_Chat> sample_mypage_Chat;
   public static ArrayList<Data_MyPage_Bookmark> sample_mypage_book;




    static {
        sample_mypage_book = new ArrayList<Data_MyPage_Bookmark>();

    }

}
