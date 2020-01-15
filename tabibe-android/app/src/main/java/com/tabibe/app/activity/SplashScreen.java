package com.tabibe.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Looper;
import android.os.Bundle;
import android.view.View;

import com.tabibe.app.MainActivity;
import com.tabibe.app.R;
import com.tabibe.app.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen extends Activity {


    @BindView(R.id.mainView)
    View view;

    private static final float ANIMATION_SCALE = 0.5f;
    private static final int ANIMATION_SCALE_OFFSET = 1000;
    private Thread mSplashThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        addDelay();
    }

    private void addDelay() {

        mSplashThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    synchronized (this) {
                        wait(Constants.SPLASH_DISPLAY_TIME);
                    }
                } catch (InterruptedException ex) {
                    //intentionally left empty
                }
                launchMainScreen();
            }
        };
        mSplashThread.start();
    }


    private void launchMainScreen() {

        Intent intent = new Intent(this,MainActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}
