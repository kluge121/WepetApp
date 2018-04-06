package wepet.projectbase.activity;

/**
 * Created by ccei on 2016-08-19.
 */
public class Chat_Data {
    public int circleImage;
    public String message;
    public int type = 0; //
    public String currentDate;

    public Chat_Data(int circleImage, String message, String currentDate, int type) {
        this.circleImage = circleImage;
        this.message = message;
        this.type = type;
        this.currentDate = currentDate;
    }
}