package io.gloop.demo.instagram.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import io.gloop.Gloop;
import io.gloop.demo.instagram.R;
import io.gloop.demo.instagram.constants.Constants;

public class SplashActivity extends Activity {

    // Set url of the server.
    //    private static final String GLOOP_HOST_URL = "192.168.0.10:8080";
//    private static final String GLOOP_HOST_URL = "192.168.0.11:8080";
        private static final String GLOOP_HOST_URL = "52.169.152.13:8080";

    //    private static final String GLOOP_API_KEY = "your-api-key-goes-here";
    //    private static final String GLOOP_API_KEY = "b8ed4e84-4db5-4b63-b369-f75cbf51a065";
    private static final String GLOOP_API_KEY = "7007a4ac-fab7-43d1-96eb-137c5b671cf9";

    private static final boolean GLOOP_DEBUG = true;

    /**
     * Duration of wait
     **/
    private static final int SPLASH_DISPLAY_LENGTH = 1000;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splashscreen);
    }

    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // setup Gloop
        Gloop.initialize(this, GLOOP_API_KEY, GLOOP_HOST_URL);

        // setup Gloop with debugging enabled
        // new Gloop(this, "your-api-key-goes-here", GLOOP_HOST_URL, GLOOP_DEBUG);

        /* New Handler to start the next Activity
         * and close this SplashActivity-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!logInWithRememberedUser()) {
                    Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private boolean logInWithRememberedUser() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0); // 0 - for private mode

        String email = pref.getString(Constants.SHARED_PREFERENCES_USER_EMAIL, "");
        String password = pref.getString(Constants.SHARED_PREFERENCES_USER_PASSWORD, "");

        if (!email.isEmpty() && !password.isEmpty()) {
            if (Gloop.login(email, password)) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
                return true;
            }
        }
        return false;
    }
}