package wepet.projectbase.Manager;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by ListenAndRepeat on 2016. 3. 7..
 */
public class MyGlideModule implements GlideModule {
    private static final int DEFAULT_DISK_CACHE_SIZE = 250 * 1024 * 1024;
    private static final int DEFAULT_MEMORY_CACHE_SIZE = 10 * 1024 * 1024;
    private static final int DEFAULT_BITMAP_POOL_SIZE = 10 * 1024 * 1024;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
