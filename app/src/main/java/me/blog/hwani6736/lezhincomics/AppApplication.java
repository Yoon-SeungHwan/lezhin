package me.blog.hwani6736.lezhincomics;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

/**
 * Created by NarZa on 2018. 9. 3..
 */
public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        ArrayList<Object> list = new ArrayList<Object>();
    }
}
