package wepet.projectbase;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by minsoo on 2016-08-22.
 */
public class CustomLoadingDialog extends Dialog {

    AnimationDrawable ani;
    ImageView img;

    public CustomLoadingDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
        img = (ImageView)findViewById(R.id.loading_dialog) ;
        img.setBackgroundResource(R.drawable.animation);
        ani = (AnimationDrawable)img.getBackground();
        ani.start();

    }


}

