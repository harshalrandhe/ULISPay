package com.ulisfintech.arthavendor.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ulisfintech.arthavendor.R;
import com.ulisfintech.arthavendor.ui.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


    }

    @Override
    protected void onResume() {
        super.onResume();

        navigateTo();

    }

    private void navigateTo() {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        }, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}