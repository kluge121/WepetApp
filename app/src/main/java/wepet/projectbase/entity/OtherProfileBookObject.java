package wepet.projectbase.entity;

import java.io.Serializable;

/**
 * Created by ccei on 2016-08-11.
 */

public class OtherProfileBookObject implements Serializable {

    public Integer pid;
    public String pid_animal;
    public String uid;
    public int category;
    public String date;
    public String date_end;
    public String img_thumb;

    public Integer breed;
    public Integer gender;
    public Integer age;
    public Integer region;
    public Integer bookmark_num;
    public Integer reply_num;
    public Integer state;

    public OtherProfileBookObject(){}
}
