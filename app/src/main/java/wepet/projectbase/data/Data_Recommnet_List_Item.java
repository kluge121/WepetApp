package wepet.projectbase.data;

import android.graphics.drawable.Drawable;

/**
 * Created by ccei on 2016-07-27.
 */
public class Data_Recommnet_List_Item {

    public Data_Recommnet_List_Item(Drawable profileImage, String profileID, String recommentContent) {
        this.profileImageWD = profileImage;
        this.profileIdWD = profileID;
        this.recommentContentWD = recommentContent;
    }

    public Data_Recommnet_List_Item(String profileID, String recommentContent) {
        this.profileIdWD = profileID;
        this.recommentContentWD = recommentContent;
    }

    public Drawable profileImageWD;
    public String profileIdWD;
    public String recommentContentWD;
}
