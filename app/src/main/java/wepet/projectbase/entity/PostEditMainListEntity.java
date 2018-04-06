package wepet.projectbase.entity;

import java.util.ArrayList;

/**
 * Created by ccei on 2016-08-05.
 */
public class PostEditMainListEntity {

    public String uid;
    public String category;
    public String date_end;

    public ArrayList<UpLoadValueObject> img;

    public String video;

    public String breed;
    public String gender;
    public String age;
    public String region;
    public String neuter;
    public String vaccin;
    public String content;
    public String nowImgae1 = null;
    public String nowImgae2= null;
    public String nowImgae3= null;
    public String nowImgae4= null;
    public ArrayList<String> CurrentImageList;


    public PostEditMainListEntity() {
    }
}
