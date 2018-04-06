package wepet.projectbase.activity;

/**
 * Created by ccei on 2016-08-17.
 */
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import wepet.projectbase.GetContext;

/**
 * 편의점을 부탁해 PJ ID를 관리하는 Preference관리 클래스
 */
public class PropertyManager {
    private static PropertyManager instance;

    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private PropertyManager() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(GetContext.getContext());
        mEditor = mPrefs.edit();
    }
    /*
     서버로 넘길 ID 또는 토큰 값
     */
    private static final String USER_ID = "uid";
    private static final String USER_UUID = "uuid";
    private static final String USER_TOKEN = "token";
    private static final String USER_SETTING1 = "setting1";
    private static final String USER_SETTING2 = "setting2";
    private static final String USER_SETTING3 = "setting3";
    private static final String USER_SETTING4 = "setting4";


    public void setSetting1(String setting1){
        mEditor.putString(USER_SETTING1, setting1);
        mEditor.commit();
    }

    public String getSetting1(){
        return mPrefs.getString(USER_SETTING1, "");
    }

    public void setSetting2(String setting2){
        mEditor.putString(USER_SETTING2, setting2);
        mEditor.commit();
    }

    public String getSetting2(){
        return mPrefs.getString(USER_SETTING2, "");
    }

    public void setSetting3(String setting3){
        mEditor.putString(USER_SETTING3, setting3);
        mEditor.commit();
    }

    public String getSetting3(){
        return mPrefs.getString(USER_SETTING3, "");
    }

    public void setSetting4(String setting4){
        mEditor.putString(USER_SETTING4, setting4);
        mEditor.commit();
    }

    public String getSetting4(){
        return mPrefs.getString(USER_SETTING4, "");
    }

    public void setUid(String uid) {
        mEditor.putString(USER_ID, uid);
        mEditor.commit();
    }

    public String getUid() {
        return mPrefs.getString(USER_ID, "");
    }

    public void setUuid(String uuid){
        mEditor.putString(USER_UUID, uuid);
        mEditor.commit();
    }

    public String getToken(){
        return mPrefs.getString(USER_TOKEN, "");
    }

    public void setToken(String token){
        mEditor.putString(USER_TOKEN, token);
        mEditor.commit();
    }

    public void setBreed(int first, int second, int third, int fourth){
        mEditor.putInt("first",first);
        mEditor.putInt("second",second);
        mEditor.putInt("third",third);
        mEditor.putInt("fourth",fourth);
        mEditor.commit();

    }

    public String getUuid(){
        return mPrefs.getString(USER_UUID, "");
    }

    public int getfirst(){
        return mPrefs.getInt("first",100);
    }
    public int getsecond(){
        return mPrefs.getInt("second",100);
    }
    public int getthird(){
        return mPrefs.getInt("third",100);
    }
    public int getfourth(){
        return mPrefs.getInt("fourth",100);
    }
}