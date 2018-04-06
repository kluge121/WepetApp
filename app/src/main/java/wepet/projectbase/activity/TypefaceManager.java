package wepet.projectbase.activity;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by ccei on 2016-08-09.
 */
public class TypefaceManager {
    private static final TypefaceManager instance = new TypefaceManager();

    public static TypefaceManager getInstance() {
        return instance;
    }

    private TypefaceManager() {}

    public static final String FONT_NAME = "NanumSquareR";
    private Typeface kopubdotum;

    public Typeface getTypeface(Context context, String fontName) {
        if(FONT_NAME.equals(fontName)) {
            if(kopubdotum == null) {
                kopubdotum = Typeface.createFromAsset(context.getAssets(), "NanumSquareR.ttf");
            }
            return kopubdotum;
        }
        return null;
    }
}